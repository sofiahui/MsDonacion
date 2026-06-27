package com.donaton.msnotificaciones;

import com.donaton.msnotificaciones.consumer.DonacionEventConsumer;
import com.donaton.msnotificaciones.model.DonacionEvent;
import com.donaton.msnotificaciones.service.NotificacionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("DonacionEventConsumer")
class DonacionEventConsumerTest {

    @Mock
    private NotificacionService notificacionService;

    @InjectMocks
    private DonacionEventConsumer consumer;

    @Test
    @DisplayName("consumir delega el evento al servicio de notificaciones")
    void consumir_delegaAlServicio() {
        DonacionEvent event = new DonacionEvent(1L, "ropa", 5, "Centro Norte", LocalDate.now());

        consumer.consumir(event);

        verify(notificacionService, times(1)).procesarAlerta(event);
    }

    @Test
    @DisplayName("consumir llama procesarAlerta exactamente una vez por evento")
    void consumir_llamaProcesarAlertaUnaVez() {
        DonacionEvent event = new DonacionEvent(2L, "alimento", 10, "Centro Sur", LocalDate.now());

        consumer.consumir(event);

        verify(notificacionService, only()).procesarAlerta(event);
    }

    @Test
    @DisplayName("consumir no lanza excepcion con evento valido")
    void consumir_noLanzaExcepcion() {
        DonacionEvent event = new DonacionEvent(3L, "medicamento", 3, "Centro Este", LocalDate.now());

        org.assertj.core.api.Assertions
                .assertThatCode(() -> consumer.consumir(event))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("consumir pasa el evento exacto al servicio sin modificarlo")
    void consumir_pasaElEventoExacto() {
        DonacionEvent event = new DonacionEvent(4L, "juguete", 15, "Centro Oeste", LocalDate.now());

        consumer.consumir(event);

        verify(notificacionService).procesarAlerta(argThat(e ->
                e.getId().equals(4L) &&
                "juguete".equals(e.getTipoDonacion()) &&
                e.getCantidad().equals(15) &&
                "Centro Oeste".equals(e.getCentroAcopio())
        ));
    }
}