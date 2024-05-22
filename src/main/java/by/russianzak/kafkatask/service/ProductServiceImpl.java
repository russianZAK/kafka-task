package by.russianzak.kafkatask.service;

import by.russianzak.kafkatask.event.ProductCreateEvent;
import by.russianzak.kafkatask.exception.TimeOutException;
import by.russianzak.kafkatask.service.dto.CreateProductRequestDto;
import by.russianzak.kafkatask.service.dto.CreateProductResponseDto;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@KafkaListener(groupId = "${consumer.second.group.id}", topics = "${topic}")
public class ProductServiceImpl implements ProductService {
    @Value("${topic}")
    private String topic;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final Map<String, CompletableFuture<ProductCreateEvent>> responseMap = new ConcurrentHashMap<>();

    public ProductServiceImpl(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public CreateProductResponseDto createProduct(CreateProductRequestDto createProductRequestDto)
        throws InterruptedException, TimeOutException, ExecutionException {

        // TODO: SAVE TO DATABASE

        String productId = UUID.randomUUID().toString();
        CompletableFuture<ProductCreateEvent> responseFuture = new CompletableFuture<>();
        responseMap.put(productId, responseFuture);

        ProductCreateEvent productCreateEvent = new ProductCreateEvent(productId, createProductRequestDto.getTitle(), createProductRequestDto.getPrice(), createProductRequestDto.getQuantity());

        kafkaTemplate.send(topic, productCreateEvent);

        try {
            ProductCreateEvent response = responseFuture.get(3, TimeUnit.SECONDS);
            return new CreateProductResponseDto(UUID.fromString(response.getProductId()), response.getTitle(), response.getPrice(), response.getQuantity());
        } catch (TimeoutException e) {
            throw new TimeOutException("Timeout", e);
        } finally {
            responseMap.remove(productId);
        }
    }

    @KafkaHandler
    public void handler(ProductCreateEvent productCreateEventResponse) {
        if (productCreateEventResponse.isResponse()) {
            CompletableFuture<ProductCreateEvent> responseFuture = responseMap.get(productCreateEventResponse.getProductId());
            if (responseFuture != null) {
                responseFuture.complete(productCreateEventResponse);
            }
        }
    }
}
