package com.donaton.msdonaciones.repository;

import com.donaton.msdonaciones.model.Donacion;
import org.springframework.stereotype.Repository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
@Repository
public interface DonacionRepository extends JpaRepository<Donacion, Long> {
    List<Donacion> findByCentroAcopio(String centroAcopio);
}
