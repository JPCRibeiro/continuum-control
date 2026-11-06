package br.edu.infnet.continuum_control.estado.api.controller;

import br.edu.infnet.continuum_control.estado.api.dto.EstadoOperacionalResponse;
import br.edu.infnet.continuum_control.estado.service.EstadoOperacionalService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/estado-operacional")
@RequiredArgsConstructor
public class EstadoOperacionalController {
    private final EstadoOperacionalService estadoOperacionalService;

    @GetMapping
    public EstadoOperacionalResponse consultar() {
        return estadoOperacionalService.consultarSistema();
    }
}