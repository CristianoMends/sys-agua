package com.api.sysagua.service.impl;

import com.api.sysagua.dto.order.CreateOrderDto;
import com.api.sysagua.dto.order.CreateProductOrderDto;
import com.api.sysagua.dto.order.UpdateOrderDto;
import com.api.sysagua.dto.order.ViewOrderDto;
import com.api.sysagua.enumeration.*;
import com.api.sysagua.exception.BusinessException;
import com.api.sysagua.model.*;
import com.api.sysagua.repository.*;
import com.api.sysagua.service.CashierService;
import com.api.sysagua.service.OrderService;
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
    public List<ViewOrderDto> list(Long id, Long customerId, Long deliveryPersonId, Long productOrderId, DeliveryStatus status, BigDecimal receivedAmountStart, BigDecimal receivedAmountEnd, BigDecimal totalAmountStart, BigDecimal totalAmountEnd, PaymentMethod paymentMethod, LocalDateTime createdAtStart, LocalDateTime createdAtEnd, LocalDateTime finishedAtStart, LocalDateTime finishedAtEnd,PaymentStatus paymentStatus) {
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

        var customer = this.customerRepository.findById(id).orElseThrow(
                () -> new BusinessException("Customer not found", HttpStatus.NOT_FOUND)
        );

        List<Long> productIds = dto.getProductOrder().stream()
                .map(CreateProductOrderDto::getProductId)
                .toList();

        List<Product> products = this.productRepository.findAllById(productIds);

        if (products.isEmpty()) {
            throw new BusinessException("No products found for the provided IDs", HttpStatus.NOT_FOUND);
        }

        List<ProductOrder> productOrders = dto.getProductOrder().stream()
                .map(dtoItem -> {
                    Product product = products.stream()
                            .filter(p -> p.getId().equals(dtoItem.getProductId()))
                            .findFirst()
                            .orElseThrow(() -> new BusinessException("Product not found", HttpStatus.NOT_FOUND));

                    return new ProductOrder(order, product, dtoItem.getQuantity(), dtoItem.getUnitPrice());
                })
                .toList();


        var deliveryPerson = this.deliveryPersonRepository.findById(id).orElseThrow(
                () -> new BusinessException("Delivery person not found", HttpStatus.NOT_FOUND)
        );

        if (order.getDeliveryStatus().equals(DeliveryStatus.FINISHED)) processOrderProducts(order);

        switch (order.getPaymentStatus()) {
            case PAID -> createPaidTransaction(order);
            case CANCELED -> createCanceledTransaction(order);
            case PENDING -> createPendingTransaction(order);
        }
        order.setCustomer(customer);
        order.setProductOrders(productOrders);
        order.setDeliveryPerson(deliveryPerson);
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
                    createProductOrderDto.getUnitPrice() ==null?product.getPrice():createProductOrderDto.getUnitPrice()
            ));
        });

        return list;
    }

    private void addWithdrawsProductFromStock(int quantity, Product product) {
        var stock = this.stockRepository.findProduct(product.getId()).orElseThrow(
                () -> new BusinessException("Stock by product not found", HttpStatus.NOT_FOUND)
        );
        stock.setTotalWithdrawals(stock.getTotalWithdrawals() + quantity);
        this.stockRepository.save(stock);
    }

    private void processOrderProducts(Order order) {
        order.getProductOrders()
                .forEach(p -> addWithdrawsProductFromStock(p.getQuantity(), p.getProduct()));

    }

    private void createPendingTransaction(Order order) {
        var t = new Transaction(
                TransactionStatus.PENDING,
                order.getReceivedAmount(),
                TransactionType.INCOME,
                "Pedido aguardando pagamento - Restante a ser pago: R$"+order.getTotalAmount().subtract(order.getReceivedAmount()),
                order,
                null
        );
        this.transactionRepository.save(t);
    }

    private void createPaidTransaction(Order order) {
        var t = new Transaction(
                TransactionStatus.PAID,
                order.getReceivedAmount(),
                TransactionType.INCOME,
                "Pedido pago",
                order,
                null
        );
        this.transactionRepository.save(t);
    }

    private void createCanceledTransaction(Order order) {
        var t = new Transaction(
                TransactionStatus.CANCELED,
                order.getReceivedAmount(),
                TransactionType.INCOME,
                "Pedido cancelado",
                order,
                null
        );
        this.transactionRepository.save(t);

    }
}
