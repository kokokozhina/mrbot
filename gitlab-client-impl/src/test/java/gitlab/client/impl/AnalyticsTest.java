package gitlab.client.impl;

import org.gitlab.api.models.GitlabCommit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Analytics.class)
public class AnalyticsTest {

    @Test
    public void checkSquashNeeded() {
        List<GitlabCommit> list = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            list.add(null);
        }
        assertEquals(Analytics.checkSquash(list), "SQUASH NEEDED : 8 commits");
    }

    @Test
    public void checkSquashNotNeeded() {
        assertEquals(Analytics.checkSquash(new ArrayList<>()), "");
    }


    @Test
    public void checkCommitDescriptionSimilarity() {
        List<GitlabCommit> list = new ArrayList<>();
        GitlabCommit commit1 = new GitlabCommit();
        commit1.setMessage("Look at my horse");
        GitlabCommit commit2 = new GitlabCommit();
        commit2.setMessage("My horse is amazing");
        list.add(commit1);
        list.add(commit2);
        assertEquals(Analytics.checkCommitDescriptionSimilarity(list), "");
    }

    @Test
    public void checkCommitDescriptionSimilarityInappropriate() {
        List<GitlabCommit> list = new ArrayList<>();
        GitlabCommit commit1 = new GitlabCommit();
        commit1.setMessage("Look at my horse");
        GitlabCommit commit2 = new GitlabCommit();
        commit2.setMessage("Look at my ho");
        list.add(commit1);
        list.add(commit2);
        assertNotEquals(Analytics.checkCommitDescriptionSimilarity(list), "");
    }
}