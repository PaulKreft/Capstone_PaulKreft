package de.neuefische.paulkreft.backend.exception;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {
    private final String environment;

    public GlobalExceptionHandler(@Value("${app.environment}") String environment) {
        this.environment = environment;
    }

    @ExceptionHandler({NoResourceFoundException.class})
    public ModelAndView noResourceFoundHandler(NoResourceFoundException e) {
        String basePath = environment.equals("production") ? "/" : "http://localhost:5173/";
        String route = environment.equals("production") ? "" : e.getResourcePath();
        return new ModelAndView("redirect:" + basePath + route);
    }

    @ExceptionHandler({EmailAlreadyRegisteredException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorMessage handleException(EmailAlreadyRegisteredException exception) {
        return new ErrorMessage(exception.getMessage());
    }

    @ExceptionHandler({IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleException(IllegalArgumentException exception) {
        return new ErrorMessage(exception.getMessage());
    }

    @ExceptionHandler({OAuth2LoginException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleException(OAuth2LoginException exception) {
        return new ErrorMessage(exception.getMessage());
    }

    @ExceptionHandler({GithubEmailNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage handleException(GithubEmailNotFoundException exception) {
        return new ErrorMessage(exception.getMessage());
    }
}
