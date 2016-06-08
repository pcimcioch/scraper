package scraper.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class StringUtilsTest {

    @Test
    public void testIsBlank() {
        assertTrue(StringUtils.isBlank(null));
        assertTrue(StringUtils.isBlank(""));
        assertFalse(StringUtils.isBlank("test"));
        assertFalse(StringUtils.isBlank("Some more test"));
        assertFalse(StringUtils.isBlank("_"));
        assertFalse(StringUtils.isBlank("\n"));
    }

    @Test
    public void testIsNotBlank() {
        assertFalse(StringUtils.isNotBlank(null));
        assertFalse(StringUtils.isNotBlank(""));
        assertTrue(StringUtils.isNotBlank("test"));
        assertTrue(StringUtils.isNotBlank("Some more test"));
        assertTrue(StringUtils.isNotBlank("_"));
        assertTrue(StringUtils.isNotBlank("\n"));
    }

    @Test
    public void testIsAnyBlank() {
        assertFalse(StringUtils.isAnyBlank());
        assertFalse(StringUtils.isAnyBlank("a"));
        assertFalse(StringUtils.isAnyBlank("a", "test", "\n"));
        assertTrue(StringUtils.isAnyBlank(""));
        assertTrue(StringUtils.isAnyBlank((String) null));
        assertTrue(StringUtils.isAnyBlank("a", "", "\n"));
        assertTrue(StringUtils.isAnyBlank("a", "test", null));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSingleMatch_incorrectGroup() {
        assertEquals("123", StringUtils.getSingleMatch("foo=aaa bar=123", "foo=([a-zA-Z]+) bar=([0-9]+)", 3));
    }

    @Test
    public void testGetSingleMatch_noMatchingGroup() {
        assertNull(StringUtils.getSingleMatch("foobar", "foo=([a-zA-Z]+) bar=([0-9]+)", 1));
    }

    @Test
    public void testGetSingleMatch_defaultValueNoMatchingGroup() {
        assertEquals("default", StringUtils.getSingleMatch("foobar", "foo=([a-zA-Z]+) bar=([0-9]+)", 1, "default"));
    }

    @Test
    public void testGetSingleMatch() {
        assertEquals("foo=aaa bar=123", StringUtils.getSingleMatch("foo=aaa bar=123", "foo=([a-zA-Z]+) bar=([0-9]+)", 0));
        assertEquals("aaa", StringUtils.getSingleMatch("foo=aaa bar=123", "foo=([a-zA-Z]+) bar=([0-9]+)", 1));
        assertEquals("123", StringUtils.getSingleMatch("foo=aaa bar=123", "foo=([a-zA-Z]+) bar=([0-9]+)", 2));
    }

    @Test
    public void testGetSingleMatch_defaultValue() {
        assertEquals("foo=aaa bar=123", StringUtils.getSingleMatch("foo=aaa bar=123", "foo=([a-zA-Z]+) bar=([0-9]+)", 0, "default"));
        assertEquals("aaa", StringUtils.getSingleMatch("foo=aaa bar=123", "foo=([a-zA-Z]+) bar=([0-9]+)", 1, "default"));
        assertEquals("123", StringUtils.getSingleMatch("foo=aaa bar=123", "foo=([a-zA-Z]+) bar=([0-9]+)", 2, "default"));
    }
}