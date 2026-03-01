package com.segurosbolivar.polizas.repository;

import com.segurosbolivar.polizas.model.Riesgo;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class RiesgoRepository {

    private final List<Riesgo> data = new ArrayList<>();

    public List<Riesgo> findByPolizaId(String polizaId) {
        return data.stream()
                .filter(r -> r.getPolizaId().equalsIgnoreCase(polizaId))
                .collect(Collectors.toList());
    }

    public Optional<Riesgo> findById(String id) {
        return data.stream()
                .filter(r -> r.getId().equalsIgnoreCase(id))
                .findFirst();
    }

    public Riesgo save(Riesgo riesgo) {
        if (riesgo.getId() == null || riesgo.getId().isBlank()) {
            riesgo.setId(UUID.randomUUID().toString());
        }
        data.add(riesgo);
        return riesgo;
    }
}