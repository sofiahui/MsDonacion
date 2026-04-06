package com.donaton.msdonaciones;

import  com.donaton.msdonaciones.model.Donacion;
import com.donaton.msdonaciones.repository.DonacionRepository;
import com.donaton.msdonaciones.service.DonacionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DonacionServiceTest {

    @Autowired
    private DonacionService service;

    @Autowired
    private DonacionRepository repository;

    @BeforeEach
    void limpiar() {
        repository.deleteAll();
    }

    @Test
    void crearDonacion_debeGuardarCorrectamente() {
        Donacion d = service.crear("ropa", 10, "persona", "Centro Norte");
        assertNotNull(d.getId());
        assertEquals("ropa", d.getTipoDonacion());
    }

    @Test
    void listar_debeRetornarDonaciones() {
        service.crear("alimento", 5, "empresa", "Centro Sur");
        List<Donacion> lista = service.listar();
        assertFalse(lista.isEmpty());
    }
}