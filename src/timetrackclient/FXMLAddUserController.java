/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timetrackclient;

import dataStructures.User;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Hobbes
 */
public class FXMLAddUserController implements Initializable {
    
    @FXML
    private Button btnAddUser;
    @FXML
    private Button btnCancel;
    @FXML
    private TextField txtUsername;
    @FXML
    private TextField txtName;
    @FXML
    private PasswordField ptxtPassword;
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
            User temp = new User(txtUsername.getText(), ptxtPassword.getText(),txtName.getText());
            //http add here so i can handel any errors i.e 201, 400, 401
            HttpRequests hr = new HttpRequests();
            int status = hr.addUser(temp);
            switch(status){
                case 201:{ //no problem
                    FXMLMainWindowController.addUserData(temp.getUsername());
                    Stage stage = (Stage)btnCancel.getScene().getWindow();
                    stage.close();
                    break;
                }
                case 401:{ //conflict
                    lblError.setText("A User with that name already exists!");
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
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Image imgAdd = new Image(getClass().getResourceAsStream("/icons/add.png"));
        Image imgCancel = new Image(getClass().getResourceAsStream("/icons/cancel.png"));
        btnAddUser.setGraphic(new ImageView(imgAdd));
        btnCancel.setGraphic(new ImageView(imgCancel));
    }    
    
}
