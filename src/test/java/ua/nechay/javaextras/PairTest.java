package ua.nechay.javaextras;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * @author anechaev
 * @since 07.05.2022
 */
public class PairTest {

    @Test
    public void testOfCreating() {
        int first = 3;
        int second = 4;
        Pair<Integer, Integer> pair = Pair.of(first, second);
        assertThat(pair.getFirst(), equalTo(first));
        assertThat(pair.getSecond(), equalTo(second));
    }

    @Test
    public void testOfZippingCollectionsWithEqualSize() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        List<String> names = Arrays.asList("John", "Bob", "Mike", "Alex", "Katty");
        List<Pair<Integer, String>> zipped = Pair.zipCollections(numbers, names);
        for (int i = 0; i < 4; i++) {
            assertThat(zipped.get(i).getFirst(), equalTo(numbers.get(i)));
            assertThat(zipped.get(i).getSecond(), equalTo(names.get(i)));
        }
    }
}
