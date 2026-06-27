package com.donaton.msnotificaciones.controller;

import com.donaton.msnotificaciones.model.Notificacion;
import com.donaton.msnotificaciones.service.NotificacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/notificaciones")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class NotificacionController {

    private final NotificacionService notificacionService;

    @GetMapping
    public List<Notificacion> obtener() {
        return notificacionService.obtenerNotificaciones();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        notificacionService.eliminarNotificacion(id);
        return ResponseEntity.noContent().build();
    }
}
