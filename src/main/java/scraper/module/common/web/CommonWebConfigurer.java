package scraper.module.common.web;

/**
 * By implementing this interface, module can register its webpage.
 */
public interface CommonWebConfigurer {

    /**
     * Url for the main webpage.
     *
     * @return url
     */
    String url();

    /**
     * Module description.
     *
     * @return module description
     */
    String moduleDescription();
}
