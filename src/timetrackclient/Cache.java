package timetrackclient;

import dataStructures.Client;
import dataStructures.User;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class Cache {
    private ObservableList<User> users = FXCollections.observableArrayList();
    private ObservableList<Client> clients = FXCollections.observableArrayList();
    
    
    public Cache(ArrayList<User> u, ArrayList<Client> c){
        users.addAll(u);
        clients.addAll(c);
    }
    
    public void refresh(ArrayList<User> u, ArrayList<Client> c){
        users.clear();
        users.addAll(u);
        clients.clear();
        clients.addAll(c);
    }
    
    public ObservableList<User> getUsers(){
        return users;
    }
    
    public ObservableList<Client> getClients(){
        return clients;
    }
    
    public void addUser(User u){
        users.add(u);
    }
    
    public void addClient(Client c){
        clients.add(c);
    }
    
    public void removeUser(User u){
        users.remove(u);
    }
    
    public void removeClient(Client c){
        clients.remove(c);
    }
}
