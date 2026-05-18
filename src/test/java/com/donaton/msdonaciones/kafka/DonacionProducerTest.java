package com.donaton.msdonaciones.kafka;

import com.donaton.msdonaciones.event.DonacionEvent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = {"donacion-creada"})
@DirtiesContext
class DonacionProducerTest {

    @Autowired
    private DonacionProducer donacionProducer;

    @Test
    void debePublicarEventoSinExcepcion() {
        DonacionEvent event = new DonacionEvent(1L, "ropa", 10, "Centro Santiago", LocalDate.now());
        assertDoesNotThrow(() -> donacionProducer.publicar(event));
    }
}
