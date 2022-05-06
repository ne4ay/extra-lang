package ua.nechay.javaextras;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author anechaev
 * @since 28.03.2022
 */
public class Validation<ExceptionT extends Throwable, ResultT> extends Either<ExceptionT, ResultT> {

    private final Either<ExceptionT, ResultT> value;

    private Validation(@Nonnull Either<ExceptionT, ResultT> either) {
        this.value = either;
    }

    public static <OriginalT, ResultT> Validation<Throwable, ResultT>
    tryOf(@Nonnull OriginalT arg, @Nonnull Function<OriginalT, ResultT> function) {
        try {
            return new Validation<>(Either.right(function.apply(arg)));
        } catch (Throwable t) {
            return new Validation<>(Either.left(t));
        }
    }

    public <MaybeExceptionT extends Throwable> Validation<ExceptionT, ResultT>
    onCatch(@Nonnull Consumer<ExceptionT> consumer, @Nonnull Class<MaybeExceptionT> exceptionType) {
        if (value.isLeft() && (exceptionType.isInstance(value.getLeft()))) {
            consumer.accept(value.getLeft());
        }
        return this;
    }

    public <MaybeExceptionT extends Throwable> Validation<ExceptionT, ResultT>
    onCatch(@Nonnull Consumer<ExceptionT> consumer, @Nonnull Class<MaybeExceptionT>... exceptionTypes) {
        return onCatch(consumer, Arrays.asList(exceptionTypes));
    }

    public <MaybeExceptionT extends Throwable> Validation<ExceptionT, ResultT>
    onCatch(@Nonnull Consumer<ExceptionT> consumer, @Nonnull Collection<Class<MaybeExceptionT>> exceptionTypes) {
        if (!value.isLeft()) {
            return this;
        }
        boolean isRelatedToGivenTypes = exceptionTypes.stream()
            .anyMatch(type -> type.isInstance(value.getLeft()));
        if (isRelatedToGivenTypes) {
            consumer.accept(value.getLeft());
        }
        return this;
    }

    public Validation<ExceptionT, ResultT> raise() throws Throwable {
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
    public Either<ExceptionT, ResultT> onLeft(@Nonnull Consumer<ExceptionT> leftConsumer) {
        return value.onLeft(leftConsumer);
    }

    @Override
    public Either<ExceptionT, ResultT> onRight(@Nonnull Consumer<ResultT> rightConsumer) {
        return value.onRight(rightConsumer);
    }

    @Override
    public <RESULT> Either<RESULT, ResultT> mapLeft(@Nonnull Function<ExceptionT, RESULT> leftFunction) {
        return value.mapLeft(leftFunction);
    }

    @Override
    public <RESULT> Either<ExceptionT, RESULT> mapRight(@Nonnull Function<ResultT, RESULT> rightFunction) {
        return value.mapRight(rightFunction);
    }

    @Override
    public ExceptionT getLeft() {
        return value.getLeft();
    }

    @Override
    public ResultT getRight() {
        return value.getRight();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Validation))
            return false;
        Validation<?, ?> that = (Validation<?, ?>) o;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
