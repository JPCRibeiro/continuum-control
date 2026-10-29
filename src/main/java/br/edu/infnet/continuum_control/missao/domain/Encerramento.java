package br.edu.infnet.continuum_control.missao.domain;

import br.edu.infnet.continuum_control.anomalia.domain.SituacaoAnomalia;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "encerramentos")
@Getter
public class Encerramento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "missao_id", nullable = false)
    private Missao missao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ResultadoMissao resultado;

    @Column(nullable = false)
    private String resumo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Impacto impacto;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SituacaoAnomalia situacaoFinalAnomalia;

    @Column(nullable = false)
    private String observacao;

    protected Encerramento() {}

    public Encerramento(Missao missao, ResultadoMissao resultado, String resumo, Impacto impacto, SituacaoAnomalia
            situacaoFinalAnomalia, String observacao) {
        this.missao = missao;
        this.resultado = resultado;
        this.resumo = resumo;
        this.impacto = impacto;
        this.situacaoFinalAnomalia = situacaoFinalAnomalia;
        this.observacao = observacao;
    }
}
