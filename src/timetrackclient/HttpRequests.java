package timetrackclient;

import dataStructures.Activity;
import dataStructures.Client;
import dataStructures.Job;
import jsonParser.JsonArrayParser;
import jsonParser.JsonParser;
import dataStructures.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

/**
 *
 * @author Hobbes
 */
public class HttpRequests {
    private static final String VERSION_NUM = "/v1";
    private static final String BASE_URL = FXMLLoginController.getAddress()+VERSION_NUM;
    private static User user;
    private static Cache cache;
    public String getUsername(){
        return user.getUsername();
    }
    
    public void initCache(){
        try {
            cache = new Cache(getUsers(), getClients());
        } catch (IOException ex) {
            Logger.getLogger(HttpRequests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void refresh(){
        try {
            cache.refresh(getUsers(), getClients());
        } catch (IOException ex) {
            Logger.getLogger(HttpRequests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public ObservableList<User> getUsersCache(){
        return cache.getUsers();
    }
    
    public ObservableList<Client> getClientsCache(){
        return cache.getClients();
    }
    
    public boolean login(User u) throws IOException{
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(BASE_URL+"/login");
        post.setHeader("Content-Type", "application/x-www-form-urlencoded");
        
        List<NameValuePair> nameValuePairs = new ArrayList<>(2);
        nameValuePairs.add(new BasicNameValuePair("Username", u.getUsername()));
        nameValuePairs.add(new BasicNameValuePair("Password", u.getPassword()));

        post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        HttpResponse response = client.execute(post);
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

        String line = rd.readLine();
        JsonParser p = new JsonParser(line);
        
        String login = p.getVal("Login");
        
        boolean log = login.equals("Success");
        if(log){
            user = new User(u.getUsername(), u.getPassword());
            initCache();
        }
        return log;
    }
    
    private static String basicAuth(){
        String ucode = user.getUsername()+":"+user.getPassword();
        String coded = new String(Base64.getEncoder().encode(ucode.getBytes()));
        return coded;
    }

    public ArrayList<Job> getActivJobs() throws IOException{
        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(BASE_URL+"/jobs/active");
        get.setHeader("Authorization", "Basic "+basicAuth());
        
        HttpResponse response = client.execute(get);
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

        String line = rd.readLine();
        ArrayList<Job> jobs = new ArrayList<>();
        
        JsonArrayParser jap = new JsonArrayParser(line);
        while (jap.hasNext()) {
            JsonParser jp = jap.next();
            jobs.add(new Job(jp.getVal("JobID"),jp.getVal("Title"),jp.getVal("Client"),jp.getVal("Status"),jp.getVal("StartDate"),jp.getVal("Description"),jp.getVal("InvNum")));
        }

        return jobs;
    }
    
    public static ArrayList<User> getUsers() throws IOException{
        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(BASE_URL+"/users");
        get.setHeader("Authorization", "Basic "+basicAuth());
        
        HttpResponse response = client.execute(get);
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

        String line = rd.readLine();
        
        ArrayList<User> users = new ArrayList<>();
        
        JsonArrayParser jap = new JsonArrayParser(line);
        while (jap.hasNext()) {
            JsonParser jp = jap.next();
            users.add(new User(jp.getVal("Username"),"",jp.getVal("Name")));
        }

        return users;
    }
    
    public int addUser(User u) throws IOException{
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(BASE_URL+"/users");
        post.setHeader("Content-Type", "application/x-www-form-urlencoded");
        post.setHeader("Authorization", "Basic "+basicAuth());

        
        List<NameValuePair> nameValuePairs = new ArrayList<>(3);
        nameValuePairs.add(new BasicNameValuePair("Username", u.getUsername()));
        nameValuePairs.add(new BasicNameValuePair("Password", u.getPassword()));
        nameValuePairs.add(new BasicNameValuePair("Name", u.getName()));

        post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        HttpResponse response = client.execute(post);
        if(response.getStatusLine().getStatusCode() == 201){
            cache.addUser(u);
        }
        return response.getStatusLine().getStatusCode();
    }

    public int deleteUser(User u) throws IOException {
        HttpClient client = new DefaultHttpClient();
        HttpDelete delete = new HttpDelete(BASE_URL+"/users/"+u.getUsername());
        delete.setHeader("Content-Type", "application/x-www-form-urlencoded");
        delete.setHeader("Authorization", "Basic "+basicAuth());
        
        HttpResponse response = client.execute(delete);
        if(response.getStatusLine().getStatusCode() == 200){
             cache.removeUser(u);
         }
        return response.getStatusLine().getStatusCode();
    }

    public static ArrayList<Client> getClients() throws IOException {
        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(BASE_URL+"/clients");
        get.setHeader("Authorization", "Basic "+basicAuth());
        
        HttpResponse response = client.execute(get);
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

        String line = rd.readLine();
        
        ArrayList<Client> clients = new ArrayList<>();

        JsonArrayParser jap = new JsonArrayParser(line);
        while (jap.hasNext()) {
            JsonParser jp = jap.next();
            Client c = new Client(jp.getVal("ClientID"),jp.getVal("ClientName"));
            clients.add(c);
        }

        return clients;
    }
    
    public Client getClient(String id) throws IOException{
        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(BASE_URL+"/clients/"+id);
        get.setHeader("Authorization", "Basic "+basicAuth());
        
        HttpResponse response = client.execute(get);
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

        String line = rd.readLine();
        
        JsonParser jp = new JsonParser(line);
        Client out = new Client(jp.getVal("ClientID"), jp.getVal("ClientName"));
        return out;
    }

    public ArrayList<Activity> getActivitiesByJob(String jobId) throws IOException {
        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(BASE_URL+"/activities/j/"+jobId);
        get.setHeader("Authorization", "Basic "+basicAuth());
        
        HttpResponse response = client.execute(get);
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

        String line = rd.readLine();
        
        ArrayList<Activity> act = new ArrayList<>();
        
        JsonArrayParser jap = new JsonArrayParser(line);
        while (jap.hasNext()) {
            JsonParser jp = jap.next();
            act.add(new Activity(jp.getVal("StartTime"),jp.getVal("EndTime"), jp.getVal("Time"), jp.getVal("ActivityID"), jp.getVal("Comment"), jp.getVal("User")));
        }
        
        return act;
    }

    public int addClient(String c) throws IOException {
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(BASE_URL+"/clients");
        post.setHeader("Content-Type", "application/x-www-form-urlencoded");
        post.setHeader("Authorization", "Basic "+basicAuth());

        
        List<NameValuePair> nameValuePairs = new ArrayList<>(1);
        nameValuePairs.add(new BasicNameValuePair("ClientName", c));

        post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        HttpResponse response = client.execute(post);
        
        if(response.getStatusLine().getStatusCode() == 201){
            String id = response.getFirstHeader("Location").getValue();
            id = id.substring(id.lastIndexOf("/")+1);
            cache.addClient(new Client(id, c));
        }
        
        return response.getStatusLine().getStatusCode();
    }

    public int deleteClient(Client c) throws IOException{
        HttpClient client = new DefaultHttpClient();
        HttpDelete delete = new HttpDelete(BASE_URL+"/clients/"+c.getId());

        delete.setHeader("Content-Type", "application/x-www-form-urlencoded");
        delete.setHeader("Authorization", "Basic "+basicAuth());

        HttpResponse response = client.execute(delete);
        if(response.getStatusLine().getStatusCode() == 200){
            cache.removeClient(c);
        }
        return response.getStatusLine().getStatusCode();
    }
    
    public ArrayList<Activity> getActivitites() throws IOException{
         HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(BASE_URL+"/activities");
        get.setHeader("Authorization", "Basic "+basicAuth());
        
        HttpResponse response = client.execute(get);
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

        String line = rd.readLine();
        
        ArrayList<Activity> act = new ArrayList<>();
        
        JsonArrayParser jap = new JsonArrayParser(line);
        while (jap.hasNext()) {
            JsonParser jp = jap.next();
            act.add(new Activity(jp.getVal("StartTime"),jp.getVal("EndTime"), jp.getVal("Time"), jp.getVal("ActivityID"), jp.getVal("Comment"), jp.getVal("User")));
        }
        
        return act;
    }

    public Job getJob(String id) throws IOException{
        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(BASE_URL+"/jobs/"+id);
        get.setHeader("Authorization", "Basic "+basicAuth());
        
        HttpResponse response = client.execute(get);
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

        String line = rd.readLine();
        
        JsonParser jp = new JsonParser(line);
        Job j = new Job(jp.getVal("JobID"),jp.getVal("Title"),jp.getVal("Client"),jp.getVal("Status"),jp.getVal("StartDate"),jp.getVal("Description"),jp.getVal("InvNum"));

        return j;
    }
    
    public String[] addJob(String title, String clientName, String status, String startDate, String desr, String inv) throws IOException {
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(BASE_URL+"/jobs");
        post.setHeader("Content-Type", "application/x-www-form-urlencoded");
        post.setHeader("Authorization", "Basic "+basicAuth());

        
        List<NameValuePair> nameValuePairs = new ArrayList<>(1);
        //requiered
        nameValuePairs.add(new BasicNameValuePair("Client", clientName));
        nameValuePairs.add(new BasicNameValuePair("Status", status));
        nameValuePairs.add(new BasicNameValuePair("StartDate", startDate));
        if (!title.isEmpty()){nameValuePairs.add(new BasicNameValuePair("Title", title));}
        if (!desr.isEmpty()){nameValuePairs.add(new BasicNameValuePair("Description", desr));}
        if (!inv.isEmpty()){nameValuePairs.add(new BasicNameValuePair("InvNum", inv));} 
        

        post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        HttpResponse response = client.execute(post);
        
        String[] out = new String[2];
        out[0] = Integer.toString(response.getStatusLine().getStatusCode());
        out[1] = response.getFirstHeader("Location").getValue();
        
        return out;
    }

    public void addActivity(Activity a, String jid) throws IOException {
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(BASE_URL+"/activities");
        post.setHeader("Content-Type", "application/x-www-form-urlencoded");
        post.setHeader("Authorization", "Basic "+basicAuth());

        
        List<NameValuePair> nameValuePairs = new ArrayList<>(5);
        //requiered
        nameValuePairs.add(new BasicNameValuePair("Job", jid));
        nameValuePairs.add(new BasicNameValuePair("User", a.getUsername()));
        nameValuePairs.add(new BasicNameValuePair("StartTime", a.getStartTimeNum()));
        nameValuePairs.add(new BasicNameValuePair("EndTime", a.getEndTimeNum()));
        nameValuePairs.add(new BasicNameValuePair("Time", a.getTimeNum()));
        if (!a.getComment().isEmpty()){nameValuePairs.add(new BasicNameValuePair("Comment", a.getComment()));}

        post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        HttpResponse response = client.execute(post);
    }

    public ArrayList<Activity> getActivitiesByUser(String username) throws IOException{
        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(BASE_URL+"/activities/u/"+username);
        get.setHeader("Authorization", "Basic "+basicAuth());
        
        HttpResponse response = client.execute(get);
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

        String line = rd.readLine();
        
        ArrayList<Activity> act = new ArrayList<>();
        
        JsonArrayParser jap = new JsonArrayParser(line);
        while (jap.hasNext()) {
            JsonParser jp = jap.next();
            act.add(new Activity(jp.getVal("StartTime"),jp.getVal("EndTime"), jp.getVal("Time"), jp.getVal("ActivityID"), jp.getVal("Comment"), jp.getVal("User")));
        }
        
        return act;
    }
    
    public ArrayList<Activity> getActivitiesByTime(String from, String to) throws IOException{
        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(BASE_URL+"/activities/t?to="+to+"&from="+from);//to=1486384895061&from=1486332000000
        get.setHeader("Authorization", "Basic "+basicAuth());
        
        HttpResponse response = client.execute(get);
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

        String line = rd.readLine();
        
        ArrayList<Activity> act = new ArrayList<>();
        
        JsonArrayParser jap = new JsonArrayParser(line);
        while (jap.hasNext()) {
            JsonParser jp = jap.next();
            act.add(new Activity(jp.getVal("StartTime"),jp.getVal("EndTime"), jp.getVal("Time"), jp.getVal("ActivityID"), jp.getVal("Comment"), jp.getVal("User")));
        }
        
        return act;
    }

    public void changePassword(String newPass) throws IOException{
        HttpClient client = new DefaultHttpClient();
        HttpPatch patch = new HttpPatch(BASE_URL+"/users/"+user.getUsername());
        patch.setHeader("Content-Type", "application/x-www-form-urlencoded");
        patch.setHeader("Authorization", "Basic "+basicAuth());

        
        List<NameValuePair> nameValuePairs = new ArrayList<>(1);
        nameValuePairs.add(new BasicNameValuePair("Password", newPass));
       

        patch.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        HttpResponse response = client.execute(patch);
    }

    public void jobInactive(String id) throws IOException{
        HttpClient client = new DefaultHttpClient();
        HttpPatch patch = new HttpPatch(BASE_URL+"/jobs/inactive/"+id);
        patch.setHeader("Content-Type", "application/x-www-form-urlencoded");
        patch.setHeader("Authorization", "Basic "+basicAuth());
        HttpResponse response = client.execute(patch);
    }

    public void patchjob(Job job) throws IOException{
        HttpClient client = new DefaultHttpClient();
        HttpPatch patch = new HttpPatch(BASE_URL+"/jobs/"+job.getJobId());
        patch.setHeader("Content-Type", "application/x-www-form-urlencoded");
        patch.setHeader("Authorization", "Basic "+basicAuth());

        List<NameValuePair> nameValuePairs = new ArrayList<>(3);
        nameValuePairs.add(new BasicNameValuePair("Title", job.getTitle()));
        nameValuePairs.add(new BasicNameValuePair("Description", job.getDescription()));
       nameValuePairs.add(new BasicNameValuePair("InvNum", job.getInvNum()));

        patch.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        HttpResponse response = client.execute(patch);
    }

    public void jobActive(String jobId) throws IOException{
        HttpClient client = new DefaultHttpClient();
        HttpPatch patch = new HttpPatch(BASE_URL+"/jobs/active/"+jobId);
        patch.setHeader("Content-Type", "application/x-www-form-urlencoded");
        patch.setHeader("Authorization", "Basic "+basicAuth());

        HttpResponse response = client.execute(patch);
    }

    public ArrayList<Job> getInactiveJobs() throws IOException{
        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(BASE_URL+"/jobs/inactive");
        get.setHeader("Authorization", "Basic "+basicAuth());
        
        HttpResponse response = client.execute(get);
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

        String line = rd.readLine();
        ArrayList<Job> jobs = new ArrayList<>();
        
        JsonArrayParser jap = new JsonArrayParser(line);
        while (jap.hasNext()) {
            JsonParser jp = jap.next();
            jobs.add(new Job(jp.getVal("JobID"),jp.getVal("Title"),jp.getVal("Client"),jp.getVal("Status"),jp.getVal("StartDate"),jp.getVal("Description"),jp.getVal("InvNum")));
        }

        return jobs;
    }
    
}
