package br.edu.infnet.continuum_control.missao.domain;

import br.edu.infnet.continuum_control.agente.domain.Agente;
import br.edu.infnet.continuum_control.anomalia.domain.Anomalia;
import br.edu.infnet.continuum_control.anomalia.domain.Risco;
import br.edu.infnet.continuum_control.shared.api.OperacaoInvalidaException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "missoes")
@Getter
public class Missao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 8)
    private String codigo;

    @Column(nullable = false)
    private String objetivo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "anomalia_id", nullable = false)
    private Anomalia anomalia;

    @Column(nullable = false)
    private LocalDateTime destinoTemporal;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Prioridade prioridade;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SituacaoMissao situacao;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "missao_agentes",
            joinColumns = @JoinColumn(name = "missao_id"),
            inverseJoinColumns = @JoinColumn(name = "agente_id")
    )
    private Set<Agente> agentes = new HashSet<>();

    protected Missao() {}

    public Missao(String codigo, String objetivo, Anomalia anomalia, LocalDateTime destinoTemporal,
                  Prioridade prioridade) {
        this.codigo = codigo;
        this.objetivo = objetivo;
        this.anomalia = anomalia;
        this.destinoTemporal = destinoTemporal;
        this.prioridade = prioridade;
        this.situacao = SituacaoMissao.PLANEJADA;
    }

    public void designar(Agente agente) {
        if (situacao != SituacaoMissao.PLANEJADA && situacao != SituacaoMissao.AUTORIZADA) {
            throw new OperacaoInvalidaException("Não é possível designar agentes em missão no estado " + situacao);
        }

        agente.validarDisponibilidade();
        this.agentes.add(agente);
    }

    public void iniciar() {
        if (situacao != SituacaoMissao.PLANEJADA && situacao != SituacaoMissao.AUTORIZADA) {
            throw new OperacaoInvalidaException("Missão não pode ser iniciada no estado " + situacao);
        }

        if (agentes.isEmpty()) {
            throw new OperacaoInvalidaException("Missão não pode ser iniciada sem agentes designados");
        }

        if (exigeAutorizacao() && situacao != SituacaoMissao.AUTORIZADA) {
            throw new OperacaoInvalidaException("Missão exige autorização prévia antes da execução");
        }

        this.situacao = SituacaoMissao.EM_EXECUCAO;
    }

    public void autorizar() {
        if (situacao != SituacaoMissao.PLANEJADA) {
            throw new OperacaoInvalidaException(
                    "Somente missões planejadas podem ser autorizadas. Estado atual: " + situacao);
        }

        this.situacao = SituacaoMissao.AUTORIZADA;
    }

    public boolean exigeAutorizacao() {
        return prioridade == Prioridade.EMERGENCIA || anomalia.getRisco() == Risco.CRITICO;
    }

    public void concluir() {
        if (situacao != SituacaoMissao.EM_EXECUCAO) {
            throw new OperacaoInvalidaException(
                    "Somente missões em execução podem ser concluídas. Estado: " + situacao);
        }
        this.situacao = SituacaoMissao.CONCLUIDA;
    }

    public void falhar() {
        if (situacao != SituacaoMissao.EM_EXECUCAO) {
            throw new OperacaoInvalidaException("Somente missões em execução podem falhar. Estado: " + situacao);
        }
        this.situacao = SituacaoMissao.FALHOU;
    }

    public void cancelar() {
        if (situacao != SituacaoMissao.PLANEJADA && situacao != SituacaoMissao.AUTORIZADA) {
            throw new OperacaoInvalidaException(
                    "Somente missões não iniciadas podem ser canceladas. Estado: " + situacao);
        }
        this.situacao = SituacaoMissao.CANCELADA;
    }

    public void validarAceitaIntervencao() {
        if (situacao != SituacaoMissao.EM_EXECUCAO) {
            throw new OperacaoInvalidaException(
                    "Somente missões em execução recebem intervenções. Estado: " + situacao);
        }
    }

    public void validarAgenteParticipante(Agente agente) {
        if (!this.agentes.contains(agente)) {
            throw new OperacaoInvalidaException(
                    "O agente " + agente.getCodinome() + " não está designado para esta missão.");
        }
    }
}
