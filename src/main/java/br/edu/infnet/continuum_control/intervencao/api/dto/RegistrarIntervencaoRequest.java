package br.edu.infnet.continuum_control.intervencao.api.dto;

import br.edu.infnet.continuum_control.shared.Impacto;
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
