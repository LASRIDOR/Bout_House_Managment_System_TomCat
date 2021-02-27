package chat;

import java.util.ArrayList;
import java.util.List;

public class ChatManager {

    private final List<MessageEntry> messages;

    public ChatManager() {
        messages = new ArrayList<>();
    }

    public void addMessage(String message, String username) {
        messages.add(new MessageEntry(message, username));
    }

    public  List<MessageEntry> getMessages(int offset){
        if (offset < 0 || offset > messages.size()) {
            offset = 0;
        }
        return messages.subList(offset, messages.size());
    }

    public int getVersion() {
        return messages.size();
    }
}
