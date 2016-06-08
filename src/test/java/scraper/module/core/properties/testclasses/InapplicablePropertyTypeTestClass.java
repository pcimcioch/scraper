package scraper.module.core.properties.testclasses;

import scraper.module.core.properties.string.StringProperty;

public class InapplicablePropertyTypeTestClass {

    @StringProperty(description = "description", viewName = "name")
    public int field1;

    public InapplicablePropertyTypeTestClass(int field1) {
        this.field1 = field1;
    }
}
