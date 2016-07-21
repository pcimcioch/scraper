package scraper.module.common.web;

import scraper.util.Utils;

/**
 * Json DTO representing available webpage.
 */
public class SubPageJsonDto {

    private final String url;

    private final String description;

    public SubPageJsonDto(String url, String description) {
        this.url = url;
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        SubPageJsonDto other = (SubPageJsonDto) obj;

        return Utils.computeEq(url, other.url, description, other.description);

    }

    @Override
    public int hashCode() {
        return Utils.computeHash(url, description);
    }
}
