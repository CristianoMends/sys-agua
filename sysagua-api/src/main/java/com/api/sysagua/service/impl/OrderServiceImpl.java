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

        order.calculateTotalAmount();

        order.setBalance(order.getTotalAmount().subtract(dto.getReceivedAmount()));
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


        if (dto.getStatus() != null) {
            DeliveryStatus newStatus = dto.getStatus();

            if (newStatus == DeliveryStatus.CANCELED && order.getDeliveryStatus() == DeliveryStatus.PENDING) {
                order.setCanceledAt(LocalDateTime.now());
                order.setPaymentStatus(PaymentStatus.CANCELED);
            }

            if (newStatus == DeliveryStatus.FINISHED && order.getDeliveryStatus() == DeliveryStatus.PENDING) {
                processOrderProducts(order);
                order.setFinishedAt(LocalDateTime.now());
            }


            order.setDeliveryStatus(newStatus);
        }

        if (dto.getReceivedAmount() != null) {
            BigDecimal previousAmount = order.getReceivedAmount();
            BigDecimal newAmount = previousAmount.add(dto.getReceivedAmount());

            if (newAmount.compareTo(order.getTotalAmount()) == 0) {
                newAmount = order.getTotalAmount();
                order.setPaymentStatus(PaymentStatus.PAID);
            }

            switch (checkPaymentStatus(order)) {
                case PAID -> {
                    order.setPaymentStatus(PaymentStatus.PAID);
                    order.setFinishedAt(LocalDateTime.now());
                }
                case CANCELED -> {
                    order.setCanceledAt(LocalDateTime.now());
                    order.setPaymentStatus(PaymentStatus.CANCELED);
                }
                case PENDING -> order.setPaymentStatus(PaymentStatus.PENDING);

            }
            order.setBalance(order.getTotalAmount().subtract(newAmount));
            if (newAmount.equals(order.getTotalAmount())) {
                order.setPaymentStatus(PaymentStatus.PAID);
            } else if (newAmount.compareTo(order.getTotalAmount()) < 0) {
                order.setReceivedAmount(newAmount);
            } else if (newAmount.compareTo(order.getTotalAmount()) > 0) {
                throw new BusinessException("Order received value overtakes total value.");
            }
        }

        var saved_order = this.orderRepository.save(order);

        if (order.getPaymentStatus() != null) {
            PaymentStatus paymentStatus = order.getPaymentStatus();

            switch (paymentStatus) {
                case PAID -> createPaidTransaction(saved_order);
                case CANCELED -> createCanceledTransaction(saved_order);
                case PENDING -> createTransaction(order, dto.getReceivedAmount(), TransactionType.INCOME);
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

    private void createTransaction(Order order, BigDecimal amout, TransactionType type) {
        var amountPending = order.getBalance();
        var description = "Quantia paga R$ " + amout + ", Quantia pendente R$" + amountPending;
        var user = this.userService.getLoggedUser();
        var t = new Transaction(
                amout,
                type,
                order.getPaymentMethod(),
                description,
                user,
                order
        );
        this.transactionRepository.save(t);
    }

    private void createPendingTransaction(Order order) {
        var user = this.userService.getLoggedUser();
        var t = new Transaction(
                order.getReceivedAmount(),
                TransactionType.INCOME,
                order.getPaymentMethod(),
                "Pedido aguardando pagamento - Restante a ser pago: R$" + order.getTotalAmount().subtract(order.getReceivedAmount()),
                user,
                order
        );
        this.transactionRepository.save(t);
    }

    private void createPaidTransaction(Order order) {
        var user = this.userService.getLoggedUser();
        var t = new Transaction(
                order.getReceivedAmount(),
                TransactionType.INCOME,
                order.getPaymentMethod(),
                String.format("Pagamento confirmado! O pedido foi pago com sucesso. Valor total: R$ %.2f, valor recebido: R$ %.2f. A transação foi concluída e o pedido está finalizado.",
                        order.getTotalAmount(), order.getReceivedAmount()),
                user,
                order
        );
        this.transactionRepository.save(t);
    }

    private void createCanceledTransaction(Order order) {
        var user = this.userService.getLoggedUser();
        var t = new Transaction(
                order.getReceivedAmount().negate(),
                TransactionType.EXPENSE,
                order.getPaymentMethod(),
                "Pedido cancelado e valor pago estornado",
                user,
                order
        );
        this.transactionRepository.save(t);

    }

    void saveStockHistory(Stock stock, MovementType type, int quantity, String description) {
        var user = this.userService.getLoggedUser();
        var history = new StockHistory(user, stock, type, quantity, description);
        this.stockHistoryRepository.save(history);
    }
}
