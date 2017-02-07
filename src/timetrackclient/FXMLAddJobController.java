/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timetrackclient;

import dataStructures.Client;
import dataStructures.Job;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Hobbes
 */
public class FXMLAddJobController implements Initializable {

    @FXML
    private Button btnAdd;
    @FXML
    private Button btnCancel;
    @FXML
    private TextField txtTitle;
    @FXML
    private TextArea txtaDescription;
    @FXML
    private ComboBox cbClients;
    @FXML
    private TextField txtInvNum;
    @FXML 
    private Label lblError;
    
    @FXML
    public void close(){
        Stage stage = (Stage)btnCancel.getScene().getWindow();
        stage.close();
    }
    
    @FXML
    public void add(){
        try {
            String title = txtTitle.getText();
            String desr = txtaDescription.getText();
            String client = ((Client)cbClients.getValue()).getId();
            String inv = txtInvNum.getText();
            String stat = "True";
            Date curDate = new Date();
            SimpleDateFormat d = new SimpleDateFormat("dd/MM/yyyy");
            String date = d.format(curDate);
            
            //http add here so i can handel any errors i.e 201, 400, 401
            HttpRequests hr = new HttpRequests();
            String[] ret = hr.addJob(title, client, stat, date, desr, inv);
            
            int status = Integer.parseInt(ret[0]);
            switch(status){
                case 201:{ //no problem
                    FXMLMainWindowController.addJobData(new Job(getID(ret[1]), title, client, stat, date, desr, inv));
                    Stage stage = (Stage)btnCancel.getScene().getWindow();
                    stage.close();
                    break;
                }
                case 400:{ //malformed or other problem
                    lblError.setText("The data you submitted was incorrect!");
                    break;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(FXMLAddUserController.class.getName()).log(Level.SEVERE, null, ex);
        }    
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // populate the comobo box
        HttpRequests hr = new HttpRequests();
        cbClients.setItems(hr.getClientsCache());
       
        
        
        // adding images
        Image imgAdd = new Image(getClass().getResourceAsStream("/icons/add.png"));
        Image imgCancel = new Image(getClass().getResourceAsStream("/icons/cancel.png"));
        
        btnAdd.setGraphic(new ImageView(imgAdd));
        btnCancel.setGraphic(new ImageView(imgCancel));
    } 
    
    private String getID(String in){
        return in.substring(in.lastIndexOf("/")+1);
    }
    
}
