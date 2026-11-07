package br.edu.infnet.continuum_control.historico.domain;

import br.edu.infnet.continuum_control.anomalia.domain.Anomalia;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Table(name = "historicos_anomalias")
@Getter
public class Historico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "anomalia_id", nullable = false)
    private Anomalia anomalia;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoHistorico tipo;

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = false)
    private LocalDateTime ocorridoEm;

    protected Historico() {}

    public Historico(Anomalia anomalia, TipoHistorico tipo, String descricao) {
        this.anomalia = anomalia;
        this.tipo = tipo;
        this.descricao = descricao;
        this.ocorridoEm = LocalDateTime.now();
    }
}
