package timetrackclient;

import dataStructures.Activity;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class FXMLActivityEditController implements Initializable {

    @FXML
    private Button btnConfirm;
    @FXML
    private Button btnCancel;
    private Activity act;
    @FXML
    private TextField txtComment;
    @FXML
    private TextField txtAdj;
    private String type;
    
    
    @FXML
    private void cancel(){
        Stage stage = (Stage)btnCancel.getScene().getWindow();
        stage.close();
    }
    
    @FXML
    private void sumbit(){
        act.setComment(txtComment.getText());
        act.setAdj(Integer.parseInt(txtAdj.getText()));
        // patch here
        HttpRequests hr = new HttpRequests();
        try {
            hr.updateActivity(act);
        } catch (IOException ex) {
            Logger.getLogger(FXMLActivityEditController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Stage stage = (Stage)btnCancel.getScene().getWindow();
        stage.close();
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        act = FXMLJobViewController.getCurrentActivity();
        txtComment.setText(act.getComment());
        txtAdj.setText(act.getTimeAdjString());
        
        Image imgDone = new Image(getClass().getResourceAsStream("/icons/edit.png"));
        Image imgCancel = new Image(getClass().getResourceAsStream("/icons/cancel.png"));
       
        btnConfirm.setGraphic(new ImageView(imgDone));
        btnCancel.setGraphic(new ImageView(imgCancel));
        
    }    
    
}
