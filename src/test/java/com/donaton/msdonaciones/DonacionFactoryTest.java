package com.donaton.msdonaciones;

import com.donaton.msdonaciones.factory.DonacionFactory;
import com.donaton.msdonaciones.model.Donacion;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;


class DonacionFactoryTest {

    private DonacionFactory factory = new DonacionFactory();

    @Test
    void crearDonacion_debeTenerFechaDeHoy() {
        Donacion d = factory.crearDonacion("alimento", 5, "empresa", "Centro Sur");
        assertEquals(LocalDate.now(), d.getFecha());
    }

    @Test
    void crearDonacion_debeAsignarCamposCorrectamente() {
        Donacion d = factory.crearDonacion("ropa", 3, "persona", "Centro Norte");
        assertEquals("ropa", d.getTipoDonacion());
        assertEquals(3, d.getCantidad());
        assertEquals("persona", d.getOrigen());
    }
}