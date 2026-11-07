package br.edu.infnet.continuum_control.anomalia.service;

import br.edu.infnet.continuum_control.anomalia.domain.Anomalia;
import br.edu.infnet.continuum_control.anomalia.domain.Risco;
import br.edu.infnet.continuum_control.anomalia.domain.SituacaoAnomalia;
import br.edu.infnet.continuum_control.anomalia.repository.AnomaliaRepository;
import br.edu.infnet.continuum_control.evento.domain.Evento;
import br.edu.infnet.continuum_control.evento.service.EventoService;
import br.edu.infnet.continuum_control.historico.domain.TipoHistorico;
import br.edu.infnet.continuum_control.historico.service.HistoricoService;
import br.edu.infnet.continuum_control.shared.api.EntidadeNaoLocalizadaException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnomaliaService {
    private final AnomaliaRepository anomaliaRepository;
    private final EventoService eventoService;
    private final HistoricoService historicoService;

    private static final List<SituacaoAnomalia> ATIVAS = List.of(
        SituacaoAnomalia.DETECTADA,
        SituacaoAnomalia.EM_ANALISE,
        SituacaoAnomalia.CONFIRMADA,
        SituacaoAnomalia.EM_CORRECAO
    );

    @Transactional
    public Anomalia registrar(Long eventoId, String divergencia, Risco risco, String origem, String observacao) {
        Evento evento = eventoService.buscarPorId(eventoId);

        Anomalia anomalia = new Anomalia(evento, divergencia, risco, origem, observacao);
        anomaliaRepository.save(anomalia);

        historicoService.registrar(anomalia, TipoHistorico.ANOMALIA,
                "Anomalia registrada no evento " + evento.getAcontecimento() + "com risco" + risco);

        return anomalia;
    }

    public List<Anomalia> listar() {
        return anomaliaRepository.findAll();
    }

    public Anomalia buscarPorId(Long id) {
        return anomaliaRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoLocalizadaException("Anomalia não localizada"));
    }

    public List<Anomalia> listarAtivas() {
        return anomaliaRepository.findBySituacaoIn(ATIVAS);
    }

    public List<Anomalia> listarCriticasAtivas() {
        return anomaliaRepository.findBySituacaoInAndRisco(ATIVAS, Risco.CRITICO);
    }

    public List<Anomalia> listarPorSituacao(SituacaoAnomalia situacao) {
        return anomaliaRepository.findBySituacao(situacao);
    }

    public List<Anomalia> listarPorRisco(Risco risco) {
        return anomaliaRepository.findByRisco(risco);
    }

    @Transactional
    public Anomalia atualizar(Long id, String divergencia, String origem, String observacao) {
        Anomalia anomalia = buscarPorId(id);
        anomalia.setDivergencia(divergencia);
        anomalia.setOrigem(origem);
        anomalia.setObservacao(observacao);

        return anomalia;
    }

    @Transactional
    public Anomalia classificarRisco(Long id, Risco risco) {
        Anomalia anomalia = buscarPorId(id);
        Risco anterior = anomalia.getRisco();
        anomalia.classificarRisco(risco);

        historicoService.registrar(anomalia, TipoHistorico.RISCO_ALTERADO,
                "Risco alterado de " + anterior + " para " + risco);

        return anomalia;
    }

    @Transactional
    public Anomalia analisar(Long id) {
        Anomalia anomalia = buscarPorId(id);
        anomalia.analisar();

        historicoService.registrar(anomalia, TipoHistorico.SITUACAO_ALTERADA, "Anomalia movida para análise");

        return anomalia;
    }

    @Transactional
    public Anomalia tornarIrreversivel(Long id) {
        Anomalia anomalia = buscarPorId(id);
        anomalia.tornarIrreversivel();

        historicoService.registrar(anomalia, TipoHistorico.SITUACAO_ALTERADA, "Anomalia declarada irreversível");

        return anomalia;
    }

    public long contarAtivas() {
        return anomaliaRepository.countBySituacaoIn(ATIVAS);
    }

    public long contarCriticasAtivas() {
        return anomaliaRepository.countBySituacaoInAndRisco(ATIVAS, Risco.CRITICO);
    }
}
