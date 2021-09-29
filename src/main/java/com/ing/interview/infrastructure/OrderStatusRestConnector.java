package com.ing.interview.infrastructure;

import com.ing.interview.infrastructure.dto.OrderStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class OrderStatusRestConnector {

    public OrderStatus checkOrderStatus(Long id) {
        return id % 2 == 0
            ? OrderStatus.builder().assignedTo("Sergi").stage("processing").lastUpdate(LocalDateTime.now()).build()
            : OrderStatus.builder().assignedTo("Tomas").stage("pending").lastUpdate(LocalDateTime.now()).build();
    }

}
