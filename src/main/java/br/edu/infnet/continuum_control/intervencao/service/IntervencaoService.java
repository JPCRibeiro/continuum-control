package br.edu.infnet.continuum_control.intervencao.service;

import br.edu.infnet.continuum_control.agente.domain.Agente;
import br.edu.infnet.continuum_control.agente.service.AgenteService;
import br.edu.infnet.continuum_control.missao.service.MissaoService;
import br.edu.infnet.continuum_control.shared.Impacto;
import br.edu.infnet.continuum_control.intervencao.domain.Intervencao;
import br.edu.infnet.continuum_control.missao.domain.Missao;
import br.edu.infnet.continuum_control.intervencao.repository.IntervencaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IntervencaoService {
    private final IntervencaoRepository intervencaoRepository;
    private final MissaoService missaoService;
    private final AgenteService agenteService;

    @Transactional
    public Intervencao registrar(Long missaoId, Long agenteId, String acao, String justificativa, Impacto impacto,
                                 String resultado) {
        Missao missao = missaoService.buscarPorId(missaoId);
        missao.validarAceitaIntervencao();

        Agente agente = agenteService.buscarPorId(agenteId);
        missao.validarAgenteParticipante(agente);

        Intervencao intervencao = new Intervencao(missao, agente, acao, justificativa, impacto, resultado);

        return intervencaoRepository.save(intervencao);
    }

    public List<Intervencao> listarPorMissao(Long missaoId) {
        missaoService.buscarPorId(missaoId);

        return intervencaoRepository.findByMissaoId(missaoId);
    }
 }
