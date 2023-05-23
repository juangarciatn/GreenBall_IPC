/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package reservas;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import main.FXMLmainController;
import model.*;

/**
 * FXML Controller class
 *
 * @author nando
 */
public class FXMLreservasController implements Initializable {

    @FXML
    private Label nickname;
    @FXML
    private Button eliminarButton;
    @FXML
    private Button volverButton;
    private ListView<Booking> listaReservas;
    private ArrayList<Booking> misreservas;
    private ObservableList<Booking> reservas;
    private Club club;
    private String username;
    public FXMLreservasController() throws ClubDAOException, IOException {
        this.club = Club.getInstance();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        username = FXMLmainController.getUser().getName();
        List<Booking> test = new ArrayList<Booking>();
        test = club.getUserBookings(username);
        misreservas = new ArrayList<Booking>(test);
        reservas = FXCollections.observableArrayList(misreservas);
        listaReservas.setItems(reservas);
        eliminarButton.disableProperty().bind(Bindings.equal(-1, listaReservas.getSelectionModel().selectedIndexProperty()));
        nickname.setText(username);
        nickname.setText(FXMLmainController.getUser().getName());
    }    

    @FXML
    private void eliminarOnAction(ActionEvent event) throws ClubDAOException {
        club.removeBooking(misreservas.get(listaReservas.getSelectionModel().getSelectedIndex()));
        reservas.remove(listaReservas.getSelectionModel().getSelectedIndex());
    }

    @FXML
    private void volverOnAction(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
    
}
