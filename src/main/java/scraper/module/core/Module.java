package scraper.module.core;

import java.util.Set;

public interface Module {
    
    String description();

	String name();
	
	Set<String> dependencies();
}
