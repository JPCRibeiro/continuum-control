package br.edu.infnet.continuum_control.intervencao.service;

import br.edu.infnet.continuum_control.agente.domain.Agente;
import br.edu.infnet.continuum_control.agente.service.AgenteService;
import br.edu.infnet.continuum_control.auth.domain.Usuario;
import br.edu.infnet.continuum_control.auth.service.UsuarioService;
import br.edu.infnet.continuum_control.historico.domain.TipoHistorico;
import br.edu.infnet.continuum_control.historico.service.HistoricoService;
import br.edu.infnet.continuum_control.missao.service.MissaoService;
import br.edu.infnet.continuum_control.shared.Impacto;
import br.edu.infnet.continuum_control.intervencao.domain.Intervencao;
import br.edu.infnet.continuum_control.missao.domain.Missao;
import br.edu.infnet.continuum_control.intervencao.repository.IntervencaoRepository;
import br.edu.infnet.continuum_control.shared.api.AcessoNegadoException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IntervencaoService {
    private final IntervencaoRepository intervencaoRepository;
    private final MissaoService missaoService;
    private final AgenteService agenteService;
    private final UsuarioService usuarioService;
    private final HistoricoService historicoService;

    @CacheEvict(cacheNames = "estadoOperacional", allEntries = true)
    @Transactional
    public Intervencao registrar(Long missaoId, Long agenteId, String acao, String justificativa, Impacto impacto,
                                 String resultado, String email) {
        Missao missao = missaoService.buscarPorId(missaoId);
        Usuario usuario = usuarioService.buscarPorEmail(email);

        if (usuario.getAgente() != null && !usuario.getAgente().getId().equals(agenteId)) {
            throw new AcessoNegadoException("Agente só pode registrar em seu próprio nome.");
        }

        Agente agente = agenteService.buscarPorId(agenteId);
        missao.validarAceitaIntervencao();
        missao.validarAgenteParticipante(agente);

        Intervencao intervencao = new Intervencao(missao, agente, acao, justificativa, impacto, resultado);
        intervencaoRepository.save(intervencao);

        historicoService.registrar(missao.getAnomalia(), TipoHistorico.INTERVENCAO,
                "Intervenção de impacto " + impacto + " registrada por " + agente.getCodinome() +
                        " na missão " + missao.getCodigo());

        return intervencao;
    }

    public List<Intervencao> listarPorMissao(Long missaoId) {
        missaoService.buscarPorId(missaoId);

        return intervencaoRepository.findByMissaoId(missaoId);
    }

    public long contarSeveras() {
        return intervencaoRepository.countByImpacto(Impacto.SEVERO);
    }
 }
