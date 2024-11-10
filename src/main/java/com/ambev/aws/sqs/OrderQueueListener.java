package com.ambev.aws.sqs;


import com.ambev.core.service.OrderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.annotation.PostConstruct;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;

@Service
@Log
public class OrderQueueListener {

    private final SqsClient sqsClient;
    private final String queueUrl;

    @Autowired
    private OrderService orderService;


    public OrderQueueListener(@Value("${aws.sqs.queue-url}") String queueUrl) {

        AwsBasicCredentials awsCreds = AwsBasicCredentials.create("AKIAYDWHS7IVCQCWBIXZ", "rpwstFxunhfgG31puuIdXDQ1sNvnuemZukIzigLG");

        this.queueUrl = queueUrl;
        this.sqsClient = SqsClient.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .build();
    }

    @PostConstruct
    public void startConsuming() throws JsonProcessingException {
        log.info("Iniciando o consumo das mensagens da fila...");
        consumeMessages();
    }

    @Scheduled(fixedDelay = 5000)
    public void consumeMessages() throws JsonProcessingException {
        ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
                .queueUrl(queueUrl)
                .maxNumberOfMessages(10)
                .waitTimeSeconds(20)
                .build();

        ReceiveMessageResponse response = sqsClient.receiveMessage(receiveMessageRequest);

        for (Message message : response.messages()) {
            log.info("Mensagem recebida: " + message.body());
            if(orderService.validateOrder(message.body())){
                log.info("Ordem Validada enviado para persistencia no BD...");
                orderService.processOrder(message.body());
            }
            deleteMessage(message.receiptHandle());
        }
    }

    private void deleteMessage(String receiptHandle) {
        DeleteMessageRequest deleteMessageRequest = DeleteMessageRequest.builder()
                .queueUrl(queueUrl)
                .receiptHandle(receiptHandle)
                .build();
        sqsClient.deleteMessage(deleteMessageRequest);
    }

    public void sendMessage(String messageBody) {
        SendMessageRequest sendMessageRequest = SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(messageBody)
                .build();
        sqsClient.sendMessage(sendMessageRequest);
    }
}

