package br.edu.infnet.continuum_control.auth.domain;

import br.edu.infnet.continuum_control.agente.domain.Agente;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "usuarios")
@Getter
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Perfil perfil;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agente_id")
    private Agente agente;

    protected Usuario() {}

    public Usuario(String nome, String email, String senha, Perfil perfil, Agente agente) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.perfil = perfil;
        this.agente = agente;
    }
}
