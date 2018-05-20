package msgr.client.api;

import java.io.IOException;
import java.util.List;

public interface MsgrClientApi {

    List<String> getChannels() throws IOException;

    void postToSlackByChannelName(String message, String s) throws IOException;
}
