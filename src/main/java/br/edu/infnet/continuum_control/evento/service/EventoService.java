package br.edu.infnet.continuum_control.evento.service;

import br.edu.infnet.continuum_control.evento.domain.Evento;
import br.edu.infnet.continuum_control.evento.domain.Importancia;
import br.edu.infnet.continuum_control.evento.repository.EventoRepository;
import br.edu.infnet.continuum_control.shared.api.EntidadeNaoLocalizadaException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventoService {
    private final EventoRepository eventoRepository;

    public Evento registrar(String acontecimento, String descricao, LocalDate data, String localizacao,
                            Importancia importancia) {
        Evento evento = new Evento(acontecimento, descricao, data, localizacao, importancia);

        return eventoRepository.save(evento);
    }

    public List<Evento> listar() {
        return eventoRepository.findAll();
    }

    public Evento buscarPorId(Long id) {
        return eventoRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoLocalizadaException("Evento não localizado"));
    }

    public List<Evento> listarPorImportancia(Importancia importancia) {
        return eventoRepository.findByImportancia(importancia);
    }

    public List<Evento> listarPorPeriodo(LocalDate inicio, LocalDate fim) {
        return eventoRepository.findByDataBetween(inicio, fim);
    }

    @Transactional
    public Evento atualizar(Long id, String acontecimento, String descricao, LocalDate data,
                            String localizacao, Importancia importancia) {
        Evento evento = buscarPorId(id);
        evento.setAcontecimento(acontecimento);
        evento.setDescricao(descricao);
        evento.setData(data);
        evento.setLocalizacao(localizacao);
        evento.setImportancia(importancia);

        return evento;
    }
}
