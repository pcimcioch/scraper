package scraper.module.core.properties.string;

import org.junit.Test;
import scraper.exception.ValidationException;

import java.lang.annotation.Annotation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class StringPropertyParserTest {

    private StringPropertyParser parser = new StringPropertyParser();

    @Test
    public void testIsApplicable() {
        assertTrue(parser.isApplicable(String.class));

        assertFalse(parser.isApplicable(Integer.class));
        assertFalse(parser.isApplicable(int.class));
        assertFalse(parser.isApplicable(char.class));
        assertFalse(parser.isApplicable(Character.class));
    }

    @Test
    public void testGetAnnotationType() {
        assertEquals(StringProperty.class, parser.getAnnotationType());
    }

    @Test
    public void testGetDescriptor_incorrectAnnotation() {
        try {
            parser.getDescriptor("name", String.class, new Override() {
                @Override
                public Class<? extends Annotation> annotationType() {
                    return Override.class;
                }
            });
            fail();
        } catch (IllegalArgumentException ex) {
            assertEquals("Annotation must be StringProperty annotation", ex.getMessage());
        }
    }

    @Test
    public void testGetDescriptor_incorrectFieldType() {
        try {
            parser.getDescriptor("a", int.class, annotation("b", "c", 23, 34, "d", false));
            fail();
        } catch (IllegalArgumentException ex) {
            assertEquals("Annotation StringProperty cannot be applied to field a with type int", ex.getMessage());
        }
    }

    @Test
    public void testGetDescriptor() {
        assertEquals(new StringPropertyDescriptor("a", "b", "c", 23, 34, "d", false), parser.getDescriptor("a", String.class, annotation("b", "c", 23, 34, "d", false)));
        assertEquals(new StringPropertyDescriptor("name", "view", "desc", 0, 12, "pattern", false),
                parser.getDescriptor("name", String.class, annotation("view", "desc", 0, 12, "pattern", false)));
    }

    @Test
    public void testValidate_incorrenctAnnotation() {
        try {
            parser.validate("value", new Override() {
                @Override
                public Class<? extends Annotation> annotationType() {
                    return Override.class;
                }
            });
            fail();
        } catch (IllegalArgumentException ex) {
            assertEquals("Annotation must be StringProperty annotation", ex.getMessage());
        }
    }

    @Test
    public void testValidate_incorrenctValueType() {
        try {
            parser.validate(12, annotation("a", "b", 1, 2, "", false));
            fail();
        } catch (IllegalArgumentException ex) {
            assertEquals("Value must be a String", ex.getMessage());
        }
    }

    @Test
    public void testValidate_minLength_valid() {
        assertValid("value", annotation("", "", 0, 100, "", false));
        assertValid("value", annotation("", "", 4, 100, "", false));
        assertValid("value", annotation("", "", 5, 100, "", false));
        assertValid("", annotation("", "", 0, 100, "", false));
        assertValid(null, annotation("", "", 0, 100, "", false));
    }

    @Test
    public void testValidate_minLength_invalid() {
        assertInvalid("value", annotation("", "", 6, 100, "", false), "Value \"value\" must be at least 6 characters long");
        assertInvalid("", annotation("", "", 1, 100, "", true), "Value must be present");
        assertInvalid("1", annotation("", "", 2, 100, "", true), "Value \"1\" must be at least 2 characters long");
    }

    @Test
    public void testValidate_maxLength_valid() {
        assertValid("value", annotation("", "", 0, 100, "", false));
        assertValid("value", annotation("", "", 0, 5, "", false));
        assertValid("value", annotation("", "", 0, 6, "", false));
        assertValid("", annotation("", "", 0, 1, "", false));
        assertValid(null, annotation("", "", 0, 1, "", false));
        assertValid("", annotation("", "", 0, 0, "", false));
        assertValid(null, annotation("", "", 0, 0, "", false));
    }

    @Test
    public void testValidate_maxLength_invalid() {
        assertInvalid("value", annotation("", "", 0, 4, "", false), "Value \"value\" must be maximum 4 characters long");
        assertInvalid("value", annotation("", "", 0, 1, "", false), "Value \"value\" must be maximum 1 characters long");
    }

    @Test
    public void testValidate_pattern_emptyPattern() {
        assertValid("value", annotation("", "", 0, 100, "", false));
        assertValid("value", annotation("", "", 0, 100, null, false));
        assertValid("", annotation("", "", 0, 100, "", false));
        assertValid("", annotation("", "", 0, 100, null, false));
        assertValid(null, annotation("", "", 0, 100, "", false));
        assertValid(null, annotation("", "", 0, 100, null, false));
    }

    @Test
    public void testValidate_pattern_valid() {
        assertValid("value", annotation("", "", 0, 100, "v.+e", false));
        assertValid(null, annotation("", "", 0, 100, "v.+e", false));
        assertValid("", annotation("", "", 0, 100, ".*", false));
    }

    @Test
    public void testValidate_pattern_invalid() {
        assertInvalid(null, annotation("", "", 0, 100, ".*", true), "Value must be present");
        assertInvalid("", annotation("", "", 0, 100, ".+", true), "Value must be present");
        assertInvalid("value", annotation("", "", 0, 100, "v.+u", false), "Value \"value\" does not match pattern \"v.+u\"");
    }

    @Test
    public void testValidate_pattern_invalidPattern() {
        assertInvalid("value", annotation("", "", 0, 100, "v.+[", false), "Incorrect pattern \"v.+[\". Unclosed character class near index 3\r\nv.+[\r\n   ^");
    }

    private void assertValid(Object value, StringProperty annotation) {
        parser.validate(value, annotation);
    }

    private void assertInvalid(Object value, StringProperty annotation, String exceptionMsg) {
        try {
            parser.validate(value, annotation);
            fail();
        } catch (ValidationException ex) {
            assertEquals(exceptionMsg, ex.getMessage());
        }
    }

    private StringProperty annotation(String viewName, String description, int minLength, int maxLength, String pattern, boolean required) {
        return new StringProperty() {

            @Override
            public Class<? extends Annotation> annotationType() {
                return StringProperty.class;
            }

            @Override
            public String viewName() {
                return viewName;
            }

            @Override
            public String pattern() {
                return pattern;
            }

            @Override
            public int minLength() {
                return minLength;
            }

            @Override
            public int maxLength() {
                return maxLength;
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
