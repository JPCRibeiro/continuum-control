package br.edu.infnet.continuum_control.shared.api;

public class OperacaoInvalidaException extends RuntimeException {
    public OperacaoInvalidaException(String message) {
        super(message);
    }
}
