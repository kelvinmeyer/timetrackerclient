package timetrackclient;

import dataStructures.Client;
import java.io.IOException;
import java.net.URL;

import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Hobbes
 */
public class FXMLAddClientController implements Initializable {

    @FXML
    private Button btnAddClient;
    @FXML
    private Button btnCancel;
    @FXML
    private TextField txtName;
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
            String temp = txtName.getText();
            //http add here so i can handel any errors i.e 201, 400, 401
            HttpRequests hr = new HttpRequests();
            String[] ret = hr.addClient(temp);
            int status = Integer.parseInt(ret[0]);
            switch(status){
                case 201:{ //no problem
                    FXMLMainWindowController.addClientData(new Client(getID(ret[1]), temp));
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
        Image imgAdd = new Image(getClass().getResourceAsStream("/icons/add.png"));
        Image imgCancel = new Image(getClass().getResourceAsStream("/icons/cancel.png"));
        btnAddClient.setGraphic(new ImageView(imgAdd));
        btnCancel.setGraphic(new ImageView(imgCancel));
    }    
    
    private String getID(String in){
        return in.substring(in.lastIndexOf("/")+1);
    }
}
