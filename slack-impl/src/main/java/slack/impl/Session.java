package slack.impl;


import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.impl.SlackSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Configuration
@ComponentScan
public class Session {

    @Bean
    public SlackSession getSession(@Autowired SlackConfigs slackConfigs) {
        return SlackSessionFactory.createWebSocketSlackSession(slackConfigs.getAuthToken());
    }

}