package scraper.module.common.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class CommonWebService {

    private final List<SubPageJsonDto> subpages = new ArrayList<>();

    @Autowired
    public CommonWebService(List<CommonWebConfigurer> configurers) {
        for(CommonWebConfigurer configurer : configurers) {
            subpages.add(toModel(configurer));
        }
    }

    private SubPageJsonDto toModel(CommonWebConfigurer configurer) {
        return new SubPageJsonDto(configurer.getModuleName(), configurer.getModuleDescription());
    }

    public List<SubPageJsonDto> getSubpages() {
        return Collections.unmodifiableList(subpages);
    }
}
