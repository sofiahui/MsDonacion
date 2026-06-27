package com.donaton.msnotificaciones;

import com.donaton.msnotificaciones.model.DonacionEvent;
import com.donaton.msnotificaciones.model.Notificacion;
import com.donaton.msnotificaciones.service.NotificacionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

// Test unitario puro — sin Spring, sin Kafka
@DisplayName("NotificacionService")
class NotificacionServiceTest {

    private NotificacionService service;
    private DonacionEvent eventEjemplo;

    @BeforeEach
    void setUp() {
        service = new NotificacionService(); // instancia fresca por test
        eventEjemplo = new DonacionEvent(1L, "ropa", 5, "Centro Norte", LocalDate.now());
    }

    @Nested
    @DisplayName("procesarAlerta()")
    class ProcesarAlerta {

        @Test
        @DisplayName("Guarda una notificacion al procesar un evento")
        void procesarAlerta_guardaNotificacion() {
            service.procesarAlerta(eventEjemplo);

            assertThat(service.obtenerNotificaciones()).hasSize(1);
        }

        @Test
        @DisplayName("El mensaje tiene el formato correcto")
        void procesarAlerta_mensajeFormateadoCorrectamente() {
            DonacionEvent event = new DonacionEvent(2L, "alimento", 20, "Centro Sur", LocalDate.now());
            service.procesarAlerta(event);

            String mensaje = service.obtenerNotificaciones().get(0).getMensaje();
            assertThat(mensaje).isEqualTo("Nueva donación: 20 unidades de alimento en Centro Sur");
        }

        @Test
        @DisplayName("Multiples eventos generan multiples notificaciones")
        void procesarAlerta_variasVeces_acumulaNotificaciones() {
            service.procesarAlerta(new DonacionEvent(1L, "ropa", 5, "Centro Norte", LocalDate.now()));
            service.procesarAlerta(new DonacionEvent(2L, "alimento", 10, "Centro Sur", LocalDate.now()));
            service.procesarAlerta(new DonacionEvent(3L, "medicamento", 3, "Centro Este", LocalDate.now()));

            assertThat(service.obtenerNotificaciones()).hasSize(3);
        }

        @Test
        @DisplayName("La notificacion generada tiene un id UUID no nulo")
        void procesarAlerta_asignaIdUUID() {
            service.procesarAlerta(eventEjemplo);

            Notificacion n = service.obtenerNotificaciones().get(0);
            assertThat(n.getId()).isNotNull().isNotBlank();
        }

        @Test
        @DisplayName("Cada notificacion tiene un id UUID distinto")
        void procesarAlerta_cadaNotificacionTieneIdUnico() {
            service.procesarAlerta(eventEjemplo);
            service.procesarAlerta(eventEjemplo);

            List<Notificacion> lista = service.obtenerNotificaciones();
            assertThat(lista.get(0).getId()).isNotEqualTo(lista.get(1).getId());
        }

        @Test
        @DisplayName("El mensaje incluye la cantidad correcta")
        void procesarAlerta_mensajeIncluyCantidad() {
            service.procesarAlerta(new DonacionEvent(1L, "ropa", 99, "Centro X", LocalDate.now()));

            assertThat(service.obtenerNotificaciones().get(0).getMensaje()).contains("99");
        }

        @Test
        @DisplayName("El mensaje incluye el tipo de donacion")
        void procesarAlerta_mensajeIncluyeTipo() {
            service.procesarAlerta(new DonacionEvent(1L, "medicamento", 5, "Centro X", LocalDate.now()));

            assertThat(service.obtenerNotificaciones().get(0).getMensaje()).contains("medicamento");
        }

        @Test
        @DisplayName("El mensaje incluye el centro de acopio")
        void procesarAlerta_mensajeIncluyCentro() {
            service.procesarAlerta(new DonacionEvent(1L, "ropa", 5, "Centro Especial", LocalDate.now()));

            assertThat(service.obtenerNotificaciones().get(0).getMensaje()).contains("Centro Especial");
        }
    }

    @Nested
    @DisplayName("obtenerNotificaciones()")
    class ObtenerNotificaciones {

        @Test
        @DisplayName("Retorna lista vacia cuando no hay notificaciones")
        void obtenerNotificaciones_listaVaciaInicial() {
            assertThat(service.obtenerNotificaciones()).isEmpty();
        }

        @Test
        @DisplayName("La lista retornada es inmutable")
        void obtenerNotificaciones_listaEsInmutable() {
            service.procesarAlerta(eventEjemplo);
            List<Notificacion> lista = service.obtenerNotificaciones();

            assertThatThrownBy(() -> lista.add(new Notificacion("x", "msg")))
                    .isInstanceOf(UnsupportedOperationException.class);
        }

        @Test
        @DisplayName("Retorna todas las notificaciones en orden de insercion")
        void obtenerNotificaciones_mantieneOrden() {
            service.procesarAlerta(new DonacionEvent(1L, "ropa", 5, "Norte", LocalDate.now()));
            service.procesarAlerta(new DonacionEvent(2L, "alimento", 10, "Sur", LocalDate.now()));

            List<Notificacion> lista = service.obtenerNotificaciones();
            assertThat(lista.get(0).getMensaje()).contains("ropa");
            assertThat(lista.get(1).getMensaje()).contains("alimento");
        }
    }

    @Nested
    @DisplayName("eliminarNotificacion()")
    class EliminarNotificacion {

        @Test
        @DisplayName("Elimina la notificacion con el id indicado")
        void eliminarNotificacion_eliminaCorrectamente() {
            service.procesarAlerta(eventEjemplo);
            String id = service.obtenerNotificaciones().get(0).getId();

            service.eliminarNotificacion(id);

            assertThat(service.obtenerNotificaciones()).isEmpty();
        }

        @Test
        @DisplayName("Elimina solo la notificacion indicada, no las demas")
        void eliminarNotificacion_soloEliminaLaIndicada() {
            service.procesarAlerta(new DonacionEvent(1L, "ropa", 5, "Norte", LocalDate.now()));
            service.procesarAlerta(new DonacionEvent(2L, "alimento", 10, "Sur", LocalDate.now()));

            String idEliminar = service.obtenerNotificaciones().get(0).getId();
            service.eliminarNotificacion(idEliminar);

            assertThat(service.obtenerNotificaciones()).hasSize(1);
            assertThat(service.obtenerNotificaciones().get(0).getMensaje()).contains("alimento");
        }

        @Test
        @DisplayName("No lanza excepcion si el id no existe")
        void eliminarNotificacion_idInexistente_noLanzaExcepcion() {
            service.procesarAlerta(eventEjemplo);

            assertThatCode(() -> service.eliminarNotificacion("id-que-no-existe"))
                    .doesNotThrowAnyException();

            assertThat(service.obtenerNotificaciones()).hasSize(1);
        }

        @Test
        @DisplayName("Lista queda vacia al eliminar la unica notificacion")
        void eliminarNotificacion_listaVaciaAlEliminarUnica() {
            service.procesarAlerta(eventEjemplo);
            String id = service.obtenerNotificaciones().get(0).getId();

            service.eliminarNotificacion(id);

            assertThat(service.obtenerNotificaciones()).isEmpty();
        }
    }
}