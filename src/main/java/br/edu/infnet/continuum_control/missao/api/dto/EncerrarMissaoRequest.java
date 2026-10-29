package br.edu.infnet.continuum_control.missao.api.dto;

import br.edu.infnet.continuum_control.missao.domain.Impacto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EncerrarMissaoRequest(
        @NotBlank
        String resumo,
        @NotNull
        Impacto impacto,
        @NotBlank
        String observacao


) {}
