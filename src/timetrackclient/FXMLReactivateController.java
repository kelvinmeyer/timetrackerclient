/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timetrackclient;

import dataStructures.Job;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Hobbes
 */
public class FXMLReactivateController implements Initializable {
    
    @FXML
    private Button btnOpen;
    @FXML
    private Button btnCancel;
    @FXML
    private ListView jobsView;
    private ObservableList<Job> jobs = FXCollections.observableArrayList();
    
    private HttpRequests hr = new HttpRequests();
    
    @FXML
    private void open(){
        Job j = (Job)(jobsView.getSelectionModel().getSelectedItem());
        try {
            hr.jobActive(j.getJobId());
        } catch (IOException ex) {
            Logger.getLogger(FXMLReactivateController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Stage stage = (Stage)btnCancel.getScene().getWindow();
        stage.close();
    }
    
    @FXML 
    private void cancel(){
        Stage stage = (Stage)btnCancel.getScene().getWindow();
        stage.close();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            // populat the list
            jobs.addAll(hr.getInactiveJobs());
            jobsView.getItems().setAll(jobs);
        } catch (IOException ex) {
            Logger.getLogger(FXMLReactivateController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Image imgOpen = new Image(getClass().getResourceAsStream("/icons/reopen.png"));
        Image imgCancel = new Image(getClass().getResourceAsStream("/icons/cancel.png")); 
        
        btnOpen.setGraphic(new ImageView(imgOpen));
        btnCancel.setGraphic(new ImageView(imgCancel));
    }    
    
}
