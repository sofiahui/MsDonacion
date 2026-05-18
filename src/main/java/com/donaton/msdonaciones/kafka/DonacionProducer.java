package com.donaton.msdonaciones.kafka;

import com.donaton.msdonaciones.event.DonacionEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DonacionProducer {

    private final KafkaTemplate<String, DonacionEvent> kafkaTemplate;
    private static final String TOPIC = "donacion-creada";

    public void publicar(DonacionEvent event) {
        kafkaTemplate.send(TOPIC, event);
        log.info("Evento publicado en Kafka - topic: {}, evento: {}", TOPIC, event);
    }
}
