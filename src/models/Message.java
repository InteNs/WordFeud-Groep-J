package models;


import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Message {

    //DB accessor will be adding these to Chat arraylist
    private User user;
    private String message;
    private LocalDateTime date;
    private DateTimeFormatter dateFormatter;


    public Message(User user, String message, Timestamp timeStamp) {
        this.user = user;
        this.message = message;
        date = timeStamp.toLocalDateTime();
        dateFormatter = DateTimeFormatter.ofPattern("HH:mm - ");

    }

    public User getUser() {
        return user;
    }

    public String toString() {
        return date.format(dateFormatter)+ user + ":\n" + message;
    }
}


