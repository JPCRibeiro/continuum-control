package br.edu.infnet.continuum_control.ocorrencia.api.dto;

import br.edu.infnet.continuum_control.ocorrencia.domain.DistorcaoTemporal;
import br.edu.infnet.continuum_control.ocorrencia.domain.EvidenciaTemporal;
import br.edu.infnet.continuum_control.ocorrencia.domain.Ocorrencia;
import br.edu.infnet.continuum_control.ocorrencia.domain.TipoOcorrencia;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record OcorrenciaResponse(
        String id,
        Long missaoId,
        Long agenteId,
        TipoOcorrencia tipo,
        String descricao,
        DistorcaoTemporal distorcaoTemporal,
        EvidenciaTemporal evidenciaTemporal
) {
    public static OcorrenciaResponse from(Ocorrencia ocorrencia) {
        return new OcorrenciaResponse(
                ocorrencia.getId(),
                ocorrencia.getMissaoId(),
                ocorrencia.getAgenteId(),
                ocorrencia.getTipo(),
                ocorrencia.getDescricao(),
                ocorrencia.getDistorcao(),
                ocorrencia.getEvidencia()
        );
    }
}
