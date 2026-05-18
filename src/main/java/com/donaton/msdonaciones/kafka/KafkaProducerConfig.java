package com.donaton.msdonaciones.kafka;

import com.donaton.msdonaciones.event.DonacionEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public ProducerFactory<String, DonacionEvent> producerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        JsonSerializer<DonacionEvent> valueSerializer = new JsonSerializer<>(mapper);

        return new DefaultKafkaProducerFactory<>(props, new StringSerializer(), valueSerializer);
    }

    @Bean
    public KafkaTemplate<String, DonacionEvent> kafkaTemplate(ProducerFactory<String, DonacionEvent> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }
}