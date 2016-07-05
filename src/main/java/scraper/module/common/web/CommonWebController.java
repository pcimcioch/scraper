package scraper.module.common.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import scraper.module.core.scope.InModuleScope;

import java.util.List;

/**
 * REST controller for "subpages" webpage operations.
 */
@RestController
@RequestMapping("/api")
@InModuleScope(module = CommonWebModule.NAME)
public class CommonWebController {

    private final CommonWebService commonWebService;

    @Autowired
    public CommonWebController(CommonWebService commonWebService) {
        this.commonWebService = commonWebService;
    }

    /**
     * Gets information about all webpages available in application.
     *
     * @return list of json DTOs describing available webpages
     */
    @RequestMapping(path = "/subpage", method = RequestMethod.GET)
    public List<SubPageJsonDto> getSubpages() {
        return commonWebService.getSubpages();
    }
}
