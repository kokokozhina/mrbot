package msgr.client.api;

import java.io.IOException;
import java.util.List;

/**
 * This interface provides methods to implement if you wand to add a messenger to Merge Request Bot
 * @author kokokozhina
 */
public interface MsgrClientApi {


    /**
     * Returns a list of titles of channels in a messenger workspace
     * @return {@code List<String>} is a list of titles of channels that belongs to messenger workspace
     * @throws IOException
     */
    List<String> getChannels() throws IOException;


    /**
     * Post a message to a slack channel
     * @param message message to send
     * @param s channel name
     * @throws IOException
     */
    void postToSlackByChannelName(String message, String s) throws IOException;
}
