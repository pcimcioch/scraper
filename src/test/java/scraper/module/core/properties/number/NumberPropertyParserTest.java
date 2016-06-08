package scraper.module.core.properties.number;

import org.junit.Test;
import scraper.exception.ValidationException;
import scraper.module.core.properties.string.StringProperty;

import java.lang.annotation.Annotation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class NumberPropertyParserTest {

    private NumberPropertyParser parser = new NumberPropertyParser();

    @Test
    public void testIsApplicable() {
        assertTrue(parser.isApplicable(int.class));
        assertTrue(parser.isApplicable(Integer.class));
        assertTrue(parser.isApplicable(long.class));
        assertTrue(parser.isApplicable(Long.class));
        assertTrue(parser.isApplicable(short.class));
        assertTrue(parser.isApplicable(Short.class));
        assertTrue(parser.isApplicable(byte.class));
        assertTrue(parser.isApplicable(Byte.class));

        assertFalse(parser.isApplicable(char.class));
        assertFalse(parser.isApplicable(String.class));
        assertFalse(parser.isApplicable(boolean.class));
        assertFalse(parser.isApplicable(Double.class));
    }

    @Test
    public void testGetAnnotationType() {
        assertEquals(NumberProperty.class, parser.getAnnotationType());
    }

    @Test
    public void testGetDescriptor_incorrectAnnotation() {
        try {
            parser.getDescriptor("name", int.class, new Override() {
                @Override
                public Class<? extends Annotation> annotationType() {
                    return Override.class;
                }
            });
            fail();
        } catch (IllegalArgumentException ex) {
            assertEquals("Annotation must be NumberProperty annotation", ex.getMessage());
        }
    }

    @Test
    public void testGetDescriptor_incorrectFieldType() {
        try {
            parser.getDescriptor("a", String.class, annotation("b", "c", 23, 34, false));
            fail();
        } catch (IllegalArgumentException ex) {
            assertEquals("Annotation NumberProperty cannot be applied to field a with type java.lang.String", ex.getMessage());
        }
    }

    @Test
    public void testGetDescriptor() {
        assertEquals(new NumberPropertyDescriptor("a", short.class, "b", "c", 23, 34, false), parser.getDescriptor("a", short.class, annotation("b", "c", 23, 34, false)));
        assertEquals(new NumberPropertyDescriptor("name", Byte.class, "view", "desc", 0, 12, false),
                parser.getDescriptor("name", Byte.class, annotation("view", "desc", 0, 12, false)));
    }

    @Test
    public void testMinMaxLongThresholds() {
        assertEquals(new NumberPropertyDescriptor("a", Long.class, "b", "c", Long.MIN_VALUE, Long.MAX_VALUE, false),
                parser.getDescriptor("a", Long.class, annotation("b", "c", Long.MIN_VALUE, Long.MAX_VALUE, false)));
        assertEquals(new NumberPropertyDescriptor("a", long.class, "b", "c", Long.MIN_VALUE, Long.MAX_VALUE, false),
                parser.getDescriptor("a", long.class, annotation("b", "c", Long.MIN_VALUE, Long.MAX_VALUE, false)));
        assertEquals(new NumberPropertyDescriptor("a", Long.class, "b", "c", -20, 30, false), parser.getDescriptor("a", Long.class, annotation("b", "c", -20, 30, false)));
        assertEquals(new NumberPropertyDescriptor("a", long.class, "b", "c", -20, 30, false), parser.getDescriptor("a", long.class, annotation("b", "c", -20, 30, false)));
    }

    @Test
    public void testMinMaxIntegerThresholds() {
        assertEquals(new NumberPropertyDescriptor("a", Integer.class, "b", "c", Integer.MIN_VALUE, Integer.MAX_VALUE, false),
                parser.getDescriptor("a", Integer.class, annotation("b", "c", Long.MIN_VALUE, Long.MAX_VALUE, false)));
        assertEquals(new NumberPropertyDescriptor("a", int.class, "b", "c", Integer.MIN_VALUE, Integer.MAX_VALUE, false),
                parser.getDescriptor("a", int.class, annotation("b", "c", Long.MIN_VALUE, Long.MAX_VALUE, false)));
        assertEquals(new NumberPropertyDescriptor("a", Integer.class, "b", "c", -20, 30, false), parser.getDescriptor("a", Integer.class, annotation("b", "c", -20, 30, false)));
        assertEquals(new NumberPropertyDescriptor("a", int.class, "b", "c", -20, 30, false), parser.getDescriptor("a", int.class, annotation("b", "c", -20, 30, false)));
    }

    @Test
    public void testMinMaxShortThresholds() {
        assertEquals(new NumberPropertyDescriptor("a", Short.class, "b", "c", Short.MIN_VALUE, Short.MAX_VALUE, false),
                parser.getDescriptor("a", Short.class, annotation("b", "c", Long.MIN_VALUE, Long.MAX_VALUE, false)));
        assertEquals(new NumberPropertyDescriptor("a", short.class, "b", "c", Short.MIN_VALUE, Short.MAX_VALUE, false),
                parser.getDescriptor("a", short.class, annotation("b", "c", Long.MIN_VALUE, Long.MAX_VALUE, false)));
        assertEquals(new NumberPropertyDescriptor("a", Short.class, "b", "c", -20, 30, false), parser.getDescriptor("a", Short.class, annotation("b", "c", -20, 30, false)));
        assertEquals(new NumberPropertyDescriptor("a", short.class, "b", "c", -20, 30, false), parser.getDescriptor("a", short.class, annotation("b", "c", -20, 30, false)));
    }

    @Test
    public void testMinMaxByteThresholds() {
        assertEquals(new NumberPropertyDescriptor("a", Byte.class, "b", "c", Byte.MIN_VALUE, Byte.MAX_VALUE, false),
                parser.getDescriptor("a", Byte.class, annotation("b", "c", Long.MIN_VALUE, Long.MAX_VALUE, false)));
        assertEquals(new NumberPropertyDescriptor("a", byte.class, "b", "c", Byte.MIN_VALUE, Byte.MAX_VALUE, false),
                parser.getDescriptor("a", byte.class, annotation("b", "c", Long.MIN_VALUE, Long.MAX_VALUE, false)));
        assertEquals(new NumberPropertyDescriptor("a", Byte.class, "b", "c", -20, 30, false), parser.getDescriptor("a", Byte.class, annotation("b", "c", -20, 30, false)));
        assertEquals(new NumberPropertyDescriptor("a", byte.class, "b", "c", -20, 30, false), parser.getDescriptor("a", byte.class, annotation("b", "c", -20, 30, false)));
    }

    @Test
    public void testValidate_incorrenctAnnotation() {
        try {
            parser.validate(12, new Override() {
                @Override
                public Class<? extends Annotation> annotationType() {
                    return Override.class;
                }
            });
            fail();
        } catch (IllegalArgumentException ex) {
            assertEquals("Annotation must be NumberProperty annotation", ex.getMessage());
        }
    }

    @Test
    public void testValidate_incorrenctValueType() {
        try {
            parser.validate("as", annotation("a", "b", 1, 2, false));
            fail();
        } catch (IllegalArgumentException ex) {
            assertEquals("Value must be a number", ex.getMessage());
        }
    }

    @Test
    public void testValidate_min_valid() {
        assertValid(55L, annotation("", "", 0, 100, false));
        assertValid(5L, annotation("", "", 4, 100, false));
        assertValid(5L, annotation("", "", 5, 100, false));
        assertValid(0L, annotation("", "", 0, 100, false));
        assertValid(null, annotation("", "", 0, 100, false));
    }

    @Test
    public void testValidate_min_invalid() {
        assertInvalid(5L, annotation("", "", 6, 100, false), "Value \"5\" must be greater or equal 6");
        assertInvalid(null, annotation("", "", 1, 100, true), "Value must be present");
        assertInvalid(1L, annotation("", "", 2, 100, true), "Value \"1\" must be greater or equal 2");
    }

    @Test
    public void testValidate_max_valid() {
        assertValid(5L, annotation("", "", 0, 100, false));
        assertValid(5L, annotation("", "", 0, 5, false));
        assertValid(5L, annotation("", "", 0, 6, false));
        assertValid(0L, annotation("", "", 0, 1, false));
        assertValid(null, annotation("", "", 0, 1, false));
        assertValid(0L, annotation("", "", 0, 0, false));
        assertValid(null, annotation("", "", 0, 0, false));
    }

    @Test
    public void testValidate_max_invalid() {
        assertInvalid(5L, annotation("", "", 0, 4, false), "Value \"5\" must be lower or equal 4");
        assertInvalid(5L, annotation("", "", 0, 1, false), "Value \"5\" must be lower or equal 1");
    }

    private void assertValid(Object value, NumberProperty annotation) {
        parser.validate(value, annotation);
    }

    private void assertInvalid(Object value, NumberProperty annotation, String exceptionMsg) {
        try {
            parser.validate(value, annotation);
            fail();
        } catch (ValidationException ex) {
            assertEquals(exceptionMsg, ex.getMessage());
        }
    }

    private NumberProperty annotation(String viewName, String description, long min, long max, boolean required) {
        return new NumberProperty() {

            @Override
            public Class<? extends Annotation> annotationType() {
                return StringProperty.class;
            }

            @Override
            public String viewName() {
                return viewName;
            }

            @Override
            public long min() {
                return min;
            }

            @Override
            public long max() {
                return max;
            }

            @Override
            public String description() {
                return description;
            }

            @Override
            public boolean required() {
                return required;
            }
        };
    }
}
