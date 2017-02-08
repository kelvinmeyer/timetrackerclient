package timetrackclient;

import dataStructures.Activity;
import dataStructures.Client;
import dataStructures.Job;
import dataStructures.User;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class Cache {
    private ObservableList<User> users = FXCollections.observableArrayList();
    private ObservableList<Client> clients = FXCollections.observableArrayList();
    private static ArrayList<Job> jobs = new ArrayList<>();
    private ObservableList<Activity> activities = FXCollections.observableArrayList();
    
    
    public Cache(ArrayList<User> u, ArrayList<Client> c, ArrayList<Job> j, ArrayList<Activity> a){
        users.addAll(u);
        clients.addAll(c);
        jobs.addAll(j);
        activities.addAll(a);
    }
    
    public void refresh(ArrayList<User> u, ArrayList<Client> c, ArrayList<Job> j, ArrayList<Activity> a){
        users.clear();
        users.addAll(u);
        clients.clear();
        clients.addAll(c);
        jobs.clear();
        jobs.addAll(j);
        activities.clear();
        activities.addAll(a);
    }
    
    public ObservableList<User> getUsers(){
        return users;
    }
    
    public ObservableList<Client> getClients(){
        return clients;
    }
    
    public ArrayList<Job> getJobs(){
        return jobs;
    }
    
    public ObservableList<Activity> getActivities(){
        return activities;
    }
    
    public void addUser(User u){
        users.add(u);
    }
    
    public void addClient(Client c){
        clients.add(c);
    }
    
    public void addJob(Job j){
        jobs.add(j);
    }
    
    public void addActivity(Activity a){
        activities.add(a);
    }
    
    public void removeUser(User u){
        users.remove(u);
    }
    
    public void removeClient(Client c){
        clients.remove(c);
    }
    
    public void removeJob(Job j){
        
    }
    
    public void removeActivity(Activity a){
        activities.remove(a);
    }
}
