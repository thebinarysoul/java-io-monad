package com.thebinarysoul.io;

/**
 * Abstraction which represents some side-effects, like logging, work with console or any kind of work with "outer world"
 * @param <T> is result type of effect
 */
@FunctionalInterface
public interface Effect<T> {
    T run();
}
