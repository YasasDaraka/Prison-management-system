package lk.ijse.prison.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.prison.bo.BoFactory;
import lk.ijse.prison.bo.coustom.LoginBO;
import lk.ijse.prison.dto.AdminDTO;

import java.sql.SQLException;

public class LoginformController {

    @FXML
    private TextField username;
    public PasswordField password;
    @FXML
    private AnchorPane logancharpane;

    LoginBO loginBO = BoFactory.getBoFactory().getBO(BoFactory.BOTypes.LOGIN);

    @FXML
    private void btnlogOnaction(ActionEvent actionEvent) throws Exception {
        if(!username.getText().isBlank() && !password.getText().isBlank()) {
            try {
                String name = username.getText();
                String pass = password.getText();
                AdminDTO reult = loginBO.search(pass);

                if (name.equals(reult.getUsername()) && pass.equals(reult.getPassword())) {
                    Stage stage = (Stage) logancharpane.getScene().getWindow();
                    stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/dashboard.fxml"))));
                    stage.setTitle("Dashboard");
                    stage.centerOnScreen();
                    stage.show();
                }if (!name.equals(reult.getUsername()) && pass.equals(reult.getPassword())) {
                    new Alert(Alert.AlertType.ERROR, "Username Invaild!").show();
                }if (name.equals(reult.getUsername()) && !pass.equals(reult.getPassword())) {
                    new Alert(Alert.AlertType.ERROR, "Password Invaild!").show();
                }if (!name.equals(reult.getUsername()) && !pass.equals(reult.getPassword())) {
                    new Alert(Alert.AlertType.ERROR, "Username & Password Invaild!").show();
                }
                //else if (isSaved == false){new Alert(Alert.AlertType.ERROR, " Username or Password Invaild!").show();}
            } catch (SQLException e) {
                e.printStackTrace();
               // new Alert(Alert.AlertType.ERROR, "Username or Password Invaild!").show();
            }
        }else{new Alert(Alert.AlertType.ERROR, "Please Fill Details!").show(); }
    }

    @FXML
    private void usernameOnAction(ActionEvent actionEvent) {
        if (!username.getText().isBlank()) {
            password.requestFocus();
        }
    }

    @FXML
    private void passwordOnAction(ActionEvent actionEvent) {
        if (!password.getText().isBlank()) {
            username.requestFocus();
        }
    }
}