package scraper.module.common.management.module.view;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import scraper.module.common.management.module.runner.ModuleRunnerModule;

/**
 * Configuration of the websocket endpoint, that sends running {@link scraper.module.core.WorkerModule} status updates.
 */
@Configuration
public class ModuleViewEndpointConfiguration extends AbstractWebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker(ModuleRunnerModule.NAME + "/topic");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(ModuleViewModule.NAME + "/endpoint").withSockJS();
    }
}
