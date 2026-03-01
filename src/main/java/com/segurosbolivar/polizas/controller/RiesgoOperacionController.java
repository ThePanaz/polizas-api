package com.segurosbolivar.polizas.controller;

import com.segurosbolivar.polizas.model.Riesgo;
import com.segurosbolivar.polizas.service.PolizaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/riesgos")
public class RiesgoOperacionController {

    private final PolizaService service;

    public RiesgoOperacionController(PolizaService service) {
        this.service = service;
    }

    @PostMapping("/{id}/cancelar")
    public ResponseEntity<?> cancelar(@PathVariable String id) {
        try {
            Riesgo r = service.cancelarRiesgo(id);
            return ResponseEntity.ok(r);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}