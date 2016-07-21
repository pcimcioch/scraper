package scraper.module.common.web;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CommonWebServiceTest {

    @Test
    public void testGetSubPages_noSubPages() {
        // given
        CommonWebService service = new CommonWebService(Collections.emptyList());

        // when
        List<SubPageJsonDto> subpages = service.getSubpages();

        // then
        assertTrue(subpages.isEmpty());
    }

    @Test
    public void testGetSubPages() {
        // given
        CommonWebConfigurer sub1 = new TestWebConfigurer("name1", "description 1");
        CommonWebConfigurer sub2 = new TestWebConfigurer("name2", "description 2");
        CommonWebConfigurer sub3 = new TestWebConfigurer("name3", "description 3");
        CommonWebService service = new CommonWebService(Arrays.asList(sub1, sub2, sub3));

        // when
        List<SubPageJsonDto> subpages = service.getSubpages();

        // then
        SubPageJsonDto expectedSub1 = new SubPageJsonDto("name1", "description 1");
        SubPageJsonDto expectedSub2 = new SubPageJsonDto("name2", "description 2");
        SubPageJsonDto expectedSub3 = new SubPageJsonDto("name3", "description 3");
        assertEquals(Arrays.asList(expectedSub1, expectedSub2, expectedSub3), subpages);
    }

    private static class TestWebConfigurer implements CommonWebConfigurer {

        private final String url;

        private final String description;

        protected TestWebConfigurer(String url, String description) {
            this.url = url;
            this.description = description;
        }

        @Override
        public String url() {
            return url;
        }

        @Override
        public String moduleDescription() {
            return description;
        }
    }
}