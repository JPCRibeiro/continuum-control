package br.edu.infnet.continuum_control.evento.api.dto;

import br.edu.infnet.continuum_control.evento.domain.Evento;
import br.edu.infnet.continuum_control.evento.domain.Importancia;

import java.time.LocalDate;

public record EventoResponse(
        Long id,
        String acontecimento,
        String descricao,
        LocalDate data,
        String localizacao,
        Importancia importancia
) {
    public static EventoResponse from(Evento evento) {
        return new EventoResponse(
                evento.getId(),
                evento.getAcontecimento(),
                evento.getDescricao(),
                evento.getData(),
                evento.getLocalizacao(),
                evento.getImportancia()
        );
    }
}
