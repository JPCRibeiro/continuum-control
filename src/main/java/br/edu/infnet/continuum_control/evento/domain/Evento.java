package br.edu.infnet.continuum_control.evento.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "eventos")
@Getter@Setter
public class Evento {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String acontecimento;

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = false)
    private LocalDate data;

    @Column(nullable = false)
    private String localizacao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Importancia importancia;

    protected Evento() {}

    public Evento(String acontecimento, String descricao, LocalDate data, String localizacao,
                  Importancia importancia) {
        this.acontecimento = acontecimento;
        this.descricao = descricao;
        this.data = data;
        this.localizacao = localizacao;
        this.importancia = importancia;
    }
}
