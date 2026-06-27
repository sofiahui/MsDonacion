package com.donaton.msdonaciones;

import com.donaton.msdonaciones.event.DonacionEvent;
import com.donaton.msdonaciones.factory.DonacionFactory;
import com.donaton.msdonaciones.kafka.DonacionProducer;
import com.donaton.msdonaciones.model.Donacion;
import com.donaton.msdonaciones.repository.DonacionRepository;
import com.donaton.msdonaciones.service.DonacionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// Test UNITARIO puro — sin contexto Spring, sin Kafka real, sin MySQL
@ExtendWith(MockitoExtension.class)
@DisplayName("DonacionService")
class DonacionServiceTest {

    @Mock private DonacionRepository repository;
    @Mock private DonacionFactory factory;
    @Mock private DonacionProducer donacionProducer;

    @InjectMocks private DonacionService service;

    private Donacion donacionEjemplo;

    @BeforeEach
    void setUp() {
        donacionEjemplo = new Donacion(1L, "ropa", 10, "persona",
                LocalDate.now(), "Centro Norte");
    }

    @Nested
    @DisplayName("listar()")
    class Listar {

        @Test
        @DisplayName("Retorna la lista completa del repositorio")
        void listar_retornaListaDelRepositorio() {
            when(repository.findAll()).thenReturn(List.of(donacionEjemplo));

            List<Donacion> resultado = service.listar();

            assertThat(resultado).hasSize(1);
            assertThat(resultado.get(0).getTipoDonacion()).isEqualTo("ropa");
            verify(repository, times(1)).findAll();
        }

        @Test
        @DisplayName("Retorna lista vacía cuando no hay donaciones")
        void listar_retornaListaVacia_cuandoNoHayDonaciones() {
            when(repository.findAll()).thenReturn(Collections.emptyList());

            List<Donacion> resultado = service.listar();

            assertThat(resultado).isEmpty();
        }
    }

    @Nested
    @DisplayName("crear()")
    class Crear {

        @Test
        @DisplayName("Crea, guarda y publica evento en Kafka")
        void crear_guardaYPublicaEvento() {
            when(factory.crearDonacion("ropa", 10, "persona", "Centro Norte"))
                    .thenReturn(donacionEjemplo);
            when(repository.save(donacionEjemplo)).thenReturn(donacionEjemplo);
            doNothing().when(donacionProducer).publicar(any(DonacionEvent.class));

            Donacion resultado = service.crear("ropa", 10, "persona", "Centro Norte");

            assertThat(resultado.getId()).isEqualTo(1L);
            assertThat(resultado.getTipoDonacion()).isEqualTo("ropa");
            assertThat(resultado.getCantidad()).isEqualTo(10);
            assertThat(resultado.getOrigen()).isEqualTo("persona");
            assertThat(resultado.getCentroAcopio()).isEqualTo("Centro Norte");

            verify(factory).crearDonacion("ropa", 10, "persona", "Centro Norte");
            verify(repository).save(donacionEjemplo);
            verify(donacionProducer).publicar(any(DonacionEvent.class));
        }

        @Test
        @DisplayName("El evento publicado contiene los datos correctos de la donacion")
        void crear_eventoContieneLosDatosCorrectos() {
            when(factory.crearDonacion(any(), anyInt(), any(), any()))
                    .thenReturn(donacionEjemplo);
            when(repository.save(any())).thenReturn(donacionEjemplo);

            service.crear("ropa", 10, "persona", "Centro Norte");

            verify(donacionProducer).publicar(argThat(event ->
                    event.getId().equals(1L) &&
                    "ropa".equals(event.getTipoDonacion()) &&
                    event.getCantidad().equals(10) &&
                    "Centro Norte".equals(event.getCentroAcopio())
            ));
        }

        @Test
        @DisplayName("save() se llama exactamente una vez con la donacion de la factory")
        void crear_llamaSaveUnaVez() {
            when(factory.crearDonacion(any(), anyInt(), any(), any()))
                    .thenReturn(donacionEjemplo);
            when(repository.save(any())).thenReturn(donacionEjemplo);

            service.crear("alimento", 5, "empresa", "Centro Sur");

            verify(repository, times(1)).save(donacionEjemplo);
        }

        @Test
        @DisplayName("publicar() se llama exactamente una vez tras guardar")
        void crear_publicaEventoUnaVez() {
            when(factory.crearDonacion(any(), anyInt(), any(), any()))
                    .thenReturn(donacionEjemplo);
            when(repository.save(any())).thenReturn(donacionEjemplo);

            service.crear("medicamento", 3, "persona", "Centro Este");

            verify(donacionProducer, times(1)).publicar(any(DonacionEvent.class));
        }
    }

    @Nested
    @DisplayName("listarConCircuitBreaker()")
    class CircuitBreaker {

        @Test
        @DisplayName("Retorna lista del repositorio cuando funciona correctamente")
        void listarConCircuitBreaker_retornaLista() {
            when(repository.findAll()).thenReturn(List.of(donacionEjemplo));

            List<Donacion> resultado = service.listarConCircuitBreaker();

            assertThat(resultado).hasSize(1);
        }

        @Test
        @DisplayName("fallbackListar retorna lista vacia ante cualquier excepcion")
        void fallbackListar_retornaListaVacia() {
            List<Donacion> resultado = service.fallbackListar(new RuntimeException("fallo"));

            assertThat(resultado).isEmpty();
        }
    }
}