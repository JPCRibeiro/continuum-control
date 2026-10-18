package br.edu.infnet.continuum_control.evento;

import br.edu.infnet.continuum_control.evento.domain.Evento;
import br.edu.infnet.continuum_control.evento.domain.Importancia;
import br.edu.infnet.continuum_control.evento.repository.EventoRepository;
import br.edu.infnet.continuum_control.evento.service.EventoService;
import br.edu.infnet.continuum_control.shared.api.EntidadeNaoLocalizadaException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EventoServiceTest {
    @Mock
    private EventoRepository eventoRepository;

    @InjectMocks
    private EventoService eventoService;

    @Test
    void deveRegistrarEvento() {
        when(eventoRepository.save(any(Evento.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        LocalDate data = LocalDate.parse("2026-10-04");

        Evento evento = eventoService.registrar("Eleição", "Eleições no Brasil", data,
                "Brasil", Importancia.ALTA);

        assertEquals("Eleição", evento.getAcontecimento());
        assertEquals("Eleições no Brasil", evento.getDescricao());
        assertEquals(data, evento.getData());
        assertEquals("Brasil", evento.getLocalizacao());
        assertEquals(Importancia.ALTA, evento.getImportancia());
    }

    @Test
    void deveLancarExcecaoAoBuscarEventoInexistente() {
        when(eventoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntidadeNaoLocalizadaException.class,
                () -> eventoService.buscarPorId(99L));
    }
}
