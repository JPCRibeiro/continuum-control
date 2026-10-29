package br.edu.infnet.continuum_control.intervencao.domain;

import br.edu.infnet.continuum_control.agente.domain.Agente;
import br.edu.infnet.continuum_control.shared.Impacto;
import br.edu.infnet.continuum_control.missao.domain.Missao;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Table(name = "intervencoes")
@Getter
public class Intervencao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "missao_id", nullable = false)
    private Missao missao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agente_id", nullable = false)
    private Agente agente;

    @Column(name = "ocorrida_em", nullable = false)
    private LocalDateTime ocorridaEm;

    @Column(nullable = false)
    private String acao;

    @Column(nullable = false)
    private String justificativa;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Impacto impacto;

    @Column(nullable = false)
    private String resultado;

    protected Intervencao() {}

    public Intervencao(Missao missao, Agente agente, String acao, String justificativa, Impacto impacto,
                       String resultado) {
        this.missao = missao;
        this.agente = agente;
        this.ocorridaEm = LocalDateTime.now();
        this.acao = acao;
        this.justificativa = justificativa;
        this.impacto = impacto;
        this.resultado = resultado;
    }
}
