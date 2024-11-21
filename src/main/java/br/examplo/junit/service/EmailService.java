package br.examplo.junit.service;

import static br.examplo.junit.domain.Constantes.PADRAO_EMAIL;
import static br.examplo.junit.domain.Mensagens.EMAIL_INVALIDO;
import static br.examplo.junit.domain.Mensagens.EMAIL_NAO_ENCONTRADO;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Pattern;

import br.examplo.junit.repository.EmailRepository;

public class EmailService {

    private final EmailRepository repository;

    public EmailService(final EmailRepository repository) {
        this.repository = repository;
    }

    public void validar(final String email) {
        final boolean ehValido = Pattern.compile(PADRAO_EMAIL)
            .matcher(email).matches();

        if (!ehValido) {
            throw new IllegalArgumentException(EMAIL_INVALIDO);
        }
    }

    public List<String> consultarTodos() {
        return repository.findAll();
    }

    public String consultar(final String email) {
        this.validar(email);

        return repository.findByEmail(email)
            .orElseThrow(() -> new NoSuchElementException(EMAIL_NAO_ENCONTRADO));
    }
}
