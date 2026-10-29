package br.edu.infnet.continuum_control.missao.repository;

import br.edu.infnet.continuum_control.missao.domain.Encerramento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EncerramentoRepository extends JpaRepository<Encerramento, Long> {
}
