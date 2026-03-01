package com.segurosbolivar.polizas.model;

public class Riesgo {

    private String id;
    private String polizaId;
    private String descripcion;
    private String estado; // ACTIVO / CANCELADO

    public Riesgo() {}

    public Riesgo(String id, String polizaId, String descripcion, String estado) {
        this.id = id;
        this.polizaId = polizaId;
        this.descripcion = descripcion;
        this.estado = estado;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getPolizaId() { return polizaId; }
    public void setPolizaId(String polizaId) { this.polizaId = polizaId; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}