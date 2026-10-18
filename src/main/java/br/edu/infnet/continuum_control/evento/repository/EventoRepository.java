package br.edu.infnet.continuum_control.evento.repository;

import br.edu.infnet.continuum_control.evento.domain.Evento;
import br.edu.infnet.continuum_control.evento.domain.Importancia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface EventoRepository extends JpaRepository<Evento, Long> {
    List<Evento> findByImportancia(Importancia importancia);

    List<Evento> findByDataBetween(LocalDate inicio, LocalDate fim);
}
