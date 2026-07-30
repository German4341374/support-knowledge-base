package dev.supportkb.web;

import dev.supportkb.article.ArticleNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

  @ExceptionHandler(ArticleNotFoundException.class)
  ProblemDetail notFound(ArticleNotFoundException exception, HttpServletRequest request) {
    return problem(HttpStatus.NOT_FOUND, exception.getMessage(), request);
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  ProblemDetail conflict(DataIntegrityViolationException exception, HttpServletRequest request) {
    return problem(
        HttpStatus.CONFLICT, "The submitted article conflicts with an existing record.", request);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  ProblemDetail invalid(MethodArgumentNotValidException exception, HttpServletRequest request) {
    ProblemDetail detail = problem(HttpStatus.BAD_REQUEST, "Request validation failed.", request);
    detail.setProperty(
        "errors",
        exception.getBindingResult().getFieldErrors().stream()
            .collect(
                java.util.stream.Collectors.toMap(
                    error -> error.getField(),
                    error -> error.getDefaultMessage(),
                    (first, ignored) -> first)));
    return detail;
  }

  private ProblemDetail problem(HttpStatus status, String detail, HttpServletRequest request) {
    ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, detail);
    problem.setTitle(status.getReasonPhrase());
    problem.setInstance(URI.create(request.getRequestURI()));
    return problem;
  }
}
