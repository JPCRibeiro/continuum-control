package br.edu.infnet.continuum_control.agente.api.controller;

import br.edu.infnet.continuum_control.agente.api.dto.AgenteResponse;
import br.edu.infnet.continuum_control.agente.api.dto.RegistrarAgenteRequest;
import br.edu.infnet.continuum_control.agente.domain.Agente;
import br.edu.infnet.continuum_control.agente.domain.Situacao;
import br.edu.infnet.continuum_control.agente.service.AgenteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agentes")
@RequiredArgsConstructor
public class AgenteController {
    private final AgenteService agenteService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AgenteResponse registrar(@Valid @RequestBody RegistrarAgenteRequest req) {
        return AgenteResponse.from(agenteService.registrar(req.codinome(), req.especialidade()));
    }

    @GetMapping("/{id}")
    public AgenteResponse buscarPorId(@PathVariable Long id) {
        return AgenteResponse.from(agenteService.buscarPorId(id));
    }

    @GetMapping("/disponiveis")
    public List<AgenteResponse> listarDisponiveis() {
        return agenteService.listarDisponiveis()
                .stream()
                .map(AgenteResponse::from)
                .toList();
    }

    @GetMapping
    public List<AgenteResponse> listar(@RequestParam(required = false) Situacao situacao,
                                       @RequestParam(required = false) String especialidade) {
        List<Agente> agentes;

        if (situacao != null) {
            agentes =  agenteService.listarPorSituacao(situacao);
        } else if  (especialidade != null) {
            agentes =  agenteService.listarPorEspecialidade(especialidade);
        } else {
            agentes =  agenteService.listar();
        }

        return agentes
                .stream()
                .map(AgenteResponse::from)
                .toList();
    }

    @PutMapping("/{id}")
    public AgenteResponse atualizar(@PathVariable Long id, @Valid @RequestBody RegistrarAgenteRequest req) {
        return AgenteResponse.from(agenteService.atualizar(id, req.codinome(), req.especialidade()));
    }

    @DeleteMapping("/{id}")
    public AgenteResponse inativar(@PathVariable Long id) {
        return AgenteResponse.from(agenteService.inativar(id));
    }
}
