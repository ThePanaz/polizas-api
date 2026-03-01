package com.segurosbolivar.polizas.service;

import com.segurosbolivar.polizas.client.CoreClient;
import com.segurosbolivar.polizas.model.Poliza;
import com.segurosbolivar.polizas.model.Riesgo;
import com.segurosbolivar.polizas.repository.PolizaRepository;
import com.segurosbolivar.polizas.repository.RiesgoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PolizaService {

    private final PolizaRepository polizaRepository;
    private final RiesgoRepository riesgoRepository;
    private final CoreClient coreClient;

    public PolizaService(PolizaRepository polizaRepository,
                         RiesgoRepository riesgoRepository,
                         CoreClient coreClient) {
        this.polizaRepository = polizaRepository;
        this.riesgoRepository = riesgoRepository;
        this.coreClient = coreClient;
    }

    // =========================
    // PÓLIZAS
    // =========================

    public List<Poliza> listar(String tipo, String estado) {
        return polizaRepository.findByTipoAndEstado(tipo, estado);
    }

    public Poliza buscarPorNumero(String numeroPoliza) {
        return polizaRepository.findByNumeroPoliza(numeroPoliza).orElse(null);
    }

    public Poliza crear(Poliza poliza) {
        if (poliza.getNumeroPoliza() == null || poliza.getNumeroPoliza().isBlank()) {
            throw new IllegalArgumentException("numeroPoliza es obligatorio");
        }
        if (poliza.getTipo() == null || poliza.getTipo().isBlank()) {
            throw new IllegalArgumentException("tipo es obligatorio");
        }
        if (poliza.getEstado() == null || poliza.getEstado().isBlank()) {
            poliza.setEstado("ACTIVA");
        }

        Poliza created = polizaRepository.saveNew(poliza);

        coreClient.enviarEventoActualizacion(created.getNumeroPoliza());
        return created;
    }

    public Poliza cancelarPoliza(String numeroPoliza) {
        // persiste cambio en repo
        Poliza polizaCancelada = polizaRepository.cancelar(numeroPoliza);

        // cancela riesgos asociados (en memoria, misma referencia)
        List<Riesgo> riesgos = riesgoRepository.findByPolizaId(numeroPoliza);
        for (Riesgo r : riesgos) {
            r.setEstado("CANCELADO");
        }

        coreClient.enviarEventoActualizacion(numeroPoliza);
        return polizaCancelada;
    }

    public Poliza renovarPoliza(String numeroPoliza, double ipc) {
        // Acepta 6 ó 0.06:
        double ipcDecimal = (ipc > 1) ? (ipc / 100.0) : ipc;

        Poliza p = polizaRepository.findByNumeroPoliza(numeroPoliza)
                .orElseThrow(() -> new IllegalArgumentException("Póliza no encontrada: " + numeroPoliza));

        if ("CANCELADA".equalsIgnoreCase(p.getEstado())) {
            throw new IllegalStateException("No se puede renovar una póliza CANCELADA");
        }
        if (ipcDecimal < 0) {
            throw new IllegalArgumentException("El IPC no puede ser negativo");
        }

        p.setCanon(redondear2(p.getCanon() * (1 + ipcDecimal)));
        p.setPrima(redondear2(p.getPrima() * (1 + ipcDecimal)));
        p.setEstado("RENOVADA");

        Poliza updated = polizaRepository.update(p);

        coreClient.enviarEventoActualizacion(numeroPoliza);
        return updated;
    }

    // =========================
    // RIESGOS
    // =========================

    public List<Riesgo> listarRiesgos(String numeroPoliza) {
        if (buscarPorNumero(numeroPoliza) == null) {
            throw new IllegalArgumentException("Póliza no encontrada: " + numeroPoliza);
        }
        return riesgoRepository.findByPolizaId(numeroPoliza);
    }

    public Riesgo agregarRiesgo(String numeroPoliza, Riesgo riesgo) {
        Poliza poliza = buscarPorNumero(numeroPoliza);
        if (poliza == null) {
            throw new IllegalArgumentException("Póliza no encontrada: " + numeroPoliza);
        }

        if (!"COLECTIVA".equalsIgnoreCase(poliza.getTipo())) {
            throw new IllegalStateException("Solo pólizas COLECTIVA pueden tener riesgos");
        }
        if (!"ACTIVA".equalsIgnoreCase(poliza.getEstado())) {
            throw new IllegalStateException("Solo pólizas ACTIVA pueden tener riesgos");
        }

        if (riesgo.getDescripcion() == null || riesgo.getDescripcion().isBlank()) {
            throw new IllegalArgumentException("descripcion del riesgo es obligatoria");
        }

        riesgo.setPolizaId(numeroPoliza);
        if (riesgo.getEstado() == null || riesgo.getEstado().isBlank()) {
            riesgo.setEstado("ACTIVO");
        }

        Riesgo created = riesgoRepository.save(riesgo);

        coreClient.enviarEventoActualizacion(numeroPoliza);
        return created;
    }

    // (Si vas a exponer el requisito 6 en controller)
    public Riesgo cancelarRiesgo(String riesgoId) {
        Riesgo r = riesgoRepository.findById(riesgoId)
                .orElseThrow(() -> new IllegalArgumentException("Riesgo no encontrado: " + riesgoId));

        r.setEstado("CANCELADO");
        coreClient.enviarEventoActualizacion(r.getPolizaId());
        return r;
    }

    private double redondear2(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }
}