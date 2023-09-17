package lk.ijse.prison.controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import lk.ijse.prison.bo.BoFactory;
import lk.ijse.prison.bo.coustom.CaseBO;
import lk.ijse.prison.dto.CaseDTO;
import lk.ijse.prison.view.tdm.CaseTM;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class CasesmanageController implements Initializable {

    public TableView <CaseTM> tableCase;
    @FXML
    private Label lbltime;
    @FXML
    private Label lblDate;
    @FXML
    private Label lblsearch;
    @FXML
    private AnchorPane casemanageranchor;
    //Cases Textfealds
    @FXML
    private TextField txtcaseid;
    @FXML
    private TextField txtprid;
    @FXML
    private TextField txtcrytype;
    @FXML
    private TextField txtsenten;
    @FXML
    private TextField txttimest;
    @FXML
    private TextField txttimeend;
    //Cases Table text
    @FXML
    private TableColumn tablecaseid;
    @FXML
    private TableColumn tableprid;
    @FXML
    private TableColumn tablecrtype;
    @FXML
    private TableColumn tablesentence;
    @FXML
    private TableColumn tableservstart;
    @FXML
    private TableColumn tableservend;

    CaseBO caseBO = BoFactory.getBoFactory().getBO(BoFactory.BOTypes.CASE);

    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeTableColumns();
        setSelectCasesDetailsToTxt();
        getAll();
        setDate();
        setTime();
    }

    // Get all Cases from database
    private void getAll(){
        try {
            ObservableList<CaseTM> obList = FXCollections.observableArrayList();
            List<CaseDTO> caseDTOList = caseBO.getAll();

            for (CaseDTO cas : caseDTOList) {
                obList.add(new CaseTM(
                        cas.getTxtcaseid(),
                        cas.getTxtprid(),
                        cas.getTxtcrytype(),
                        cas.getTxtsenten(),
                        cas.getTxttimest(),
                        cas.getTxttimeend()
                ));
            }
            tableCase.setItems(obList);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "SQL Error!").show();
        }
    }
//st cases to table
    private void initializeTableColumns(){
        tablecaseid.setCellValueFactory(new PropertyValueFactory<>("txtcaseid"));
        tableprid.setCellValueFactory(new PropertyValueFactory<>("txtprid"));
        tablecrtype.setCellValueFactory(new PropertyValueFactory<>("txtcrytype"));
        tablesentence.setCellValueFactory(new PropertyValueFactory<>("txtsenten"));
        tableservstart.setCellValueFactory(new PropertyValueFactory<>("txttimest"));
        tableservend.setCellValueFactory(new PropertyValueFactory<>("txttimeend"));

    }
    //set selected item to textfeals from table
    private void setSelectCasesDetailsToTxt() {
        tableCase.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txtcaseid.setText(newSelection.getTxtcaseid());
                txtprid.setText(newSelection.getTxtprid());
                txtcrytype.setText(newSelection.getTxtcrytype());
                txtsenten.setText(newSelection.getTxtsenten());
                txttimest.setText(newSelection.getTxttimest());
                txttimeend.setText(newSelection.getTxttimeend());
            }
        });
    }
    //navigate
    @FXML
    private void dashOnAction(MouseEvent mouseEvent) throws IOException {
        Stage stage = (Stage) casemanageranchor.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/dashboard.fxml"))));
        stage.setTitle("Dashboard");
        stage.centerOnScreen();
        stage.show();
    }
    @FXML
     void caseprimanage(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) casemanageranchor.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/prisonermanageform.fxml"))));
        stage.setTitle("Prisoner manage");
        stage.centerOnScreen();
        stage.show();
    }
    @FXML
    void casejailermanage(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) casemanageranchor.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/jailermanageform.fxml"))));
        stage.setTitle("Jailer manage");
        stage.centerOnScreen();
        stage.show();
    }
    @FXML
    void casestaffmanage(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) casemanageranchor.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/staffmanagerform.fxml"))));
        stage.setTitle("Staff manage");
        stage.centerOnScreen();
        stage.show();

    }
    @FXML
    void casevisitmanage(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) casemanageranchor.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/visitormanageform.fxml"))));
        stage.setTitle("Visit manage");
        stage.centerOnScreen();
        stage.show();
    }
    @FXML
    void casecellmanage(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) casemanageranchor.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/cellmanageform.fxml"))));
        stage.setTitle("Prison cells manage");
        stage.centerOnScreen();
        stage.show();
    }
    @FXML
    void adminmanage(MouseEvent mouseEvent) throws IOException {
        Stage stage = (Stage) casemanageranchor.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/adminform.fxml"))));
        stage.setTitle("Admin manage");
        stage.centerOnScreen();
        stage.show();
    }
    @FXML
    void logout(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) casemanageranchor.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/loginform.fxml"))));
        stage.setTitle("Login");
        stage.centerOnScreen();
        stage.show();
    }
//Cases Actions
    @FXML
    void btnSave(ActionEvent actionEvent) {
        lblsearch.setText("");
        String caseId = txtcaseid.getText();
        String prId = txtprid.getText();
        String crType = txtcrytype.getText();
        String sentence = txtsenten.getText();
        String timeSt = txttimest.getText();
        String timeEnd = txttimeend.getText();

        if(!txtcaseid.getText().isBlank() && !txtprid.getText().isBlank() && !txtcrytype.getText().isBlank() && !txtsenten.getText().isBlank() && !txttimest.getText().isBlank() && !txttimeend.getText().isBlank()) {
            try {
                   boolean exists = caseBO.exist(prId);
                if (exists) {
                    new Alert(Alert.AlertType.ERROR, "Prisoner Case Alredy added!").show();
                }else {
                    boolean isSaved = caseBO.save(new CaseDTO(caseId,prId,crType,sentence,timeSt,timeEnd));
                    if (isSaved) {
                        new Alert(Alert.AlertType.CONFIRMATION, "Prisoner Case Saved!").show();
                        getAll();
                        initializeTableColumns();
                    }
                }
            } catch (SQLException | ClassNotFoundException e) {
                new Alert(Alert.AlertType.ERROR, "Please Check Details & Try Again!").show();
                txtClear();
            }
        }else{new Alert(Alert.AlertType.ERROR, "Please Fill Details!").show(); }
    }
    @FXML
    void btnSearch(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        lblsearch.setText("");
        CaseDTO cas = null;
        if(!txtcaseid.getText().isBlank() && txtprid.getText().isBlank()){
          cas =  caseBO.searchBy(txtcaseid.getText(),1);
        }else if (!txtprid.getText().isBlank() && txtcaseid.getText().isBlank()){
            cas =  caseBO.searchBy(txtprid.getText(),2);
        }
        if (cas != null) {
            search();
            txtcaseid.setText(cas.getTxtcaseid());
            txtprid.setText(cas.getTxtprid());
            txtcrytype.setText(cas.getTxtcrytype());
            txtsenten.setText(cas.getTxtsenten());
            txttimest.setText(cas.getTxttimest());
            txttimeend.setText(cas.getTxttimeend());
            lblsearch.requestFocus();

        } else {
            new Alert(Alert.AlertType.ERROR, "Please enter only Valid Inmate Code or Case ID to search!").show();
            txtClear();
        }
    }
    private void search(){
        tableCase.getItems().stream()
                .filter(item -> item.getTxtcaseid().equals(txtcaseid.getText()) )
                .findAny()
                .ifPresent(item -> {
                    tableCase.getSelectionModel().select(item);
                    tableCase.scrollTo(item);
                });
        tableCase.getItems().stream()
                .filter(item -> item.getTxtprid().equals(txtprid.getText()) )
                .findAny()
                .ifPresent(item -> {
                    tableCase.getSelectionModel().select(item);
                    tableCase.scrollTo(item);
                });

    }
    @FXML
    void btnDelete(ActionEvent actionEvent) {
        lblsearch.setText("");
        String caseId = txtcaseid.getText();
        if(!txtcaseid.getText().isBlank()) {
            try {
                boolean isDeleted = caseBO.delete(caseId);
                if (isDeleted) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Prisoner Case Deleted!").show();
                    txtClear();
                    getAll();
                    initializeTableColumns();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Case Id Not Exist!").show();
                }
            } catch (SQLException | ClassNotFoundException e) {
                new Alert(Alert.AlertType.ERROR, "Please Check Details & Try Again!").show();
                txtClear();
            }
        }else{new Alert(Alert.AlertType.ERROR, "Please Fill Case Id!").show(); }
    }
    @FXML
    void btnUpdate(ActionEvent actionEvent) {
        lblsearch.setText("");
        String caseId = txtcaseid.getText();
        String prId = txtprid.getText();
        String crType = txtcrytype.getText();
        String sentence = txtsenten.getText();
        String timeSt = txttimest.getText();
        String timeEnd = txttimeend.getText();

        if(!txtcaseid.getText().isBlank() && !txtprid.getText().isBlank() && !txtcrytype.getText().isBlank() && !txtsenten.getText().isBlank() && !txttimest.getText().isBlank() && !txttimeend.getText().isBlank()) {
            try {
                boolean isUpdated = caseBO.update(new CaseDTO(caseId,prId,crType,sentence,timeSt,timeEnd));
                if (isUpdated){new Alert(Alert.AlertType.CONFIRMATION, "Case is updated!").show();
                    getAll();
                    initializeTableColumns();
                }
                else if (isUpdated == false){new Alert(Alert.AlertType.ERROR, "Case Not  updated!").show();}
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Prisoner Case Already Saved!").show();
                txtClear();
            }
        }else{new Alert(Alert.AlertType.ERROR, "Please Fill Details!").show(); }
    }

    @FXML
    void btnClear(ActionEvent actionEvent) {
        txtClear();
    }
    @FXML
    void txtClear() {
        txtcaseid.setText("");
        txtprid.setText("");
        txtcrytype.setText("");
        txtsenten.setText("");
        txttimest.setText("");
        txttimeend.setText("");
        lblsearch.setText("");
    }
    @FXML
    private void setDate() {
        lblDate.setText(String.valueOf(LocalDate.now()));
    }
    @FXML
    private void setTime() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO, e ->{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss");
            lbltime.setText(LocalTime.now().format(formatter));
        }),new KeyFrame(Duration.seconds(1)));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    @FXML
    void imCodeONAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        if(!txtprid.getText().isBlank()){
          boolean prisonor = caseBO.existPrisoner(txtprid.getText());
          if(prisonor) {
              CaseDTO cas =  caseBO.searchBy(txtprid.getText(),2);
              if (cas != null) {
                  search();
                  txtcaseid.setText(cas.getTxtcaseid());
                  txtprid.setText(cas.getTxtprid());
                  txtcrytype.setText(cas.getTxtcrytype());
                  txtsenten.setText(cas.getTxtsenten());
                  txttimest.setText(cas.getTxttimest());
                  txttimeend.setText(cas.getTxttimeend());
                  lblsearch.setText("Prisoner Case Alredy Registerd!");
                  lblsearch.requestFocus();
              } else {
                  lblsearch.setText("Case Details Not Exist!");
                  searchTxt();
              }
          }
          else{
              lblsearch.setText("Prisoner Id Not Exist!");
          }
        }else{
            lblsearch.setText("Please Fill Prisoner Id!");
        }
    }
    @FXML
    void idOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        if (!txtcaseid.getText().isBlank()){
            CaseDTO cas =  caseBO.searchBy(txtcaseid.getText(),1);
            if (cas != null) {
                search();
                txtcaseid.setText(cas.getTxtcaseid());
                txtprid.setText(cas.getTxtprid());
                txtcrytype.setText(cas.getTxtcrytype());
                txtsenten.setText(cas.getTxtsenten());
                txttimest.setText(cas.getTxttimest());
                txttimeend.setText(cas.getTxttimeend());
                lblsearch.setText("Prisoner Case Alredy Registerd!");
                lblsearch.requestFocus();
                searchTxt();
            } else {
                lblsearch.setText("Case Id Not Exist!");
                searchTxt();
            }
        }else{lblsearch.setText("Please Fill Case Id!");}
    }
    @FXML
    void typeOnAction(ActionEvent actionEvent) {
        if (!txtcrytype.getText().isBlank()){
            searchTxt();
        }
    }
    @FXML
    void sentOnAction(ActionEvent actionEvent) {
        if (!txtsenten.getText().isBlank()){
            searchTxt();
        }
    }
    @FXML
    void startOnAction(ActionEvent actionEvent) {
        if (!txttimest.getText().isBlank()){
            searchTxt();
        }
    }
    @FXML
    void endOnAction(ActionEvent actionEvent) {
        if (!txttimeend.getText().isBlank()){
            searchTxt();
        }
    }
    @FXML
    private void searchTxt(){
        int emptyFieldIndex = -1;
        switch (emptyFieldIndex) {
            case -1:
                if (txtprid.getText().isBlank()) {
                    txtprid.requestFocus();
                    emptyFieldIndex = 0;
                }
            case 0:
                if (emptyFieldIndex < 0 && (txtcaseid.getText().isBlank())) {
                    txtcaseid.requestFocus();
                    emptyFieldIndex = 1;
                }
            case 1:
                if (emptyFieldIndex < 0 && (txtcrytype.getText().isBlank())) {
                    txtcrytype.requestFocus();
                    emptyFieldIndex = 2;
                }
            case 2:
                if (emptyFieldIndex < 0 && (txtsenten.getText().isBlank())) {
                    txtsenten.requestFocus();
                    emptyFieldIndex = 3;
                }
            case 3:
                if (emptyFieldIndex < 0 && (txttimest.getText().isBlank())) {
                    txttimest.requestFocus();
                    emptyFieldIndex = 4;
                }
            case 4:
                if (emptyFieldIndex < 0 && (txttimeend.getText().isBlank())) {
                    txttimeend.requestFocus();
                    emptyFieldIndex = 5;
                }
                break;
            default:
                break;
        }
    }
}
