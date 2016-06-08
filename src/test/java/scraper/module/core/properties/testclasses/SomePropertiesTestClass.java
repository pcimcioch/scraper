package scraper.module.core.properties.testclasses;

import scraper.module.core.properties.string.StringProperty;

public class SomePropertiesTestClass {

    @SuppressWarnings("unused")
    private int field1;

    @StringProperty(viewName = "view", description = "description", minLength = 10, maxLength = 12, pattern = "")
    protected String field2;

    String field3;

    public SomePropertiesTestClass(int field1, String field2, String field3) {
        this.field1 = field1;
        this.field2 = field2;
        this.field3 = field3;
    }
}
