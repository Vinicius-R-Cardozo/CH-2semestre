package br.com.fiap.clyvovet.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class TratadorDeErros {

    record ErroCampo(String campo, String mensagem) {}
    record Erro(String mensagem) {}

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    List<ErroCampo> dadosInvalidos(MethodArgumentNotValidException excecao) {
        return excecao.getBindingResult().getFieldErrors().stream()
                .map(erro -> new ErroCampo(erro.getField(), erro.getDefaultMessage()))
                .toList();
    }

    @ExceptionHandler(RegraDeNegocioException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    Erro regraDeNegocio(RegraDeNegocioException excecao) {
        return new Erro(excecao.getMessage());
    }

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    Erro naoEncontrado(RecursoNaoEncontradoException excecao) {
        return new Erro(excecao.getMessage());
    }
}
