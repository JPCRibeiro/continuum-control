package br.edu.infnet.continuum_control.missao.repository;

import br.edu.infnet.continuum_control.missao.domain.Missao;
import br.edu.infnet.continuum_control.missao.domain.SituacaoMissao;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MissaoRepository extends JpaRepository<Missao, Long> {
    @Override
    @EntityGraph(attributePaths = "agentes")
    List<Missao> findAll();

    @Override
    @EntityGraph(attributePaths = "agentes")
    Optional<Missao> findById(Long id);

    @EntityGraph(attributePaths = "agentes")
    List<Missao> findBySituacao(SituacaoMissao situacao);

    @EntityGraph(attributePaths = "agentes")
    List<Missao> findByAnomaliaId(Long anomaliaId);

    @EntityGraph(attributePaths = "agentes")
    List<Missao> findByAgentesId(Long agenteId);
}