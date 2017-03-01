package kz.codingwolves.unichat.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

@Configuration
public class WebSocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {

    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        messages
                .nullDestMatcher().authenticated()
                .simpDestMatchers("/app/**").hasRole("USER")
                .simpSubscribeDestMatchers("/queue/**").denyAll()
                .simpSubscribeDestMatchers("/topic/**").hasRole("USER")
                .simpSubscribeDestMatchers("/user/queue/roulette").hasRole("USER")
                .simpSubscribeDestMatchers("/user/queue/shout").hasRole("USER")
                .simpSubscribeDestMatchers("/user/queue/private").hasRole("USER")
                .anyMessage().denyAll();
    }
    
    @Override
    protected boolean sameOriginDisabled() {
        return true;
    }
}