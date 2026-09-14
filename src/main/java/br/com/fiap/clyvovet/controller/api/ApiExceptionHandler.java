package br.com.fiap.clyvovet.controller.api;

import br.com.fiap.clyvovet.dto.ErroCampoResponse;
import br.com.fiap.clyvovet.dto.ErroResponse;
import br.com.fiap.clyvovet.exception.RecursoNaoEncontradoException;
import br.com.fiap.clyvovet.exception.RegraDeNegocioException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice(annotations = RestController.class)
public class ApiExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ErroCampoResponse>> tratarValidacao(MethodArgumentNotValidException excecao) {
        List<ErroCampoResponse> erros = excecao.getBindingResult().getFieldErrors().stream()
                .map(erro -> new ErroCampoResponse(erro.getField(), erro.getDefaultMessage()))
                .toList();
        return ResponseEntity.badRequest().body(erros);
    }

    @ExceptionHandler(RegraDeNegocioException.class)
    public ResponseEntity<ErroResponse> tratarRegraDeNegocio(RegraDeNegocioException excecao) {
        return ResponseEntity.badRequest().body(new ErroResponse(excecao.getMessage()));
    }

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroResponse> tratarNaoEncontrado(RecursoNaoEncontradoException excecao) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErroResponse(excecao.getMessage()));
    }
}
