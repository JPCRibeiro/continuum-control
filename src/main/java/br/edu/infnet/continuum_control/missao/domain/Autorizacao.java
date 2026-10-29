package br.edu.infnet.continuum_control.missao.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Table(name = "autorizacoes")
@Getter
public class Autorizacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "missao_id", nullable = false)
    private Missao missao;

    @Column(nullable = false)
    private String responsavel;

    @Column(name = "autorizada_em", nullable = false)
    private LocalDateTime autorizadaEm;

    @Column(nullable = false)
    private String observacao;

    protected Autorizacao() {}

    public Autorizacao(Missao missao, String responsavel, String observacao) {
        this.missao = missao;
        this.responsavel = responsavel;
        this.autorizadaEm = LocalDateTime.now();
        this.observacao = observacao;
    }
}