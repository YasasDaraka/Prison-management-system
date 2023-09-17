package lk.ijse.prison.controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import lk.ijse.prison.bo.BoFactory;
import lk.ijse.prison.bo.coustom.DashboardBO;
import lombok.SneakyThrows;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML
    private AnchorPane dashbord;
    @FXML
    private Label prisoners;
    @FXML
    private Label jailers;
    @FXML
    private Label staff;
    @FXML
    private Label date;
    @FXML
    private Label time;

    DashboardBO dashboardBO = BoFactory.getBoFactory().getBO(BoFactory.BOTypes.DASHBOARD);

    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        prisoners.setText(dashboardBO.getPriCount());
        jailers.setText(dashboardBO.getJailer());
        staff.setText(dashboardBO.getStaff());
        setDate();
        setTime();
    }

    @FXML
    private void jailermanage(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) dashbord.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/jailermanageform.fxml"))));
        stage.setTitle("Jailer manage");
        stage.centerOnScreen();
        stage.show();

    }

    @FXML
    private void staffmanage(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) dashbord.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/staffmanagerform.fxml"))));
        stage.setTitle("Staff manage");
        stage.centerOnScreen();
        stage.show();

    }


    @FXML
    private void visitormanage(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) dashbord.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/visitormanageform.fxml"))));
        stage.setTitle("Visit manage");
        stage.centerOnScreen();
        stage.show();
    }

    @FXML
    private void cellmanage(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) dashbord.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/cellmanageform.fxml"))));
        stage.setTitle("Prison cells manage");
        stage.centerOnScreen();
        stage.show();
    }

    @FXML
    private void casemanage(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) dashbord.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/casesmanageform.fxml"))));
        stage.setTitle("Cases manage");
        stage.centerOnScreen();
        stage.show();
    }

    @FXML
    private void prisonemanage(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) dashbord.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/prisonermanageform.fxml"))));
        stage.setTitle("Prisoner manage");
        stage.centerOnScreen();
        stage.show();

    }

    @FXML
    private void adminmanage(MouseEvent mouseEvent) throws IOException {
        Stage stage = (Stage) dashbord.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/adminform.fxml"))));
        stage.setTitle("Admin manage");
        stage.centerOnScreen();
        stage.show();

    }
    @FXML
    private void logout(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) dashbord.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/loginform.fxml"))));
        stage.setTitle("Login");
        stage.centerOnScreen();
        stage.show();
    }
    @FXML
    private void setDate() {
        date.setText(String.valueOf(LocalDate.now()));
    }
    @FXML
    private void setTime() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO, e ->{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss");
            time.setText(LocalTime.now().format(formatter));
        }),new KeyFrame(Duration.seconds(1)));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }
}

