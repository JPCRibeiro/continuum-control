package br.edu.infnet.continuum_control.ocorrencia.api.dto;

import br.edu.infnet.continuum_control.ocorrencia.domain.DistorcaoTemporal;
import br.edu.infnet.continuum_control.ocorrencia.domain.EvidenciaTemporal;
import br.edu.infnet.continuum_control.ocorrencia.domain.Tipo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegistrarOcorrenciaRequest(
        @NotNull
        Long agenteId,
        @NotNull
        Tipo tipo,
        @NotBlank
        String descricao,
        @Valid
        DistorcaoTemporal distorcao,
        @Valid
        EvidenciaTemporal evidencia
) {}