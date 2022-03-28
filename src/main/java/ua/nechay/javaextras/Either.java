package ua.nechay.javaextras;

import javax.annotation.Nonnull;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Represents a disjoint union, containing either something or something else.
 *
 * @param <LEFT_TYPE>  - type of the left value
 * @param <RIGHT_TYPE> - type of the right value
 * @author anechaev
 * @since 28.03.2022
 */
public abstract class Either<LEFT_TYPE, RIGHT_TYPE> {

    /**
     * @return the left value or throws an exception if this is right
     * @throws NoSuchElementException if this is right value
     */
    public LEFT_TYPE getLeft() {
        throw new NoSuchElementException("No left element present");
    }

    public RIGHT_TYPE getRight() {
        throw new NoSuchElementException("No right element present");
    }

    /**
     * @return true if it is left value, false otherwise
     */
    public abstract boolean isLeft();

    /**
     * @return true if it is right value, false otherwise
     */
    public abstract boolean isRight();

    public abstract Either<LEFT_TYPE, RIGHT_TYPE> onLeft(@Nonnull Consumer<LEFT_TYPE> leftConsumer);

    public abstract Either<LEFT_TYPE, RIGHT_TYPE> onRight(@Nonnull Consumer<RIGHT_TYPE> rightConsumer);

    public abstract <RESULT> Either<RESULT, RIGHT_TYPE> mapLeft(@Nonnull Function<LEFT_TYPE, RESULT> leftFunction);

    public abstract <RESULT> Either<LEFT_TYPE, RESULT> mapRight(@Nonnull Function<RIGHT_TYPE, RESULT> rightFunction);

    public Either<RIGHT_TYPE, LEFT_TYPE> swap() {
        return isRight() ? Either.left(getRight()) : Either.right(getLeft());
    }

    public static <LEFT_TYPE, RIGHT_TYPE> Either<LEFT_TYPE, RIGHT_TYPE> left(LEFT_TYPE left) {
        return new Left<>(left);
    }

    public static <LEFT_TYPE, RIGHT_TYPE> Either<LEFT_TYPE, RIGHT_TYPE> right(RIGHT_TYPE right) {
        return new Right<>(right);
    }

    private static class Left<LEFT_TYPE, RIGHT_TYPE> extends Either<LEFT_TYPE, RIGHT_TYPE> {

        private final LEFT_TYPE left;

        private Left(LEFT_TYPE left) {
            this.left = left;
        }

        @Override
        public boolean isLeft() {
            return true;
        }

        @Override
        public boolean isRight() {
            return false;
        }

        @Override
        public Either<LEFT_TYPE, RIGHT_TYPE> onLeft(@Nonnull Consumer<LEFT_TYPE> leftConsumer) {
            leftConsumer.accept(left);
            return this;
        }

        @Override
        public Either<LEFT_TYPE, RIGHT_TYPE> onRight(@Nonnull Consumer<RIGHT_TYPE> rightConsumer) {
            return this;
        }

        @Override
        public <RESULT> Either<RESULT, RIGHT_TYPE> mapLeft(@Nonnull Function<LEFT_TYPE, RESULT> leftFunction) {
            return Either.left(leftFunction.apply(left));
        }

        @Override
        public <RESULT> Either<LEFT_TYPE, RESULT> mapRight(@Nonnull Function<RIGHT_TYPE, RESULT> rightFunction) {
            return Either.left(left);
        }

        @Override
        public LEFT_TYPE getLeft() {
            return left;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (!(o instanceof Left))
                return false;
            Left<?, ?> left1 = (Left<?, ?>) o;
            return Objects.equals(left, left1.left);
        }

        @Override
        public int hashCode() {
            return Objects.hash(left);
        }
    }

    private static class Right<LEFT_TYPE, RIGHT_TYPE> extends Either<LEFT_TYPE, RIGHT_TYPE> {

        private final RIGHT_TYPE right;

        private Right(RIGHT_TYPE right) {
            this.right = right;
        }

        @Override
        public boolean isLeft() {
            return false;
        }

        @Override
        public boolean isRight() {
            return true;
        }

        @Override
        public Either<LEFT_TYPE, RIGHT_TYPE> onLeft(@Nonnull Consumer<LEFT_TYPE> leftConsumer) {
            return this;
        }

        @Override
        public Either<LEFT_TYPE, RIGHT_TYPE> onRight(@Nonnull Consumer<RIGHT_TYPE> rightConsumer) {
            rightConsumer.accept(right);
            return this;
        }

        @Override
        public <RESULT> Either<RESULT, RIGHT_TYPE> mapLeft(@Nonnull Function<LEFT_TYPE, RESULT> leftFunction) {
            return Either.right(right);
        }

        @Override
        public <RESULT> Either<LEFT_TYPE, RESULT> mapRight(@Nonnull Function<RIGHT_TYPE, RESULT> rightFunction) {
            return Either.right(rightFunction.apply(right));
        }

        @Override
        public RIGHT_TYPE getRight() {
            return right;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (!(o instanceof Right))
                return false;
            Right<?, ?> right1 = (Right<?, ?>) o;
            return Objects.equals(right, right1.right);
        }

        @Override
        public int hashCode() {
            return Objects.hash(right);
        }
    }

}
