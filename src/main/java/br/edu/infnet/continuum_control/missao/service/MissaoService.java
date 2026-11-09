package br.edu.infnet.continuum_control.missao.service;

import br.edu.infnet.continuum_control.agente.domain.Agente;
import br.edu.infnet.continuum_control.agente.service.AgenteService;
import br.edu.infnet.continuum_control.anomalia.domain.Anomalia;
import br.edu.infnet.continuum_control.anomalia.domain.SituacaoAnomalia;
import br.edu.infnet.continuum_control.anomalia.service.AnomaliaService;
import br.edu.infnet.continuum_control.auth.domain.Usuario;
import br.edu.infnet.continuum_control.auth.service.UsuarioService;
import br.edu.infnet.continuum_control.historico.domain.TipoHistorico;
import br.edu.infnet.continuum_control.historico.service.HistoricoService;
import br.edu.infnet.continuum_control.missao.domain.*;
import br.edu.infnet.continuum_control.missao.repository.AutorizacaoRepository;
import br.edu.infnet.continuum_control.missao.repository.EncerramentoRepository;
import br.edu.infnet.continuum_control.missao.repository.MissaoRepository;
import br.edu.infnet.continuum_control.shared.GeradorCodigo;
import br.edu.infnet.continuum_control.shared.Impacto;
import br.edu.infnet.continuum_control.shared.api.AcessoNegadoException;
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
    private final UsuarioService usuarioService;
    private final HistoricoService historicoService;

    @Transactional
    public Missao registrar(String objetivo, Long anomaliaId, LocalDateTime destinoTemporal,
                            Prioridade prioridade) {
        Anomalia anomalia = anomaliaService.buscarPorId(anomaliaId);
        anomalia.validarAceitaNovaMissao();

        String codigo = geradorCodigo.gerar();
        Missao missao = new Missao(codigo, objetivo, anomalia, destinoTemporal, prioridade);

        missaoRepository.save(missao);

        historicoService.registrar(missao.getAnomalia(), TipoHistorico.MISSAO_CRIADA,
                "Missão " + codigo + " criada com prioridade " + prioridade);

        return missao;
    }

    @Transactional
    public Missao designar(Long missaoId, Long agenteId) {
        Missao missao = buscarPorId(missaoId);

        Agente agente =  agenteService.buscarPorId(agenteId);

        missao.designar(agente);

        historicoService.registrar(missao.getAnomalia(), TipoHistorico.AGENTE_DESIGNADO,
                "Agente " + agente.getCodinome() + " designado para a missão " + missao.getCodigo());

        return missao;
    }

    @Transactional
    public Missao iniciar(Long missaoId, String email) {
        Missao missao = buscarPorId(missaoId);
        Usuario usuario = usuarioService.buscarPorEmail(email);

        if (usuario.getAgente() != null && !missao.possuiAgente(usuario.getAgente())) {
            throw new AcessoNegadoException("Agente não participa desta missão.");
        }

        missao.iniciar();
        missao.getAnomalia().iniciarCorrecao();
        missao.getAgentes().forEach(Agente::entrarMissao);

        historicoService.registrar(missao.getAnomalia(), TipoHistorico.SITUACAO_ALTERADA,
                "Missão " + missao.getCodigo() + " iniciada; anomalia em correção");

        return missao;
    }

    @Transactional
    public Autorizacao autorizar(Long missaoId, String responsavel, String observacao) {
        Missao missao = buscarPorId(missaoId);

        missao.autorizar();
        Autorizacao autorizacao = new Autorizacao(missao, responsavel, observacao);

        historicoService.registrar(missao.getAnomalia(), TipoHistorico.AUTORIZACAO,
                "Missão " + missao.getCodigo() + " autorizada por " + responsavel);

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

        historicoService.registrar(missao.getAnomalia(), TipoHistorico.ENCERRAMENTO,
                "Missão " + missao.getCodigo() + " encerrada com sucesso");

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

        historicoService.registrar(missao.getAnomalia(), TipoHistorico.ENCERRAMENTO,
                "Missão " + missao.getCodigo() + " encerrada com falha");

        return encerramentoRepository.save(encerramento);
    }

    @Transactional
    public Missao cancelar(Long missaoId) {
        Missao missao = buscarPorId(missaoId);
        missao.cancelar();

        historicoService.registrar(missao.getAnomalia(), TipoHistorico.ENCERRAMENTO,
                "Missão " + missao.getCodigo() + " cancelada");

        return missao;
    }

    @Transactional
    public Missao atualizar(Long id, String objetivo, LocalDateTime destinoTemporal, Prioridade prioridade) {
        Missao missao = buscarPorId(id);
        missao.validarAceitaAlteracao();

        missao.setObjetivo(objetivo);
        missao.setDestinoTemporal(destinoTemporal);
        missao.setPrioridade(prioridade);

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

    public long contarPorSituacao(SituacaoMissao situacao) {
        return missaoRepository.countBySituacao(situacao);
    }
}