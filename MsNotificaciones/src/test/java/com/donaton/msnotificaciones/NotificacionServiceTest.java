package com.donaton.msnotificaciones;

import com.donaton.msnotificaciones.model.DonacionEvent;
import com.donaton.msnotificaciones.service.NotificacionService;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class NotificacionServiceTest {

    private final NotificacionService service = new NotificacionService();

    @Test
    void debeGuardarNotificacionAlProcesarAlerta() {
        DonacionEvent event = new DonacionEvent(1L, "ropa", 5, "Centro Norte", LocalDate.now());
        service.procesarAlerta(event);
        assertFalse(service.obtenerNotificaciones().isEmpty());
    }

    @Test
    void debeMostrarMensajeFormateadoCorrectamente() {
        DonacionEvent event = new DonacionEvent(2L, "alimento", 20, "Centro Sur", LocalDate.now());
        service.procesarAlerta(event);
        String mensaje = service.obtenerNotificaciones().get(0);
        assertEquals("Nueva donación: 20 unidades de alimento en Centro Sur", mensaje);
    }
}
