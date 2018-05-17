package gitlab.client.impl;

import org.gitlab.api.GitlabAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class Connection {

    @Bean
    public GitlabAPI getConnection(@Autowired GitlabConfigs gitlabConfigs) {
        return GitlabAPI.connect(gitlabConfigs.getHostUrl(), gitlabConfigs.getPrivateToken());
    }

}
