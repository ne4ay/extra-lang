package ua.nechay.javaextras;

import javax.annotation.Nonnull;
import java.util.function.Function;

/**
 * Function that accepts three arguments and produces a result.
 * This is the three-arity specialization of {@link Function}.
 *
 * This is a functional interface,
 * whose functional method is {@link #apply(Object, Object, Object)}.
 *
 * @param <T> the type of the first argument to the function
 * @param <U> the type of the second argument to the function
 * @param <V> the type of the third argument to the function
 * @param <R> the type of the result of the function
 * @author anechaev
 * @since 29.03.2022
 */
@FunctionalInterface
public interface TriFunction<T, U, V, R> {

    /**
     * Applies this function to the given arguments.
     *
     * @param t the first function argument
     * @param u the second function argument
     * @param v the third function argument
     * @return the function result
     */
    R apply(T t, U u, V v);

    /**
     * Returns a composed function that first applies this function to
     * its input, and then applies the {@code after} function to the result.
     * If evaluation of either function throws an exception, it is relayed to
     * the caller of the composed function.
     *
     * @param <P> the type of output of the {@code after} function, and of the
     *           composed function
     * @param after the function to apply after this function is applied
     * @return a composed function that first applies this function and then
     * applies the {@code after} function
     * @throws NullPointerException if after is null
     */
    @Nonnull
    default <P> TriFunction<T, U, V, P> andThen(@Nonnull Function<? super R, ? extends P> after) {
        return (T t, U u, V v) -> after.apply(apply(t, u, v));
    }
}
