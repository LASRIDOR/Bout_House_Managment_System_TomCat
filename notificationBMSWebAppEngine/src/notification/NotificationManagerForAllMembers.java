package notification;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NotificationManagerForAllMembers {
    List<Notification> everyoneNotification;

    public NotificationManagerForAllMembers(){
        everyoneNotification = new ArrayList<>();
    }

    public List<Notification> getEveryoneNotification(){
        return Collections.unmodifiableList(this.everyoneNotification);
    }

    public void addNotification(String header, String message) {
        everyoneNotification.add(new Notification(header, message));
    }

    public void deleteNotification(int index) {
        if(index >= 0 && index < everyoneNotification.size())
            this.everyoneNotification.remove(index);
    }

    public  List<Notification> getMessages(int offset){
        if (offset < 0 || offset > everyoneNotification.size()) {
            offset = 0;
        }
        return everyoneNotification.subList(offset, everyoneNotification.size());
    }

    public int getVersion() {
        return everyoneNotification.size();
    }
}
