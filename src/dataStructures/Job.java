package dataStructures;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import timetrackclient.HttpRequests;


/**
 *
 * @author Hobbes
 */
public class Job {
    private String title;
    private Client client;
    private String status;
    private String startDate;
    private ArrayList<Activity> activities;
    private String description;
    private String invNum;
    private String jobId;
    
    public Job(String jobId, String title, String client, String status, String startDate, String description, String invnum){
        this.jobId = jobId;
        this.title = title;
        this.status = status;
        this.startDate = startDate;
        this.description = description;
        this.invNum = invnum;
        HttpRequests hr = new HttpRequests();
        try {
            this.client = new Client(client);
            this.activities = hr.getActivitiesByJob(jobId);
        } catch (IOException ex) {
            Logger.getLogger(Job.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getTitle() {
        return title;
    }

    public Client getClient() {
        return client;
    }

    public String getStatus() {
        return status;
    }

    public String getStartDate() {
        return startDate;
    }

    public ArrayList<Activity> getActivities() {
        return activities;
    }

    public String getDescription() {
        return description;
    }

    public String getInvNum() {
        return invNum;
    }

    public String getJobId() {
        return jobId;
    }

    public String getJobTime() {
        int time = 0;
        for(Activity act: activities){
            time+=act.getTimeInt();
        }
        return (time>60)? time/60+"h "+time%60+"m":time+"m";
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setInvNum(String invNum) {
        this.invNum = invNum;
    }
    
    @Override
    public String toString(){
        return title+":"+client.getName();
    }
}
