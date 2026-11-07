package br.edu.infnet.continuum_control.anomalia;

import br.edu.infnet.continuum_control.anomalia.domain.Anomalia;
import br.edu.infnet.continuum_control.anomalia.domain.Risco;
import br.edu.infnet.continuum_control.anomalia.domain.SituacaoAnomalia;
import br.edu.infnet.continuum_control.anomalia.repository.AnomaliaRepository;
import br.edu.infnet.continuum_control.anomalia.service.AnomaliaService;
import br.edu.infnet.continuum_control.evento.domain.Evento;
import br.edu.infnet.continuum_control.evento.domain.Importancia;
import br.edu.infnet.continuum_control.evento.service.EventoService;
import br.edu.infnet.continuum_control.historico.service.HistoricoService;
import br.edu.infnet.continuum_control.shared.api.EntidadeNaoLocalizadaException;
import br.edu.infnet.continuum_control.shared.api.OperacaoInvalidaException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AnomaliaServiceTest {
    @Mock
    private AnomaliaRepository anomaliaRepository;

    @Mock
    private EventoService eventoService;

    @Mock
    private HistoricoService historicoService;

    @InjectMocks
    private AnomaliaService anomaliaService;

    @Test
    void deveRegistrarAnomaliaComEventoESituacaoDetectada() {
        LocalDate data = LocalDate.parse("2026-10-04");
        Evento evento = new Evento("Eleição", "Eleições no Brasil", data, "Brasil", Importancia.ALTA);

        when(eventoService.buscarPorId(1L)).thenReturn(evento);
        when(anomaliaRepository.save(any(Anomalia.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        Anomalia anomalia = anomaliaService.registrar(1L, "Fissura", Risco.ALTO, null, null);

        assertEquals(evento, anomalia.getEvento());
        assertEquals(data, anomalia.getEvento().getData());
        assertEquals("Fissura", anomalia.getDivergencia());
        assertEquals(Risco.ALTO, anomalia.getRisco());
        assertEquals(SituacaoAnomalia.DETECTADA, anomalia.getSituacao());
        assertNotNull(anomalia.getDetectadaEm());
    }

    @Test
    void deveLancarExcecaoAoBuscarAnomaliaInexistente() {
        when(anomaliaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntidadeNaoLocalizadaException.class,
                () -> anomaliaService.buscarPorId(99L));
    }

    @Test
    void deveClassificarRiscoDaAnomalia() {
        Evento evento = new Evento("Eleição", "Eleições no Brasil",
                LocalDate.parse("2026-10-04"), "Brasil", Importancia.ALTA);

        Anomalia anomalia = new Anomalia(evento, "Fissura", Risco.BAIXO, null, null);

        when(anomaliaRepository.findById(1L)).thenReturn(Optional.of(anomalia));

        anomaliaService.classificarRisco(1L, Risco.CRITICO);

        assertEquals(Risco.CRITICO, anomalia.getRisco());
    }

    @Test
    void deveLancarExcecaoAoTentarAnalisarAnomaliaIrreversivel() {
        Evento evento = new Evento("Eleição", "Eleições", LocalDate.parse("2026-10-04"), "Brasil", Importancia.ALTA);
        Anomalia anomalia = new Anomalia(evento, "Fissura", Risco.ALTO, null, null);

        anomalia.tornarIrreversivel();

        when(anomaliaRepository.findById(1L)).thenReturn(Optional.of(anomalia));

        assertThrows(OperacaoInvalidaException.class,
                () -> anomaliaService.analisar(1L));
    }
}
