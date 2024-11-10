package com.ambev.aws.controller;

import com.ambev.aws.sqs.OrderQueueListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sqs")
public class SqsController {

    private final OrderQueueListener orderQueueListener;

    @Autowired
    public SqsController(OrderQueueListener orderQueueListener) {
        this.orderQueueListener = orderQueueListener;
    }

    @PostMapping("/send")
    public String sendMessage(@RequestBody String message) {
        orderQueueListener.sendMessage(message);
        return "Mensagem enviada para a fila!";
    }
}
