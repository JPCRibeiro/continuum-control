package br.edu.infnet.continuum_control.historico.repository;

import br.edu.infnet.continuum_control.historico.domain.Historico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoricoRepository extends JpaRepository<Historico, Long> {
    List<Historico> findByAnomaliaIdOrderByOcorridoEmAsc(Long anomaliaId);
}
