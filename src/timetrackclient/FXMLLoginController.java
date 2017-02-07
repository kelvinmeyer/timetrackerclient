/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timetrackclient;

import dataStructures.User;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 *
 * @author Hobbes
 */
public class FXMLLoginController implements Initializable {
    
    @FXML
    private Label lblError;
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnLogin;
    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField ptxtPassword;
    @FXML
    private CheckBox cbRemember;
    
    private HttpRequests hr;
    private static SettingsManger sm;
    
    @FXML
    private void Cancel(){
        System.exit(0);
    }
    
    @FXML
    private void Login() throws IOException{
        try{
            boolean login = hr.login(new User(txtUsername.getText(), ptxtPassword.getText()));
            if(login){
                //if remeber write to file
                if(cbRemember.isSelected()){
                    sm.setUsername(txtUsername.getText());
                    sm.setPassword(ptxtPassword.getText());
                    sm.setRemember(true);
                    sm.writeFile();
                }
                //changing the scene
                Stage stage;
                Parent root;

                stage = (Stage)btnLogin.getScene().getWindow();
                root = FXMLLoader.load(getClass().getResource("FXMLMainWindow.fxml"));
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle("Time Tracker");
                stage.show();
            }else{
                //show error
                lblError.setVisible(true);
            }
        } catch (ConnectException ex) {
            lblError.setText("Server not found!");
            lblError.setVisible(true);
        }
        
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        sm = new SettingsManger();
        hr = new HttpRequests();
        cbRemember.setSelected(sm.isRemember());
        if(sm.isRemember()){
            txtUsername.setText(sm.getUsername());
            ptxtPassword.setText(sm.getPassword());
       }
    }
    
    public static String getAddress(){
        return sm.getAddres();
    }
    
}
