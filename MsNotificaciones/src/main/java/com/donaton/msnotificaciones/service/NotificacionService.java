package com.donaton.msnotificaciones.service;

import com.donaton.msnotificaciones.model.DonacionEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class NotificacionService {

    private final List<String> notificaciones = new ArrayList<>();

    public void procesarAlerta(DonacionEvent event) {
        String mensaje = String.format(
                "Nueva donación: %d unidades de %s en %s",
                event.getCantidad(), event.getTipoDonacion(), event.getCentroAcopio()
        );
        notificaciones.add(mensaje);
        log.info("Notificación registrada: {}", mensaje);
    }

    public List<String> obtenerNotificaciones() {
        return Collections.unmodifiableList(notificaciones);
    }
}
