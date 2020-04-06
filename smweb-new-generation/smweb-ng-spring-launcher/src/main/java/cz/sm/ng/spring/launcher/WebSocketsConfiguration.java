package cz.sm.ng.spring.launcher;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;


/**
 * Configures Spring Boot websocket technology to
 * use STOMP endpoints with SockJS fallback option.
 * These endpoints will use their relevant domains
 * </core/ws> and </clodwar/ws> with prefix /user.
 * This prefix is used to pass session ID so STOMP
 * implementation can handle sessions on background.
 *
 * @author Norbert Dopjera
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketsConfiguration implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/core/ws", "/clodwar/ws", "/queue", "/user");
        config.setApplicationDestinationPrefixes("/");
        config.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/core/ws/connector").withSockJS();
        registry.addEndpoint("/clodwar/ws/connector").withSockJS();
    }

} // WebSocketsConfiguration
