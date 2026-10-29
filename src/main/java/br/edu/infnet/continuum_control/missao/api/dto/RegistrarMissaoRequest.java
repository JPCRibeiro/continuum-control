package br.edu.infnet.continuum_control.missao.api.dto;

import br.edu.infnet.continuum_control.missao.domain.Prioridade;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record RegistrarMissaoRequest(
        @NotBlank
        String objetivo,
        @NotNull
        Long anomaliaId,
        @NotNull
        LocalDateTime destinoTemporal,
        @NotNull
        Prioridade prioridade
) {}
