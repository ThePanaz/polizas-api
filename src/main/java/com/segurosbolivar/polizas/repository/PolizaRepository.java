package com.segurosbolivar.polizas.repository;

import com.segurosbolivar.polizas.model.Poliza;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class PolizaRepository {

    private final List<Poliza> data = new ArrayList<>();

    public PolizaRepository() {
        // Data pruebas (ya con canon/prima para que no queden en 0)
        data.add(build("P-001", "Juan Perez", "AUTO",      "ACTIVA",    1200000,  95000));
        data.add(build("P-002", "Maria Gomez","VIDA",      "CANCELADA",  800000,  65000));
        data.add(build("P-003", "Carlos Ruiz","HOGAR",     "ACTIVA",    1500000, 110000));
        data.add(build("P-004", "Agrouniverso","COLECTIVA","ACTIVA",    5000000, 320000));
        data.add(build("P-005", "Salud Coop","COLECTIVA", "CANCELADA", 4200000, 280000));
        data.add(build("P-006", "Nova Black","COLECTIVA", "ACTIVA",    6000000, 350000));
    }

    private Poliza build(String numero, String titular, String tipo, String estado, double canon, double prima) {
        Poliza p = new Poliza(numero, titular, tipo, estado);
        p.setCanon(canon);
        p.setPrima(prima);
        return p;
    }

    public List<Poliza> findByTipoAndEstado(String tipo, String estado) {
        return data.stream()
                .filter(p -> tipo == null || tipo.isBlank() || p.getTipo().equalsIgnoreCase(tipo))
                .filter(p -> estado == null || estado.isBlank() || p.getEstado().equalsIgnoreCase(estado))
                .toList();
    }

    public List<Poliza> findAll() {
        return data;
    }

    public Optional<Poliza> findByNumeroPoliza(String numeroPoliza) {
        return data.stream()
                .filter(p -> p.getNumeroPoliza().equalsIgnoreCase(numeroPoliza))
                .findFirst();
    }

    /**
     * Crea una póliza nueva (si ya existe, lanza error).
     * También asegura canon/prima con valores por defecto si vienen en 0.
     */
    public Poliza saveNew(Poliza poliza) {
        boolean exists = findByNumeroPoliza(poliza.getNumeroPoliza()).isPresent();
        if (exists) {
            throw new IllegalArgumentException("Ya existe la póliza " + poliza.getNumeroPoliza());
        }

        normalizeDefaults(poliza);
        data.add(poliza);
        return poliza;
    }

    /**
     * Actualiza una póliza existente (reemplaza en la lista).
     */
    public Poliza update(Poliza poliza) {
        int idx = indexOf(poliza.getNumeroPoliza());
        if (idx < 0) {
            throw new IllegalArgumentException("No existe la póliza " + poliza.getNumeroPoliza());
        }

        normalizeDefaults(poliza);
        data.set(idx, poliza);
        return poliza;
    }

    public Poliza cancelar(String numeroPoliza) {
        Poliza p = findByNumeroPoliza(numeroPoliza)
                .orElseThrow(() -> new IllegalArgumentException("Póliza no encontrada: " + numeroPoliza));

        p.setEstado("CANCELADA");
        return update(p);
    }

    public Poliza renovar(String numeroPoliza, double ipc) {
        Poliza p = findByNumeroPoliza(numeroPoliza)
                .orElseThrow(() -> new IllegalArgumentException("Póliza no encontrada: " + numeroPoliza));

        if ("CANCELADA".equalsIgnoreCase(p.getEstado())) {
            throw new IllegalStateException("No se puede renovar una póliza CANCELADA");
        }

        // ejemplo de ajuste simple con IPC (si ipc viene como 0.06 => +6%)
        p.setCanon(p.getCanon() * (1 + ipc));
        p.setPrima(p.getPrima() * (1 + ipc));

        return update(p);
    }

    private int indexOf(String numeroPoliza) {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getNumeroPoliza().equalsIgnoreCase(numeroPoliza)) return i;
        }
        return -1;
    }

    private void normalizeDefaults(Poliza poliza) {
        if (poliza.getCanon() <= 0) poliza.setCanon(1000000);
        if (poliza.getPrima() <= 0) poliza.setPrima(50000);
        if (poliza.getEstado() == null || poliza.getEstado().isBlank()) {
            poliza.setEstado("ACTIVA");
        }
    }
}