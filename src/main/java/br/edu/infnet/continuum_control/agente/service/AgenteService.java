package br.edu.infnet.continuum_control.agente.service;

import br.edu.infnet.continuum_control.agente.domain.Agente;
import br.edu.infnet.continuum_control.agente.domain.SituacaoAgente;
import br.edu.infnet.continuum_control.agente.repository.AgenteRepository;
import br.edu.infnet.continuum_control.shared.api.EntidadeNaoLocalizadaException;
import br.edu.infnet.continuum_control.shared.GeradorCodigo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AgenteService {
    private final AgenteRepository agenteRepository;
    private final GeradorCodigo geradorCodigo;

    public Agente registrar(String codinome, String especialidade) {
        Agente agente = new Agente(geradorCodigo.gerar(), codinome, especialidade);

        return agenteRepository.save(agente);
    }

    public List<Agente> listar() {
        return agenteRepository.findAll();
    }

    public Agente buscarPorId(Long id) {
        return agenteRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoLocalizadaException("Agente não localizado"));
    }

    public List<Agente> listarDisponiveis() {
        return listarPorSituacao(SituacaoAgente.DISPONIVEL);
    }

    public List<Agente> listarPorSituacao(SituacaoAgente situacao) {
        return agenteRepository.findBySituacao(situacao);
    }

    public List<Agente> listarPorEspecialidade(String especialidade) {
        return agenteRepository.findByEspecialidadeIgnoreCase(especialidade);
    }

    @Transactional
    public Agente atualizar(Long id, String codinome, String especialidade) {
        Agente agente = buscarPorId(id);
        agente.setCodinome(codinome);
        agente.setEspecialidade(especialidade);

        return agente;
    }

    @Transactional
    public Agente inativar(Long id) {
        Agente agente = buscarPorId(id);
        agente.inativar();

        return agente;
    }
}
