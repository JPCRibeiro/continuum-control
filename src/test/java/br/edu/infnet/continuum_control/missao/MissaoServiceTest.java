package br.edu.infnet.continuum_control.missao;

import br.edu.infnet.continuum_control.agente.domain.Agente;
import br.edu.infnet.continuum_control.agente.domain.SituacaoAgente;
import br.edu.infnet.continuum_control.agente.service.AgenteService;
import br.edu.infnet.continuum_control.anomalia.domain.Anomalia;
import br.edu.infnet.continuum_control.anomalia.domain.Risco;
import br.edu.infnet.continuum_control.anomalia.domain.SituacaoAnomalia;
import br.edu.infnet.continuum_control.anomalia.service.AnomaliaService;
import br.edu.infnet.continuum_control.evento.domain.Evento;
import br.edu.infnet.continuum_control.evento.domain.Importancia;
import br.edu.infnet.continuum_control.missao.domain.Impacto;
import br.edu.infnet.continuum_control.missao.domain.Missao;
import br.edu.infnet.continuum_control.missao.domain.Prioridade;
import br.edu.infnet.continuum_control.missao.domain.SituacaoMissao;
import br.edu.infnet.continuum_control.missao.repository.EncerramentoRepository;
import br.edu.infnet.continuum_control.missao.repository.MissaoRepository;
import br.edu.infnet.continuum_control.missao.service.MissaoService;
import br.edu.infnet.continuum_control.shared.api.OperacaoInvalidaException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MissaoServiceTest {
    @Mock
    private MissaoRepository missaoRepository;

    @Mock
    private AnomaliaService anomaliaService;

    @Mock
    private AgenteService agenteService;

    @Mock
    private EncerramentoRepository encerramentoRepository;

    @InjectMocks
    private MissaoService missaoService;

    @Test
    void deveIniciarMissaoComSucesso() {
        Evento evento = new Evento("Eleição", "Eleições no Brasil", LocalDate.parse("2026-10-04"), "Brasil",
                Importancia.ALTA);
        Anomalia anomalia = new Anomalia(evento, "Fissura", Risco.ALTO, null, null);
        Agente agente = new Agente("ns02m9qm", "James Bond", "Espionagem");
        Missao missao = new Missao("dsav231n", "Verificar fissura", anomalia,
                LocalDateTime.parse("2026-10-04T20:17"), Prioridade.ALTA);

        missao.designar(agente);

        when(missaoRepository.findById(1L)).thenReturn(Optional.of(missao));

        missaoService.iniciar(1L);

        assertEquals(SituacaoMissao.EM_EXECUCAO, missao.getSituacao());
        assertEquals(SituacaoAgente.EM_MISSAO, agente.getSituacao());
        assertEquals(SituacaoAnomalia.EM_CORRECAO, missao.getAnomalia().getSituacao());
    }

    @Test
    void naoDeveIniciarMissaoEmergenciaSemAutorizacao() {
        Evento evento = new Evento("Eleição", "Eleições no Brasil", LocalDate.parse("2026-10-04"), "Brasil",
                Importancia.ALTA);
        Anomalia anomalia = new Anomalia(evento, "Fissura", Risco.ALTO, null, null);
        Agente agente = new Agente("ns02m9qm", "James Bond", "Espionagem");
        Missao missao = new Missao("dsav231n", "Verificar fissura", anomalia,
                LocalDateTime.parse("2026-10-04T20:17"), Prioridade.EMERGENCIA);

        missao.designar(agente);

        when(missaoRepository.findById(1L)).thenReturn(Optional.of(missao));

        assertThrows(OperacaoInvalidaException.class,
                () -> missaoService.iniciar(1L));
    }

    @Test
    void naoDeveIniciarMissaoSeAgenteIndisponivel() {
        Evento evento = new Evento("Eleição", "Eleições no Brasil", LocalDate.parse("2026-10-04"), "Brasil",
                Importancia.ALTA);
        Anomalia anomalia = new Anomalia(evento, "Fissura", Risco.ALTO, null, null);
        Agente agente = new Agente("ns02m9qm", "James Bond", "Espionagem");
        Missao missao = new Missao("dsav231n", "Verificar fissura", anomalia,
                LocalDateTime.parse("2026-10-04T20:17"), Prioridade.ALTA);

        missao.designar(agente);
        agente.entrarMissao();

        when(missaoRepository.findById(1L)).thenReturn(Optional.of(missao));

        assertThrows(OperacaoInvalidaException.class,
                () -> missaoService.iniciar(1L));
    }

    @Test
    void naoDeveDesignarAgenteParaMissaoEmAndamento() {
        Evento evento = new Evento("Eleição", "Eleições no Brasil", LocalDate.parse("2026-10-04"), "Brasil",
                Importancia.ALTA);
        Anomalia anomalia = new Anomalia(evento, "Fissura", Risco.ALTO, null, null);
        Agente agente = new Agente("ns02m9qm", "James Bond", "Espionagem");
        Missao missao = new Missao("dsav231n", "Verificar fissura", anomalia,
                LocalDateTime.parse("2026-10-04T20:17"), Prioridade.ALTA);

        missao.designar(agente);
        missao.iniciar();

        Agente novoAgente = new Agente("yf05mx2m", "Loki", "Viajante");

        when(missaoRepository.findById(1L)).thenReturn(Optional.of(missao));
        when(agenteService.buscarPorId(2L)).thenReturn(novoAgente);

        assertThrows(OperacaoInvalidaException.class,
                () -> missaoService.designar(1L, 2L)
        );
    }

    @Test
    void naoDeveRegistrarMissaoEmAnomaliaIrreversivel() {
        Evento evento = new Evento("Eleição", "Eleições no Brasil", LocalDate.parse("2026-10-04"), "Brasil",
                Importancia.ALTA);
        Anomalia anomalia = new Anomalia(evento, "Fissura", Risco.ALTO, null, null);

        anomalia.tornarIrreversivel();

        when(anomaliaService.buscarPorId(1L)).thenReturn(anomalia);

        assertThrows(OperacaoInvalidaException.class,
                () -> missaoService.registrar("Verificar fissura", 1L, LocalDateTime.parse("2026-10-04T20:17"),
                        Prioridade.ALTA));
    }

    @Test
    void concluirDeveLiberarAgentesEEstabilizarAnomalia() {
        Evento evento = new Evento("Eleição", "Eleições no Brasil", LocalDate.parse("2026-10-04"), "Brasil",
                Importancia.ALTA);
        Anomalia anomalia = new Anomalia(evento, "Fissura", Risco.ALTO, null, null);
        Agente agente = new Agente("ns02m9qm", "James Bond", "Espionagem");
        Missao missao = new Missao("dsav231n", "Verificar fissura", anomalia,
                LocalDateTime.parse("2026-10-04T20:17"), Prioridade.ALTA);

        missao.designar(agente);
        missao.iniciar();
        agente.entrarMissao();
        anomalia.iniciarCorrecao();

        when(missaoRepository.findById(1L)).thenReturn(Optional.of(missao));
        when(encerramentoRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        missaoService.concluir(1L, "Fissura corrigida", Impacto.CONTROLADO, null);

        assertEquals(SituacaoMissao.CONCLUIDA, missao.getSituacao());
        assertEquals(SituacaoAgente.DISPONIVEL, agente.getSituacao());
        assertEquals(SituacaoAnomalia.ESTABILIZADA, anomalia.getSituacao());
    }
}
