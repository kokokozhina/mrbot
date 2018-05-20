package bot;

import git.client.api.GitClientApi;
import msgr.client.api.MsgrClientApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@ComponentScan({"git.client.api", "gitlab.client.impl", "msgr.client.api", "slack.impl"})
public class Bot {

    @Autowired
    private GitClientApi gitClientApi;

    @Autowired
    private MsgrClientApi msgrClientApi;

    public List<String> getGroups() throws IOException {
        return gitClientApi.getGroups();
    }

    public List<String> getProjectsByGroupName(String group) throws IOException {
        return gitClientApi.getProjectsByGroupName(group);
    }

    public List<String> getChannels() throws IOException {
        return msgrClientApi.getChannels();
    }

    public void notifyOfMergeRequests(List<List<String>> properties) throws IOException {
        for (List<String> property : properties) {
            List<String> messages = gitClientApi.getMrsByGroupAndProject(property.get(0), property.get(1));
            for (String message : messages) {
                msgrClientApi.postToSlackByChannelName(message, property.get(2));
            }

        }
    }

    public GitClientApi getGitClientApi() {
        return gitClientApi;
    }

    public void setGitClientApi(GitClientApi gitClientApi) {
        this.gitClientApi = gitClientApi;
    }

    public MsgrClientApi getMsgrClientApi() {
        return msgrClientApi;
    }

    public void setMsgrClientApi(MsgrClientApi msgrClientApi) {
        this.msgrClientApi = msgrClientApi;
    }
}