package br.edu.infnet.continuum_control.intervencao.repository;

import br.edu.infnet.continuum_control.intervencao.domain.Intervencao;
import br.edu.infnet.continuum_control.shared.Impacto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IntervencaoRepository extends JpaRepository<Intervencao, Long> {
    List<Intervencao> findByMissaoId(Long missaoId);

    long countByImpacto(Impacto impacto);
}
