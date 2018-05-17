package slack.impl;

import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackSession;
import msgr.client.api.MsgrClientApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SlackClientImpl implements MsgrClientApi {
    @Autowired
    private Channels channels;

    @Autowired
    private SlackSession session;

    @PostConstruct
    public void init() throws IOException {
        session.connect();
    }

    @PreDestroy
    public void destroy() throws IOException {
        session.disconnect();
    }

    @Override
    public void postToSlackChannels(List<String> messages) throws IOException {

        for (String channel : channels.getChannels()) {
            SlackChannel slackChannel = session.findChannelByName(channel);
            for (String message : messages) {
                session.sendMessage(slackChannel, message); //make sure bot is a member of the channel.
            }
        }
    }

    @Override
    public List<String> getChannels() throws IOException {

        List<String> allChannels = session.getChannels().stream()
                .filter(s -> !s.isDirect())
                .map(s -> s.getName())
                .collect(Collectors.toList());

        return allChannels;
    }

    @Override
    public void postToSlackByChannelName(String message, String channelName) throws IOException {

        SlackChannel slackChannel = session.findChannelByName(channelName);
        session.sendMessage(slackChannel, message);
    }
}
