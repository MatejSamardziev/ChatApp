package org.example.vebproekt.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;


@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Enable a simple memory-based message broker to carry the messages back to the client on destinations prefixed with "/topic" and "/queue"
        config.enableSimpleBroker("/topic", "/queue");

        // Application destination prefix
        // to route messages to specific server-side controller methods.
        /*When you configure the WebSocket message broker with a prefix using setApplicationDestinationPrefixes("/app"),
        any message sent to a destination prefixed with /app will be routed to the methods
         annotated with @MessageMapping that match the remaining part of the destination.
        */
        config.setApplicationDestinationPrefixes("/app");

        // User destination prefix for private messages
        config.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Register a WebSocket endpoint at "/ws" and enable the SockJS fallback options
        registry.addEndpoint("/ws").withSockJS();
    }
}