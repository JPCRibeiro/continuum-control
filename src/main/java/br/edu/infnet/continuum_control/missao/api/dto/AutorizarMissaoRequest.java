package br.edu.infnet.continuum_control.missao.api.dto;

import jakarta.validation.constraints.NotBlank;

public record AutorizarMissaoRequest(
        @NotBlank
        String responsavel,
        @NotBlank
        String observacao
) {}
