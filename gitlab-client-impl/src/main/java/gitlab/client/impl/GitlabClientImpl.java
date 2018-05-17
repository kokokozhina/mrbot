package gitlab.client.impl;

import git.client.api.GitClientApi;
import org.gitlab.api.GitlabAPI;
import org.gitlab.api.models.GitlabCommit;
import org.gitlab.api.models.GitlabGroup;
import org.gitlab.api.models.GitlabMergeRequest;
import org.gitlab.api.models.GitlabProject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static gitlab.client.impl.Analytics.checkCommitDescriptionSimilarity;
import static gitlab.client.impl.Analytics.checkSquash;

@Component
@ComponentScan
public class GitlabClientImpl implements GitClientApi {

    @Autowired
    private GitlabConfigs gitlabConfigs;

    @Autowired
    private GitlabAPI gitlabConnection;

    private List<GitlabCommit> masterCommits = new ArrayList<>();

    private List<GitlabGroup> getGroups(GitlabAPI api) throws IOException {
        return api.getGroups();
    }

    private List<GitlabProject> getGroupProjects(GitlabAPI api, GitlabGroup group) throws IOException {
        return api.getGroupProjects(group);
    }

    private List<GitlabMergeRequest> getMergeRequests(GitlabAPI api, GitlabProject project) throws IOException {
        return api.getMergeRequests(project);
    }

    private List<GitlabMergeRequest> excludeWipMergeRequests(List<GitlabMergeRequest> mergeRequests) {
        List<GitlabMergeRequest> mergeRequestsWithoutWip = new ArrayList<GitlabMergeRequest>();

        for (GitlabMergeRequest mergeRequest : mergeRequests) {
            if (!mergeRequest.getTitle().contains("WIP")) {
                mergeRequestsWithoutWip.add(mergeRequest);
            }
        }

        return mergeRequestsWithoutWip;
    }

    private List<GitlabCommit> commitsOfMergeRequest(GitlabMergeRequest mergeRequest) {

        List<GitlabCommit> commitsUnfiltered = new ArrayList<>();
        List<GitlabCommit> commits = new ArrayList<>();

        try {
            commitsUnfiltered = gitlabConnection.getCommits(mergeRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (GitlabCommit commit : commitsUnfiltered) {
            if (!masterCommits.contains(commit)) {
                commits.add(commit);
            }
        }

        return commits;
    }

    private String linkOfMergeRequest(GitlabMergeRequest mergeRequest, String group, String project) {
        return gitlabConfigs.getHostUrl() + "/" + group + "/" + project + "/merge_requests/" + mergeRequest.getIid();
    }

    private String printMergeRequest(GitlabMergeRequest mergeRequest, GitlabGroup group, GitlabProject project) {

        String mrToString = "Merge Request №" + mergeRequest.getId()
                + "\ntitle: " + mergeRequest.getTitle()
                + "\nfrom: " + mergeRequest.getSourceBranch()
                + "\nto: " + mergeRequest.getTargetBranch()
                + "\nlink: " + linkOfMergeRequest(mergeRequest, group.getName(), project.getName());

        List<GitlabCommit> commits = commitsOfMergeRequest(mergeRequest);

        String checkSquashResult = checkSquash(commits);
        if (!checkSquashResult.isEmpty()) {
            mrToString += "\n " + checkSquashResult;
        }

        String commitDescriptionSimilarityResult = checkCommitDescriptionSimilarity(commits);
        if (!commitDescriptionSimilarityResult.isEmpty()) {
            mrToString += "\n " + commitDescriptionSimilarityResult;
        }

        return mrToString;
    }

    private List<String> convertMergeRequestsToStringList(List<GitlabMergeRequest> mergeRequests,
                                                          GitlabGroup group, GitlabProject project) {
        return mergeRequests.stream().map(s -> printMergeRequest(s, group, project)).collect(Collectors.toList());
    }


    private List<String> getMergeRequests() throws IOException {
        List<String> requiringMergeRequests = new ArrayList<>();


        Set<String> setOfGroupsRequiringMergeRequests
                = new HashSet<String>(gitlabConfigs.getGroups());

        List<GitlabGroup> groups = this.getGroups(gitlabConnection);

        for (GitlabGroup group : groups) {

            if (!setOfGroupsRequiringMergeRequests.contains(group.getName())) {
                continue;
            }

            List<GitlabProject> projects = this.getGroupProjects(gitlabConnection, group);
            for (GitlabProject project : projects) {

                masterCommits = gitlabConnection.getAllCommits(project.getId(), "master");

                List<GitlabMergeRequest> mergeRequests = this.getMergeRequests(gitlabConnection, project);

                mergeRequests = excludeWipMergeRequests(mergeRequests);

                requiringMergeRequests.addAll(convertMergeRequestsToStringList(mergeRequests,
                        group, project));
            }
        }


        return requiringMergeRequests;
    }

    @Override
    public List<String> getGroups() throws IOException {
        List<GitlabGroup> groups = this.getGroups(gitlabConnection);
        List<String> groupsTitles = new ArrayList<>();
        for (GitlabGroup group : groups) {
            groupsTitles.add(group.getName());
        }

        return groupsTitles;
    }

    @Override
    public List<String> getProjectsByGroupName(String groupName) throws IOException {
        List<GitlabGroup> groups = this.getGroups(gitlabConnection);

        for (GitlabGroup group : groups) {

            if (group.getName().equals(groupName)) {
                List<GitlabProject> projects = this.getGroupProjects(gitlabConnection, group);
                return projects.stream().map(s -> s.getName()).collect(Collectors.toList());
            }

        }

        return new ArrayList<>();
    }

    @Override
    public List<String> getMrsByGroupAndProject(String groupName, String projectName) throws IOException {
        List<String> requiringMergeRequests = new ArrayList<>();


        List<GitlabGroup> groups = this.getGroups(gitlabConnection);

        for (GitlabGroup group : groups) {

            if (group.getName().equals(groupName)) {
                List<GitlabProject> projects = this.getGroupProjects(gitlabConnection, group);
                for (GitlabProject project : projects) {
                    if (project.getName().equals(projectName)) {
                        masterCommits = gitlabConnection.getAllCommits(project.getId(), "master");

                        List<GitlabMergeRequest> mergeRequests = this.getMergeRequests(gitlabConnection, project);

                        mergeRequests = excludeWipMergeRequests(mergeRequests);

                        requiringMergeRequests.addAll(convertMergeRequestsToStringList(mergeRequests,
                                group, project));
                    }

                }
            }
        }

        return requiringMergeRequests;
    }




}