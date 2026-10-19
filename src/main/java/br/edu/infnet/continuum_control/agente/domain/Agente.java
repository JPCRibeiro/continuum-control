package br.edu.infnet.continuum_control.agente.domain;

import br.edu.infnet.continuum_control.shared.api.OperacaoInvalidaException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "agentes")
@Getter
public class Agente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 8)
    private String codigo;

    @Setter
    @Column(nullable = false)
    private String codinome;

    @Setter
    @Column(nullable = false)
    private String especialidade;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SituacaoAgente situacao;

    protected Agente() {}

    public Agente(String codigo, String codinome, String especialidade) {
        this.codigo = codigo;
        this.codinome = codinome;
        this.especialidade = especialidade;
        this.situacao = SituacaoAgente.DISPONIVEL;
    }

    public void inativar() {
        if (situacao == SituacaoAgente.EM_MISSAO) {
            throw new OperacaoInvalidaException("Agente em missao não pode ser inativado");
        }

        this.situacao = SituacaoAgente.INATIVO;
    }
}