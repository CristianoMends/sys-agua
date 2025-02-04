package com.api.sysagua.dto.order;

import com.api.sysagua.enumeration.OrderStatus;
import com.api.sysagua.model.Customer;
import com.api.sysagua.model.DeliveryPerson;
import com.api.sysagua.model.Order;
import com.api.sysagua.model.Product;
import com.api.sysagua.repository.CustomerRepository;
import com.api.sysagua.repository.DeliveryPersonRepository;
import com.api.sysagua.repository.OrderRepository;
import com.api.sysagua.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;


import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateOrderDto {

    private CustomerRepository customerRepository;
    private OrderRepository orderRepository;
   // private ProductRepository productRepository;
    private DeliveryPersonRepository deliveryPersonRepository;

    @Schema(description = "Id do pedido", example = "4213")
    private Long id;

    @Schema(description = "Cliente para o qual o pedido é destinado.", example = "Joao")
    private Long customerId;

    @Schema(description = "Entregador responsável pelo pedido", example = "Pedro")
    private Long deliveryPersonId;

    @Schema(description = "Produtos pedidos", example = "Garrafao 20l")
    private List<Long> productsId;

    @Schema(description = "Status do pedido", example = "realizado")
    private OrderStatus status;

    @Schema(description = "Valor recebido do pedido", example = "50.00")
    private BigDecimal receivedAmount;

    @Schema(description = "Valor total do pedido", example = "80.00")
    private BigDecimal totalAmount;

    public void update(Long orderId, UpdateOrderDto dto){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado"));

        Customer customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));

        /*List<Product> products = productRepository.findAllById(dto.getProductsId());
        if (products.isEmpty()) {
            throw new EntityNotFoundException("Nenhum produto encontrado para os IDs fornecidos");
        }
*/
        DeliveryPerson deliveryPerson = deliveryPersonRepository.findById(dto.getDeliveryPersonId())
                .orElseThrow(() -> new EntityNotFoundException("Entregador não encontrado"));

        order.setCustomer(customer);
       // order.setProductOrders(products);
        order.setDeliveryPerson(deliveryPerson);
        order.setStatus(dto.getStatus());

        orderRepository.save(order);
    }

}
