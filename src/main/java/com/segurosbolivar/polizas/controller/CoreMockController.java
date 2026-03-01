package com.segurosbolivar.polizas.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/core-mock")
public class CoreMockController {

    private static final Logger log = LoggerFactory.getLogger(CoreMockController.class);

    @PostMapping("/evento")
    public ResponseEntity<?> evento(@RequestBody Map<String, Object> body) {
        log.info("CORE-MOCK recibido: {}", body);
        return ResponseEntity.ok(body); // responde 200 y hace echo
    }
}