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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import javafx.scene.input.MouseEvent;
import model.*;

/**
 * FXML Controller class
 *
 * @author Juan, Dani, Nando
 */
public class FXMLreservasController implements Initializable {

    @FXML
    private Label nickname;
    @FXML
    private Button eliminarButton;
    @FXML
    private Button volverButton;
    @FXML
    private ListView<String> listaReservas;
    private ArrayList<Booking> misreservas;
    private ObservableList<String> reservas;
    private Club club;
    private String username;
    private static List<Court> courts;
    private int omitidas = 0;
    @FXML
    private Button pagarButton;
    public FXMLreservasController() throws ClubDAOException, IOException {
        this.club = Club.getInstance();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        username = FXMLmainController.getUser().getNickName();
        courts = club.getCourts();
        vistaReservas();
        nickname.setText(FXMLmainController.getUser().getName());
        
    }    

    @FXML
    private void eliminarOnAction(ActionEvent event) {
        try {
            club.removeBooking(misreservas.get(listaReservas.getSelectionModel().getSelectedIndex()+omitidas));
        } catch(ClubDAOException e) {System.out.println("mmmm");}
        reservas.remove(listaReservas.getSelectionModel().getSelectedIndex());
        vistaReservas();
        listaOnMousePressed();
    }

    @FXML
    private void volverOnAction(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
    
    private int indicePista(Court pista){
        for(int i = 0; i < courts.size(); i++){
            if(pista.equals(courts.get(i))) return i+1;
        }
        return 0;
    }
    
    private void vistaReservas() {
            misreservas = new ArrayList<Booking>(club.getUserBookings(username));
            List<String> reservasString = new ArrayList<String>();
            omitidas = 0;
            int vistas = 0;
            for(int i = 0; i < misreservas.size() && vistas<10; i++) {
                        if(!misreservas.get(i).getMadeForDay().isBefore(LocalDate.now())) {
                                    String res = "Fecha: " + misreservas.get(i).getMadeForDay() + ", hora: " + misreservas.get(i).getFromTime() + ", pista: " + indicePista(misreservas.get(i).getCourt());
                                    if(misreservas.get(i).getPaid()) {
                                        res = res.concat(", pagado");
                                    } else {
                                        res = res.concat(", pendiente de pago");
                                    }
                                    reservasString.add(res);
                                    vistas++; 
                        } else {
                                    omitidas++;
                        }
        }
        reservas = FXCollections.observableArrayList(reservasString);
        listaReservas.setItems(reservas);
    }
                        //misreservas.get(listaReservas.getSelectionModel().getSelectedIndex()+omitidas)

    @FXML
    private void listaOnMousePressed() {
        if (misreservas.get(listaReservas.getSelectionModel().getSelectedIndex()+omitidas).getMadeForDay().equals(LocalDate.now())
              || (misreservas.get(listaReservas.getSelectionModel().getSelectedIndex()+omitidas).getMadeForDay().minusDays(1).equals(LocalDate.now())
              && misreservas.get(listaReservas.getSelectionModel().getSelectedIndex()+omitidas).getFromTime().compareTo(LocalTime.now())< 0)) {
                
                        eliminarButton.setDisable(true);
        } else eliminarButton.setDisable(false);
        
       
        pagarButton.setDisable(misreservas.get(listaReservas.getSelectionModel().getSelectedIndex()+omitidas).getPaid()
        || !club.hasCreditCard(username));
        
    }

    @FXML
    private void pagarOnAction(ActionEvent event) throws ClubDAOException {
        
        misreservas.get(listaReservas.getSelectionModel().getSelectedIndex()+omitidas).setPaid(true);
        Booking aux = misreservas.get(listaReservas.getSelectionModel().getSelectedIndex()+omitidas);
        club.removeBooking(misreservas.get(listaReservas.getSelectionModel().getSelectedIndex()+omitidas));
        club.registerBooking(aux.getBookingDate(), aux.getMadeForDay(),aux.getFromTime(),aux.getPaid(),aux.getCourt(), aux.getMember());
        
        vistaReservas();
        
    }
}
