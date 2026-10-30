package br.edu.infnet.continuum_control.ocorrencia.service;

import br.edu.infnet.continuum_control.agente.domain.Agente;
import br.edu.infnet.continuum_control.agente.service.AgenteService;
import br.edu.infnet.continuum_control.missao.domain.Missao;
import br.edu.infnet.continuum_control.missao.service.MissaoService;
import br.edu.infnet.continuum_control.ocorrencia.domain.DistorcaoTemporal;
import br.edu.infnet.continuum_control.ocorrencia.domain.EvidenciaTemporal;
import br.edu.infnet.continuum_control.ocorrencia.domain.Ocorrencia;
import br.edu.infnet.continuum_control.ocorrencia.domain.Tipo;
import br.edu.infnet.continuum_control.ocorrencia.repository.OcorrenciaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OcorrenciaService {
    private final OcorrenciaRepository ocorrenciaRepository;
    private final MissaoService missaoService;
    private final AgenteService agenteService;

    @Transactional
    public Ocorrencia registrar(Long missaoId, Long agenteId, Tipo tipo, String descricao,
                                DistorcaoTemporal distorcaoTemporal, EvidenciaTemporal evidenciaTemporal) {
        Missao missao = missaoService.buscarPorId(missaoId);
        missao.validarAceitaOcorrencia();

        Agente agente = agenteService.buscarPorId(agenteId);
        missao.validarAgenteParticipante(agente);

        Ocorrencia ocorrencia = new Ocorrencia(missaoId, agenteId, tipo, descricao, distorcaoTemporal,
                evidenciaTemporal);

        return ocorrenciaRepository.save(ocorrencia);
    }

    public List<Ocorrencia> listarPorMissao(Long missaoId) {
        missaoService.buscarPorId(missaoId);

        return ocorrenciaRepository.findByMissaoId(missaoId);
    }
}
