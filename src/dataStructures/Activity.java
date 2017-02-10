package dataStructures;

import java.text.SimpleDateFormat;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Hobbes
 */
public class Activity {
    private SimpleStringProperty startTime; // unix timestamp
    private SimpleStringProperty endTime;   // unix timestamp
    private SimpleStringProperty time;      // time in mins 
    private String actId;
    private SimpleStringProperty comment;
    private SimpleStringProperty username;
    
    private final SimpleDateFormat formatDate = new SimpleDateFormat("d/M/yyyy");
    private final SimpleDateFormat formatTime = new SimpleDateFormat("h:mm a");

    public Activity(String startTime, String endTime, String time, String actId, String comment, String user) {
        this.startTime = new SimpleStringProperty(startTime);
        this.endTime = new SimpleStringProperty(endTime);
        this.time = new SimpleStringProperty(time);
        this.actId = actId;
        this.comment = new SimpleStringProperty(comment);
        this.username = new SimpleStringProperty(user);
    }
    
    public String getID(){
        return actId;
    }
    
    public String getDate(){
        long st = Long.parseLong(startTime.get());
        return formatDate.format(st);
    }
    
    public String getStartTime() {
        long st = Long.parseLong(startTime.get());
        return formatTime.format(st);
    }
    
    public String getStartTimeNum(){
        return startTime.get();
    }

    public String getEndTime() {
        long et = Long.parseLong(endTime.get());
        return formatTime.format(et);
    }
    
    public String getEndTimeNum(){
        return endTime.get();
    }

    public String getTime() {
        return time.get()+" mins";
    }
    
    public int getTimeInt(){
        return Integer.parseInt(time.get());
    }
    
    public String getTimeNum() {
        return time.get();
    }

    public String getComment() {
        return comment.get().equals("null") ? "" : comment.get();
    }

    public String getUsername() {
        return username.get();
    }
}
