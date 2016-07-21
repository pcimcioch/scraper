package scraper.module.core.properties.option;

import org.junit.Test;
import scraper.exception.IllegalAnnotationException;
import scraper.exception.ValidationException;
import scraper.util.Utils;

import java.lang.annotation.Annotation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class EnumPropertyParserTest {

    private EnumPropertyParser parser = new EnumPropertyParser();

    @Test
    public void testIsApplicable() {
        assertTrue(parser.isApplicable(TestType1.class));
        assertTrue(parser.isApplicable(TestType2.class));

        assertFalse(parser.isApplicable(Integer.class));
        assertFalse(parser.isApplicable(int.class));
        assertFalse(parser.isApplicable(char.class));
        assertFalse(parser.isApplicable(Character.class));
        assertFalse(parser.isApplicable(String.class));
    }

    @Test
    public void testGetAnnotationType() {
        assertEquals(EnumProperty.class, parser.getAnnotationType());
    }

    @Test
    public void testGetDescriptor_incorrectAnnotation() {
        try {
            parser.getDescriptor("name", TestType1.class, new Override() {
                @Override
                public Class<? extends Annotation> annotationType() {
                    return Override.class;
                }
            });
            fail();
        } catch (IllegalAnnotationException ex) {
            assertEquals("Annotation must be EnumProperty annotation", ex.getMessage());
        }
    }

    @Test
    public void testGetDescriptor_incorrectFieldType() {
        try {
            parser.getDescriptor("name", Integer.class, annotation("b", "c", true));
            fail();
        } catch (IllegalAnnotationException ex) {
            assertEquals("Annotation EnumProperty cannot be applied to field [name] with type [java.lang.Integer]", ex.getMessage());
        }
    }

    @Test
    public void testGetDescriptor() {
        assertEquals(new EnumPropertyDescriptor("a", Utils.set("VALUE1", "VALUE2"), "b", "c", true), parser.getDescriptor("a", TestType1.class, annotation("b", "c", true)));
        assertEquals(new EnumPropertyDescriptor("name", Utils.set("ENUM1", "ENUM2", "ENUM3"), "view", "desc", false),
                parser.getDescriptor("name", TestType2.class, annotation("view", "desc", false)));
    }

    @Test
    public void testValidate_incorrectAnnotation() {
        try {
            parser.validate("value", new Override() {
                @Override
                public Class<? extends Annotation> annotationType() {
                    return Override.class;
                }
            });
            fail();
        } catch (IllegalAnnotationException ex) {
            assertEquals("Annotation must be EnumProperty annotation", ex.getMessage());
        }
    }

    @Test
    public void testValidate_incorrectValueType() {
        try {
            parser.validate(12, annotation("a", "b", true));
            fail();
        } catch (IllegalAnnotationException ex) {
            assertEquals("Value must be an Enum", ex.getMessage());
        }
    }

    @Test
    public void testValidate_required_valid() {
        assertValid(TestType1.VALUE2, annotation("a", "b", true));
        assertValid(TestType2.ENUM1, annotation("a", "b", false));
        assertValid(null, annotation("a", "b", false));
    }

    @Test
    public void testValidate_required_invalid() {
        assertValid(null, annotation("a", "b", true));
    }

    private void assertValid(Object value, EnumProperty annotation) {
        parser.validate(value, annotation);
    }

    private void assertInvalid(Object value, EnumProperty annotation, String exceptionMsg) {
        try {
            parser.validate(value, annotation);
            fail();
        } catch (ValidationException ex) {
            assertEquals(exceptionMsg, ex.getMessage());
        }
    }

    private EnumProperty annotation(String viewName, String description, boolean required) {
        return new EnumProperty() {

            @Override
            public Class<? extends Annotation> annotationType() {
                return EnumProperty.class;
            }

            @Override
            public String viewName() {
                return viewName;
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

    private enum TestType1 {
        VALUE1,
        VALUE2
    }

    private enum TestType2 {
        ENUM1,
        ENUM2,
        ENUM3
    }
}
