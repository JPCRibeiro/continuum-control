package br.edu.infnet.continuum_control.anomalia.api.dto;

import br.edu.infnet.continuum_control.anomalia.domain.Risco;
import jakarta.validation.constraints.NotNull;

public record ClassificarRiscoRequest(
        @NotNull
        Risco risco
) {}
