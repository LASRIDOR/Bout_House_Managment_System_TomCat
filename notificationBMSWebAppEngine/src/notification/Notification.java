package notification;

import java.util.Objects;

public class Notification implements Comparable<Notification>{
    private final String header;
    private final String message;
    private final long timestamp;

    public Notification(String header, String message) {
        this.header = header;
        this.message = message;
        this.timestamp = System.currentTimeMillis();
    }

    public String getHeader(){return header;}

    public String getMessage() {
        return message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public int compareTo(Notification o) {
        if (o == null) {
            return 1;
        }

        return (int)(this.getTimestamp() - o.getTimestamp());
    }

    @Override
    public String toString() {
        return "header='" + header + '\'' +
                ", message='" + message + '\'';
    }
}
