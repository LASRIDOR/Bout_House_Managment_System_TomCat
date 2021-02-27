package notification;

import BMS.boutHouse.form.field.infoField.InfoField;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class NotificationManagerForMember {
    Map<InfoField<String>, List<Notification>> perMemberNotifications;

    public NotificationManagerForMember(){
        this.perMemberNotifications = new Hashtable<>();
    }

    public void addNotification(InfoField<String> member, String header, String message) {
        if(!perMemberNotifications.containsKey(member)) {
            perMemberNotifications.put(member, new ArrayList<>());
        }

        perMemberNotifications.get(member).add(new Notification(header, message));
    }

    public void deleteNotification(InfoField<String> member, int index){
        if (perMemberNotifications.containsKey(member)) {
            perMemberNotifications.get(member).remove(index);
        }
    }

    public  List<Notification> getMessages(InfoField<String> member, int offset){
        List<Notification> missingNotifications;

        if (perMemberNotifications.containsKey(member)) {
            if (offset < 0 || offset > perMemberNotifications.get(member).size()) {
                offset = 0;
            }
            missingNotifications =  perMemberNotifications.get(member).subList(offset, perMemberNotifications.get(member).size());
        }else{
            missingNotifications = new ArrayList<>();
        }

        return missingNotifications;
    }

    public int getVersion(InfoField<String> member) {
        return perMemberNotifications.containsKey(member) ? perMemberNotifications.get(member).size() : 0;
    }
}
