package de.neuefische.paulkreft.backend.exception;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
}
