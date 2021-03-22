package countries;

import java.io.IOException;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.*;

import java.time.ZoneId;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Homework2 {

    private List<Country> countries;

    public Homework2() {
        countries = new CountryRepository().getAll();
    }

    public static int charCount(String s, char c) {
        int count = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == c) {
                count++;
            }
        }
        return count;
    }

    public static int vowelCount(String s) {
        int count = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == 'a' || s.charAt(i) == 'e' || s.charAt(i) == 'i' || s.charAt(i) == 'o' || s.charAt(i) == 'u') {
                count++;
            }
        }
        return count;
    }

    /**
     * Returns the longest country name translation.
     */
    public Optional<String> streamPipeline1() {
        return countries.stream().flatMap(country -> country.getTranslations().values().stream()).max(Comparator.comparing(translation -> translation.length()));
    }

    /**
     * Returns the longest Italian (i.e., {@code "it"}) country name translation.
     */
    public Optional<String> streamPipeline2() {
        return countries.stream().filter(country -> country.getTranslations().containsKey("it")).map(country -> country.getTranslations().get("it")).max(Comparator.comparing(translation -> translation.length()));
    }

    /**
     * Prints the longest country name translation together with its language code in the form language=translation.
     */
    public void streamPipeline3() {
        countries.stream().flatMap(country -> country.getTranslations().entrySet().stream()).sorted(Comparator.comparing(translation -> translation.getValue().length(), Comparator.reverseOrder())).limit(1).forEach(translation -> System.out.println(translation.getKey() + "=" + translation.getValue()));
    }

    /**
     * Prints single word country names (i.e., country names that do not contain any space characters).
     */
    public void streamPipeline4() {
        countries.stream().map(country -> country.getName()).filter(name -> !name.contains(" ")).forEach(System.out::println);
    }

    /**
     * Returns the country name with the most number of words.
     */
    public Optional<String> streamPipeline5() {
        return countries.stream().map(country -> country.getName()).max(Comparator.comparing(name -> name.split(" ").length));
    }

    /**
     * Returns whether there exists at least one capital that is a palindrome.
     */
    public boolean streamPipeline6() {
        return countries.stream().anyMatch(country -> new StringBuilder(country.getCapital()).equals(new StringBuilder(country.getCapital()).reverse()));
    }

    /**
     * Returns the country name with the most number of {@code 'e'} characters ignoring case.
     */
    public Optional<String> streamPipeline7() {
        return countries.stream().map(country -> country.getName()).max(Comparator.comparing(name -> charCount(name.toLowerCase(), 'e')));
    }

    /**
     *  Returns the capital with the most number of English vowels (i.e., {@code 'a'}, {@code 'e'}, {@code 'i'}, {@code 'o'}, {@code 'u'}).
     */
    public Optional<String> streamPipeline8() {
        return countries.stream().map(country -> country.getName()).max(Comparator.comparing(name -> vowelCount(name.toLowerCase())));
    }

    /**
     * Returns a map that contains for each character the number of occurrences in country names ignoring case.
     */
    public Map<Character, Long> streamPipeline9() {
        return countries.stream().flatMap(country -> country.getName().chars().mapToObj(value -> (char) value)).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

    /**
     * Returns a map that contains the number of countries for each possible timezone.
     */
    public Map<ZoneId, Long> streamPipeline10() {
        return countries.stream().flatMap(country -> country.getTimezones().stream()).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

    /**
     * Returns the number of country names by region that starts with their two-letter country code ignoring case.
     */
    public Map<Region, Long> streamPipeline11() {
        return countries.stream().filter(country -> country.getName().toLowerCase().startsWith(country.getCode().toLowerCase())).collect(Collectors.groupingBy(Country::getRegion, Collectors.counting()));
    }

    /**
     * Returns a map that contains the number of countries whose population is greater or equal than the population average versus the the number of number of countries with population below the average.
     */
    public Map<Boolean, Long> streamPipeline12() {
        return countries.stream().filter(country -> country.getArea() != null).collect(Collectors.partitioningBy(country -> (double) country.getPopulation() / country.getArea().doubleValue() >= countries.stream().filter(value -> value.getArea() != null).mapToDouble(value -> (double) value.getPopulation() / value.getArea().doubleValue()).average().getAsDouble(), Collectors.counting()));
    }

    /**
     * Returns a map that contains for each country code the name of the corresponding country in Portuguese ({@code "pt"}).
     */
    public Map<String, String> streamPipeline13() {
        return countries.stream().collect(Collectors.toMap(Country::getCode, country -> country.getTranslations().get("pt")));
    }

    /**
     * Returns the list of capitals by region whose name is the same is the same as the name of their country.
     */
    public Map<Region, List<String>> streamPipeline14() {
        return countries.stream().collect(Collectors.groupingBy(Country::getRegion, Collectors.filtering(country -> country.getName().equals(country.getCapital()), Collectors.mapping(Country::getCapital, Collectors.toList()))));
    }

    /**
     *  Returns a map of country name-population density pairs.
     */
    public Map<String, Double> streamPipeline15() {
        return countries.stream().collect(Collectors.toMap(Country::getName, country -> { if (country.getArea() != null) { return (double) country.getPopulation() / country.getArea().doubleValue(); } else { return Double.NaN; } }));
    }

}
