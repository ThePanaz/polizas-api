package com.segurosbolivar.polizas.controller;

import com.segurosbolivar.polizas.model.Poliza;
import com.segurosbolivar.polizas.service.PolizaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/polizas")
public class PolizaController {

    private final PolizaService service;

    public PolizaController(PolizaService service) {
        this.service = service;
    }

    @GetMapping
    public List<Poliza> listar(
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String estado
    ) {
        return service.listar(tipo, estado);
    }

    @GetMapping("/{numeroPoliza}")
    public ResponseEntity<?> buscar(@PathVariable String numeroPoliza) {
        Poliza poliza = service.buscarPorNumero(numeroPoliza);
        if (poliza == null) {
            return ResponseEntity.status(404).body("Poliza no encontrada");
        }
        return ResponseEntity.ok(poliza);
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Poliza poliza) {
        try {
            return ResponseEntity.ok(service.crear(poliza));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{numeroPoliza}/cancelar")
    public ResponseEntity<?> cancelar(@PathVariable String numeroPoliza) {
        try {
            return ResponseEntity.ok(service.cancelarPoliza(numeroPoliza));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @PostMapping("/{numeroPoliza}/renovar")
    public ResponseEntity<?> renovar(
            @PathVariable String numeroPoliza,
            @RequestParam double ipc
    ) {
        try {
            return ResponseEntity.ok(service.renovarPoliza(numeroPoliza, ipc));
        } catch (IllegalArgumentException e) {
            // Si el mensaje viene por "no encontrada", responde 404, si no, 400
            if (e.getMessage() != null && e.getMessage().toLowerCase().contains("no encontrada")) {
                return ResponseEntity.status(404).body(e.getMessage());
            }
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        }
    }
}