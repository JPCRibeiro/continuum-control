package br.edu.infnet.continuum_control.estado.service;

import br.edu.infnet.continuum_control.agente.domain.SituacaoAgente;
import br.edu.infnet.continuum_control.agente.service.AgenteService;
import br.edu.infnet.continuum_control.anomalia.service.AnomaliaService;
import br.edu.infnet.continuum_control.estado.api.dto.EstadoOperacionalResponse;
import br.edu.infnet.continuum_control.estado.domain.EstadoLinhaTemporal;
import br.edu.infnet.continuum_control.intervencao.service.IntervencaoService;
import br.edu.infnet.continuum_control.missao.domain.SituacaoMissao;
import br.edu.infnet.continuum_control.missao.service.MissaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EstadoOperacionalService {
    private final AgenteService agenteService;
    private final MissaoService missaoService;
    private final AnomaliaService anomaliaService;
    private final IntervencaoService intervencaoService;

    @Cacheable(cacheNames = "estadoOperacional", key = "'atual'")
    public EstadoOperacionalResponse consultarSistema() {
        long agentesDisponiveis = agenteService.contarPorSituacao(SituacaoAgente.DISPONIVEL);
        long agentesEmMissao = agenteService.contarPorSituacao(SituacaoAgente.EM_MISSAO);
        long missoesEmExecucao = missaoService.contarPorSituacao(SituacaoMissao.EM_EXECUCAO);
        long anomaliasAtivas = anomaliaService.contarAtivas();
        long anomaliasCriticasAtivas = anomaliaService.contarCriticasAtivas();
        long missoesFalhadas = missaoService.contarPorSituacao(SituacaoMissao.FALHOU);
        long intervencoesSeveras = intervencaoService.contarSeveras();

        int indice = calcularIndice(anomaliasAtivas, anomaliasCriticasAtivas, missoesEmExecucao, missoesFalhadas,
                intervencoesSeveras);

        return new EstadoOperacionalResponse(
                agentesDisponiveis,
                agentesEmMissao,
                missoesEmExecucao,
                anomaliasAtivas,
                anomaliasCriticasAtivas,
                classificarEstado(indice),
                indice
        );
    }

    private int calcularIndice(long anomaliasAtivas, long anomaliasCriticas, long missaoesEmExecucao,
                              long missoesFalhadas, long intervencoesSeveras) {
        long indice = 100 - (anomaliasAtivas * 3) - (anomaliasCriticas * 30) - (missaoesEmExecucao * 2)
                - (missoesFalhadas * 8) - (intervencoesSeveras * 5);

        return Math.clamp(indice, 0, 100);
    }


    private EstadoLinhaTemporal classificarEstado(int indice) {
        if (indice >= 70) {
            return EstadoLinhaTemporal.ESTAVEL;
        }

        if (indice >= 40) {
            return EstadoLinhaTemporal.INSTAVEL;
        }

        return EstadoLinhaTemporal.CRITICO;
    }
}
