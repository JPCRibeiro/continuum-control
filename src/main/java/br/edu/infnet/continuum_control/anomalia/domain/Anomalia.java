package br.edu.infnet.continuum_control.anomalia.domain;

import br.edu.infnet.continuum_control.evento.domain.Evento;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "anomalias")
@Getter
public class Anomalia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evento_id", nullable = false)
    private Evento evento;

    @Setter
    @Column(nullable = false)
    private String divergencia;

    @Column(nullable = false)
    private LocalDateTime detectadaEm;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Risco risco;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SituacaoAnomalia situacao;

    @Setter
    private String origem;

    @Setter
    private String observacao;

    protected Anomalia() {}

    public Anomalia(Evento evento, String divergencia, Risco risco, String origem, String observacao) {
        this.evento = evento;
        this.divergencia = divergencia;
        this.detectadaEm = LocalDateTime.now();
        this.risco = risco;
        this.situacao = SituacaoAnomalia.DETECTADA;
        this.origem = origem;
        this.observacao = observacao;
    }

    public void classificarRisco(Risco novoRisco) {
        this.risco = novoRisco;
    }
}
