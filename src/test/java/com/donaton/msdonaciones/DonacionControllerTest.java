package com.donaton.msdonaciones;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class DonacionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getdonaciones_debeRetornar200() throws Exception {
        mockMvc.perform(get("/donaciones"))
               .andExpect(status().isCreated());
    }

    @Test
    void postDonacion_debeCrearYRetornar200() throws Exception {
        String json = """
            {
                "tipoDonacion": "ropa",
                "cantidad": 10,
                "origen": "persona",
                "centroAcopio": "Centro Norte"
            }
            """;

        mockMvc.perform(post("/donaciones")
               .contentType(MediaType.APPLICATION_JSON)
               .content(json))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.tipoDonacion").value("ropa"));
    }
}
