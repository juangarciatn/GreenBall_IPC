/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package profile;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import main.FXMLmainController;
import model.Club;
import model.ClubDAOException;

/**
 * FXML Controller class
 *
 * @author Juan
 */
public class FXMLprofileController implements Initializable {

    @FXML
    private ImageView labelPicture;
    @FXML
    private Label labelUsername;
    @FXML
    private Label labelName;
    @FXML
    private Label labelSurname;
    
    private Club club;
    
    public FXMLprofileController() throws ClubDAOException, IOException {
        this.club = Club.getInstance();
        getUser();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void getUser() {
//        if (FXMLmainController.getUser().getImage() != null) labelPicture.setImage(FXMLmainController.getUser().getImage());
        this.labelUsername.setText(FXMLmainController.getUser().getNickName());
        this.labelName.setText(FXMLmainController.getUser().getName());
        this.labelSurname.setText(FXMLmainController.getUser().getSurname());
        
    }
}
