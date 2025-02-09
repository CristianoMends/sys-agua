package com.api.sysagua;

import com.api.sysagua.enumeration.DeliveryStatus;
import com.api.sysagua.enumeration.PaymentMethod;
import com.api.sysagua.model.Order;
import com.api.sysagua.repository.OrderRepository;
import com.api.sysagua.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Order order1;
    private Order order2;

    @BeforeEach
    void setUp() {
        order1 = new Order();
        order1.setStatus(DeliveryStatus.PENDING);
        order1.setReceivedAmount(new BigDecimal("50.00"));
        order1.setTotalAmount(new BigDecimal("100.00"));
        order1.setPaymentMethod(PaymentMethod.MONEY);
        order1.setCreatedAt(LocalDateTime.now().minusDays(1));
        order1.setFinishedAt(LocalDateTime.now());

        order2 = new Order();
        order2.setStatus(DeliveryStatus.PENDING);
        order2.setReceivedAmount(new BigDecimal("150.00"));
        order2.setTotalAmount(new BigDecimal("200.00"));
        order2.setPaymentMethod(PaymentMethod.PIX);
        order2.setCreatedAt(LocalDateTime.now().minusDays(2));
        order2.setFinishedAt(LocalDateTime.now().minusDays(1));
    }
    @Test
    void testListAllOrders() {
        when(orderRepository.list(null, null, null, null, null, null, null, null, null, null, null, null, null, null))
                .thenReturn(List.of(order1, order2));

        List<Order> orders = orderService.list(null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        assertThat(orders).isNotEmpty();
        assertThat(orders).contains(order1);
    }
}
