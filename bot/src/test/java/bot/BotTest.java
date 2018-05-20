package bot;

import git.client.api.GitClientApi;
import msgr.client.api.MsgrClientApi;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Bot.class)
public class BotTest {

    private Bot bot;
    private List<String> list;


    @MockBean
    private GitClientApi gitClientApi;

    @MockBean
    private MsgrClientApi msgrClientApi;

    @Before
    public void setUp() throws Exception {
        bot = new Bot();
        bot.setGitClientApi(gitClientApi);
        bot.setMsgrClientApi(msgrClientApi);

        list = new ArrayList<>();
    }


    @Test
    public void getGroups() throws IOException {
        when(gitClientApi.getGroups()).thenReturn(list);
        assertEquals(bot.getGroups(), list);
    }

    @Test
    public void getProjectsByGroupName() throws IOException {
        when(gitClientApi.getProjectsByGroupName("name")).thenReturn(list);
        assertEquals(bot.getProjectsByGroupName("name"), list);
    }

    @Test
    public void notifyOfMergeRequests() throws IOException {
        List<String> oneMessageList = new ArrayList<>();
        oneMessageList.add("oneMessageString");

        List<List<String>> properties = new ArrayList<>();
        List<String> property = new ArrayList<>();
        property.add("str0");
        property.add("str1");
        property.add("str2");
        properties.add(property);

        when(gitClientApi.getMrsByGroupAndProject(Matchers.anyString(), Matchers.anyString()))
                .thenReturn(oneMessageList);

        doNothing().when(msgrClientApi).postToSlackByChannelName(Matchers.anyString(), Matchers.anyString());
        bot.notifyOfMergeRequests(properties);

        verify(msgrClientApi, times(1))
                .postToSlackByChannelName(Matchers.anyString(), Matchers.anyString());

    }

    @Test
    public void getChannels() throws IOException {
        when(msgrClientApi.getChannels()).thenReturn(list);
        assertEquals(bot.getChannels(), list);
    }

}