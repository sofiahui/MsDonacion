package com.donaton.msnotificaciones.consumer;

import com.donaton.msnotificaciones.model.DonacionEvent;
import com.donaton.msnotificaciones.service.NotificacionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DonacionEventConsumer {

    private final NotificacionService notificacionService;

    @KafkaListener(topics = "donacion-creada", groupId = "notificaciones-group")
    public void consumir(DonacionEvent event) {
        log.info("Evento recibido desde Kafka: {}", event);
        notificacionService.procesarAlerta(event);
    }
}
