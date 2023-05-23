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
    
    private void setTimeSlotsGrid(LocalDate date) {
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
            int slotIndex = 3;
            for (LocalDateTime startTime = date.atTime(firstSlotStart);
                    !startTime.isAfter(date.atTime(lastSlotStart));
                    startTime = startTime.plus(slotLength)) {
                        
                    Court courtAt = courts.get(row-1);
                //---------------------------------------------------------------------------------------
                // creamos el SlotTime, lo anyadimos a la lista de la columna y asignamos sus manejadores
                TimeSlot timeSlot = new TimeSlot(startTime, slotLength, courtAt, row, user);
                timeSlots.add(timeSlot);
                registerHandlers(timeSlot);
                //-----------------------------------------------------------
                // lo anyadimos al grid en la posicion x= 1, y= slotIndex
                grid.add(timeSlot.getView(), row, slotIndex);
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
                                    if(user != null) {
                                                if(styles.contains("time-slot")) {
                                                            Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
                                                            alerta.setTitle("Reserva");
                                                            alerta.setHeaderText("Confirma la reserva");
                                                            alerta.setContentText("Has seleccionado: "
                                                                        + timeSlot.getDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)) + ", "
                                                                        + timeSlot.getTime().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
                                                                        + ", Pista: " + timeSlot.getPista());
                                                            Optional<ButtonType> result = alerta.showAndWait();
                                                            if (result.isPresent() && result.get() == ButtonType.OK) {
                                                                        styles.remove("time-slot");
                                                                        styles.add("time-slot-libre");
                                                            }
                                                } else {
                                                            Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
                                                            alerta.setTitle("Cancelar reserva");
                                                            alerta.setHeaderText("¿Estás seguro de que quieres cancelar la reserva?");
                                                            alerta.setContentText("Has seleccionado: "
                                                                        + timeSlot.getDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)) + ", "
                                                                        + timeSlot.getTime().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
                                                                        + ", Pista: " + timeSlot.getPista());
                                                            Optional<ButtonType> result = alerta.showAndWait();
                                                            if (result.isPresent() && result.get() == ButtonType.OK) {
                                                                        styles.remove("time-slot-libre");
                                                                        styles.add("time-slot");
                                                            }
                                                }
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
            Stage stageLogin = new Stage();
            stageLogin.setScene(scene);
            stageLogin.setTitle("Perfil");
            stageLogin.initModality(Modality.APPLICATION_MODAL);
            stageLogin.showAndWait();
    }

    @FXML
    private void misReservasButton(ActionEvent event) throws IOException {
        FXMLLoader miCargador = new FXMLLoader(getClass().getResource("/reservas/FXMLreservas.fxml"));
            Parent root = miCargador.load();
            // acceso al controlador de datos persona
            FXMLreservasController controladorLogin = miCargador.getController();
            Scene scene = new Scene(root,500,300);
            Stage stageReservas = new Stage();
            stageReservas.setScene(scene);
            stageReservas.setTitle("Mis reservas");
            stageReservas.initModality(Modality.APPLICATION_MODAL);
            stageReservas.showAndWait();
    }

    @FXML
    private void cerrarSesionButton(ActionEvent event) {
        this.loggedIn = false;
        labelUser.setText("Iniciar sesión");
        this.user = null;
        FXMLloginController.setIsOk(false);
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
        private final Member user;

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

        public TimeSlot(LocalDateTime start, Duration duration, Court court, int pista, Member user) {
            this.start = start;
            this.duration = duration;
            this.court = court;
            this.pista = pista;
            this.user = user;
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
        
        public int getPista() {
            return pista;
        }
        
        public Member getUser() {
            return user;
        }

        public Node getView() {
            return view;
        }
    }
    

    @FXML
    private void labelUserClick(MouseEvent event) throws IOException, ClubDAOException {
        if (loggedIn) {
            FXMLLoader miCargador = new FXMLLoader(getClass().getResource("/profile/FXMLprofile.fxml"));
            Parent root = miCargador.load();
            // acceso al controlador de datos persona
            FXMLprofileController controladorLogin = miCargador.getController();
            Scene scene = new Scene(root,500,300);
            Stage stageLogin = new Stage();
            stageLogin.setScene(scene);
            stageLogin.setTitle("Perfil");
            stageLogin.setMinHeight(400);
            stageLogin.setMinWidth(500);
            stageLogin.initModality(Modality.WINDOW_MODAL);
            stageLogin.showAndWait();
        } else {
            FXMLLoader miCargador = new FXMLLoader(getClass().getResource("/login/FXMLlogin.fxml"));
            Parent root = miCargador.load();
            // acceso al controlador de datos persona
            FXMLloginController controladorLogin = miCargador.getController();
            Scene scene = new Scene(root,400,200);
            Stage stageLogin = new Stage();
            stageLogin.setScene(scene);
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
        }
    }
    
   
    public static Member getUser() {
        return user;
    }
    
}
