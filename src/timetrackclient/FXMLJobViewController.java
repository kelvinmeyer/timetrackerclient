/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timetrackclient;

import dataStructures.Activity;
import dataStructures.Job;
import dataStructures.User;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Hobbes
 */
public class FXMLJobViewController implements Initializable {
    
    private String id;
    private boolean tracking = false;
    private Job job;
    private static Activity a;
    private static Job j;
    private long st;
    private HttpRequests hr;
    
    private Image imgStart;
    private Image imgStop;
    
    @FXML
    private Button btnStart;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnDelete;
    @FXML
    private Label lblTitle;
    @FXML
    private Label lblClient;
    @FXML
    private Label lblStartDate;
    @FXML
    private Label lblInvNum;
    @FXML
    private TextArea txtaDescr;
    @FXML
    private TableView<Activity> timesTable;
    @FXML
    private TableColumn clmDate;
    @FXML
    private TableColumn clmTime;
    @FXML
    private TableColumn clmUser;
    @FXML
    private TableColumn clmComment;
    @FXML
    private TableColumn clmStartTime;
    @FXML
    private TableColumn clmEndTime;
    private ObservableList<Activity> timesList;
    @FXML
    private HBox spacer;
    @FXML
    private Button btnSearch;
    @FXML
    private ChoiceBox cbSort;
    @FXML
    private ChoiceBox cbFilter;
    private static ObservableList<String> usernames = FXCollections.observableArrayList();
    @FXML
    private TextArea txtaTime;
    @FXML
    private Button btnEditActivity;
    
    @FXML
    private void start() throws IOException{
        long unixTime = System.currentTimeMillis();
        if(!tracking){//start timing
            btnStart.setGraphic(new ImageView(imgStop));
            tracking = true;
            st = unixTime;
        } else { // end timing and sumbit activity
            btnStart.setGraphic(new ImageView(imgStart));
            tracking = false;
            long et = unixTime;
            int time = (int) (et-st)/1000;//convert to seconds
            if(time<60){ // convert to nearest min
                time = 1;
            } else if(time%60<30) { //round down
                time = time/60;
            } else { //round up
                time /= 60;
                time++;
            }
            //submit activity
            // get comment
            Activity a = new Activity(Long.toString(st), Long.toString(et), Integer.toString(time), "id", "", hr.getUsername(), 0);
            try {
                String location = hr.addActivity(a, job.getJobId());
                a.setID(location.substring(location.lastIndexOf("/")+1));
            } catch (IOException ex) {
                Logger.getLogger(FXMLJobViewController.class.getName()).log(Level.SEVERE, null, ex);
            }
            //add to local observable list
            timesList.add(a);
        }
    }
    
    @FXML
    private void search() throws ParseException{
        String sort = (String) cbSort.getSelectionModel().selectedItemProperty().getValue();
        String filter = (String) cbFilter.getSelectionModel().selectedItemProperty().getValue();
        if(sort.equals("Time")){
            switch(filter){
                case "Today":{
                    LocalDate now = LocalDate.now();
                    String strDate = (now.getDayOfMonth())+"-"+(now.getMonthValue())+"-"+now.getYear()+",00:00:00";
                    DateFormat formatter = new SimpleDateFormat("d-M-yyyy,HH:mm:ss");
                    Date d = formatter.parse(strDate);
                    try {
                        timesList.clear();
                        timesList.addAll(hr.getActivitiesByTime(Long.toString(d.getTime()), Long.toString(System.currentTimeMillis())));
                        txtaTime.setText(timeString());
                    } catch (IOException ex) {
                        Logger.getLogger(FXMLMainWindowController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                }
                case "Yeasterday":{
                    
                    break;
                }
                case "This Week":{
                    
                    break;
                }
                case "Last Week":{
                    
                    break;
                }
                case "This Month":{
                    LocalDate now = LocalDate.now();
                    String strDate = "01-"+(now.getMonthValue())+"-"+now.getYear()+",00:00:00";
                    DateFormat formatter = new SimpleDateFormat("d-M-yyyy,HH:mm:ss");
                    Date d = formatter.parse(strDate);
                    try {
                        timesList.clear();
                        timesList.addAll(hr.getActivitiesByTime(Long.toString(d.getTime()), Long.toString(System.currentTimeMillis())));
                        txtaTime.setText(timeString());
                    } catch (IOException ex) {
                        Logger.getLogger(FXMLMainWindowController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                }
                case "Last Month":{
                   
                }
                case "This Year":{
                    LocalDate now = LocalDate.now();
                    String strDate = "01-01-"+now.getYear()+",00:00:00";
                    DateFormat formatter = new SimpleDateFormat("d-M-yyyy,HH:mm:ss");
                    Date d = formatter.parse(strDate);
                    try {
                        timesList.clear();
                        timesList.addAll(hr.getActivitiesByTime(Long.toString(d.getTime()), Long.toString(System.currentTimeMillis())));
                        txtaTime.setText(timeString());
                    } catch (IOException ex) {
                        Logger.getLogger(FXMLMainWindowController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                }
                case "All Time":{
                    try {
                        timesList.clear();
                        timesList.addAll(hr.getActivitites());
                        txtaTime.setText(timeString());
                    } catch (IOException ex) {
                        Logger.getLogger(FXMLMainWindowController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                }
            }
        } else {
            //get with filter as user name
            try {
                timesList.clear();
                timesList.addAll(hr.getActivitiesByUser(filter));
                txtaTime.setText(timeString());
            } catch (IOException ex) {
                Logger.getLogger(FXMLMainWindowController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    @FXML
    public void editWindow() throws IOException{
        j = job;
        Parent root;
        root = FXMLLoader.load(getClass().getResource("FXMLEditJob.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Add Client");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    @FXML
    public void editActivity() throws IOException{
        a = (Activity)timesTable.getSelectionModel().getSelectedItem();
        if(!a.equals(null)){//put something in here so that doesnt throw an error
            Parent root = FXMLLoader.load(getClass().getResource("FXMLActivityEdit.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Edit Activity");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        
    }
    
    @FXML
    public void deleteTime() {
        Activity a = timesTable.getSelectionModel().getSelectedItem();
        if(a != null){
            try {
                if (hr.deleteActivity(a) == 200) {timesList.remove(a);}
            } catch (IOException ex) {
                Logger.getLogger(FXMLJobViewController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        id = FXMLMainWindowController.getId();
        hr = new HttpRequests();
        try {
            job = hr.getJob(id);
        } catch (IOException ex) {
            Logger.getLogger(FXMLJobViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        HBox.setHgrow(spacer, Priority.ALWAYS);
        //set labels
        lblTitle.setText(job.getTitle());
        lblClient.setText("For: "+job.getClient());
        lblStartDate.setText(job.getStartDate());
        if(!job.getInvNum().equals("null")){
            lblInvNum.setText(job.getInvNum());
        } else {
            lblInvNum.setText("");

        }
        txtaDescr.setText(job.getDescription());
        
        usernames.clear();
        try {
                ArrayList<User> userslist = hr.getUsers();
                userslist.forEach((u) -> {
                    usernames.add(u.getUsername());
                });
            } catch (IOException ex) {
                Logger.getLogger(FXMLMainWindowController.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        cbSort.getItems().addAll("Time", "User");
        cbSort.getSelectionModel().selectFirst();
        cbFilter.getItems().addAll("Today", "This Month", "This Year", "All Time");
        cbFilter.getSelectionModel().selectFirst();
        
        cbSort.getSelectionModel().selectedItemProperty().addListener(new
                    ChangeListener<String>() {
                        @Override
                        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                            switch(newValue){
                                case "Time":{
                                    cbFilter.getItems().clear();
                                    cbFilter.getItems().addAll("Today", "This Month", "This Year", "All Time");
                                    cbFilter.getSelectionModel().selectFirst();
                                    break;
                                }
                                case "User":{
                                    cbFilter.getItems().clear();
                                    cbFilter.getItems().addAll(usernames);
                                    cbFilter.getSelectionModel().selectFirst();
                                    break;
                                }
                            }
                        }
                    });
        
        timesList = FXCollections.observableArrayList(job.getActivities());
        clmDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        clmTime.setCellValueFactory(new PropertyValueFactory<>("time"));
        clmStartTime.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        clmEndTime.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        clmUser.setCellValueFactory(new PropertyValueFactory<>("username"));
        clmComment.setCellValueFactory(new PropertyValueFactory<>("comment"));
        timesTable.setItems(timesList);
        Label lblNoActivities = new Label("No times recorded for this job");
        timesTable.setPlaceholder(lblNoActivities);
        
        
        imgStart = new Image(getClass().getResourceAsStream("/icons/start.png"));
        imgStop = new Image(getClass().getResourceAsStream("/icons/stop.png"));
        Image imgEdit = new Image(getClass().getResourceAsStream("/icons/edit.png")); 
        Image imgSearch = new Image(getClass().getResourceAsStream("/icons/search.png"));
        Image imgDelete = new Image(getClass().getResourceAsStream("/icons/delete.png"));
        Image imgTimeEdit = new Image(getClass().getResourceAsStream("/icons/time_adjust.png"));
        
        btnStart.setGraphic(new ImageView(imgStart));
        btnEdit.setGraphic(new ImageView(imgEdit));
        btnSearch.setGraphic(new ImageView(imgSearch));
        btnDelete.setGraphic(new ImageView(imgDelete));
        btnEditActivity.setGraphic(new ImageView(imgTimeEdit));
        try {
            search();
        } catch (ParseException ex) {
            Logger.getLogger(FXMLJobViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static Job getjob(){
        return j;
    }
    
     public static Activity getCurrentActivity(){
        return a;
    }
    
    private String timeString() throws IOException{
        String out[] = new String[usernames.size()+1];
        out[0] = "Time: "+calcTime()+" mins";
        int index = 1;
        for (String u : usernames) {
            out[index]=u+": "+calcTimeUser(u)+" mins";
            index++;
        }
        return arrayToString(out);
    }
     
    private int calcTime(){
        int time = 0;
        Iterator i = timesList.iterator();
        while(i.hasNext()){
           Activity a = (Activity)(i.next());
           time += a.calcTime();
        }
        return time;
    }
    
    private int calcTimeUser(String username){
        int time = 0;
        Iterator i = timesList.iterator();
        while(i.hasNext()){
           Activity act = (Activity)(i.next());
           if(act.getUsername().equals(username)){
            time += act.calcTime();
           }
        }
        return time;
    }
    
    private String arrayToString(String[] s){
        String out = "";
        for(String b: s){
            out += b+"\n";
        }
        return out;
    }
}
