package br.edu.infnet.continuum_control.ocorrencia.domain;

import java.time.Duration;

public record DistorcaoTemporal(
        Double intensidade,
        Duration duracao
) {}