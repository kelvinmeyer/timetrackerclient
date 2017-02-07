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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
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
public class FXMLEditJobController implements Initializable {

    @FXML
    private Button btnEdit;
    @FXML
    private Button btnCancel;
    @FXML
    private TextField txtTitle;
    @FXML
    private TextArea txtaDescription;
    @FXML
    private TextField txtInvNum;
    
    private Job job;
    
    
    @FXML
    private void cancel(){
        Stage stage = (Stage)btnCancel.getScene().getWindow();
        stage.close();
    }
    
    @FXML
    private void update(){
        job.setTitle(txtTitle.getText());
        job.setDescription(txtaDescription.getText());
        job.setInvNum(txtInvNum.getText());
        HttpRequests hr = new HttpRequests();
        try {
            hr.patchjob(job);
        } catch (IOException ex) {
            Logger.getLogger(FXMLEditJobController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Stage stage = (Stage)btnCancel.getScene().getWindow();
        stage.close();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        job = FXMLJobViewController.getjob();
        
        //set vals
        txtTitle.setText(job.getTitle());
        txtInvNum.setText(job.getInvNum());
        txtaDescription.setText(job.getDescription());
        
        
        Image imgEdit = new Image(getClass().getResourceAsStream("/icons/edit.png"));
        Image imgCancel = new Image(getClass().getResourceAsStream("/icons/cancel.png"));
        
        btnEdit.setGraphic(new ImageView(imgEdit));
        btnCancel.setGraphic(new ImageView(imgCancel));
    }    
    
}
