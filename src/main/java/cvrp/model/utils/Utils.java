package cvrp.model.utils;

import java.text.Normalizer;
import java.util.stream.Stream;

public class Utils {
    public static String removeAccents(String str) {
        return Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
    }

    public static String[] concatenate(String[] a, String[] b) {
        return Stream.of(a, b).flatMap(Stream::of).toArray(String[]::new);
    }
}
