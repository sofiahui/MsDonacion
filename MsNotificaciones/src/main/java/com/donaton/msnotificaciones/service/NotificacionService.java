package com.donaton.msnotificaciones.service;

import com.donaton.msnotificaciones.model.DonacionEvent;
import com.donaton.msnotificaciones.model.Notificacion;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class NotificacionService {

    private final List<Notificacion> notificaciones = new ArrayList<>();

    public void procesarAlerta(DonacionEvent event) {
        String mensaje = String.format(
                "Nueva donación: %d unidades de %s en %s",
                event.getCantidad(), event.getTipoDonacion(), event.getCentroAcopio()
        );
        notificaciones.add(new Notificacion(UUID.randomUUID().toString(), mensaje));
        log.info("Notificación registrada: {}", mensaje);
    }

    public List<Notificacion> obtenerNotificaciones() {
        return Collections.unmodifiableList(notificaciones);
    }

    public void eliminarNotificacion(String id) {
        notificaciones.removeIf(n -> n.getId().equals(id));
    }
}
