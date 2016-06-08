package scraper.module.core.properties.testclasses;

import scraper.module.core.properties.bool.BoolProperty;
import scraper.module.core.properties.string.StringProperty;

public class OnlyPropertiesTestClass {

    @StringProperty(viewName = "view", description = "description", minLength = 12, maxLength = 34, pattern = "v.*")
    public String field1;

    @StringProperty(viewName = "view2", description = "description2", minLength = 10, maxLength = 12, pattern = ".*b")
    private String field2;

    @BoolProperty(viewName = "view3", description = "description3")
    private boolean field3;

    public OnlyPropertiesTestClass(String field1, String field2, boolean field3) {
        this.field1 = field1;
        this.field2 = field2;
        this.field3 = field3;
    }
}
