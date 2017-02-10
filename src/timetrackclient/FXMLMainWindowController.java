package timetrackclient;

import dataStructures.Activity;
import dataStructures.Client;
import dataStructures.Job;
import dataStructures.User;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.net.URL;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.time.LocalDate;

import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.geometry.Orientation;

import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javax.swing.JOptionPane;



/**
 * FXML Controller class
 *
 * @author Hobbes
 */
public class FXMLMainWindowController implements Initializable {
    
    //http requester
    private HttpRequests hr;
    
    //Jobs tab
    @FXML
    private FlowPane fp;
    @FXML
    private Button btnAddJob;
    @FXML
    private Button btnRefresh;
    private static String focusJobId;
    @FXML
    private Button btnReOpen;
    
    //times tab
    @FXML
    private ChoiceBox cbSort;
    @FXML
    private ChoiceBox cbFilter;
    @FXML
    private HBox spacer;
    @FXML
    private Button btnSearch;
    @FXML
    private TableView<Activity> timesTable;
    @FXML
    private TableColumn clmDate;
    @FXML
    private TableColumn clmTime;
    @FXML
    private TableColumn clmUser;
    @FXML
    private TableColumn clmStartTime;
    @FXML
    private TableColumn clmEndTime;
    @FXML
    private TableColumn clmComment;
    private ObservableList<Activity> timesList;
    
    //Clients tab
    @FXML
    private ListView clients;
    @FXML
    private Button btnAddClient;
    @FXML
    private Button btnDeleteClient;
    @FXML
    private Button btnImportClients;
    
    //Users tab
    @FXML
    private ListView users;
    @FXML
    private Button btnAddUser;
    @FXML
    private Button btnDeleteUser;
    
    //Settings tab
    private static SettingsManger sm;
    @FXML
    private CheckBox cbRemeber;
    @FXML
    private TextField txtHost;
    @FXML
    private TextField txtPort;
    @FXML
    private Button btnChangePassword;
    @FXML
    private Button btnSaveSettings;
    @FXML
    private Label lblVersion;
    
    @FXML
    private void addUser() throws IOException{
        Parent root;
        root = FXMLLoader.load(getClass().getResource("FXMLAddUser.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Add User");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        //main window controller send the data
    }
    
    @FXML
    private void addJob() throws IOException{
        Parent root;
        root = FXMLLoader.load(getClass().getResource("FXMLAddJob.fxml"));
        Stage stage = new Stage();
        stage.setTitle("New Job");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        stage.setOnHiding((WindowEvent we) -> {
            refresh();
        });   
    }
    
    
    @FXML
    private void deleteUser(){
        User a = (User)users.getSelectionModel().getSelectedItem();
        try {
            int status = hr.deleteUser(a);
//            if(status == 200){
//                hr.removeUserCache(a);
//            }
            // TODO else with an error
        } catch (IOException ex) {
            System.out.println("well that didnt work");
        }
    }
    
    @FXML
    private void addClient() throws IOException{
        Parent root;
        root = FXMLLoader.load(getClass().getResource("FXMLAddClient.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Add Client");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    @FXML
    private void deleteClient() throws IOException{
        Client a = (Client)clients.getSelectionModel().getSelectedItem();
        try {
            int status = hr.deleteClient(a);
//            if(status == 200){
//                clientnames.remove(a);
//            }
            // TODO else with an error
        } catch (IOException ex) {
            System.out.println("well that didnt work");
        }
    }
    
    @FXML
    private void importClients(){
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File file = fileChooser.showOpenDialog(stage);
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = "";
            while((line = br.readLine()) != null){
                line = line.replaceAll("\"", "");
                if(Character.isDigit(line.charAt(0))){
                    line = line.substring(line.indexOf(",")+1);//cut the front
                    line = line.substring(0, line.indexOf(","));//cut the end
                    hr.addClient(line);
                }           
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FXMLMainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FXMLMainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    private void SaveSettuings(){
        //set the manger to values
        if(!cbRemeber.isSelected()){
            sm.setUsername("");
            sm.setPassword("");
            sm.setRemember(false);
        }
        else{
            sm.setRemember(true);
        }
        sm.setPort(txtPort.getText());
        sm.setServerAddrs(txtHost.getText());
        sm.writeFile();
    }
    
     @FXML
    private void changePassword(){
        String newPass = JOptionPane.showInputDialog("Enter new password");
        if(!newPass.isEmpty()){
            try {
                hr.changePassword(newPass);
            } catch (IOException ex) {
                Logger.getLogger(FXMLMainWindowController.class.getName()).log(Level.SEVERE, null, ex);
            }
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
                    } catch (IOException ex) {
                        Logger.getLogger(FXMLMainWindowController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                }
                case "All Time":{
                    try {
                        timesList.clear();
                        timesList.addAll(hr.getActivitites());
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
            } catch (IOException ex) {
                Logger.getLogger(FXMLMainWindowController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    @FXML
    private void reopenJob() throws IOException{
        Parent root;
        root = FXMLLoader.load(getClass().getResource("FXMLReactivate.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Activate job");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        stage.setOnHiding((WindowEvent we) -> {
            refresh();
        });     
    }
    
    @FXML
    private void refresh(){
        hr.refresh();
        refreshJobsView();
    }
    
    public void refreshJobsView(){
        fp.getChildren().clear();
        hr.getJobsCache().forEach((j) -> {
            addCard(fp, j);
        });
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        hr = new HttpRequests();
        try {
            //job init
            fp.setHgap(10);
            fp.setVgap(10);
            refreshJobsView();
            
            //times init
            HBox.setHgrow(spacer, Priority.ALWAYS);
            cbSort.getItems().addAll("Time", "User");
            cbSort.getSelectionModel().selectFirst();
            
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
                                    cbFilter.getItems().addAll(hr.getUsersCache());
                                    cbFilter.getSelectionModel().selectFirst();
                                    break;
                                }
                            }
                        }
                    });
            
            cbFilter.getItems().addAll("Today", "This Month", "This Year", "All Time");
            cbFilter.getSelectionModel().selectFirst();
            timesList = FXCollections.observableArrayList(hr.getActivitites());
            clmDate.setCellValueFactory(new PropertyValueFactory<>("date"));
            clmTime.setCellValueFactory(new PropertyValueFactory<>("time"));
            clmUser.setCellValueFactory(new PropertyValueFactory<>("username"));
            clmStartTime.setCellValueFactory(new PropertyValueFactory<>("startTime"));
            clmEndTime.setCellValueFactory(new PropertyValueFactory<>("endTime"));
            clmComment.setCellValueFactory(new PropertyValueFactory<>("comment"));
            timesTable.setItems(timesList);
            
            
            //client inti
            clients.setItems(hr.getClientsCache());
            
            //user init
            users.setItems(hr.getUsersCache());
            
            
            //settings init
            sm = new SettingsManger();
            cbRemeber.setSelected(sm.isRemember());
            txtHost.setText(sm.getServerAddrs());
            txtPort.setText(sm.getPort());
            lblVersion.setText("Version: "+Timetrackclient.VERSION_NUM);
            
            //button icons
            Image imgAdd     = new Image(getClass().getResourceAsStream("/icons/add.png"));
            Image imgDelete  = new Image(getClass().getResourceAsStream("/icons/delete.png"));
            Image imgImport  = new Image(getClass().getResourceAsStream("/icons/import.png"));
            Image imgSearch  = new Image(getClass().getResourceAsStream("/icons/search.png"));
            Image imgReopen  = new Image(getClass().getResourceAsStream("/icons/reopen.png"));
            Image imgRefresh = new Image(getClass().getResourceAsStream("/icons/refresh.png"));
            
            btnAddJob.setGraphic(new ImageView(imgAdd));
            btnReOpen.setGraphic(new ImageView(imgReopen));
            btnRefresh.setGraphic(new ImageView(imgRefresh));
            
            btnSearch.setGraphic(new ImageView(imgSearch));
            
            btnAddUser.setGraphic(new ImageView(imgAdd));
            btnDeleteUser.setGraphic(new ImageView(imgDelete));
            
            btnAddClient.setGraphic(new ImageView(imgAdd));
            btnDeleteClient.setGraphic(new ImageView(imgDelete));
            btnImportClients.setGraphic(new ImageView(imgImport));
        } catch (IOException ex) {
            Logger.getLogger(FXMLMainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    public void addCard(FlowPane fp, Job j){
        AnchorPane p = new AnchorPane();
        p.setMinSize(150, 150);
        p.setMaxSize(150, 150);
        
        p.getStyleClass().add("card");
        p.setId(j.getJobId());
        
        FlowPane f = new FlowPane(Orientation.VERTICAL);
        
        addAnchorPaneLabel(f, j.getTitle(), j.getClient().getName());
        addAnchorPaneLabel(f, j.getStartDate(), j.getJobTime());
        addAnchorPaneLabelDescr(f, j.getDescription());
        addAnchorPaneLabel(f, j.getInvNum(), "");
        p.getChildren().add(f);
        addAnchorPaneButton(p);
        
        fp.getChildren().add(p);
    }
    
    public void addAnchorPaneLabel(FlowPane p, String l1,String l2){
        AnchorPane apane = new AnchorPane();
        Label left = new Label(l1);
        Label right = new Label(l2);
        right.setMaxWidth(73);
        left.setMaxWidth(73);
        apane.getChildren().addAll(left , right);
        apane.setTopAnchor(left, 5.0);
        apane.setTopAnchor(right, 5.0);
        apane.setLeftAnchor(left, 5.0);
        apane.setRightAnchor(right, 5.0);
        apane.setMinWidth(150);
        apane.setMaxWidth(150);
        p.getChildren().add(apane);
    }
    
    public void addAnchorPaneLabelDescr(FlowPane p, String l1){
        AnchorPane apane = new AnchorPane();
        Label left = new Label(l1);
        left.setPrefWidth(148);
        apane.getChildren().add(left);
        apane.setTopAnchor(left, 5.0);
        apane.setLeftAnchor(left, 5.0);
        apane.setMinWidth(150);
        apane.setMaxWidth(150);
        p.getChildren().add(apane);
    }
    
    public void addAnchorPaneButton(AnchorPane p){
        AnchorPane apane = new AnchorPane();
        
        Button left = new Button();
        Image imgDone = new Image(getClass().getResourceAsStream("/icons/done.png"));
        Image imgView = new Image(getClass().getResourceAsStream("/icons/access_time.png"));
        left.setGraphic(new ImageView(imgView));
        
        left.setOnAction((ActionEvent event) -> {
            focusJobId = p.getId();
            Parent root;
            try {
                root = FXMLLoader.load(getClass().getResource("FXMLJobView.fxml"));
                Stage stage = new Stage();
                stage.setTitle("Job");
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException ex) {
                Logger.getLogger(FXMLMainWindowController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        Button right = new Button();
        
        right.setGraphic(new ImageView(imgDone));
        
        right.setOnAction((ActionEvent event) -> {
            try {
                // get id and change status on server
                hr.jobInactive(p.getId());
                //change local view maybe
            } catch (IOException ex) {
                Logger.getLogger(FXMLMainWindowController.class.getName()).log(Level.SEVERE, null, ex);
            }
            refresh();
        });
        
        apane.getChildren().addAll(left , right);
        apane.setTopAnchor(left, 5.0);
        apane.setTopAnchor(right, 5.0);
        apane.setLeftAnchor(left, 5.0);
        apane.setRightAnchor(right, 5.0);
        apane.setMinWidth(150);
        apane.setMaxWidth(150);
        p.getChildren().add(apane);
        p.setBottomAnchor(apane, 5.0);
    }
    
    public static String getId(){
       return focusJobId;
    }
}