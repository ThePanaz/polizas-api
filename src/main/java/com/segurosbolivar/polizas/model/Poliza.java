package com.segurosbolivar.polizas.model;

public class Poliza {

    private String numeroPoliza;
    private String titular;
    private String tipo;
    private String estado;
    private double canon;
    private double prima;

    public Poliza() {}

    public Poliza(String numeroPoliza, String titular, String tipo, String estado) {
        this.numeroPoliza = numeroPoliza;
        this.titular = titular;
        this.tipo = tipo;
        this.estado = estado;
    }

    public String getNumeroPoliza() {
        return numeroPoliza;
    }

    public void setNumeroPoliza(String numeroPoliza) {
        this.numeroPoliza = numeroPoliza;
    }

    public String getTitular() {
        return titular;
    }

    public void setTitular(String titular) {
        this.titular = titular;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public double getCanon() {
        return canon;
    }

    public void setCanon(double canon) {
        this.canon = canon;
    }

    public double getPrima() {
        return prima;
    }

    public void setPrima(double prima) {
        this.prima = prima;
    }
}