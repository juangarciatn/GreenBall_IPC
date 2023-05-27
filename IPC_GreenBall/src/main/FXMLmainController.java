/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package main;

import java.io.IOException;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Locale;
import java.util.Optional;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import login.FXMLloginController;
import profile.FXMLprofileController;
import reservas.FXMLreservasController;
import model.*;

/**
 * FXML Controller class
 *
 * @author Juan
 */
public class FXMLmainController implements Initializable {

    @FXML
    private Label labelUser;
    
    private static Member user;
    
    private final Club club;
    
    private static List<Court> courts;
    
    private boolean loggedIn = false;
    @FXML
    private DatePicker dpBookingDay;
    @FXML
    private Label labelCol;
    
    private final LocalTime firstSlotStart = LocalTime.of(9, 0);
    private final Duration slotLength = Duration.ofMinutes(60);
    private final LocalTime lastSlotStart = LocalTime.of(22, 0);
    
    private static final PseudoClass SELECTED_PSEUDO_CLASS = PseudoClass.getPseudoClass("selected");

    private List<TimeSlot> timeSlots = new ArrayList<>();

    private ObjectProperty<TimeSlot> timeSlotSelected;
    
    private LocalDate daySelected;
    
    
    @FXML
    private Label slotSelected;
    @FXML
    private GridPane grid;
    @FXML
    private MenuBar menuBar;
    @FXML
    private Menu menu;

    public FXMLmainController() throws ClubDAOException, IOException {
        this.club = Club.getInstance();
        courts = club.getCourts();
    }

   /** public FXMLmainController(Member m) throws ClubDAOException, IOException {
        this.user = m;
        this.club = Club.getInstance();
        courts = club.getCourts();
    } 
     */

    /**
     * Initializes the controller class.
     */
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        menuBar.setUseSystemMenuBar(false);

        menuBar.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            event.consume();
        });
        
        timeSlotSelected = new SimpleObjectProperty<>();
        
        dpBookingDay.valueProperty().addListener((a, b, c) -> {
            setTimeSlotsGrid(c);
            labelCol.setText(c.getDayOfWeek().getDisplayName(TextStyle.FULL_STANDALONE, Locale.getDefault()));
        });
        
        dpBookingDay.setValue(LocalDate.now()); 
        
        dpBookingDay.setDayCellFactory(picker -> new DateCell() {
        @Override
        public void updateItem(LocalDate date, boolean empty) {
            super.updateItem(date, empty);

            if (date.isBefore(LocalDate.now())) {
                setDisable(true);
                setStyle("-fx-background-color: #dddddd;"); // Cambia el color de fondo de las fechas deshabilitadas
            }
            }
        });
        
        dpBookingDay.setEditable(false);
        
        setTimeSlotsGrid(dpBookingDay.getValue());
        
        
        
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm");
        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("E MMM d");
        timeSlotSelected.addListener((a, b, c) -> {
            if (c == null) {
                slotSelected.setText("");
            } else {
                slotSelected.setText(c.getDate().format(dayFormatter)
                        + "-"
                        + c.getStart().format(timeFormatter)
                        + " pista "
                        + c.getPista());
            }
        });
    }    
    
    public void setTimeSlotsGrid(LocalDate date) {
        //actualizamos la seleccion
        timeSlotSelected.setValue(null);
        
        ObservableList<Node> children = grid.getChildren();
        for (TimeSlot timeSlot : timeSlots) {
            children.remove(timeSlot.getView());
        }
        timeSlots = new ArrayList<>();
        

        //----------------------------------------------------------------------------------
        // desde la hora de inicio y hasta la hora de fin creamos slotTime segun la duracion
        
        for (int row = 1; row<7;row++){
            List<Booking> reservas = club.getBookings();
            int slotIndex = 2;
            for (LocalDateTime startTime = date.atTime(firstSlotStart);
                    !startTime.isAfter(date.atTime(lastSlotStart));
                    startTime = startTime.plus(slotLength)) {
                    
                    
                    Court courtAt = courts.get(row-1);
                //---------------------------------------------------------------------------------------
                // creamos el SlotTime, lo anyadimos a la lista de la columna y asignamos sus manejadores
                TimeSlot timeSlot = new TimeSlot(startTime, slotLength, courtAt, row);
                timeSlots.add(timeSlot);
                
                ObservableList<String> styles = timeSlot.getView().getStyleClass();
                
                for(int iR = 0; iR < reservas.size(); iR++) {
                    if(timeSlot.getTime().equals(reservas.get(iR).getFromTime()) 
                            && timeSlot.getCourt().equals(reservas.get(iR).getCourt()) 
                            && timeSlot.getDate().equals(reservas.get(iR).getMadeForDay()) ) {
                        System.out.println("espacio asignado en " + row + " " + slotIndex); 
                        timeSlot.setUser(reservas.get(iR).getMember());
                        timeSlot.setReserva(reservas.get(iR));
                        styles.remove("time-slot");
                        if(user != null && user.equals(timeSlot.getUser())) {
                                    styles.add("time-slot-user");
                        } else {           
                                    styles.add("time-slot-ocupado");
                        }
                       
                    } 
                }
                registerHandlers(timeSlot);
                
                //-----------------------------------------------------------
                // lo anyadimos al grid en la posicion x= 1, y= slotIndex
                grid.add(timeSlot.getView(), row, slotIndex);
                
                if (timeSlot.getReserva()!= null && !timeSlot.getUser().equals(user)) {
                    Label label = new Label(timeSlot.getReserva().getMember().getNickName());
                    label.setDisable(true);
                    grid.add(label, row, slotIndex);
                }
                
                slotIndex++;
            }
        }
    }
    
    private void registerHandlers(TimeSlot timeSlot) {

            timeSlot.getView().setOnMousePressed((MouseEvent event) -> {
            
                        //---------------------------------------------slot----------------------------
                        //solamente puede estar seleccionado un slot dentro de la lista de slot
                        timeSlots.forEach(slot -> {
                                    slot.setSelected(slot == timeSlot);
                        });

                        //----------------------------------------------------------------
                        //actualizamos el label Dia-Hora, esto es ad hoc,  para mi diseño
            
                        timeSlotSelected.setValue(timeSlot);
                        //----------------------------------------------------------------
                        // si es un doubleClik  vamos a mostrar una alerta y cambiar el estilo de la celda
                        if (event.getClickCount() > 1 ) {
                                    ObservableList<String> styles = timeSlot.getView().getStyleClass();
                                    //sesión iniciada y casilla no tiene user, hacer reserva
                                    if(user != null && timeSlot.getUser() == null) {
                                                if(sePuedeReservar(timeSlot)) {
                                                            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
                                                            confirmacion.setTitle("Reserva");
                                                            confirmacion.setHeaderText("Confirma la reserva");
                                                            confirmacion.setContentText("Has seleccionado: "
                                                                        + timeSlot.getDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)) + ", "
                                                                        + timeSlot.getTime().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
                                                                        + ", Pista: " + timeSlot.getPista());
                                                            Optional<ButtonType> resultConfirmacion = confirmacion.showAndWait();
                                                            if (resultConfirmacion.isPresent() && resultConfirmacion.get() == ButtonType.OK) {
                                                                        styles.remove("time-slot");
                                                                        styles.add("time-slot-user");
                                                                        try{
                                                                                    timeSlot.setUser(user);
                                                                                    timeSlot.setReserva(club.registerBooking(timeSlot.getStart(), timeSlot.getDate(), timeSlot.getTime(), club.hasCreditCard(user.getNickName()), timeSlot.getCourt(), user ));
                                                                        } catch( ClubDAOException e) { System.out.println("Error al hacer reserva: " + e);}
                                                            }
                                                } else {
                                                    Alert alerta = new Alert(Alert.AlertType.ERROR);
                                                alerta.setTitle("Tiempo máximo excedido");
                                                alerta.setHeaderText("");
                                                alerta.setContentText("No puedes reservar más de dos horas seguidas");
                                                Optional<ButtonType> result = alerta.showAndWait();
                                                if (result.isPresent() && result.get() == ButtonType.OK) {}
                                                }
                                    //sesión iniciada y casilla coincide, cancelar reserva                        
                                    } else if(user != null && timeSlot.getUser() != null && timeSlot.getUser().equals(user)) {
                                                Alert cancelar = new Alert(Alert.AlertType.CONFIRMATION);
                                                cancelar.setTitle("Cancelar reserva");
                                                cancelar.setHeaderText("¿Estás seguro de querer cancelar la reserva?");
                                                cancelar.setContentText("Has seleccionado: "
                                                            + timeSlot.getDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)) + ", "
                                                            + timeSlot.getTime().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
                                                            + ", Pista: " + timeSlot.getPista());
                                                Optional<ButtonType> resultCancelar = cancelar.showAndWait();
                                                if (resultCancelar.isPresent() && resultCancelar.get() == ButtonType.OK) {
                                                            styles.remove("time-slot-user");
                                                            styles.add("time-slot");
                                                            try{
                                                                        club.removeBooking(timeSlot.getReserva());
                                                                        timeSlot.setReserva(null);
                                                                        timeSlot.setUser(null);
                                                            } catch( ClubDAOException e) { System.out.println("Error al cancelar reserva: " + e);}
                                                }
                                    //casilla tiene user, ver reserva
                                    } else if(timeSlot.getUser() != null) {
                                                Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
                                                alerta.setTitle("Ver reserva");
                                                alerta.setHeaderText("Esta pista ha sido reservada por: " + timeSlot.getUser().getNickName());
                                                alerta.setContentText("Has seleccionado: "
                                                            + timeSlot.getDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)) + ", "
                                                            + timeSlot.getTime().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
                                                            + ", Pista: " + timeSlot.getPista());
                                                Optional<ButtonType> result = alerta.showAndWait();
                                                if (result.isPresent() && result.get() == ButtonType.OK) {}
                                    }        
                        }
            });
    }

    @FXML
    private void menuOnMouseEntered(MouseEvent event) {
        if (loggedIn) menu.show();
        else menu.hide();
    }


    @FXML
    private void miPerfilButton(ActionEvent event) throws IOException {
        FXMLLoader miCargador = new FXMLLoader(getClass().getResource("/profile/FXMLprofile.fxml"));
            Parent root = miCargador.load();
            // acceso al controlador de datos persona
            FXMLprofileController controladorLogin = miCargador.getController();
            Scene scene = new Scene(root,500,300);  
            Stage stagePerfil = new Stage();
            stagePerfil.setScene(scene);
            stagePerfil.initStyle(StageStyle.UNDECORATED);
            stagePerfil.setTitle("Perfil");
            stagePerfil.initModality(Modality.APPLICATION_MODAL);
            stagePerfil.showAndWait();
    }

    @FXML
    private void misReservasButton(ActionEvent event) throws IOException {
        FXMLLoader miCargador = new FXMLLoader(getClass().getResource("/reservas/FXMLreservas.fxml"));
            Parent root = miCargador.load();
            // acceso al controlador de datos persona
            FXMLreservasController controladorLogin = miCargador.getController();
            Scene scene = new Scene(root,620,421);
            Stage stageReservas = new Stage();
            stageReservas.setScene(scene);
            stageReservas.initStyle(StageStyle.UNDECORATED);
            stageReservas.setTitle("Mis reservas");
            stageReservas.initModality(Modality.APPLICATION_MODAL);
            stageReservas.showAndWait();
            setTimeSlotsGrid(dpBookingDay.getValue());
    }

    @FXML
    private void cerrarSesionButton(ActionEvent event) {
        this.loggedIn = false;
        labelUser.setText("Iniciar sesión");
        this.user = null;
        FXMLloginController.setIsOk(false);
        setTimeSlotsGrid(dpBookingDay.getValue());
    }

    private void menuOnMouseExited(MouseEvent event) {
        menu.setDisable(false);
    }

    private void menuOnMouseClicked(MouseEvent event) {
        menu.setDisable(true);
    }

    
    public class TimeSlot {

        private final LocalDateTime start;
        private final Duration duration;
        protected final Pane view;
        private final Court court;
        private final int pista;
        private Member user = null;
        private Booking reserva;
        private int turno;

        private final BooleanProperty selected = new SimpleBooleanProperty();

        public final BooleanProperty selectedProperty() {
            return selected;
        }

        public final boolean isSelected() {
            return selectedProperty().get();
        }

        public final void setSelected(boolean selected) {
            selectedProperty().set(selected);
        }

        public TimeSlot(LocalDateTime start, Duration duration, Court court, int pista) {
            this.start = start;
            this.duration = duration;
            this.court = court;
            this.pista = pista;
            this.turno = start.getHour()-8;
            view = new Pane();
            view.getStyleClass().add("time-slot");
            // ---------------------------------------------------------------
            // de esta manera cambiamos la apariencia del TimeSlot cuando los seleccionamos
            selectedProperty().addListener((obs, wasSelected, isSelected)
                    -> view.pseudoClassStateChanged(SELECTED_PSEUDO_CLASS, isSelected));

        }

        public LocalDateTime getStart() {
            return start;
        }

        public LocalTime getTime() {
            return start.toLocalTime();
        }

        public LocalDate getDate() {
            return start.toLocalDate();
        }

        public DayOfWeek getDayOfWeek() {
            return start.getDayOfWeek();
        }

        public Duration getDuration() {
            return duration;
        }
        
        public Court getCourt() {
            return court;
        }
        
        public int getTurno() {
            return turno;
        }
        
        public int getPista() {
            return pista;
        }
        
        public Member getUser() {
            return user;
        }
        
        public void setUser(Member member) {
            this.user = member;
        }
        
        public Booking getReserva() {
             return reserva;
        }
        
        public void setReserva(Booking reserva) {
            this.reserva = reserva;
        }

        public Node getView() {
            return view;
        }
    }
    

    @FXML
    private void labelUserClick(MouseEvent event) throws IOException, ClubDAOException {
        if (!loggedIn) {
            FXMLLoader miCargador = new FXMLLoader(getClass().getResource("/login/FXMLlogin.fxml"));
            Parent root = miCargador.load();
            // acceso al controlador de datos persona
            FXMLloginController controladorLogin = miCargador.getController();
            Scene scene = new Scene(root,400,200);
            Stage stageLogin = new Stage();
            stageLogin.setScene(scene);
            stageLogin.initStyle(StageStyle.UNDECORATED);
            stageLogin.setTitle("Iniciar sesión");
            stageLogin.setMinHeight(200);
            stageLogin.setMinWidth(400);
            stageLogin.initModality(Modality.APPLICATION_MODAL);
            stageLogin.showAndWait();
            userChange();
        }
    }
    
    public void userChange() throws ClubDAOException, IOException {
        if (FXMLloginController.isOk()){
            // labelUser.setText("Hola, " + user);
            user = club.getMemberByCredentials(FXMLloginController.getUsername(), FXMLloginController.getPassword());
            labelUser.setText("Hola, " + user.getNickName());
            loggedIn = true;
            setTimeSlotsGrid(dpBookingDay.getValue());
        }
    }
    
   
    public static Member getUser() {
        return user;
    }
    
    public boolean sePuedeReservar(TimeSlot timeSlot) {
            int ind = timeSlot.getTurno()-1 + (timeSlot.getPista()-1)*14;
            
            System.out.println(ind);
            System.out.println(timeSlots.get(ind).getTime().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)) + ", Pista: " + timeSlots.get(ind).getPista());
            if(ind == 0) {//si es el primer elemento
                        if(timeSlots.get(ind + 1).getUser() != null && timeSlots.get(ind + 2).getUser() != null && timeSlots.get(ind + 1).getUser() == user && timeSlots.get(ind + 2).getUser() == user) return false;
            } else if(ind == 77) { //si es el último elemento
                        if(timeSlots.get(ind - 1).getUser() != null && timeSlots.get(ind - 2).getUser() != null && timeSlots.get(ind - 1).getUser() == user && timeSlots.get(ind - 2).getUser() == user) return false;
            } else if(ind == 1) {
                        if(timeSlots.get(ind + 1).getUser() != null && timeSlots.get(ind + 2).getUser() != null && timeSlots.get(ind + 1).getUser() == user && timeSlots.get(ind + 2).getUser() == user
                                || timeSlots.get(ind + 1).getUser() != null && timeSlots.get(ind - 1).getUser() != null && timeSlots.get(ind + 1).getUser() == user && timeSlots.get(ind - 1).getUser() == user) return false;
            }else if(
                    timeSlots.get(ind + 1).getUser() != null && timeSlots.get(ind + 2).getUser() != null && timeSlots.get(ind + 1).getUser() == user && timeSlots.get(ind + 2).getUser() == user
                    || timeSlots.get(ind - 1).getUser() != null && timeSlots.get(ind - 2).getUser() != null && timeSlots.get(ind - 1).getUser() == user && timeSlots.get(ind - 2).getUser() == user
                    || timeSlots.get(ind + 1).getUser() != null && timeSlots.get(ind - 1).getUser() != null && timeSlots.get(ind + 1).getUser() == user && timeSlots.get(ind - 1).getUser() == user) {
                        return false;
            }
            
            return true;
    }
}
