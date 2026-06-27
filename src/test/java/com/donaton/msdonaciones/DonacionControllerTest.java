package com.donaton.msdonaciones;

import com.donaton.msdonaciones.controller.DonacionController;
import com.donaton.msdonaciones.model.Donacion;
import com.donaton.msdonaciones.service.DonacionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// Test UNITARIO con MockMvc standalone — sin contexto Spring ni Kafka
@ExtendWith(MockitoExtension.class)
@DisplayName("DonacionController")
class DonacionControllerTest {

    @Mock
    private DonacionService service;

    @InjectMocks
    private DonacionController controller;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private Donacion donacionEjemplo;

    @BeforeEach
    void setUp() {
        // MockMvc standalone: no necesita @WebMvcTest ni contexto Spring
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        // ObjectMapper con soporte de LocalDate (Java 8 time)
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        donacionEjemplo = new Donacion(1L, "ropa", 10, "persona",
                LocalDate.of(2024, 1, 15), "Centro Norte");
    }

    @Nested
    @DisplayName("GET /donaciones")
    class Listar {

        @Test
        @DisplayName("200 OK: retorna lista de donaciones")
        void listar_returns200ConLista() throws Exception {
            when(service.listar()).thenReturn(List.of(donacionEjemplo));

            mockMvc.perform(get("/donaciones"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].id").value(1))
                    .andExpect(jsonPath("$[0].tipoDonacion").value("ropa"))
                    .andExpect(jsonPath("$[0].cantidad").value(10))
                    .andExpect(jsonPath("$[0].centroAcopio").value("Centro Norte"));

            verify(service, times(1)).listar();
        }

        @Test
        @DisplayName("200 OK: retorna lista vacia cuando no hay donaciones")
        void listar_returns200ListaVacia() throws Exception {
            when(service.listar()).thenReturn(Collections.emptyList());

            mockMvc.perform(get("/donaciones"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$").isEmpty());
        }
    }

    @Nested
    @DisplayName("POST /donaciones")
    class Crear {

        @Test
        @DisplayName("200 OK: crea donacion y retorna con id asignado")
        void crear_returns200ConDonacionGuardada() throws Exception {
            when(service.crear("ropa", 10, "persona", "Centro Norte"))
                    .thenReturn(donacionEjemplo);

            mockMvc.perform(post("/donaciones")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(donacionEjemplo)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.tipoDonacion").value("ropa"))
                    .andExpect(jsonPath("$.cantidad").value(10))
                    .andExpect(jsonPath("$.origen").value("persona"))
                    .andExpect(jsonPath("$.centroAcopio").value("Centro Norte"));

            verify(service).crear("ropa", 10, "persona", "Centro Norte");
        }

        @Test
        @DisplayName("Delega exactamente una vez al servicio con los parametros correctos")
        void crear_delegaAlServicioConParametrosCorrectos() throws Exception {
            when(service.crear(anyString(), anyInt(), anyString(), anyString()))
                    .thenReturn(donacionEjemplo);

            mockMvc.perform(post("/donaciones")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(donacionEjemplo)))
                    .andExpect(status().isOk());

            verify(service, times(1)).crear(
                    eq("ropa"), eq(10), eq("persona"), eq("Centro Norte"));
        }
    }

    @Nested
    @DisplayName("GET /donaciones/{id}")
    class ObtenerPorId {

        @Test
        @DisplayName("200 OK: retorna donacion cuando existe el id")
        void obtener_returns200CuandoExiste() throws Exception {
            when(service.listar()).thenReturn(List.of(donacionEjemplo));

            mockMvc.perform(get("/donaciones/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.tipoDonacion").value("ropa"));
        }

        @Test
        @DisplayName("404 NOT FOUND: cuando el id no existe")
        void obtener_returns404CuandoNoExiste() throws Exception {
            when(service.listar()).thenReturn(Collections.emptyList());

            mockMvc.perform(get("/donaciones/999"))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("404 NOT FOUND: cuando existe otra donacion pero no la buscada")
        void obtener_returns404CuandoIdNoCoinciде() throws Exception {
            when(service.listar()).thenReturn(List.of(donacionEjemplo)); // id=1

            mockMvc.perform(get("/donaciones/2"))
                    .andExpect(status().isNotFound());
        }
    }
}