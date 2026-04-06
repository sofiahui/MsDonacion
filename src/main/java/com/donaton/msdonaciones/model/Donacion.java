package com.donaton.msdonaciones.model;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Donacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String tipoDonacion;   // ropa, alimento, medicamento
    private Integer cantidad;
    private String origen;         // persona, empresa
    private LocalDate fecha;
    private String centroAcopio;
}