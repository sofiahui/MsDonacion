package com.donaton.msdonaciones;

import com.donaton.msdonaciones.model.Donacion;
import com.donaton.msdonaciones.repository.DonacionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class DonacionRepositoryTest {

    @Autowired
    private DonacionRepository repository;

    @BeforeEach
    void limpiar() {
        repository.deleteAll();
    }

    @Test
    void findByCentroAcopio_debeRetornarDonacionesDelCentro() {
        Donacion d = new Donacion();
        d.setTipoDonacion("ropa");
        d.setCantidad(5);
        d.setOrigen("persona");
        d.setFecha(LocalDate.now());
        d.setCentroAcopio("Centro Norte");
        repository.save(d);

        List<Donacion> resultado = repository.findByCentroAcopio("Centro Norte");
        assertFalse(resultado.isEmpty());
    }
}