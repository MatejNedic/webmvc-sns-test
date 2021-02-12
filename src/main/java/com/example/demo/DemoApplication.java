package com.example.demo;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import io.awspring.cloud.messaging.core.QueueMessagingTemplate;
import io.awspring.cloud.messaging.listener.annotation.SqsListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.support.MessageBuilder;

@SpringBootApplication
public class DemoApplication {

    private final QueueMessagingTemplate queueMessagingTemplate;

    @Autowired
    public DemoApplication(AmazonSQSAsync amazonSqs, AmazonSNS amazonSNSAsync) {
        this.queueMessagingTemplate = new QueueMessagingTemplate(amazonSqs);
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(DemoApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void sendMessage() {
        this.queueMessagingTemplate.send("InfrastructureStack-spring-aws",
                MessageBuilder.withPayload("Spring cloud Aws SQS sample!").build());
    }

    @SqsListener("InfrastructureStack-spring-aws")
    private void listenToMessage(String message) {
        LOGGER.info("This is message you want to see: {}", message);
    }


}
