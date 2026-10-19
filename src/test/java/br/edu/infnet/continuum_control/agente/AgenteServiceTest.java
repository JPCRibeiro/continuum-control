package br.edu.infnet.continuum_control.agente;

import br.edu.infnet.continuum_control.agente.domain.Agente;
import br.edu.infnet.continuum_control.agente.domain.SituacaoAgente;
import br.edu.infnet.continuum_control.agente.repository.AgenteRepository;
import br.edu.infnet.continuum_control.agente.service.AgenteService;
import br.edu.infnet.continuum_control.shared.GeradorCodigo;
import br.edu.infnet.continuum_control.shared.api.EntidadeNaoLocalizadaException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AgenteServiceTest {
    @Mock
    private AgenteRepository agenteRepository;

    @Mock
    private GeradorCodigo geradorCodigo;

    @InjectMocks
    private AgenteService agenteService;

    @Test
    void deveRegistrarAgenteComoDisponivel() {
        when(geradorCodigo.gerar()).thenReturn("ns02m9qm");
        when(agenteRepository.save(any(Agente.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        Agente agente = agenteService.registrar("James Bond", "Espionagem");

        assertEquals("ns02m9qm", agente.getCodigo());
        assertEquals("James Bond", agente.getCodinome());
        assertEquals("Espionagem", agente.getEspecialidade());
        assertEquals(SituacaoAgente.DISPONIVEL, agente.getSituacao());
    }

    @Test
    void deveLancarExcecaoAoBuscarAgenteInexistente() {
        when(agenteRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntidadeNaoLocalizadaException.class,
                () -> agenteService.buscarPorId(99L));
    }

    @Test
    void deveInativarAgenteDisponivel() {
        Agente agente = new Agente("ns02m9qm", "James Bond", "Espionagem");
        when(agenteRepository.findById(1L)).thenReturn(Optional.of(agente));

        agenteService.inativar(1L);

        assertEquals(SituacaoAgente.INATIVO, agente.getSituacao());
    }
}
