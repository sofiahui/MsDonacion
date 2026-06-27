package com.donaton.msdonaciones;

import com.donaton.msdonaciones.factory.DonacionFactory;
import com.donaton.msdonaciones.model.Donacion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

// Test unitario puro — sin Spring
@DisplayName("DonacionFactory")
class DonacionFactoryTest {

    private DonacionFactory factory;

    @BeforeEach
    void setUp() {
        factory = new DonacionFactory();
    }

    @Test
    @DisplayName("crearDonacion asigna todos los campos correctamente")
    void crearDonacion_asignaCamposCorrectos() {
        Donacion d = factory.crearDonacion("ropa", 10, "persona", "Centro Norte");

        assertThat(d.getTipoDonacion()).isEqualTo("ropa");
        assertThat(d.getCantidad()).isEqualTo(10);
        assertThat(d.getOrigen()).isEqualTo("persona");
        assertThat(d.getCentroAcopio()).isEqualTo("Centro Norte");
    }

    @Test
    @DisplayName("crearDonacion asigna la fecha de hoy automaticamente")
    void crearDonacion_asignaFechaDeHoy() {
        Donacion d = factory.crearDonacion("alimento", 5, "empresa", "Centro Sur");

        assertThat(d.getFecha()).isEqualTo(LocalDate.now());
    }

    @Test
    @DisplayName("crearDonacion no asigna id (lo hace JPA al persistir)")
    void crearDonacion_idEsNull() {
        Donacion d = factory.crearDonacion("medicamento", 3, "persona", "Centro Este");

        assertThat(d.getId()).isNull();
    }

    @Test
    @DisplayName("crearDonacion con tipo alimento")
    void crearDonacion_tipoAlimento() {
        Donacion d = factory.crearDonacion("alimento", 20, "empresa", "Centro Oeste");

        assertThat(d.getTipoDonacion()).isEqualTo("alimento");
        assertThat(d.getOrigen()).isEqualTo("empresa");
    }

    @Test
    @DisplayName("crearDonacion con tipo medicamento")
    void crearDonacion_tipoMedicamento() {
        Donacion d = factory.crearDonacion("medicamento", 1, "persona", "Centro Norte");

        assertThat(d.getTipoDonacion()).isEqualTo("medicamento");
        assertThat(d.getCantidad()).isEqualTo(1);
    }
}