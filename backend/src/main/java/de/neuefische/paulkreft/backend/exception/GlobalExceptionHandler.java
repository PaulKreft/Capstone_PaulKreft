package de.neuefische.paulkreft.backend.exception;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
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
    public String handleException(EmailAlreadyRegisteredException exception) {
        return exception.getMessage();
    }

    @ExceptionHandler({IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleException(IllegalArgumentException exception) {
        return exception.getMessage();
    }

    @ExceptionHandler({OAuth2AuthenticationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleException(OAuth2AuthenticationException exception) {
        return exception.getMessage();
    }

    @ExceptionHandler({GithubEmailNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleException(GithubEmailNotFoundException exception) {
        return exception.getMessage();
    }

    @ExceptionHandler({UsernameNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleException(UsernameNotFoundException exception) {
        return exception.getMessage();
    }

    @ExceptionHandler({LobbyGoneException.class})
    @ResponseStatus(HttpStatus.GONE)
    public String handleException(LobbyGoneException exception) {
        return exception.getMessage();
    }

    @ExceptionHandler({RequestQueueNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleException(RequestQueueNotFoundException exception) {
        return exception.getMessage();
    }

    @ExceptionHandler({PlayerNotPartOfLobbyException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleException(PlayerNotPartOfLobbyException exception) {
        return exception.getMessage();
    }

    @ExceptionHandler({LobbyCapacityExceededException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleException(LobbyCapacityExceededException exception) {
        return exception.getMessage();
    }
}
