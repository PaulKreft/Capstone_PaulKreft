package de.neuefische.paulkreft.backend.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;


class GlobalExceptionHandlerTest {

    @Test
    void noResourceFoundHandlerTest_whenEnvDevelopment_returnLocalhostPlusPath() {
        // Given
        String basePath = "http://localhost:5173/";
        String path = "test";
        String env = "development";
        NoResourceFoundException exception = new NoResourceFoundException(HttpMethod.GET, path);
        GlobalExceptionHandler handler = new GlobalExceptionHandler(env);

        // When
        ModelAndView expected = new ModelAndView("redirect:" + basePath + path);
        ModelAndView actual = handler.noResourceFoundHandler(exception);

        // Then
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    void noResourceFoundHandlerTest_whenEnvProduction_returnRelativePath() {
        // Given
        String basePath = "/";
        String path = "test";
        String env = "production";
        NoResourceFoundException exception = new NoResourceFoundException(HttpMethod.GET, path);
        GlobalExceptionHandler handler = new GlobalExceptionHandler(env);

        // When
        ModelAndView expected = new ModelAndView("redirect:" + basePath + path);
        ModelAndView actual = handler.noResourceFoundHandler(exception);

        // Then
        assertEquals(expected.toString(), actual.toString());
    }
}