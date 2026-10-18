package br.edu.infnet.continuum_control.evento.api.dto;

import br.edu.infnet.continuum_control.evento.domain.Importancia;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record RegistrarEventoRequest(
        @NotBlank
        String acontecimento,
        @NotBlank
        String descricao,
        @NotNull
        LocalDate data,
        @NotBlank
        String localizacao,
        @NotNull
        Importancia importancia
) {}
