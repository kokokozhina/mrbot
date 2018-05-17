package bot;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Bot.class)
public class BotTest {

    @Autowired
    Bot bot;

    @Test
    public void getGroups() throws IOException {
        assertNotNull(bot.getGroups());
        assertEquals(bot.getGroups().size(), 2);
    }

    @Test
    public void getProjectsByGroupName() throws IOException {
        assertNotNull(bot.getProjectsByGroupName("test"));
        assertEquals(bot.getProjectsByGroupName("dog-lovers").size(), 2);
    }

    @Test
    public void notifyOfMergeRequests() {
        //?
    }
}