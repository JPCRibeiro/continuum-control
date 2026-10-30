package br.edu.infnet.continuum_control.ocorrencia.domain;

public record EvidenciaTemporal(
        String objeto,
        String periodoEstimado,
        Double confiabilidade
) {}