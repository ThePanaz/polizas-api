package com.segurosbolivar.polizas.client;

public interface CoreClient {

    /**
     * Notifica a CORE (mock) que hubo una actualización sobre una póliza.
     * @param numeroPoliza Número de póliza que cambió.
     */
    void enviarEventoActualizacion(String numeroPoliza);
}