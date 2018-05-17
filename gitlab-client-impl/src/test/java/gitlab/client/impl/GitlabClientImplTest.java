package gitlab.client.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = GitlabClientImpl.class)
public class GitlabClientImplTest {

    @Autowired
    GitlabClientImpl gitlabClient;

    //weak tests
    //may i check by injecting real data into the fields? not the common case
    @Test
    public void getGroups() throws IOException {
        assertNotNull(gitlabClient.getGroups());
        assertEquals(gitlabClient.getGroups().size(), 2);
    }

    @Test
    public void getProjectsByGroupName() throws IOException {
        assertNotNull(gitlabClient.getProjectsByGroupName("test"));
        assertEquals(gitlabClient.getProjectsByGroupName("dog-lovers").size(), 2);
    }

    @Test
    public void getMrsByGroupAndProject() throws IOException {
        assertNotNull(gitlabClient.getMrsByGroupAndProject("test", "test"));
        assertEquals(gitlabClient.getMrsByGroupAndProject("dog-lovers",
                "make-doggo-great-again").size(), 2);
    }
}