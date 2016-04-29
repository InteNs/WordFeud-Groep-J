package models;


import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Message {

    //DB accessor will be adding these to Chat arraylist
    private String user;
    private String message;
    private LocalDateTime date;
    private DateTimeFormatter dateFormatter;


    public Message(String user, String message, Timestamp timeStamp) {
        this.user = user;
        this.message = message;
        date = timeStamp.toLocalDateTime();
        dateFormatter = DateTimeFormatter.ofPattern("dd MMM 'om' HH:mm");

    }

    public String toString() {
        return user+ " zij op "+date.format(dateFormatter)+": \n"+message;
    }
}


