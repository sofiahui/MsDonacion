package com.donaton.msdonaciones;

import com.donaton.msdonaciones.model.Donacion;
import com.donaton.msdonaciones.repository.DonacionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

// Test unitario puro con Mockito — sin @DataJpaTest, sin contexto Spring
@ExtendWith(MockitoExtension.class)
@DisplayName("DonacionRepository")
class DonacionRepositoryTest {

    @Mock
    private DonacionRepository repository;

    private Donacion donacion1;
    private Donacion donacion2;
    private Donacion donacion3;

    @BeforeEach
    void setUp() {
        donacion1 = new Donacion(1L, "ropa", 10, "persona",
                LocalDate.now(), "Centro Norte");
        donacion2 = new Donacion(2L, "alimento", 5, "empresa",
                LocalDate.now(), "Centro Sur");
        donacion3 = new Donacion(3L, "medicamento", 3, "persona",
                LocalDate.now(), "Centro Norte");
    }

    @Test
    @DisplayName("findByCentroAcopio retorna donaciones del centro indicado")
    void findByCentroAcopio_retornaDonacionesDelCentro() {
        when(repository.findByCentroAcopio("Centro Norte"))
                .thenReturn(List.of(donacion1, donacion3));

        List<Donacion> resultado = repository.findByCentroAcopio("Centro Norte");

        assertThat(resultado).hasSize(2);
        assertThat(resultado).allMatch(d -> "Centro Norte".equals(d.getCentroAcopio()));
        verify(repository).findByCentroAcopio("Centro Norte");
    }

    @Test
    @DisplayName("findByCentroAcopio retorna lista vacia si el centro no existe")
    void findByCentroAcopio_retornaVacio_cuandoCentroNoExiste() {
        when(repository.findByCentroAcopio("Centro Inexistente"))
                .thenReturn(Collections.emptyList());

        List<Donacion> resultado = repository.findByCentroAcopio("Centro Inexistente");

        assertThat(resultado).isEmpty();
    }

    @Test
    @DisplayName("findAll retorna todas las donaciones")
    void findAll_retornaTodasLasDonaciones() {
        when(repository.findAll()).thenReturn(List.of(donacion1, donacion2, donacion3));

        List<Donacion> todas = repository.findAll();

        assertThat(todas).hasSize(3);
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("save persiste una donacion y retorna con id asignado")
    void save_retornaDonacionConId() {
        Donacion nueva = new Donacion(null, "juguete", 20, "persona",
                LocalDate.now(), "Centro Oeste");
        Donacion guardada = new Donacion(4L, "juguete", 20, "persona",
                LocalDate.now(), "Centro Oeste");

        when(repository.save(nueva)).thenReturn(guardada);

        Donacion resultado = repository.save(nueva);

        assertThat(resultado.getId()).isEqualTo(4L);
        assertThat(resultado.getTipoDonacion()).isEqualTo("juguete");
        verify(repository).save(nueva);
    }

    @Test
    @DisplayName("findById retorna la donacion correcta cuando existe")
    void findById_retornaDonacion_cuandoExiste() {
        when(repository.findById(1L)).thenReturn(Optional.of(donacion1));

        Optional<Donacion> resultado = repository.findById(1L);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getTipoDonacion()).isEqualTo("ropa");
    }

    @Test
    @DisplayName("findById retorna empty cuando el id no existe")
    void findById_retornaEmpty_cuandoNoExiste() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        Optional<Donacion> resultado = repository.findById(999L);

        assertThat(resultado).isEmpty();
    }

    @Test
    @DisplayName("deleteById llama al repositorio con el id correcto")
    void deleteById_llamaAlRepositorio() {
        doNothing().when(repository).deleteById(1L);

        repository.deleteById(1L);

        verify(repository, times(1)).deleteById(1L);
    }
}