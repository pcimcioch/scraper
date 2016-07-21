package scraper.module.core.testclasses;

import scraper.module.core.properties.string.StringProperty;
import scraper.util.Utils;

public class TestWorkerSettings {

    @StringProperty(viewName = "Option", description = "", maxLength = 10)
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
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        TestWorkerSettings other = (TestWorkerSettings) obj;

        return Utils.computeEq(option, other.option);
    }

    @Override
    public int hashCode() {
        return Utils.computeHash(option);
    }
}
