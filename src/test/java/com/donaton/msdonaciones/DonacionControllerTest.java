package com.donaton.msdonaciones;

import com.donaton.msdonaciones.model.Donacion;
import com.donaton.msdonaciones.repository.DonacionRepository;
import com.donaton.msdonaciones.service.DonacionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DonacionControllerTest {

    @Autowired
    private DonacionService service;

    @Autowired
    private DonacionRepository repository;

    @BeforeEach
    void limpiar() {
        repository.deleteAll();
    }

    @Test
    void crearDonacion_debeGuardarYRetornarConId() {
        Donacion d = service.crear("ropa", 10, "persona", "Centro Norte");
        assertNotNull(d.getId());
        assertEquals("ropa", d.getTipoDonacion());
    }

    @Test
    void listarDonaciones_debeRetornarListaNoNula() {
        service.crear("alimento", 5, "empresa", "Centro Sur");
        assertFalse(service.listar().isEmpty());
    }
}