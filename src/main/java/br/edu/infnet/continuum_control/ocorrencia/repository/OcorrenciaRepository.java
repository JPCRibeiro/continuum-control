package br.edu.infnet.continuum_control.ocorrencia.repository;

import br.edu.infnet.continuum_control.ocorrencia.domain.Ocorrencia;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OcorrenciaRepository extends MongoRepository<Ocorrencia, String> {
    List<Ocorrencia> findByMissaoId(Long missaoId);
}
