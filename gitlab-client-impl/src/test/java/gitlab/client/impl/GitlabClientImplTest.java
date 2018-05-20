package gitlab.client.impl;

import org.gitlab.api.GitlabAPI;
import org.gitlab.api.models.GitlabGroup;
import org.gitlab.api.models.GitlabMergeRequest;
import org.gitlab.api.models.GitlabProject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = GitlabClientImpl.class)
public class GitlabClientImplTest {

    private static List<GitlabGroup> groups;
    private static GitlabGroup group;
    private static List<GitlabProject> projects;
    private static GitlabProject project;
    private static List<GitlabMergeRequest> mergeRequests;
    private static GitlabMergeRequest mergeRequest;

    private GitlabClientImpl gitlabClient;

    @Mock
    private GitlabConfigs gitlabConfigs;

    @Mock
    private GitlabAPI gitlabConnection;

    @Before
    public void setUp() {
        gitlabClient = new GitlabClientImpl();
        gitlabClient.setGitlabConfigs(gitlabConfigs);
        gitlabClient.setGitlabConnection(gitlabConnection);

        groups = new ArrayList<>();
        group = new GitlabGroup();
        group.setName("wonderful_life");
        groups.add(group);

        projects = new ArrayList<>();
        project = new GitlabProject();
        project.setName("project_of_wonderful_life");
        projects.add(project);

        mergeRequests = new ArrayList<>();
        mergeRequest = new GitlabMergeRequest();
        mergeRequest.setTitle("hurts");
        mergeRequests.add(mergeRequest);
    }

    @Test
    public void getGroupsTest() throws IOException {
        //arrange
        when(gitlabConnection.getGroups()).thenReturn(groups);

        //act

        //assert
        assertEquals(gitlabClient.getGroups().size(), 1);
    }

    @Test
    public void getProjectsByGroupNameTest() throws IOException {
        when(gitlabConnection.getGroups()).thenReturn(groups);
        when(gitlabConnection.getGroupProjects(group)).thenReturn(projects);

        assertEquals(gitlabClient.getProjectsByGroupName("wonderful_life").size(), 1);
    }

    @Test
    public void getMrsByGroupAndProjectTest() throws IOException {
        when(gitlabConnection.getGroups()).thenReturn(groups);
        when(gitlabConnection.getGroupProjects(group)).thenReturn(projects);
        when(gitlabConnection.getMergeRequests(project)).thenReturn(mergeRequests);

        assertEquals(gitlabClient.getMrsByGroupAndProject("wonderful_life",
                "project_of_wonderful_life").size(), 1);
    }
}