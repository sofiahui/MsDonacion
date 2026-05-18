package com.donaton.msnotificaciones.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DonacionEvent {
    private Long id;
    private String tipoDonacion;
    private Integer cantidad;
    private String centroAcopio;
    private LocalDate fecha;
}
