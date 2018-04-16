package lou.ignite.computegrid;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import org.apache.ignite.lang.IgniteCallable;

import lou.ignite.Ig;
import lou.ignite.util.Base;

/**
 * Executes callable jobs the grid.
 *  
 * @author phuc
 */
public class ComputeCallables extends Base {

    public static void main(String[] args) {
        try (Ig ig = Ig.client()) {
            // create jobs as Callable
            Collection<IgniteCallable<Integer>> countCharacters = Arrays
                .stream("One two three four five six seven".split(" "))
                .<IgniteCallable<Integer>>map(word -> characterCountJob(word))
                .collect(Collectors.toList());

            // execute the jobs on the grid
            Collection<Integer> results = ig.computeCall(countCharacters);

            // add up all the results
            int sum = results.stream().mapToInt(Integer::intValue).sum();

            println("Total number of characters is '" + sum + "'.");
        }
    }

    // return a callable to run on some node
    private static IgniteCallable<Integer> characterCountJob(String word) {
        return () -> {
            println("Counting characters in word: " + word);
            return word.length();
        };
    }
}
