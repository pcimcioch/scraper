package scraper.module.core.testclasses;

import scraper.module.core.properties.string.StringProperty;
import scraper.util.Utils;

public class TestWorkerSettings {

    @StringProperty(viewName = "Option", description = "")
    private String option;

    public TestWorkerSettings() {
    }

    public TestWorkerSettings(String option) {
        this.option = option;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TestWorkerSettings other = (TestWorkerSettings) o;

        return Utils.computeEq(option, other.option);
    }

    @Override
    public int hashCode() {
        return Utils.computeHash(option);
    }
}
