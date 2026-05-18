package com.donaton.msdonaciones.service;

import com.donaton.msdonaciones.event.DonacionEvent;
import com.donaton.msdonaciones.factory.DonacionFactory;
import com.donaton.msdonaciones.kafka.DonacionProducer;
import com.donaton.msdonaciones.model.Donacion;
import com.donaton.msdonaciones.repository.DonacionRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DonacionService {

    private final DonacionRepository repository;
    private final DonacionFactory factory;
    private final DonacionProducer donacionProducer;

    public List<Donacion> listar() {
        return repository.findAll();
    }

    public Donacion crear(String tipo, int cantidad, String origen, String centro) {
        Donacion d = factory.crearDonacion(tipo, cantidad, origen, centro);
        Donacion guardada = repository.save(d);

        DonacionEvent event = new DonacionEvent(
                guardada.getId(),
                guardada.getTipoDonacion(),
                guardada.getCantidad(),
                guardada.getCentroAcopio(),
                guardada.getFecha()
        );
        donacionProducer.publicar(event);

        return guardada;
    }

    // Circuit Breaker: si falla, retorna lista vacía en vez de error
    @CircuitBreaker(name = "donacionService", fallbackMethod = "fallbackListar")
    public List<Donacion> listarConCircuitBreaker() {
        return repository.findAll();
    }

    public List<Donacion> fallbackListar(Exception e) {
        return new ArrayList<>();
    }
}
