package br.edu.infnet.continuum_control.anomalia.api.dto;

import br.edu.infnet.continuum_control.anomalia.domain.Risco;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegistrarAnomaliaRequest(
    @NotNull
    Long eventoId,
    @NotBlank
    String divergencia,
    @NotNull
    Risco risco,
    String origem,
    String observacao
) {}
