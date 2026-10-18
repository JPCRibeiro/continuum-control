package br.edu.infnet.continuum_control.evento.api.controller;

import br.edu.infnet.continuum_control.evento.api.dto.EventoResponse;
import br.edu.infnet.continuum_control.evento.api.dto.RegistrarEventoRequest;
import br.edu.infnet.continuum_control.evento.domain.Evento;
import br.edu.infnet.continuum_control.evento.domain.Importancia;
import br.edu.infnet.continuum_control.evento.service.EventoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/eventos")
@RequiredArgsConstructor
public class EventoController {
    private final EventoService eventoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventoResponse registrar(@Valid @RequestBody RegistrarEventoRequest req) {
        return EventoResponse.from(eventoService.registrar(
                req.acontecimento(),
                req.descricao(),
                req.data(),
                req.localizacao(),
                req.importancia()
        ));
    }

    @GetMapping("/{id}")
    public EventoResponse buscarPorId(@PathVariable Long id) {
        return EventoResponse.from(eventoService.buscarPorId(id));
    }

    @GetMapping
    public List<EventoResponse> listar(
            @RequestParam(required = false) Importancia importancia,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim ) {
        List<Evento> eventos;

        if (importancia != null) {
            eventos = eventoService.listarPorImportancia(importancia);
        } else if (inicio != null && fim != null) {
            eventos = eventoService.listarPorPeriodo(inicio, fim);
        } else {
            eventos = eventoService.listar();
        }

        return eventos.stream().map(EventoResponse::from).toList();
    }

    @PutMapping("/{id}")
    public EventoResponse atualizar(@PathVariable Long id, @Valid @RequestBody RegistrarEventoRequest req) {
        return EventoResponse.from(eventoService.atualizar(
                id,
                req.acontecimento(),
                req.descricao(),
                req.data(),
                req.localizacao(),
                req.importancia()));
    }
}
