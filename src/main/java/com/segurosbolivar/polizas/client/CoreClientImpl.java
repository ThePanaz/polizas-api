package com.segurosbolivar.polizas.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class CoreClientImpl implements CoreClient {

    private static final Logger log = LoggerFactory.getLogger(CoreClientImpl.class);

    private final RestTemplate restTemplate;

    private static final String CORE_MOCK_URL = "http://localhost:8080/core-mock/evento";
    private static final String API_KEY = "123456";

    public CoreClientImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void enviarEventoActualizacion(String polizaId) {
        try {
            Map<String, Object> body = new HashMap<>();
            body.put("evento", "ACTUALIZACION");
            body.put("polizaId", polizaId); // tu id real hoy es String (P-001), está ok

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("x-api-key", API_KEY);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

            restTemplate.exchange(CORE_MOCK_URL, HttpMethod.POST, request, Void.class);
            log.info("Notificación enviada a CORE-MOCK: polizaId={}", polizaId);

        } catch (Exception e) {
            // “intentar notificar” -> no tumbar flujo
            log.warn("No se pudo notificar CORE-MOCK: polizaId={}, error={}", polizaId, e.getMessage());
        }
    }
}