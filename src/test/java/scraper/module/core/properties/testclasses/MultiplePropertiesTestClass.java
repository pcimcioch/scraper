package scraper.module.core.properties.testclasses;

import scraper.module.core.properties.bool.BoolProperty;
import scraper.module.core.properties.string.StringProperty;

public class MultiplePropertiesTestClass {

    @StringProperty(description = "description", viewName = "view")
    @BoolProperty(description = "description 2", viewName = "view 2")
    public String field1;

    public MultiplePropertiesTestClass(String field1) {
        this.field1 = field1;
    }
}
