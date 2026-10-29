package br.edu.infnet.continuum_control.missao.api.dto;

import br.edu.infnet.continuum_control.missao.domain.Impacto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegistrarIntervencaoRequest(
        @NotNull
        Long agenteId,
        @NotBlank
        String acao,
        @NotBlank
        String justificativa,
        @NotNull
        Impacto impacto,
        @NotBlank
        String resultado
) {}
