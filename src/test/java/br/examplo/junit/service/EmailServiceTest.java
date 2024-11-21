package br.examplo.junit.service;

import static br.examplo.junit.domain.Mensagens.EMAIL_INVALIDO;
import static br.examplo.junit.domain.Mensagens.EMAIL_NAO_ENCONTRADO;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import br.examplo.junit.repository.EmailRepository;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    private EmailService emailService;

    @Mock
    private EmailRepository repository;

    @BeforeEach
    public void setUp() {
        emailService = new EmailService(repository);
    }

    @Test
    public void deveLancarErroQuandoEmailInvalido() {
        final String email = "teste.example.com";

        final Exception exception = assertThrows(IllegalArgumentException.class,
            () -> emailService.validar(email));

        assertEquals(EMAIL_INVALIDO, exception.getMessage());
    }

    @Test
    public void naoDeveLancarErroQuandoEmailValido() {
        final String email = "teste@example.com";
        emailService.validar(email);
    }

    @Test
    public void deveConsultarTodosEmailsCadastradosBancoDeDados() {
        final List<String> emailsValidos = asList("teste@example.com", "fulano@email.com", "fulano@example.com");

        Mockito.when(repository.findAll()).thenReturn(emailsValidos);

        final List<String> resultado = emailService.consultarTodos();

        assertEquals(emailsValidos, resultado);
    }

    @Test
    public void deveConsultarEmailNoBancoDeDados() {
        final String emailEsperado = "teste@example.com";

        Mockito.when(repository.findByEmail(emailEsperado))
            .thenReturn(Optional.of(emailEsperado));

        final String resultado = emailService.consultar(emailEsperado);

        verify(repository, Mockito.times(1)).findByEmail(emailEsperado);

        assertEquals(emailEsperado, resultado);
    }

    @Test
    public void deveLancarExcecaoQuandoConsultarComEmailInvalido() {
        final String email = "teste.example.com";

        final Exception exception = assertThrows(IllegalArgumentException.class,
            () -> emailService.consultar(email));

        assertEquals(EMAIL_INVALIDO, exception.getMessage());
    }

    @Test
    public void deveLancarExcecaoQuandoEmailNaoExistirNoBancoDeDados() {
        final String email = "teste@example.com";

        Mockito.when(repository.findByEmail(email))
            .thenReturn(Optional.empty());

        final Exception exception = assertThrows(NoSuchElementException.class,
            () -> emailService.consultar(email));

        verify(repository, Mockito.times(1)).findByEmail(email);

        assertEquals(EMAIL_NAO_ENCONTRADO, exception.getMessage());
    }

}