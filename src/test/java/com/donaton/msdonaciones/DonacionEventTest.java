package com.donaton.msdonaciones;

import com.donaton.msdonaciones.event.DonacionEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("DonacionEvent")
class DonacionEventTest {

    @Test
    @DisplayName("AllArgsConstructor asigna todos los campos")
    void allArgsConstructor_asignaCampos() {
        LocalDate fecha = LocalDate.of(2024, 6, 1);
        DonacionEvent e = new DonacionEvent(1L, "ropa", 10, "Centro Norte", fecha);

        assertThat(e.getId()).isEqualTo(1L);
        assertThat(e.getTipoDonacion()).isEqualTo("ropa");
        assertThat(e.getCantidad()).isEqualTo(10);
        assertThat(e.getCentroAcopio()).isEqualTo("Centro Norte");
        assertThat(e.getFecha()).isEqualTo(fecha);
    }

    @Test
    @DisplayName("NoArgsConstructor crea evento con campos null")
    void noArgsConstructor_camposNull() {
        DonacionEvent e = new DonacionEvent();

        assertThat(e.getId()).isNull();
        assertThat(e.getTipoDonacion()).isNull();
    }

    @Test
    @DisplayName("Setters y getters funcionan correctamente")
    void settersYGetters() {
        DonacionEvent e = new DonacionEvent();
        e.setId(2L);
        e.setTipoDonacion("alimento");
        e.setCantidad(5);
        e.setCentroAcopio("Centro Sur");
        e.setFecha(LocalDate.now());

        assertThat(e.getId()).isEqualTo(2L);
        assertThat(e.getTipoDonacion()).isEqualTo("alimento");
        assertThat(e.getCantidad()).isEqualTo(5);
    }

    @Test
    @DisplayName("equals y hashCode basados en el mismo contenido")
    void equalsHashCode() {
        LocalDate fecha = LocalDate.now();
        DonacionEvent e1 = new DonacionEvent(1L, "ropa", 10, "Centro Norte", fecha);
        DonacionEvent e2 = new DonacionEvent(1L, "ropa", 10, "Centro Norte", fecha);

        assertThat(e1).isEqualTo(e2);
        assertThat(e1.hashCode()).isEqualTo(e2.hashCode());
    }

    @Test
    @DisplayName("toString no lanza excepcion")
    void toStringNoLanzaExcepcion() {
        DonacionEvent e = new DonacionEvent(1L, "ropa", 10, "Centro", LocalDate.now());
        assertThat(e.toString()).isNotNull().contains("ropa");
    }
}