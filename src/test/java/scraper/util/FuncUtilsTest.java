package scraper.util;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FuncUtilsTest {

    @Test
    public void testMap() {
        assertEquals(Arrays.asList(12, 22, 33, 12), FuncUtils.map(Arrays.asList("12", "22", "33", "12"), Integer::parseInt));
        assertEquals(Arrays.asList("567", "222"), FuncUtils.map(Arrays.asList(567, 222), String::valueOf));
        assertTrue(FuncUtils.map(Collections.<String>emptyList(), Integer::parseInt).isEmpty());
    }

    @Test
    public void testMapFromArray() {
        assertEquals(Arrays.asList(12, 22, 33, 12), FuncUtils.map(new String[]{"12", "22", "33", "12"}, Integer::parseInt));
        assertEquals(Arrays.asList("567", "222"), FuncUtils.map(new Integer[]{567, 222}, String::valueOf));
        assertTrue(FuncUtils.map(new String[]{}, Integer::parseInt).isEmpty());
    }

    @Test
    public void testMapSet() {
        assertEquals(Utils.set(12, 22, 33), FuncUtils.mapSet(Arrays.asList("12", "22", "33", "12"), Integer::parseInt));
        assertEquals(Utils.set("567", "222"), FuncUtils.mapSet(Arrays.asList(567, 222), String::valueOf));
        assertTrue(FuncUtils.mapSet(Collections.<String>emptySet(), Integer::parseInt).isEmpty());
    }

    @Test
    public void testMapSetFromArray() {
        assertEquals(Utils.set(12, 22, 33), FuncUtils.mapSet(new String[]{"12", "22", "33", "12"}, Integer::parseInt));
        assertEquals(Utils.set("567", "222"), FuncUtils.mapSet(new Integer[]{567, 222}, String::valueOf));
        assertTrue(FuncUtils.mapSet(new String[]{}, Integer::parseInt).isEmpty());
    }

    @Test
    public void testFilter() {
        assertEquals(Arrays.asList(12, 22, 12), FuncUtils.filter(Arrays.asList(12, 7, 22, 5, 10, 12, 0), a -> a > 10));
        assertTrue(FuncUtils.filter(Arrays.asList(3, 6, 8, 2), a -> a > 10).isEmpty());
        assertTrue(FuncUtils.filter(Collections.<Integer>emptyList(), a -> a > 10).isEmpty());
    }

    @Test
    public void testFilterFromArray() {
        assertEquals(Arrays.asList(12, 22, 12), FuncUtils.filter(new Integer[]{12, 7, 22, 5, 10, 12, 0}, a -> a > 10));
        assertTrue(FuncUtils.filter(new Integer[]{3, 6, 8, 2}, a -> a > 10).isEmpty());
        assertTrue(FuncUtils.filter(new Integer[]{}, a -> a > 10).isEmpty());
    }

    @Test
    public void testFilterSet() {
        assertEquals(Utils.set(12, 22), FuncUtils.filterSet(Arrays.asList(12, 7, 22, 5, 10, 12, 0), a -> a > 10));
        assertTrue(FuncUtils.filterSet(Arrays.asList(3, 6, 8, 2), a -> a > 10).isEmpty());
        assertTrue(FuncUtils.filterSet(Collections.<Integer>emptyList(), a -> a > 10).isEmpty());
    }

    @Test
    public void testFilterSetFromArray() {
        assertEquals(Utils.set(12, 22), FuncUtils.filterSet(new Integer[]{12, 7, 22, 5, 10, 12, 0}, a -> a > 10));
        assertTrue(FuncUtils.filterSet(new Integer[]{3, 6, 8, 2}, a -> a > 10).isEmpty());
        assertTrue(FuncUtils.filterSet(new Integer[]{}, a -> a > 10).isEmpty());
    }

    @Test
    public void testMapFilter() {
        assertEquals(Arrays.asList(12, 22, 12), FuncUtils.mapFilter(Arrays.asList("12", "8", "22", "12", "1"), Integer::parseInt, a -> a > 10));
        assertTrue(FuncUtils.mapFilter(Arrays.asList("12", "8", "22", "12", "1"), Integer::parseInt, a -> a > 30).isEmpty());
        assertTrue(FuncUtils.mapFilter(Collections.<String>emptyList(), Integer::parseInt, a -> a > 30).isEmpty());
    }

    @Test
    public void testMapFilterFromAray() {
        assertEquals(Arrays.asList(12, 22, 12), FuncUtils.mapFilter(new String[]{"12", "8", "22", "12", "1"}, Integer::parseInt, a -> a > 10));
        assertTrue(FuncUtils.mapFilter(new String[]{"12", "8", "22", "12", "1"}, Integer::parseInt, a -> a > 30).isEmpty());
        assertTrue(FuncUtils.mapFilter(new String[]{}, Integer::parseInt, a -> a > 30).isEmpty());
    }

    @Test
    public void testMapFilterSet() {
        assertEquals(Utils.set(12, 22), FuncUtils.mapFilterSet(Arrays.asList("12", "8", "22", "12", "1"), Integer::parseInt, a -> a > 10));
        assertTrue(FuncUtils.mapFilterSet(Arrays.asList("12", "8", "22", "12", "1"), Integer::parseInt, a -> a > 30).isEmpty());
        assertTrue(FuncUtils.mapFilterSet(Collections.<String>emptyList(), Integer::parseInt, a -> a > 30).isEmpty());
    }

    @Test
    public void testMapFilterSetFromArray() {
        assertEquals(Utils.set(12, 22), FuncUtils.mapFilterSet(new String[]{"12", "8", "22", "12", "1"}, Integer::parseInt, a -> a > 10));
        assertTrue(FuncUtils.mapFilterSet(new String[]{"12", "8", "22", "12", "1"}, Integer::parseInt, a -> a > 30).isEmpty());
        assertTrue(FuncUtils.mapFilterSet(new String[]{}, Integer::parseInt, a -> a > 30).isEmpty());
    }

    @Test
    public void testToMap() {
        assertEquals(Utils.map(12, 2, 555, 3), FuncUtils.toMap(Arrays.asList("12", "555"), Integer::parseInt, String::length));
        assertEquals(Utils.map(3, "4", 563, "564", 21, "22"), FuncUtils.toMap(Arrays.asList(4, 564, 22), t -> t - 1, String::valueOf));
        assertTrue(FuncUtils.toMap(Collections.emptyList(), Integer::parseInt, String::length).isEmpty());
    }

    @Test
    public void testToMapFromAray() {
        assertEquals(Utils.map(12, 2, 555, 3), FuncUtils.toMap(new String[]{"12", "555"}, Integer::parseInt, String::length));
        assertEquals(Utils.map(3, "4", 563, "564", 21, "22"), FuncUtils.toMap(new Integer[]{4, 564, 22}, t -> t - 1, String::valueOf));
        assertTrue(FuncUtils.toMap(new String[]{}, Integer::parseInt, String::length).isEmpty());
    }
}