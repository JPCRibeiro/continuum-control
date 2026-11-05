package br.edu.infnet.continuum_control.intervencao;

import br.edu.infnet.continuum_control.agente.domain.Agente;
import br.edu.infnet.continuum_control.agente.service.AgenteService;
import br.edu.infnet.continuum_control.anomalia.domain.Anomalia;
import br.edu.infnet.continuum_control.anomalia.domain.Risco;
import br.edu.infnet.continuum_control.evento.domain.Evento;
import br.edu.infnet.continuum_control.evento.domain.Importancia;
import br.edu.infnet.continuum_control.intervencao.domain.Intervencao;
import br.edu.infnet.continuum_control.missao.domain.*;
import br.edu.infnet.continuum_control.intervencao.repository.IntervencaoRepository;
import br.edu.infnet.continuum_control.intervencao.service.IntervencaoService;
import br.edu.infnet.continuum_control.missao.service.MissaoService;
import br.edu.infnet.continuum_control.shared.Impacto;
import br.edu.infnet.continuum_control.shared.api.AcessoNegadoException;
import br.edu.infnet.continuum_control.shared.api.OperacaoInvalidaException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class IntervencaoServiceTest {
    @Mock
    IntervencaoRepository intervencaoRepository;

    @Mock
    private MissaoService missaoService;

    @Mock
    private AgenteService agenteService;

    @InjectMocks
    private IntervencaoService intervencaoService;

    @Test
    void deveRegistrarIntervencaoComSucesso() {
        Evento evento = new Evento("Eleição", "Eleições no Brasil", LocalDate.parse("2026-10-04"), "Brasil",
                Importancia.ALTA);
        Anomalia anomalia = new Anomalia(evento, "Fissura", Risco.ALTO, null, null);
        Agente agente = new Agente("ns02m9qm", "James Bond", "Espionagem");
        Missao missao = new Missao("dsav231n", "Verificar fissura", anomalia,
                LocalDateTime.parse("2026-10-04T20:17"), Prioridade.ALTA);

        missao.designar(agente);
        missao.iniciar();
        agente.entrarMissao();

        when(missaoService.buscarPorId(1L)).thenReturn(missao);
        when(agenteService.buscarPorId(1L)).thenReturn(agente);
        when(intervencaoRepository.save(any(Intervencao.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        Intervencao intervencao = intervencaoService.registrar(1L, 1L,
                "Substituição da fita original pela cópia restaurada",
                "Impedir que a divergência se propague no registro histórico", Impacto.CONTROLADO,
                "Transmissão restaurada sem efeitos colaterais detectados");

        assertEquals(missao, intervencao.getMissao());
        assertEquals(agente, intervencao.getAgente());
        assertNotNull(intervencao.getOcorridaEm());
    }

    @Test
    void naoDeveRegistrarIntervencaoSeNaoForEmExecucao() {
        Evento evento = new Evento("Eleição", "Eleições no Brasil", LocalDate.parse("2026-10-04"), "Brasil",
                Importancia.ALTA);
        Anomalia anomalia = new Anomalia(evento, "Fissura", Risco.ALTO, null, null);
        Missao missao = new Missao("dsav231n", "Verificar fissura", anomalia,
                LocalDateTime.parse("2026-10-04T20:17"), Prioridade.ALTA);

        when(missaoService.buscarPorId(1L)).thenReturn(missao);

        assertThrows(OperacaoInvalidaException.class,
                () -> intervencaoService.registrar(1L, 1L, "Substituição da fita",
                        "Impedir propagação da divergência", Impacto.CONTROLADO, "Restaurada"));
    }

    @Test
    void naoDeveRegistrarIntervencaoDeAgenteNaoDesignado() {
        Evento evento = new Evento("Eleição", "Eleições no Brasil", LocalDate.parse("2026-10-04"), "Brasil",
                Importancia.ALTA);
        Anomalia anomalia = new Anomalia(evento, "Fissura", Risco.ALTO, null, null);
        Agente agente = new Agente("ns02m9qm", "James Bond", "Espionagem");
        Missao missao = new Missao("dsav231n", "Verificar fissura", anomalia,
                LocalDateTime.parse("2026-10-04T20:17"), Prioridade.ALTA);

        missao.designar(agente);
        missao.iniciar();

        Agente novoAgente = new Agente("yf05mx2m", "Loki", "Viajante");

        when(missaoService.buscarPorId(1L)).thenReturn(missao);
        when(agenteService.buscarPorId(2L)).thenReturn(novoAgente);

        assertThrows(AcessoNegadoException.class,
                () -> intervencaoService.registrar(1L, 2L, "Substituição da fita",
                        "Impedir propagação da divergência", Impacto.CONTROLADO, "Restaurada"));
    }
}
