package ua.nechay.javaextras;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author anechaev
 * @since 07.05.2022
 */
public class Pair<FirstT, SecondT> {
    private final FirstT first;
    private final SecondT second;

    public Pair(@Nullable FirstT first, @Nullable SecondT second) {
        this.first = first;
        this.second = second;
    }

    @Nonnull
    public static <FirstT, SecondT> Pair<FirstT, SecondT> of(FirstT first, SecondT second) {
        return new Pair<>(first, second);
    }

    @Nonnull
    public Pair<SecondT, FirstT> swap() {
        return new Pair<>(second, first);
    }

    @Nullable
    public FirstT getFirst() {
        return first;
    }

    @Nullable
    public SecondT getSecond() {
        return second;
    }

    @Nonnull
    public <ResultT> Pair<ResultT, SecondT> mapFirst(Function<FirstT, ResultT> firstFunction) {
        return Pair.of(firstFunction.apply(first), second);
    }

    @Nonnull
    public <ResultT> Pair<FirstT, ResultT> mapSecond(Function<SecondT, ResultT> secondFunction) {
        return Pair.of(first, secondFunction.apply(second));
    }

    @Nonnull
    public <ResultT> ResultT merge(BiFunction<FirstT, SecondT, ResultT> mergeFunction) {
        return mergeFunction.apply(first, second);
    }

    @Nonnull
    public static <FirstT, SecondT> List<Pair<FirstT, SecondT>>
    zipCollections(Collection<FirstT> firstCollection, Collection<SecondT> secondCollection) {
        return zipToStream(firstCollection, secondCollection)
            .collect(Collectors.toList());
    }

    @Nonnull
    public static <FirstT, SecondT, C extends Collection<Pair<FirstT, SecondT>>> C zipCollections(
        Collection<FirstT> firstCollection, Collection<SecondT> secondCollection, Collector<Pair<FirstT, SecondT>, ?, C> collector) {
        return zipToStream(firstCollection, secondCollection)
            .collect(collector);
    }

    @Nonnull
    public static <FirstT, SecondT> Stream<Pair<FirstT, SecondT>>
    zipToStream(Collection<FirstT> firstCollection, Collection<SecondT> secondCollection) {
        int firstSize = firstCollection.size();
        int secondSize = secondCollection.size();
        int maxSize = Math.max(firstSize, secondSize);
        Iterator<FirstT> firstIterator = firstCollection.iterator();
        Iterator<SecondT> secondIterator = secondCollection.iterator();
        return IntStream.range(0, maxSize)
            .boxed()
            .map(i -> {
                FirstT nextFirst = i < firstSize ? firstIterator.next() : null;
                SecondT nextSecond = i < secondSize ? secondIterator.next() : null;
                return Pair.of(nextFirst, nextSecond);
            });
    }
}
