package br.edu.infnet.continuum_control.missao.repository;

import br.edu.infnet.continuum_control.missao.domain.Intervencao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IntervencaoRepository extends JpaRepository<Intervencao, Long> {
    List<Intervencao> findByMissaoId(Long missaoId);
}
