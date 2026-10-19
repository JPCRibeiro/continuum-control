package br.edu.infnet.continuum_control.agente.repository;

import br.edu.infnet.continuum_control.agente.domain.Agente;
import br.edu.infnet.continuum_control.agente.domain.SituacaoAgente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AgenteRepository extends JpaRepository<Agente, Long> {
    List<Agente> findBySituacao(SituacaoAgente situacao);

    List<Agente> findByEspecialidadeIgnoreCase(String especialidade);
}
