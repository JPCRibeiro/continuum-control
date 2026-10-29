package br.edu.infnet.continuum_control.missao.api.dto;

import br.edu.infnet.continuum_control.missao.domain.Autorizacao;

import java.time.LocalDateTime;

public record AutorizacaoResponse(
        Long id,
        Long missaoId,
        String responsavel,
        LocalDateTime autorizadaEm,
        String observacao
) {
    public static AutorizacaoResponse from(Autorizacao autorizacao) {
        return new AutorizacaoResponse(
                autorizacao.getId(),
                autorizacao.getMissao().getId(),
                autorizacao.getResponsavel(),
                autorizacao.getAutorizadaEm(),
                autorizacao.getObservacao()
        );
    }
}