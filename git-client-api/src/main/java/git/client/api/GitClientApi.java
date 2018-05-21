package git.client.api;

import java.io.IOException;
import java.util.List;

/**
 * This interface provides methods to implement
 * if you want to add a git server instance
 * in Merge Request Bot
 * @author kokokozhina
 */
public interface GitClientApi {

    /**
     * Returns a list of titles of groups in the git server instance
     * @return {@code List<String>} is a list of titles of groups in the git server instance
     * @throws IOException if connection is invalid
     */
    List<String> getGroups() throws IOException;


    /**
     * Returns a list of titles of projects that belongs to certain group
     * @param group title of the group
     * @return {@code List<String>} is a list of titles of projects that belongs to certain group
     * @throws IOException if connection is invalid
     */
    List<String> getProjectsByGroupName(String group) throws IOException;

    /**
     * Returns a list of titles of merge requests that belongs to certain project from the group
     * @param group title of the group
     * @param project title of the project
     * @return {@code List<String>} is a list of titles of merge requests that belongs to certain project from the group
     * @throws IOException if connection is invalid
     */
    List<String> getMrsByGroupAndProject(String group, String project) throws IOException;
}
