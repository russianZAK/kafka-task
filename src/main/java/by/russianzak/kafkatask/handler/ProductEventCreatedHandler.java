package by.russianzak.kafkatask.handler;

import by.russianzak.kafkatask.event.ProductCreateEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@KafkaListener(groupId = "${consumer.first.group.id}", topics = "${topic}")
public class ProductEventCreatedHandler {
    @Value("${topic}")
    private String topic;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public ProductEventCreatedHandler(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaHandler()
    public void handler(ProductCreateEvent productCreateEvent) {
        if (!productCreateEvent.isResponse()) {
            productCreateEvent.setResponse(true);
            productCreateEvent.setTitle(productCreateEvent.getTitle() + "RESPONDED");
            kafkaTemplate.send(topic, productCreateEvent);
        }
    }
}
