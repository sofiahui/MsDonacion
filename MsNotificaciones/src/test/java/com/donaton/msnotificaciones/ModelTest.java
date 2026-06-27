package com.donaton.msnotificaciones;

import com.donaton.msnotificaciones.model.DonacionEvent;
import com.donaton.msnotificaciones.model.Notificacion;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Modelos")
class ModelTest {

    @Nested
    @DisplayName("Notificacion")
    class NotificacionTests {

        @Test
        @DisplayName("AllArgsConstructor asigna todos los campos")
        void allArgs() {
            Notificacion n = new Notificacion("uuid-123", "mensaje test");
            assertThat(n.getId()).isEqualTo("uuid-123");
            assertThat(n.getMensaje()).isEqualTo("mensaje test");
        }

        @Test
        @DisplayName("NoArgsConstructor crea objeto con campos null")
        void noArgs() {
            Notificacion n = new Notificacion();
            assertThat(n.getId()).isNull();
            assertThat(n.getMensaje()).isNull();
        }

        @Test
        @DisplayName("Setters funcionan correctamente")
        void setters() {
            Notificacion n = new Notificacion();
            n.setId("id-1");
            n.setMensaje("nuevo mensaje");
            assertThat(n.getId()).isEqualTo("id-1");
            assertThat(n.getMensaje()).isEqualTo("nuevo mensaje");
        }

        @Test
        @DisplayName("equals y hashCode basados en contenido")
        void equalsHashCode() {
            Notificacion n1 = new Notificacion("id-1", "msg");
            Notificacion n2 = new Notificacion("id-1", "msg");
            assertThat(n1).isEqualTo(n2);
            assertThat(n1.hashCode()).isEqualTo(n2.hashCode());
        }

        @Test
        @DisplayName("toString no lanza excepcion")
        void toStringWorks() {
            assertThat(new Notificacion("id", "msg").toString()).isNotNull();
        }
    }

    @Nested
    @DisplayName("DonacionEvent")
    class DonacionEventTests {

        @Test
        @DisplayName("AllArgsConstructor asigna todos los campos")
        void allArgs() {
            LocalDate fecha = LocalDate.of(2024, 6, 1);
            DonacionEvent e = new DonacionEvent(1L, "ropa", 10, "Centro Norte", fecha);

            assertThat(e.getId()).isEqualTo(1L);
            assertThat(e.getTipoDonacion()).isEqualTo("ropa");
            assertThat(e.getCantidad()).isEqualTo(10);
            assertThat(e.getCentroAcopio()).isEqualTo("Centro Norte");
            assertThat(e.getFecha()).isEqualTo(fecha);
        }

        @Test
        @DisplayName("NoArgsConstructor crea objeto con campos null")
        void noArgs() {
            DonacionEvent e = new DonacionEvent();
            assertThat(e.getId()).isNull();
            assertThat(e.getTipoDonacion()).isNull();
        }

        @Test
        @DisplayName("Setters funcionan correctamente")
        void setters() {
            DonacionEvent e = new DonacionEvent();
            e.setId(5L);
            e.setTipoDonacion("alimento");
            e.setCantidad(20);
            e.setCentroAcopio("Centro Sur");
            e.setFecha(LocalDate.now());

            assertThat(e.getId()).isEqualTo(5L);
            assertThat(e.getTipoDonacion()).isEqualTo("alimento");
        }

        @Test
        @DisplayName("equals y hashCode basados en contenido")
        void equalsHashCode() {
            LocalDate fecha = LocalDate.now();
            DonacionEvent e1 = new DonacionEvent(1L, "ropa", 10, "Norte", fecha);
            DonacionEvent e2 = new DonacionEvent(1L, "ropa", 10, "Norte", fecha);
            assertThat(e1).isEqualTo(e2);
            assertThat(e1.hashCode()).isEqualTo(e2.hashCode());
        }

        @Test
        @DisplayName("toString no lanza excepcion y contiene el tipo")
        void toStringWorks() {
            DonacionEvent e = new DonacionEvent(1L, "ropa", 5, "Norte", LocalDate.now());
            assertThat(e.toString()).isNotNull().contains("ropa");
        }
    }
}