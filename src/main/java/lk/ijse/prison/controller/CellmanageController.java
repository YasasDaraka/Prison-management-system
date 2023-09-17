package lk.ijse.prison.controller;

import com.jfoenix.controls.JFXComboBox;
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
import lk.ijse.prison.bo.coustom.CellBO;
import lk.ijse.prison.dto.CellDTO;
import lk.ijse.prison.view.tdm.CellTM;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class CellmanageController implements Initializable {

    public TableView <CellTM>tableCells;
    @FXML
    private Label lblsearch;
    @FXML
    private Label lbltime;
    @FXML
    private AnchorPane cellsmanageanchor;
    @FXML
    private Label lblDate;
    //Cell Add Textfealds
    @FXML
    private TextField txtbulno;
    @FXML
    private TextField txtammcell;
    @FXML
    private JFXComboBox<String> txttype;
    @FXML
    private TextField txtlevel;

    //Cell Table
    @FXML
    private TableColumn tablebuilno;
    @FXML
    private TableColumn tableammcell;
    @FXML
    private TableColumn tablegender;
    @FXML
    private TableColumn tablesectype;

    CellBO cellBO = BoFactory.getBoFactory().getBO(BoFactory.BOTypes.CELL);
    @Override
    public void initialize(java.net.URL url, ResourceBundle resourceBundle) {
        ObservableList<String> gen = FXCollections.observableArrayList("Male","Female");
        txttype.setItems(gen);

        initializeTableColumns();
        setSelectCellDetailsToTxt();
        getAll();
        setDate();
        setTime();
    }
    //get details from database
    private void getAll() {
        try {
            ObservableList<CellTM> obList = FXCollections.observableArrayList();
            List<CellDTO> cellDTOList = cellBO.getAll();

            for (CellDTO cellDTO : cellDTOList) {
                obList.add(new CellTM(
                        cellDTO.getTxtbulno(),
                        cellDTO.getTxtammcell(),
                        cellDTO.getTxttype(),
                        cellDTO.getTxtlevel()
                ));
            }
            tableCells.setItems(obList);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "SQL Error!").show();
        }
    }
//set database detais to table
    private void initializeTableColumns() {
        tablebuilno.setCellValueFactory(new PropertyValueFactory<>("txtbulno"));
        tableammcell.setCellValueFactory(new PropertyValueFactory<>("txtammcell"));
        tablegender.setCellValueFactory(new PropertyValueFactory<>("txttype"));
        tablesectype.setCellValueFactory(new PropertyValueFactory<>("txtlevel"));
    }
    //set select item to textfeald from table
    private void setSelectCellDetailsToTxt() {
        tableCells.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txtbulno.setText(newSelection.getTxtbulno());
                txtammcell.setText(String.valueOf(newSelection.getTxtammcell()));
                txttype.setValue(newSelection.getTxttype());
                txtlevel.setText(newSelection.getTxtlevel());
            }
        });
    }
    //Navigation
    @FXML
    private void dashOnAction(MouseEvent mouseEvent) throws IOException {
        Stage stage = (Stage) cellsmanageanchor.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/dashboard.fxml"))));
        stage.setTitle("Dashboard");
        stage.centerOnScreen();
        stage.show();
    }
    @FXML
    void cellprimanage(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) cellsmanageanchor.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/prisonermanageform.fxml"))));
        stage.setTitle("Prisoner manage");
        stage.centerOnScreen();
        stage.show();

    }
    @FXML
    void celljailermanage(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) cellsmanageanchor.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/jailermanageform.fxml"))));
        stage.setTitle("Jailer manage");
        stage.centerOnScreen();
        stage.show();
    }
    @FXML
    void cellstaffmanage(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) cellsmanageanchor.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/staffmanagerform.fxml"))));
        stage.setTitle("Staff manage");
        stage.centerOnScreen();
        stage.show();
    }
    @FXML
    void cellvisitmanage(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) cellsmanageanchor.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/visitormanageform.fxml"))));
        stage.setTitle("Visit manage");
        stage.centerOnScreen();
        stage.show();
    }
    @FXML
    void cellcasesmanage(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) cellsmanageanchor.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/casesmanageform.fxml"))));
        stage.setTitle("Cases manage");
        stage.centerOnScreen();
        stage.show();
    }
    @FXML
    void adminmanage(MouseEvent mouseEvent) throws IOException {
        Stage stage = (Stage) cellsmanageanchor.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/adminform.fxml"))));
        stage.setTitle("Admin manage");
        stage.centerOnScreen();
        stage.show();
    }
    @FXML
    void logout(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) cellsmanageanchor.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/loginform.fxml"))));
        stage.setTitle("Login");
        stage.centerOnScreen();
        stage.show();
    }
//Prisoner Cell Actions
    @FXML
    void btnSave(ActionEvent actionEvent){
        lblsearch.setText("");
        String buildNo = txtbulno.getText();
        int cell = Integer.parseInt(txtammcell.getText());
        String type = txttype.getValue();
        String level = txtlevel.getText();

        if(!txtbulno.getText().isBlank() && !txtammcell.getText().isBlank() && txttype.getValue()!= null && !txtlevel.getText().isBlank())
        {
            if (txtammcell.getText().matches("\\d+")) {
                try {
                    boolean isSaved = cellBO.save(new CellDTO(buildNo,cell,type,level));
                    if (isSaved) {
                        new Alert(Alert.AlertType.CONFIRMATION, "Building saved!").show();
                        getAll();
                        initializeTableColumns();
                    }
                } catch (SQLException | ClassNotFoundException e) {
                    new Alert(Alert.AlertType.ERROR, "Please Check Details & Try Again!").show();
                    txtClear();
                }
            }else {lblsearch.setText("Please Enter Correct Value \nFor Cells!");}
        }else{new Alert(Alert.AlertType.ERROR, "Please Fill Details!").show(); }
    }
    @FXML
    void btnSearch(ActionEvent actionEvent) {
        lblsearch.setText("");
        if(!txtbulno.getText().isBlank()) {
            try {
                CellDTO cellDTO = cellBO.search(txtbulno.getText());
                if (cellDTO != null) {
                    search();
                    txtbulno.setText(cellDTO.getTxtbulno());
                    txtammcell.setText(String.valueOf(cellDTO.getTxtammcell()));
                    txttype.setValue(cellDTO.getTxttype());
                    txtlevel.setText(cellDTO.getTxtlevel());
                    lblsearch.requestFocus();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Building Id Not Exist!").show();
                }

            } catch (SQLException | ClassNotFoundException e) {
                new Alert(Alert.AlertType.ERROR, "Please Check Details & Try Again!").show();
                txtClear();
            }
        }else{new Alert(Alert.AlertType.ERROR, "Please Fill Building Id!").show(); }
    }
    private void search(){
        tableCells.getItems().stream()
                .filter(item -> item.getTxtbulno().equals(txtbulno.getText()) )
                .findAny()
                .ifPresent(item -> {
                    tableCells.getSelectionModel().select(item);
                    tableCells.scrollTo(item);
                });

    }
    @FXML
    void btnDelete(ActionEvent actionEvent) {
        lblsearch.setText("");
        String buildNo = txtbulno.getText();
        if(!txtbulno.getText().isBlank()) {
            try {
                boolean isDeleted = cellBO.delete(buildNo);
                if (isDeleted) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Building Deleted!").show();
                    txtClear();
                    getAll();
                    initializeTableColumns();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Building Id Not Exist!").show();
                }
            } catch (SQLException | ClassNotFoundException e) {
                new Alert(Alert.AlertType.ERROR, "Please Check Details & Try Again!").show();
                txtClear();
            }
        }else{new Alert(Alert.AlertType.ERROR, "Please Fill Building Id!").show(); }
    }
    @FXML
    void btnUpdate(ActionEvent actionEvent) {
        lblsearch.setText("");
        String buildNo = txtbulno.getText();
        int cell = Integer.parseInt(txtammcell.getText());
        String type = txttype.getValue();
        String level = txtlevel.getText();

        if(!txtbulno.getText().isBlank() && !txtammcell.getText().isBlank() && txttype.getValue()!= null && !txtlevel.getText().isBlank()) {
            if (txtammcell.getText().matches("\\d+")) {
                try {
                    boolean isUpdated = cellBO.update(new CellDTO(buildNo,cell,type,level));
                    if (isUpdated) {
                        new Alert(Alert.AlertType.CONFIRMATION, "Building Details updated!").show();
                        getAll();
                        initializeTableColumns();
                    } else {
                        new Alert(Alert.AlertType.ERROR, "Building ID Not Exist!").show();
                    }
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                    new Alert(Alert.AlertType.ERROR, "Please Check Details & Try Again!").show();
                    txtClear();
                }
            }else {lblsearch.setText("Please Enter Correct Value \nFor Cells!");}
        }else{new Alert(Alert.AlertType.ERROR, "Please Fill Details!").show(); }
    }
    @FXML
    void btnClear(ActionEvent actionEvent) {
        txtClear();
    }

    @FXML
    void txtClear(){
        txtbulno.setText("");
        txtammcell.setText("");
        txttype.setValue(null);
        txtlevel.setText("");
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
    void buildOnAction(ActionEvent actionEvent) {
        lblsearch.setText("");
        if(!txtbulno.getText().isBlank()) {
            try {
                CellDTO cellDTO = cellBO.search(txtbulno.getText());
                if (cellDTO != null) {
                    search();
                    txtbulno.setText(cellDTO.getTxtbulno());
                    txtammcell.setText(String.valueOf(cellDTO.getTxtammcell()));
                    txttype.setValue(cellDTO.getTxttype());
                    txtlevel.setText(cellDTO.getTxtlevel());
                    lblsearch.setText("Building Alredy Registerd!");
                    lblsearch.requestFocus();
                } else {
                    lblsearch.setText("Building Id Not Exist!");
                    detailTxt();
                }
            } catch (SQLException | ClassNotFoundException e) {
               lblsearch.setText("Please Check Details & Try Again!");
                txtClear();
            }
        }else{lblsearch.setText("Please Fill Building Id!");}
    }

    @FXML
    void cellOnAction(ActionEvent actionEvent) {
        if (!txtammcell.getText().isBlank()){
            if (txtammcell.getText().matches("\\d+")) {
                detailTxt();
            }else{txtammcell.setText("");
                lblsearch.setText("Please Enter Correct Value \nFor Cells!");}
        }
    }

    @FXML
    void typeOnAction(ActionEvent actionEvent) {
        if (txttype.getValue()!= null){
            detailTxt();
        }
    }

    @FXML
    void levelOnAction(ActionEvent actionEvent) {
        if (!txtlevel.getText().isBlank()){
            detailTxt();
        }
    }
    @FXML
    private void detailTxt() {
        int emptyFieldIndex = -1;
        switch (emptyFieldIndex) {
            case -1:
                if (txtbulno.getText().isBlank()) {
                    txtbulno.requestFocus();
                    emptyFieldIndex = 0;
                }
            case 0:
                if (emptyFieldIndex < 0 && (txtammcell.getText().isBlank())) {
                    txtammcell.requestFocus();
                    emptyFieldIndex = 1;
                }
            case 1:
                if (emptyFieldIndex < 0 && (txttype.getValue() == null)) {
                    txttype.requestFocus();
                    emptyFieldIndex = 2;
                }
            case 2:
                if (emptyFieldIndex < 0 && (txtlevel.getText().isBlank())) {
                    txtlevel.requestFocus();
                    emptyFieldIndex = 3;
                }
                break;
            default:
                break;
        }

    }
}
