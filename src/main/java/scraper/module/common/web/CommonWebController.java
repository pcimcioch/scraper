package scraper.module.common.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import scraper.module.core.scope.InModuleScope;

import java.util.List;

@RestController
@RequestMapping("/api")
@InModuleScope(module = CommonWebModule.NAME)
public class CommonWebController {

    private final CommonWebService commonWebService;

    @Autowired
    public CommonWebController(CommonWebService commonWebService) {
        this.commonWebService = commonWebService;
    }

    @RequestMapping(path = "/subpage", method = RequestMethod.GET)
    public List<SubPageJsonDto> getSubpages() {
        return commonWebService.getSubpages();
    }
}
