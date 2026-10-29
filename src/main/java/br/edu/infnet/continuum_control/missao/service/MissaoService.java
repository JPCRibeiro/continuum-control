package br.edu.infnet.continuum_control.missao.service;

import br.edu.infnet.continuum_control.agente.domain.Agente;
import br.edu.infnet.continuum_control.agente.service.AgenteService;
import br.edu.infnet.continuum_control.anomalia.domain.Anomalia;
import br.edu.infnet.continuum_control.anomalia.domain.SituacaoAnomalia;
import br.edu.infnet.continuum_control.anomalia.service.AnomaliaService;
import br.edu.infnet.continuum_control.missao.domain.*;
import br.edu.infnet.continuum_control.missao.repository.AutorizacaoRepository;
import br.edu.infnet.continuum_control.missao.repository.EncerramentoRepository;
import br.edu.infnet.continuum_control.missao.repository.MissaoRepository;
import br.edu.infnet.continuum_control.shared.GeradorCodigo;
import br.edu.infnet.continuum_control.shared.api.EntidadeNaoLocalizadaException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MissaoService {
    private final MissaoRepository missaoRepository;
    private final AnomaliaService anomaliaService;
    private final GeradorCodigo geradorCodigo;
    private final AgenteService agenteService;
    private final AutorizacaoRepository autorizacaoRepository;
    private final EncerramentoRepository encerramentoRepository;

    public Missao registrar(String objetivo, Long anomaliaId, LocalDateTime destinoTemporal,
                            Prioridade prioridade) {
        Anomalia anomalia = anomaliaService.buscarPorId(anomaliaId);
        anomalia.validarAceitaNovaMissao();

        String codigo = geradorCodigo.gerar();
        Missao missao = new Missao(codigo, objetivo, anomalia, destinoTemporal, prioridade);

        return missaoRepository.save(missao);
    }

    @Transactional
    public Missao designar(Long missaoId, Long agenteId) {
        Missao missao = buscarPorId(missaoId);

        Agente agente =  agenteService.buscarPorId(agenteId);

        missao.designar(agente);

        return missao;
    }

    @Transactional
    public Missao iniciar(Long missaoId) {
        Missao missao = buscarPorId(missaoId);
        missao.iniciar();
        missao.getAnomalia().iniciarCorrecao();
        missao.getAgentes().forEach(Agente::entrarMissao);

        return missao;
    }

    @Transactional
    public Autorizacao autorizar(Long missaoId, String responsavel, String observacao) {
        Missao missao = buscarPorId(missaoId);

        missao.autorizar();
        Autorizacao autorizacao = new Autorizacao(missao, responsavel, observacao);

        return autorizacaoRepository.save(autorizacao);
    }

    @Transactional
    public Encerramento concluir(Long missaoId, String resumo, Impacto impacto, String observacao) {
        Missao missao = buscarPorId(missaoId);

        missao.concluir();
        missao.getAgentes().forEach(Agente::liberar);
        missao.getAnomalia().estabilizar();

        Encerramento encerramento = new Encerramento(missao, ResultadoMissao.SUCESSO, resumo, impacto,
                missao.getAnomalia().getSituacao(), observacao);

        return encerramentoRepository.save(encerramento);
    }

    @Transactional
    public Encerramento falhar(Long missaoId, String resumo, Impacto impacto, String observacao) {
        Missao missao = buscarPorId(missaoId);

        missao.falhar();
        missao.getAgentes().forEach(Agente::liberar);
        missao.getAnomalia().confirmar();

        Encerramento encerramento = new Encerramento(missao, ResultadoMissao.FALHA, resumo, impacto,
                SituacaoAnomalia.CONFIRMADA, observacao);

        return encerramentoRepository.save(encerramento);
    }

    @Transactional
    public Missao cancelar(Long missaoId) {
        Missao missao = buscarPorId(missaoId);
        missao.cancelar();

        return missao;
    }

    public List<Missao> listar() {
        return  missaoRepository.findAll();
    }

    public Missao buscarPorId(Long id) {
        return missaoRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoLocalizadaException("Missão não localizada"));
    }

    public List<Missao> listarEmExecucao() {
        return listarPorSituacao(SituacaoMissao.EM_EXECUCAO);
    }

    public List<Missao> listarPorSituacao(SituacaoMissao situacao) {
        return missaoRepository.findBySituacao(situacao);
    }

    public List<Missao> listarPorAnomalia(Long anomaliaId) {
        return missaoRepository.findByAnomaliaId(anomaliaId);
    }

    public List<Missao> listarPorAgente(Long agenteId) {
        return missaoRepository.findByAgentesId(agenteId);
    }
}