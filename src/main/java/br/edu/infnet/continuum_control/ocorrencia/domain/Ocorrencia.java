package br.edu.infnet.continuum_control.ocorrencia.domain;

import org.springframework.data.annotation.Id;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "ocorrencias")
@Getter
public class Ocorrencia {
    @Id
    private String id;
    private Long missaoId;
    private Long agenteId;
    private Tipo tipo;
    private String descricao;
    private DistorcaoTemporal distorcao;
    private EvidenciaTemporal evidencia;

    protected Ocorrencia() {}

    public Ocorrencia(Long missaoId, Long agenteId, Tipo tipo, String descricao, DistorcaoTemporal distorcao,
                      EvidenciaTemporal evidencia) {
        this.missaoId = missaoId;
        this.agenteId = agenteId;
        this.tipo = tipo;
        this.descricao = descricao;
        this.distorcao = distorcao;
        this.evidencia = evidencia;
    }
}
