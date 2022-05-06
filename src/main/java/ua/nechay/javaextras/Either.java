package ua.nechay.javaextras;

import javax.annotation.Nonnull;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Represents a disjoint union, containing either something or something else.
 *
 * @param <LeftT>  - type of the left value
 * @param <RightT> - type of the right value
 * @author anechaev
 * @since 28.03.2022
 */
public abstract class Either<LeftT, RightT> {

    /**
     * @return the left value or throws an exception if this is right
     * @throws NoSuchElementException if this is right value
     */
    public LeftT getLeft() {
        throw new NoSuchElementException("No left element present");
    }

    public RightT getRight() {
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

    public abstract Either<LeftT, RightT> onLeft(@Nonnull Consumer<LeftT> leftConsumer);

    public abstract Either<LeftT, RightT> onRight(@Nonnull Consumer<RightT> rightConsumer);

    public abstract <ResultT> Either<ResultT, RightT> mapLeft(@Nonnull Function<LeftT, ResultT> leftFunction);

    public abstract <ResultT> Either<LeftT, ResultT> mapRight(@Nonnull Function<RightT, ResultT> rightFunction);

    public Either<RightT, LeftT> swap() {
        return isRight() ? Either.left(getRight()) : Either.right(getLeft());
    }

    public static <LeftT, RightT> Either<LeftT, RightT> left(LeftT left) {
        return new Left<>(left);
    }

    public static <LeftT, RightT> Either<LeftT, RightT> right(RightT right) {
        return new Right<>(right);
    }

    private static class Left<LeftT, RightT> extends Either<LeftT, RightT> {

        private final LeftT left;

        private Left(LeftT left) {
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
        public Either<LeftT, RightT> onLeft(@Nonnull Consumer<LeftT> leftConsumer) {
            leftConsumer.accept(left);
            return this;
        }

        @Override
        public Either<LeftT, RightT> onRight(@Nonnull Consumer<RightT> rightConsumer) {
            return this;
        }

        @Override
        public <ResultT> Either<ResultT, RightT> mapLeft(@Nonnull Function<LeftT, ResultT> leftFunction) {
            return Either.left(leftFunction.apply(left));
        }

        @Override
        public <ResultT> Either<LeftT, ResultT> mapRight(@Nonnull Function<RightT, ResultT> rightFunction) {
            return Either.left(left);
        }

        @Override
        public LeftT getLeft() {
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

    private static class Right<LeftT, RightT> extends Either<LeftT, RightT> {

        private final RightT right;

        private Right(RightT right) {
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
        public Either<LeftT, RightT> onLeft(@Nonnull Consumer<LeftT> leftConsumer) {
            return this;
        }

        @Override
        public Either<LeftT, RightT> onRight(@Nonnull Consumer<RightT> rightConsumer) {
            rightConsumer.accept(right);
            return this;
        }

        @Override
        public <ResultT> Either<ResultT, RightT> mapLeft(@Nonnull Function<LeftT, ResultT> leftFunction) {
            return Either.right(right);
        }

        @Override
        public <ResultT> Either<LeftT, ResultT> mapRight(@Nonnull Function<RightT, ResultT> rightFunction) {
            return Either.right(rightFunction.apply(right));
        }

        @Override
        public RightT getRight() {
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
