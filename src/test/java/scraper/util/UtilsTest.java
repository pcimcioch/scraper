package scraper.util;

import org.junit.Test;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class UtilsTest {

    @Test
    public void testSet() {
        // given
        Set<String> expectedSet = new HashSet<>();
        expectedSet.add("1");
        expectedSet.add("3");
        expectedSet.add("2");

        // when then
        assertEquals(expectedSet, Utils.set("1", "2", "3"));
    }

    @Test
    public void testSet_emptyCollection() {
        // when
        Set<Integer> set = Utils.set();

        // then
        assertNotNull(set);
        assertTrue(set.isEmpty());
    }

    @Test
    public void testGenerateUUID() {
        Set<String> uuids = new HashSet<>();
        for (int i = 0; i < 1000; ++i) {
            String uuid = Utils.generateUUID();
            assertEquals(36, uuid.length());
            assertTrue(uuids.add(uuid));
        }
    }

    @Test
    public void testEq_simpleTypes() {
        assertTrue(Utils.eq(null, null));
        assertTrue(Utils.eq("Test", "Test"));
        assertFalse(Utils.eq("Test", null));
        assertFalse(Utils.eq(null, "Test"));
    }

    @Test
    public void testEq_complexTypes() {
        // given
        AlwaysEqualsTestClass alwaysEq = new AlwaysEqualsTestClass();
        NeverEqualsTestClass neverEq = new NeverEqualsTestClass();

        // when then
        assertTrue(Utils.eq(alwaysEq, alwaysEq));
        assertTrue(Utils.eq(alwaysEq, new AlwaysEqualsTestClass()));
        assertFalse(Utils.eq(neverEq, alwaysEq));
        assertFalse(Utils.eq(neverEq, new NeverEqualsTestClass()));
    }

    @Test
    public void testHash() {
        assertEquals(0, Utils.hash(null));
        assertEquals("test".hashCode(), Utils.hash("test"));
        assertEquals("test2".hashCode(), Utils.hash("test2"));
        assertEquals(Integer.hashCode(12), Utils.hash(12));
    }

    @Test(expected = IllegalArgumentException.class)
    public void computeEq_oddNumberOfParameters() {
        Utils.computeEq(1, 2, "test");
    }

    @Test
    public void computeEq() {
        assertTrue(Utils.computeEq("test", "test"));
        assertTrue(Utils.computeEq(2, 2, "test", "test", 34L, 34L, new AlwaysEqualsTestClass(), new AlwaysEqualsTestClass()));
        assertTrue(Utils.computeEq(5, 5, "test", "test", null, null));

        assertFalse(Utils.computeEq(2, 1));
        assertFalse(Utils.computeEq(2, null));
        assertFalse(Utils.computeEq(2, 2, "test", "test", 34L, 35L));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMap_oddNumberOfParameters() {
        Utils.map(1, "asd", 2);
    }

    @Test
    public void testMap_emptyMap() {
        // when
        Map<Integer, String> map = Utils.map();

        // then
        assertNotNull(map);
        assertTrue(map.isEmpty());
    }

    @Test
    public void testMap() {
        // when
        Map<Integer, String> map = Utils.map(1, "value 1", 5, "value 5", 7, "value 7");

        // then
        assertNotNull(map);
        assertEquals(3, map.size());
        assertEquals("value 1", map.get(1));
        assertEquals("value 5", map.get(5));
        assertEquals("value 7", map.get(7));
    }

    @Test
    public void testMap_incorrectTypes() {
        // when
        Map<Integer, String> map = Utils.map(1, "value 1", 5, 7);

        assertNotNull(map);
        assertEquals(2, map.size());
        assertEquals("value 1", map.get(1));
        assertEquals(7, map.get(5));
    }

    private static class AlwaysEqualsTestClass {

        @Override
        public boolean equals(Object obj) {
            return true;
        }

        @Override
        public int hashCode() {
            return 5;
        }
    }

    private static class NeverEqualsTestClass {

        @Override
        public boolean equals(Object obj) {
            return false;
        }

        @Override
        public int hashCode() {
            return 5;
        }
    }
}
