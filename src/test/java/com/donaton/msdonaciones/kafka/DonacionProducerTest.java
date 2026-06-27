package com.donaton.msdonaciones.kafka;

import com.donaton.msdonaciones.event.DonacionEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

// Test unitario puro del producer — sin Kafka real
@ExtendWith(MockitoExtension.class)
@DisplayName("DonacionProducer")
class DonacionProducerTest {

    @Mock
    private KafkaTemplate<String, DonacionEvent> kafkaTemplate;

    @InjectMocks
    private DonacionProducer producer;

    private DonacionEvent evento;

    @BeforeEach
    void setUp() {
        evento = new DonacionEvent(1L, "ropa", 10, "Centro Norte", LocalDate.now());
    }

    @Test
    @DisplayName("publicar envia el evento al topic correcto")
    void publicar_enviaAlTopicCorrecto() {
        producer.publicar(evento);

        verify(kafkaTemplate, times(1)).send(eq("donacion-creada"), eq(evento));
    }

    @Test
    @DisplayName("publicar llama a kafkaTemplate.send exactamente una vez")
    void publicar_llamaSendUnaVez() {
        producer.publicar(evento);

        verify(kafkaTemplate, times(1)).send(anyString(), any(DonacionEvent.class));
    }

    @Test
    @DisplayName("publicar no lanza excepcion con evento valido")
    void publicar_noLanzaExcepcionConEventoValido() {
        org.assertj.core.api.Assertions
            .assertThatCode(() -> producer.publicar(evento))
            .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("publicar envia el evento exacto que recibe")
    void publicar_enviaElEventoExacto() {
        producer.publicar(evento);

        verify(kafkaTemplate).send(anyString(), argThat(e ->
                e.getId().equals(1L) &&
                "ropa".equals(e.getTipoDonacion()) &&
                e.getCantidad().equals(10) &&
                "Centro Norte".equals(e.getCentroAcopio())
        ));
    }
}