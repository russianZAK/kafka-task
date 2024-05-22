package by.russianzak.kafkatask.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@EnableKafka
public class KafkaConfig {

    @Value("${topic}")
    private String topic;

    @Value("${partitions}")
    private int partitions;

    @Bean
    NewTopic createTopic() {
        return TopicBuilder.name(topic)
            .partitions(partitions)
            .build();
    }
}
