package br.edu.infnet.continuum_control.ocorrencia.service;

import br.edu.infnet.continuum_control.agente.domain.Agente;
import br.edu.infnet.continuum_control.agente.service.AgenteService;
import br.edu.infnet.continuum_control.auth.domain.Usuario;
import br.edu.infnet.continuum_control.auth.service.UsuarioService;
import br.edu.infnet.continuum_control.historico.domain.TipoHistorico;
import br.edu.infnet.continuum_control.historico.service.HistoricoService;
import br.edu.infnet.continuum_control.missao.domain.Missao;
import br.edu.infnet.continuum_control.missao.service.MissaoService;
import br.edu.infnet.continuum_control.ocorrencia.domain.DistorcaoTemporal;
import br.edu.infnet.continuum_control.ocorrencia.domain.EvidenciaTemporal;
import br.edu.infnet.continuum_control.ocorrencia.domain.Ocorrencia;
import br.edu.infnet.continuum_control.ocorrencia.domain.TipoOcorrencia;
import br.edu.infnet.continuum_control.ocorrencia.repository.OcorrenciaRepository;
import br.edu.infnet.continuum_control.shared.api.AcessoNegadoException;
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
    private final UsuarioService usuarioService;
    private final HistoricoService historicoService;

    @Transactional
    public Ocorrencia registrar(Long missaoId, Long agenteId, TipoOcorrencia tipo, String descricao,
                                DistorcaoTemporal distorcaoTemporal, EvidenciaTemporal evidenciaTemporal,
                                String email) {
        Missao missao = missaoService.buscarPorId(missaoId);
        Usuario usuario = usuarioService.buscarPorEmail(email);

        if (usuario.getAgente() != null && !usuario.getAgente().getId().equals(agenteId)) {
            throw new AcessoNegadoException("Agente só pode registrar em seu próprio nome.");
        }

        Agente agente = agenteService.buscarPorId(agenteId);
        missao.validarAceitaOcorrencia();
        missao.validarAgenteParticipante(agente);

        Ocorrencia ocorrencia = new Ocorrencia(missaoId, agenteId, tipo, descricao, distorcaoTemporal,
                evidenciaTemporal);
        ocorrenciaRepository.save(ocorrencia);

        historicoService.registrar(missao.getAnomalia(), TipoHistorico.OCORRENCIA,
                "Ocorrência do tipo " + tipo + " registrada por " + agente.getCodinome());

        return ocorrencia;
    }

    public List<Ocorrencia> listarPorMissao(Long missaoId) {
        missaoService.buscarPorId(missaoId);

        return ocorrenciaRepository.findByMissaoId(missaoId);
    }
}
