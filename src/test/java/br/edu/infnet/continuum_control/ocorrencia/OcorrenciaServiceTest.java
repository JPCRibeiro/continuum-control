package br.edu.infnet.continuum_control.ocorrencia;

import br.edu.infnet.continuum_control.agente.domain.Agente;
import br.edu.infnet.continuum_control.agente.service.AgenteService;
import br.edu.infnet.continuum_control.anomalia.domain.Anomalia;
import br.edu.infnet.continuum_control.anomalia.domain.Risco;
import br.edu.infnet.continuum_control.auth.domain.Perfil;
import br.edu.infnet.continuum_control.auth.domain.Usuario;
import br.edu.infnet.continuum_control.auth.service.UsuarioService;
import br.edu.infnet.continuum_control.evento.domain.Evento;
import br.edu.infnet.continuum_control.evento.domain.Importancia;
import br.edu.infnet.continuum_control.missao.domain.Missao;
import br.edu.infnet.continuum_control.missao.domain.Prioridade;
import br.edu.infnet.continuum_control.missao.service.MissaoService;
import br.edu.infnet.continuum_control.ocorrencia.domain.DistorcaoTemporal;
import br.edu.infnet.continuum_control.ocorrencia.domain.Ocorrencia;
import br.edu.infnet.continuum_control.ocorrencia.domain.Tipo;
import br.edu.infnet.continuum_control.ocorrencia.repository.OcorrenciaRepository;
import br.edu.infnet.continuum_control.ocorrencia.service.OcorrenciaService;
import br.edu.infnet.continuum_control.shared.api.OperacaoInvalidaException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OcorrenciaServiceTest {
    @Mock
    OcorrenciaRepository ocorrenciaRepository;

    @Mock
    private MissaoService missaoService;

    @Mock
    private AgenteService agenteService;

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private OcorrenciaService ocorrenciaService;

    private static final String EMAIL = "supervisor@continuum.control";

    @Test
    void deveRegistrarOcorrenciaComSucesso() {
        Evento evento = new Evento("Eleição", "Eleições no Brasil", LocalDate.parse("2026-10-04"),
                "Brasil", Importancia.ALTA);
        Anomalia anomalia = new Anomalia(evento, "Fissura", Risco.ALTO, null, null);
        Agente agente = new Agente("ns02m9qm", "James Bond", "Espionagem");
        Missao missao = new Missao("dsav231n", "Verificar fissura", anomalia,
                LocalDateTime.parse("2026-10-04T20:17"), Prioridade.ALTA);
        Usuario supervisor = new Usuario("Supervisor", EMAIL, "hash", Perfil.SUPERVISOR, null);

        missao.designar(agente);
        missao.iniciar();
        agente.entrarMissao();

        when(missaoService.buscarPorId(1L)).thenReturn(missao);
        when(usuarioService.buscarPorEmail(EMAIL)).thenReturn(supervisor);
        when(agenteService.buscarPorId(1L)).thenReturn(agente);
        when(ocorrenciaRepository.save(any(Ocorrencia.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        Ocorrencia ocorrencia = ocorrenciaService.registrar(1L, 1L, Tipo.DISTORCAO_TEMPORAL,
                "Oscilação detectada no campo temporal durante a transmissão",
                new DistorcaoTemporal(7.5, Duration.parse("PT1H30M")), null, EMAIL);

        assertEquals(1L, ocorrencia.getMissaoId());
        assertEquals(1L, ocorrencia.getAgenteId());
        assertEquals(Tipo.DISTORCAO_TEMPORAL, ocorrencia.getTipo());
        assertEquals(7.5, ocorrencia.getDistorcao().intensidade());
    }

    @Test
    void naoDeveRegistrarOcorrenciaSeNaoForEmExecucao() {
        Evento evento = new Evento("Eleição", "Eleições no Brasil", LocalDate.parse("2026-10-04"), "Brasil",
                Importancia.ALTA);
        Anomalia anomalia = new Anomalia(evento, "Fissura", Risco.ALTO, null, null);
        Missao missao = new Missao("dsav231n", "Verificar fissura", anomalia,
                LocalDateTime.parse("2026-10-04T20:17"), Prioridade.ALTA);
        Usuario supervisor = new Usuario("Supervisor", EMAIL, "hash", Perfil.SUPERVISOR, null);

        when(missaoService.buscarPorId(1L)).thenReturn(missao);
        when(usuarioService.buscarPorEmail(EMAIL)).thenReturn(supervisor);

        assertThrows(OperacaoInvalidaException.class,
                () -> ocorrenciaService.registrar(1L, 1L, Tipo.DISTORCAO_TEMPORAL,
                        "Oscilação detectada no campo temporal durante a transmissão",
                        new DistorcaoTemporal(7.5, Duration.parse("PT1H30M")), null, EMAIL));
    }

    @Test
    void naoDeveRegistrarOcorrenciaDeAgenteNaoDesignado() {
        Evento evento = new Evento("Eleição", "Eleições no Brasil", LocalDate.parse("2026-10-04"), "Brasil",
                Importancia.ALTA);
        Anomalia anomalia = new Anomalia(evento, "Fissura", Risco.ALTO, null, null);
        Agente agente = new Agente("ns02m9qm", "James Bond", "Espionagem");
        Missao missao = new Missao("dsav231n", "Verificar fissura", anomalia,
                LocalDateTime.parse("2026-10-04T20:17"), Prioridade.ALTA);
        Usuario supervisor = new Usuario("Supervisor", EMAIL, "hash", Perfil.SUPERVISOR, null);

        missao.designar(agente);
        missao.iniciar();

        Agente novoAgente = new Agente("yf05mx2m", "Loki", "Viajante");

        when(missaoService.buscarPorId(1L)).thenReturn(missao);
        when(usuarioService.buscarPorEmail(EMAIL)).thenReturn(supervisor);
        when(agenteService.buscarPorId(2L)).thenReturn(novoAgente);

        assertThrows(OperacaoInvalidaException.class,
                () -> ocorrenciaService.registrar(1L, 2L, Tipo.DISTORCAO_TEMPORAL,
                        "Oscilação detectada no campo temporal durante a transmissão",
                        new DistorcaoTemporal(7.5, Duration.parse("PT1H30M")), null, EMAIL));
    }
}
