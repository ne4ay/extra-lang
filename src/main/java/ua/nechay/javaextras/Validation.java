package ua.nechay.javaextras;

import javax.annotation.Nonnull;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author anechaev
 * @since 28.03.2022
 */
public class Validation<EXCEPTION_TYPE extends Throwable, RESULT_TYPE> extends Either<EXCEPTION_TYPE, RESULT_TYPE> {

    private final Either<EXCEPTION_TYPE, RESULT_TYPE> value;

    private Validation(Either<EXCEPTION_TYPE, RESULT_TYPE> either) {
        this.value = either;
    }

    public static <ORIGINAL_TYPE, RESULT_TYPE> Validation<Throwable, RESULT_TYPE>
    tryOf(@Nonnull ORIGINAL_TYPE arg, @Nonnull Function<ORIGINAL_TYPE, RESULT_TYPE> function) {
        try {
            return new Validation<>(Either.right(function.apply(arg)));
        } catch (Throwable t) {
            return new Validation<>(Either.left(t));
        }
    }

    public <MAYBE_EXCEPTION_TYPE extends Throwable> Validation<EXCEPTION_TYPE, RESULT_TYPE>
    onCatch(@Nonnull Class<MAYBE_EXCEPTION_TYPE> exceptionType, @Nonnull Consumer<EXCEPTION_TYPE> consumer) {
        if (value.isLeft() && (exceptionType.isInstance(value.getLeft()))) {
            consumer.accept(value.getLeft());
        }
        return this;
    }

    public Validation<EXCEPTION_TYPE, RESULT_TYPE> raise() throws Throwable {
        if (value.isLeft()) {
            throw value.getLeft();
        }
        return this;
    }

    @Override
    public boolean isLeft() {
        return value.isLeft();
    }

    @Override
    public boolean isRight() {
        return value.isRight();
    }

    @Override
    public Either<EXCEPTION_TYPE, RESULT_TYPE> onLeft(@Nonnull Consumer<EXCEPTION_TYPE> leftConsumer) {
        return value.onLeft(leftConsumer);
    }

    @Override
    public Either<EXCEPTION_TYPE, RESULT_TYPE> onRight(@Nonnull Consumer<RESULT_TYPE> rightConsumer) {
        return value.onRight(rightConsumer);
    }

    @Override
    public <RESULT> Either<RESULT, RESULT_TYPE> mapLeft(@Nonnull Function<EXCEPTION_TYPE, RESULT> leftFunction) {
        return value.mapLeft(leftFunction);
    }

    @Override
    public <RESULT> Either<EXCEPTION_TYPE, RESULT> mapRight(@Nonnull Function<RESULT_TYPE, RESULT> rightFunction) {
        return value.mapRight(rightFunction);
    }

    @Override
    public EXCEPTION_TYPE getLeft() {
        return value.getLeft();
    }

    @Override
    public RESULT_TYPE getRight() {
        return value.getRight();
    }
}
