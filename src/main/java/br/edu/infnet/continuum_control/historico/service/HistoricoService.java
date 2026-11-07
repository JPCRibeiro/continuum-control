package br.edu.infnet.continuum_control.historico.service;

import br.edu.infnet.continuum_control.anomalia.domain.Anomalia;
import br.edu.infnet.continuum_control.historico.domain.Historico;
import br.edu.infnet.continuum_control.historico.domain.TipoHistorico;
import br.edu.infnet.continuum_control.historico.repository.HistoricoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoricoService {
    private final HistoricoRepository historicoRepository;

    public Historico registrar(Anomalia anomalia, TipoHistorico tipo, String descricao) {
        Historico historico = new Historico(anomalia, tipo, descricao);

        return historicoRepository.save(historico);
    }

    public List<Historico> listarPorAnomalia(Long anomaliaId) {
        return historicoRepository.findByAnomaliaIdOrderByOcorridoEmAsc(anomaliaId);
    }
}
