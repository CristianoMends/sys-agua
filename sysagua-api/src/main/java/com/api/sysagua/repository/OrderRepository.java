package com.api.sysagua.repository;

import com.api.sysagua.enumeration.DeliveryStatus;
import com.api.sysagua.enumeration.PaymentMethod;
import com.api.sysagua.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("""
            select o from Order o 
            inner join o.productOrders productOrders
            where (:id is null or o.id = :id) 
            and (:customerId is null or o.customer.id = :customerId) 
            and (:deliveryPersonId is null or o.deliveryPerson.id = :deliveryPersonId) 
            and (:productOrderId is null or productOrders.id = :productOrderId) 
            and (:deliveryStatus is null or o.deliveryStatus = :deliveryStatus) 
            and ((:receivedAmountStart is null or :receivedAmountEnd is null) or o.receivedAmount between :receivedAmountStart and :receivedAmountEnd) 
            and ((:totalAmountStart is null or :totalAmountEnd is null) or o.totalAmount between :totalAmountStart and :totalAmountEnd) 
            and (:paymentMethod is null or o.paymentMethod = :paymentMethod) 
            and ((CAST(:createdAtStart as TIMESTAMP) is null or CAST(:createdAtEnd as TIMESTAMP) is null) or o.createdAt between :createdAtStart and :createdAtEnd) 
            and ((CAST(:finishedAtStart as TIMESTAMP) is null or CAST(:finishedAtEnd as TIMESTAMP) is null) or o.finishedAt between :finishedAtStart and :finishedAtEnd)
            order by o.id
            """)
    List<Order> list(
            @Param("id") Long id,
            @Param("customerId") Long customerId,
            @Param("deliveryPersonId") Long deliveryPersonId,
            @Param("productOrderId") Long productOrderId,
            @Param("deliveryStatus") DeliveryStatus deliveryStatus,
            @Param("receivedAmountStart") BigDecimal receivedAmountStart,
            @Param("receivedAmountEnd") BigDecimal receivedAmountEnd,
            @Param("totalAmountStart") BigDecimal totalAmountStart,
            @Param("totalAmountEnd") BigDecimal totalAmountEnd,
            @Param("paymentMethod") PaymentMethod paymentMethod,
            @Param("createdAtStart") LocalDateTime createdAtStart,
            @Param("createdAtEnd") LocalDateTime createdAtEnd,
            @Param("finishedAtStart") LocalDateTime finishedAtStart,
            @Param("finishedAtEnd") LocalDateTime finishedAtEnd);


}
