package com.segurosbolivar.polizas.controller;

import com.segurosbolivar.polizas.model.Riesgo;
import com.segurosbolivar.polizas.service.PolizaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/polizas/{numeroPoliza}/riesgos")
public class RiesgoController {

    private final PolizaService service;

    public RiesgoController(PolizaService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<?> listar(@PathVariable String numeroPoliza) {
        try {
            List<Riesgo> riesgos = service.listarRiesgos(numeroPoliza);
            return ResponseEntity.ok(riesgos);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> crear(@PathVariable String numeroPoliza, @RequestBody Riesgo riesgo) {
        try {
            return ResponseEntity.ok(service.agregarRiesgo(numeroPoliza, riesgo));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        }
    }
}