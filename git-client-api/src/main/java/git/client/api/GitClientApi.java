package git.client.api;

import java.io.IOException;
import java.util.List;

public interface GitClientApi {
    List<String> getMergeRequests() throws IOException;
    List<String> getGroups() throws IOException;

    List<String> getProjectsByGroupName(String group) throws IOException;

    List<String> getMrsByGroupAndProject(String group, String project) throws IOException;
}
