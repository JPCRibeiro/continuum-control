package br.edu.infnet.continuum_control.anomalia.repository;

import br.edu.infnet.continuum_control.anomalia.domain.Anomalia;
import br.edu.infnet.continuum_control.anomalia.domain.Risco;
import br.edu.infnet.continuum_control.anomalia.domain.SituacaoAnomalia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnomaliaRepository extends JpaRepository<Anomalia, Long> {
    List<Anomalia> findBySituacaoIn(List<SituacaoAnomalia> situacoes);

    List<Anomalia> findByRisco(Risco risco);

    List<Anomalia> findBySituacao(SituacaoAnomalia situacao);
}
