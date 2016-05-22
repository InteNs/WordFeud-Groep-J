package models;


import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Message {
    private User user;
    private String message;
    private LocalDateTime date;
    private DateTimeFormatter dateFormatter;

    public Message(User user, String message, Timestamp timeStamp) {
        this.user = user;
        this.message = message;
        date = timeStamp.toLocalDateTime();
        dateFormatter = DateTimeFormatter.ofPattern("HH:mm");
    }

    public String getTimeString() {
        return date.format(dateFormatter);
    }

    public User getUser() {
        return user;
    }

    public String toString() {
        return message;
    }
}


