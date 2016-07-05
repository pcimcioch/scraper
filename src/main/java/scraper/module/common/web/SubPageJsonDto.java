package scraper.module.common.web;

import scraper.util.Utils;

/**
 * Json DTO representing available webpage.
 */
public class SubPageJsonDto {

    private final String name;

    private final String description;

    public SubPageJsonDto(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SubPageJsonDto other = (SubPageJsonDto) o;

        return Utils.computeEq(name, other.name, description, other.description);

    }

    @Override
    public int hashCode() {
        return Utils.computeHash(name, description);
    }
}
