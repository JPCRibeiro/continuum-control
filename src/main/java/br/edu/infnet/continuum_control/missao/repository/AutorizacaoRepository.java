package br.edu.infnet.continuum_control.missao.repository;

import br.edu.infnet.continuum_control.missao.domain.Autorizacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AutorizacaoRepository extends JpaRepository<Autorizacao, Long> {
}
