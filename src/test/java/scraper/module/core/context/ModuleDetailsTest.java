package scraper.module.core.context;

import org.junit.Test;
import scraper.exception.ValidationException;

import static org.junit.Assert.fail;
import static scraper.module.core.context.ModuleDetails.validateInstance;
import static scraper.module.core.context.ModuleDetails.validateModule;

public class ModuleDetailsTest {

    @Test
    public void testValidateModule_passed() {
        // when
        validateModule("module");
        validateModule("module.sub");
        validateModule("module123");
        validateModule("123");
        validateModule("a");
        validateModule("AZS");
        validateModule("Az4.5f");
    }

    @Test
    public void testValidateModule_failed() {
        // when
        assertModuleValidationFailed(null);
        assertModuleValidationFailed("");
        assertModuleValidationFailed("a_s");
        assertModuleValidationFailed("%");
        assertModuleValidationFailed("*asdf");
        assertModuleValidationFailed("kol=f");
    }

    @Test
    public void testValidateInstance_passed() {
        // when
        validateInstance(null);
        validateInstance("module");
        validateInstance("module.sub");
        validateInstance("module123");
        validateInstance("123");
        validateInstance("a");
        validateInstance("AZS");
        validateInstance("Az4.5f");
    }

    @Test
    public void testValidateInstance_failed() {
        // when
        assertInstanceValidationFailed("");
        assertInstanceValidationFailed("a_s");
        assertInstanceValidationFailed("%");
        assertInstanceValidationFailed("*asdf");
        assertInstanceValidationFailed("kol=f");
    }

    @Test
    public void testValidationInModuleDetailsConstructor_passed() {
        new ModuleDetails("module", "instance");
        new ModuleDetails("1", "a");
        new ModuleDetails("o.m", "as12");
        new ModuleDetails("ASZ", "Aa1.1oS");
    }

    @Test
    public void testValidationInModuleDetailsConstructor_failed() {
        assertConstructorValidationFailed("module", "");
        assertConstructorValidationFailed("module", "$");
        assertConstructorValidationFailed("module", "asd*");
        assertConstructorValidationFailed("", "instance");
        assertConstructorValidationFailed("<>", "instance");
        assertConstructorValidationFailed("AAA.y()", "instance");
        assertConstructorValidationFailed("", "");
        assertConstructorValidationFailed("=", "+");
        assertConstructorValidationFailed("Nothing_", "here_");
    }

    private void assertModuleValidationFailed(String module) {
        try {
            validateModule(module);
            fail();
        } catch (ValidationException ex) {
            // expected
        }
    }

    private void assertInstanceValidationFailed(String instance) {
        try {
            validateInstance(instance);
            fail();
        } catch (ValidationException ex) {
            // expected
        }
    }

    private void assertConstructorValidationFailed(String module, String instance) {
        try {
            new ModuleDetails(module, instance);
            fail();
        } catch (ValidationException ex) {
            // expected
        }
    }
}