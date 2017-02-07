package dataStructures;

import java.io.IOException;
import timetrackclient.HttpRequests;

/**
 *
 * @author Hobbes
 */
public class Client {
    String id;
    String name;

    public Client(String id, String name) {
        this.id = id;
        this.name = name;
    }
    
    
    //constructor to make a client based on there id
    public Client(String id) throws IOException{
        HttpRequests hr = new HttpRequests();
        Client c = hr.getClient(id);
        this.id = c.id;
        this.name = c.name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    
    @Override
    public String toString(){
        return name;
    }
}
