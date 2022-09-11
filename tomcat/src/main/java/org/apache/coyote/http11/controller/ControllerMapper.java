package org.apache.coyote.http11.controller;

import static nextstep.jwp.exception.ExceptionType.INVALID_HANDLER_EXCEPTION;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

public enum ControllerMapper {

    ROOT_CONTROLLER((path) -> path.equals("/"), RootController.getINSTANCE()),
    LOGIN_CONTROLLER((path) -> path.startsWith("/login"), LoginController.getINSTANCE()),
    RESISTER((path) -> path.startsWith("/register"), RegisterController.getINSTANCE()),
    RESOURCE_CONTROLLER((path) -> path.contains("."), ResourceController.getINSTANCE());

    private final Predicate<String> regex;
    private final Handler handler;

    ControllerMapper(final Predicate<String> regex, final Handler handler) {
        this.regex = regex;
        this.handler = handler;
    }

    public static Handler findController(final String path) {
        return Arrays.stream(values())
                .filter(it -> it.regex.test(path))
                .map(ControllerMapper::getHandler)
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(INVALID_HANDLER_EXCEPTION.getMessage()));
    }

    public Handler getHandler() {
        return handler;
    }
}
