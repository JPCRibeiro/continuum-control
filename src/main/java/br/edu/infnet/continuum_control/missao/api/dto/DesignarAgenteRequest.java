package br.edu.infnet.continuum_control.missao.api.dto;

import jakarta.validation.constraints.NotNull;

public record DesignarAgenteRequest(
        @NotNull
        Long agenteId
) {}