package scraper.util;

import org.junit.Test;
import scraper.util.testclasses.ComplexTestClass;
import scraper.util.testclasses.SimpleTestClass;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ReflectionUtilsTest {

    @Test
    public void testGetAllFields_simpleClass() throws NoSuchFieldException {
        // when
        List<Field> fields = ReflectionUtils.getAllFields(SimpleTestClass.class);

        // then
        Field field1 = SimpleTestClass.class.getDeclaredField("field1");
        Field field2 = SimpleTestClass.class.getDeclaredField("field2");
        Field field3 = SimpleTestClass.class.getDeclaredField("field3");
        Field field4 = SimpleTestClass.class.getDeclaredField("field4");
        assertEquals(Arrays.asList(field1, field2, field3, field4), fields);
    }

    @Test
    public void testGetAllFields_complexClass() throws NoSuchFieldException {
        // when
        List<Field> fields = ReflectionUtils.getAllFields(ComplexTestClass.class);

        // then
        Field field10 = ComplexTestClass.class.getDeclaredField("field10");
        Field field20 = ComplexTestClass.class.getDeclaredField("field20");
        Field field30 = ComplexTestClass.class.getDeclaredField("field30");
        Field field40 = ComplexTestClass.class.getDeclaredField("field40");
        Field field1 = SimpleTestClass.class.getDeclaredField("field1");
        Field field2 = SimpleTestClass.class.getDeclaredField("field2");
        Field field3 = SimpleTestClass.class.getDeclaredField("field3");
        Field field4 = SimpleTestClass.class.getDeclaredField("field4");
        assertEquals(Arrays.asList(field10, field20, field30, field40, field1, field2, field3, field4), fields);
    }
}
