package br.edu.ufersa.locadora.exceptions;

public class PermissaoNegadaException extends RuntimeException {

    public PermissaoNegadaException() {
        super();
    }

    public PermissaoNegadaException(String mensagem) {
        super(mensagem);
    }

    public PermissaoNegadaException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }

    public PermissaoNegadaException(Throwable causa) {
        super(causa);
    }
}