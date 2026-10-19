package br.edu.infnet.continuum_control.anomalia.api.dto;

import jakarta.validation.constraints.NotBlank;

public record AtualizarAnomaliaRequest(
        @NotBlank
        String divergencia,
        String origem,
        String observacao
) {}