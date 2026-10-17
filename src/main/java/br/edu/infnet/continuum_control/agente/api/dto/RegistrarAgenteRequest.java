package br.edu.infnet.continuum_control.agente.api.dto;

import jakarta.validation.constraints.NotBlank;

public record RegistrarAgenteRequest(
    @NotBlank
    String codinome,
    @NotBlank
    String especialidade
) {}
