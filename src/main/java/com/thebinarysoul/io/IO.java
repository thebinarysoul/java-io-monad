package com.thebinarysoul.io;

import com.thebinarysoul.data.Either;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * The data structure that implements the IO Monad abstraction to encapsulate side effects
 *
 * @param <A> is result type of effect execution
 */
public class IO<A> {
    private final Effect<A> effect;

    private IO(Effect<A> effect) {
        this.effect = effect;
    }

    /**
     * This is the point of wrapping a side-effect into IO Monad
     */
    public static <T> IO<T> apply(final Effect<T> effect) {
        return new IO<>(effect);
    }

    /**
     * This is a binding function which is mapping inner effect into another IO Monad
     */
    public <B> IO<B> flatMap(final Function<? super A, ? extends IO<B>> function) {
        return IO.apply(() -> function.apply(effect.run()).unsafeRun());
    }

    /**
     * This is a function which is mapping inner effect into a new effect, by represented
     * of combination apply and flatMap operations
     */
    public <B> IO<B> map(final Function<? super A, ? extends B> function) {
        return this.flatMap(result -> IO.apply(() -> function.apply(result)));
    }

    /**
     * This is a function which is mapping inner effect into a new effect with result type Void, by represented
     * of combination apply and flatMap operations
     */
    public IO<Void> mapToVoid(final Consumer<? super A> function) {
        return this.flatMap(result -> IO.apply(() -> {
            function.accept(result);
            //We can't instantiate Void, hence we can return null only
            return null;
        }));
    }

    /**
     * Launches a side effect and gets its result or dies.
     */
    public A unsafeRun() {
        return effect.run();
    }

    /**
     * Launches a side effect and gets the result of a successful execution or an error that occurred during this execution.
     */
    public Either<Exception, A> safeRun() {
        try {
            return Either.right(unsafeRun());
        } catch (Exception ex) {
            return Either.left(ex);
        }
    }


    public static IO<Void> putStrLn(final String line) {
        return IO.apply(() -> {
            System.out.println(line);
            return null;
        });
    }

    public static IO<String> readLn() {
        return IO.apply(() -> System.console().readLine());
    }

    public static void main(String[] args) {
        IO.apply(() -> "What is your name friend?")
                .mapToVoid(System.out::println)
                .map(ignored -> System.console().readLine())
                .map(name -> String.format("Hello %s! Do you want to talk about Monads?", name))
                .mapToVoid(System.out::println);

    }
}
