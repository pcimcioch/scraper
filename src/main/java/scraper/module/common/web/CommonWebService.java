package scraper.module.common.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Service for common web operations.
 */
@Service
public class CommonWebService {

    private final List<SubPageJsonDto> subpages = new ArrayList<>();

    @Autowired
    public CommonWebService(List<CommonWebConfigurer> configurers) {
        for (CommonWebConfigurer configurer : configurers) {
            subpages.add(toModel(configurer));
        }
    }

    private SubPageJsonDto toModel(CommonWebConfigurer configurer) {
        return new SubPageJsonDto(configurer.getModuleName(), configurer.getModuleDescription());
    }

    /**
     * Gets descriptions of all webpages available in application.
     *
     * @return list of json DTOs describing available subpages
     */
    public List<SubPageJsonDto> getSubpages() {
        return Collections.unmodifiableList(subpages);
    }
}
