package slack.impl;

import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackSession;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SlackClientImpl.class)
public class SlackClientImplTest {

    private SlackClientImpl slackClient;
    private static List<SlackChannel> slackChannels;
    private static SlackChannel slackChannel;

    @MockBean
    private SlackSession session;

    @Before
    public void setUp() {
        slackClient = new SlackClientImpl();
        slackClient.setSession(session);

        slackChannel = new SlackChannel("", "ready_to_go", "", "",
                false, false, false);
        slackChannels = new ArrayList<>();
        slackChannels.add(slackChannel);
    }

    @Test
    public void getChannelsTest() throws IOException {
        when(session.getChannels()).thenReturn(slackChannels);

        assertEquals(slackClient.getChannels().size(), 1);
    }

    @Test
    public void postToSlackByChannelNameTest() throws IOException {
        when(session.findChannelByName(slackChannel.getName())).thenReturn(slackChannel);
        when(session.sendMessage(slackChannel, "")).thenThrow(new NullPointerException());

        try {
            slackClient.postToSlackByChannelName("", slackChannel.getName());
            fail("Error");
        } catch (NullPointerException e) {
            assertEquals(1, 1);
        } catch (Exception e) {
            assertEquals(1, 0);
        }

    }
}