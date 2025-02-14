package com.api.sysagua.service.impl;

import com.api.sysagua.dto.order.*;
import com.api.sysagua.enumeration.*;
import com.api.sysagua.exception.BusinessException;
import com.api.sysagua.model.*;
import com.api.sysagua.repository.*;
import com.api.sysagua.service.CashierService;
import com.api.sysagua.service.OrderService;
import com.api.sysagua.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private DeliveryPersonRepository deliveryPersonRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private CashierService cashierService;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private StockHistoryRepository stockHistoryRepository;
    @Autowired
    private UserService userService;

    @Override
    @Transactional
    public void create(CreateOrderDto dto) {
        var order = new Order();
        order.setCustomer(getCustomerById(dto.getCustomerId()));
        order.setDeliveryPerson(getDeliveryPersonById(dto.getDeliveryPersonId()));
        order.setProductOrders(createListProductOrder(order, dto.getProductOrders()));
        order.setDeliveryStatus(DeliveryStatus.PENDING);
        order.setReceivedAmount(dto.getReceivedAmount());
        order.setTotalAmount(dto.getTotalAmount());
        order.setBalance(dto.getTotalAmount().subtract(dto.getReceivedAmount()));
        order.setPaymentMethod(dto.getPaymentMethod());
        order.setDescription(dto.getDescription());

        switch (checkPaymentStatus(order)) {
            case PAID -> {
                order.setPaymentStatus(PaymentStatus.PAID);
                order.setFinishedAt(LocalDateTime.now());
            }
            case PENDING -> {
                order.setPaymentStatus(PaymentStatus.PENDING);
            }
        }
        var saved = this.orderRepository.save(order);

        switch (saved.getPaymentStatus()) {
            case PAID -> createPaidTransaction(saved);
            case PENDING -> createPendingTransaction(saved);
        }
    }

    private PaymentStatus checkPaymentStatus(Order order) {
        if (order.getTotalAmount() == null) order.calculateTotalAmount();

        if (order.getReceivedAmount().compareTo(order.getTotalAmount()) >= 0) {
            return PaymentStatus.PAID;
        }
        return PaymentStatus.PENDING;
    }

    @Override
    public List<ViewOrderDto> list(Long id, Long customerId, Long deliveryPersonId, Long productOrderId, DeliveryStatus status, BigDecimal receivedAmountStart, BigDecimal receivedAmountEnd, BigDecimal totalAmountStart, BigDecimal totalAmountEnd, BigDecimal balanceStart, BigDecimal balanceEnd, PaymentMethod paymentMethod, LocalDateTime createdAtStart, LocalDateTime createdAtEnd, LocalDateTime finishedAtStart, LocalDateTime finishedAtEnd, PaymentStatus paymentStatus) {
        return this.orderRepository.list(
                id,
                customerId,
                deliveryPersonId,
                productOrderId,
                status,
                receivedAmountStart,
                receivedAmountEnd,
                totalAmountStart,
                totalAmountEnd,
                balanceStart,
                balanceEnd,
                paymentMethod,
                createdAtStart,
                createdAtEnd,
                finishedAtStart,
                finishedAtEnd,
                paymentStatus
        ).stream().map(Order::toView).toList();
    }

    @Override
    public void update(Long id, UpdateOrderDto dto) {
        var order = this.orderRepository.findById(id).orElseThrow(
                () -> new BusinessException("Order not found", HttpStatus.NOT_FOUND)
        );


        if(dto.getStatus() != null){
            DeliveryStatus newStatus = dto.getStatus();

            if(newStatus == DeliveryStatus.FINISHED){
                processOrderProducts(order);
                order.setFinishedAt(LocalDateTime.now());
            }

            if(newStatus == DeliveryStatus.CANCELED){
                order.setCanceledAt(LocalDateTime.now());
                order.setPaymentStatus(PaymentStatus.CANCELED);

            }

            order.setDeliveryStatus(newStatus);
        }

        if(dto.getReceivedAmount() != null) {
            order.setReceivedAmount(dto.getReceivedAmount());

            if (dto.getReceivedAmount().compareTo(dto.getTotalAmount()) == 0) {
                order.setPaymentStatus(PaymentStatus.PAID);

            }else{
                order.setPaymentStatus(PaymentStatus.PENDING);
                BigDecimal saldo = dto.getTotalAmount().subtract(dto.getReceivedAmount());
                order.setBalance(saldo);
            }
        }

        if(order.getPaymentStatus() != null){
            PaymentStatus paymentStatus = order.getPaymentStatus();

            switch (paymentStatus) {
                case PAID ->
                        createPaidTransaction(order);
                case CANCELED ->
                    createCanceledTransaction(order);
                case PENDING -> createPendingTransaction(order);
            }
            order.setPaymentStatus(paymentStatus);
        }
        this.orderRepository.save(order);
    }

    private DeliveryPerson getDeliveryPersonById(Long id) {
        return this.deliveryPersonRepository.findById(id).orElseThrow(
                () -> new BusinessException("Delivery Person with id " + id + " not found", HttpStatus.NOT_FOUND)
        );
    }

    private Customer getCustomerById(Long id) {
        return this.customerRepository.findById(id).orElseThrow(
                () -> new BusinessException("Customer with id " + id + " not found", HttpStatus.NOT_FOUND)
        );
    }

    private Product getProductById(Long id) {
        return this.productRepository.findById(id).orElseThrow(
                () -> new BusinessException("Product with id " + id + " not found", HttpStatus.NOT_FOUND)
        );
    }

    private List<ProductOrder> createListProductOrder(Order order, List<CreateProductOrderDto> dtos) {
        var list = new ArrayList<ProductOrder>();

        dtos.forEach(createProductOrderDto -> {
            var product = this.getProductById(createProductOrderDto.getProductId());

            list.add(new ProductOrder(
                    order,
                    product,
                    createProductOrderDto.getQuantity(),
                    createProductOrderDto.getUnitPrice() == null ? product.getPrice() : createProductOrderDto.getUnitPrice()
            ));
        });

        return list;
    }

    private void addWithdrawsProductFromStock(int quantity, Product product) {
        var stock = this.stockRepository.findProduct(product.getId()).orElseThrow(
                () -> new BusinessException("Stock by product not found", HttpStatus.NOT_FOUND)
        );
        stock.setTotalWithdrawals(stock.getTotalWithdrawals() + quantity);
        var saved = this.stockRepository.save(stock);

        saveStockHistory(saved, MovementType.WITHDRAWAL, quantity, "Retirado produto " + product.getName() + " do estoque");
    }

    private void processOrderProducts(Order order) {
        order.getProductOrders()
                .forEach(p -> addWithdrawsProductFromStock(p.getQuantity(), p.getProduct()));

    }

    private void createPendingTransaction(Order order) {
        var user = this.userService.getLoggedUser();
        var t = new Transaction(
                TransactionStatus.PENDING,
                order.getReceivedAmount(),
                TransactionType.INCOME,
                "Pedido aguardando pagamento - Restante a ser pago: R$" + order.getTotalAmount().subtract(order.getReceivedAmount()),
                user,
                order,
                null
        );
        this.transactionRepository.save(t);
    }

    private void createPaidTransaction(Order order) {
        var user = this.userService.getLoggedUser();
        var t = new Transaction(
                TransactionStatus.PAID,
                order.getReceivedAmount(),
                TransactionType.INCOME,
                String.format("Pagamento confirmado! O pedido foi pago com sucesso. Valor total: R$ %.2f, valor recebido: R$ %.2f. A transação foi concluída e o pedido está finalizado.",
                        order.getTotalAmount(), order.getReceivedAmount()),
                user,
                order,
                null
        );
        this.transactionRepository.save(t);
    }

    private void createCanceledTransaction(Order order) {
        var user = this.userService.getLoggedUser();
        var t = new Transaction(
                TransactionStatus.CANCELED,
                order.getReceivedAmount(),
                TransactionType.EXPENSE,
                "Pedido cancelado. O valor foi registrado como despesa.",
                user,
                order,
                null
        );
        this.transactionRepository.save(t);

    }

    void saveStockHistory(Stock stock, MovementType type, int quantity, String description) {
        var user = this.userService.getLoggedUser();
        var history = new StockHistory(user, stock, type, quantity, description);
        this.stockHistoryRepository.save(history);
    }
}
