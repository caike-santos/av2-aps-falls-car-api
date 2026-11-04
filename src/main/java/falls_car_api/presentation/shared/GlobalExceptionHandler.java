package falls_car_api.presentation.shared;

import falls_car_api.domain.shared.DomainException;
import falls_car_api.domain.shared.EntidadeNaoEncontradaException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.Instant;
import java.util.stream.Collectors;

/**
 * Envelope padrão de erro da equipe: timestamp, status, erro, mensagem, caminho.
 * ARQUIVO COMPARTILHADO: se outro dev já tiver criado um @RestControllerAdvice, fiquem só com um.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    public record ErroResponse(String timestamp, int status, String erro, String mensagem, String caminho) {}

    @ExceptionHandler(EntidadeNaoEncontradaException.class)
    public ResponseEntity<ErroResponse> naoEncontrada(EntidadeNaoEncontradaException ex, HttpServletRequest req) {
        return montar(HttpStatus.NOT_FOUND, ex.getClass().getSimpleName(), ex.getMessage(), req);
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErroResponse> regraDeDominio(DomainException ex, HttpServletRequest req) {
        return montar(HttpStatus.UNPROCESSABLE_ENTITY, ex.getClass().getSimpleName(), ex.getMessage(), req);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponse> validacao(MethodArgumentNotValidException ex, HttpServletRequest req) {
        String mensagem = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return montar(HttpStatus.BAD_REQUEST, "ValidacaoException", mensagem, req);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroResponse> corpoInvalido(HttpMessageNotReadableException ex, HttpServletRequest req) {
        return montar(HttpStatus.BAD_REQUEST, "CorpoInvalidoException",
                "Corpo da requisição inválido ou ilegível (confira tipos, UUIDs e valores de enum).", req);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErroResponse> parametroInvalido(MethodArgumentTypeMismatchException ex, HttpServletRequest req) {
        return montar(HttpStatus.BAD_REQUEST, "ParametroInvalidoException",
                "Valor inválido para o parâmetro '" + ex.getName() + "'.", req);
    }

    private ResponseEntity<ErroResponse> montar(HttpStatus status, String erro, String mensagem, HttpServletRequest req) {
        return ResponseEntity.status(status)
                .body(new ErroResponse(Instant.now().toString(), status.value(), erro, mensagem, req.getRequestURI()));
    }
}
