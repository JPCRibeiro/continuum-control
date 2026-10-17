package br.edu.infnet.continuum_control.shared;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class GeradorCodigo {
    private static final char[] CARACTERES = "0123456789abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final Random RANDOM = new Random();

    public String gerar() {
        StringBuilder sb = new StringBuilder(8);
        for (int i = 0; i < 8; i++) {
            sb.append(CARACTERES[RANDOM.nextInt(CARACTERES.length)]);
        }
        return sb.toString();
    }
}