package scraper.module.core.properties.bool;

import org.junit.Test;

import java.lang.annotation.Annotation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class BoolPropertyParserTest {

    private BoolPropertyParser parser = new BoolPropertyParser();

    @Test
    public void testIsApplicable() {
        assertTrue(parser.isApplicable(boolean.class));
        assertTrue(parser.isApplicable(Boolean.class));

        assertFalse(parser.isApplicable(Integer.class));
        assertFalse(parser.isApplicable(int.class));
        assertFalse(parser.isApplicable(char.class));
        assertFalse(parser.isApplicable(Character.class));
        assertFalse(parser.isApplicable(String.class));
    }

    @Test
    public void testGetAnnotationType() {
        assertEquals(BoolProperty.class, parser.getAnnotationType());
    }

    @Test
    public void testGetDescriptor_incorrectAnnotation() {
        try {
            parser.getDescriptor("name", boolean.class, new Override() {
                @Override
                public Class<? extends Annotation> annotationType() {
                    return Override.class;
                }
            });
            fail();
        } catch (IllegalArgumentException ex) {
            assertEquals("Annotation must be BoolProperty annotation", ex.getMessage());
        }
    }

    @Test
    public void testGetDescriptor_incorrectFieldType() {
        try {
            parser.getDescriptor("name", Integer.class, annotation("b", "c"));
            fail();
        } catch (IllegalArgumentException ex) {
            assertEquals("Annotation BoolProperty cannot be applied to field name with type java.lang.Integer", ex.getMessage());
        }
    }

    @Test
    public void testGetDescriptor() {
        assertEquals(new BoolPropertyDescriptor("a", "b", "c"), parser.getDescriptor("a", Boolean.class, annotation("b", "c")));
        assertEquals(new BoolPropertyDescriptor("name", "view", "desc"), parser.getDescriptor("name", boolean.class, annotation("view", "desc")));
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
            assertEquals("Annotation must be BoolProperty annotation", ex.getMessage());
        }
    }

    @Test
    public void testValidate_incorrenctValueType() {
        try {
            parser.validate(12, annotation("a", "b"));
            fail();
        } catch (IllegalArgumentException ex) {
            assertEquals("Value must be a Boolean", ex.getMessage());
        }
    }

    private BoolProperty annotation(String viewName, String description) {
        return new BoolProperty() {

            @Override
            public Class<? extends Annotation> annotationType() {
                return BoolProperty.class;
            }

            @Override
            public String viewName() {
                return viewName;
            }

            @Override
            public String description() {
                return description;
            }
        };
    }
}
