package lk.ijse.prison.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import lk.ijse.prison.bo.BoFactory;
import lk.ijse.prison.bo.coustom.VisitorBO;
import lk.ijse.prison.dto.*;
import lk.ijse.prison.view.tdm.VisitTimeTM;
import lk.ijse.prison.view.tdm.VisitorTM;
import lombok.SneakyThrows;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameGrabber;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class VisitormanageController implements Initializable , Runnable{

    public TableView<VisitorTM>tableVesDetail;
    public TableView<VisitTimeTM>tableVesTime;

    @FXML
    private Label lbltime;
    @FXML
    private Label lblDate;
    @FXML
    private Label lblsearch;
    @FXML
    private AnchorPane visitmanageanchor;
    //Visitor Textfeald
    @FXML
    private TextField txtid;
    @FXML
    private TextField txtname;
    @FXML
    private TextField txtaddress;
    @FXML
    private JFXComboBox<String> txtgender;
    @FXML
    private DatePicker selectdate;
    @FXML
    private TextField txttime;
    @FXML
    private TextField txtnic;
    //Visiter Table
    @FXML
    private TableColumn tablenic;
    @FXML
    private TableColumn tablename;
    @FXML
    private TableColumn tableaddress;
    @FXML
    private TableColumn tablegender;
    //Visiter Dsetail Table
    @FXML
    private TableColumn dtableNic;
    @FXML
    private TableColumn dtableCode;
    @FXML
    private TableColumn dtabledate;
    @FXML
    private TableColumn dtabletime;
    @FXML
    private Button btnVisit;

    private Boolean isCameraEnabled = true;
    public ImageView panePhoto;
    @FXML
    private JFXButton btnCapture;
    private volatile boolean isCapturing;
    private FrameGrabber grabber;
    private Java2DFrameConverter converter;
    private BufferedImage image;

    VisitorBO visitorBO = BoFactory.getBoFactory().getBO(BoFactory.BOTypes.VISITOR);

    @FXML
    void onCapture(ActionEvent event) {
        isCameraEnabled = true;
        if(isCameraEnabled){
            if (!isCapturing) {
                // Start
                isCapturing = true;
                Thread captureThread = new Thread(this);
                captureThread.start();
                btnCapture.setText("Take Picture");
                btnCapture.setStyle("-fx-background-color:  #c93329;");
            } else {
                // Stop
                isCapturing = false;
                btnCapture.setText("Open Camera");
                btnCapture.setStyle("-fx-background-color:   #158392;");
            }}
    }
    public void run() {
        try {
            // Continuously capture photos until isCapturing is set to false
            grabber.start();
            while (isCapturing) {if (isCameraEnabled) {
                Frame frame = grabber.grab();
                image = converter.convert(frame);
                Image fxImage = SwingFXUtils.toFXImage(image, null); // convert to JavaFX Image
                panePhoto.setImage(fxImage); // set Image to ImageView pane
            }else {
                break;
            }
            }
            grabber.stop();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> gen = FXCollections.observableArrayList("Male","Female");
        txtgender.setItems(gen);

        grabber = new OpenCVFrameGrabber(0);
        converter = new Java2DFrameConverter();

        getAllDetail();
        initializeTableDetail();
        getAllTime();
        initializeTableTime();
        setSelectVisiterDetailsToTxt();
        setSelectVisitTimeDetailsToTxt();
        setDate();
        setTime();
    }
    //get all data from visit details database
    private void getAllTime() {
        try {
            ObservableList<VisitTimeTM> obList = FXCollections.observableArrayList();
            ArrayList<VisitTimeDTO> timeList = visitorBO.getAllVisit();

            for (VisitTimeDTO visitTime : timeList) {
                obList.add(new VisitTimeTM(
                        visitTime.getPriId(),
                        visitTime.getNic(),
                        visitTime.getDate(),
                        visitTime.getTime()
                ));
            }
            tableVesTime.setItems(obList);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "SQL Error!").show();
        }
    }
//set  database details to table
    private void initializeTableTime() {
        dtableNic.setCellValueFactory(new PropertyValueFactory<>("nic"));
        dtableCode.setCellValueFactory(new PropertyValueFactory<>("priId"));
        dtabledate.setCellValueFactory(new PropertyValueFactory<>("date"));
        dtabletime.setCellValueFactory(new PropertyValueFactory<>("time"));
    }
    //set select data to textfeals from tabale
    private void setSelectVisitTimeDetailsToTxt() {
        tableVesTime.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txtid.setText(newSelection.getPriId());
                selectdate.setValue(LocalDate.parse(newSelection.getDate()));
                txttime.setText(newSelection.getTime());
                txtnic.setText(newSelection.getNic());
            }
        });
    }
    //get all data from visitor database
    private void getAllDetail(){
        try {
            ObservableList<VisitorTM> obList = FXCollections.observableArrayList();
            List<VisitorDTO> visList = visitorBO.getAllVisitors();

            for (VisitorDTO visitorDTODetail : visList) {
                obList.add(new VisitorTM(
                        visitorDTODetail.getPriId(),
                        visitorDTODetail.getName(),
                        visitorDTODetail.getAddress(),
                        visitorDTODetail.getGender()
                ));
            }
            tableVesDetail.setItems(obList);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "SQL Error!").show();
        }
    }
//set visitor database details to table
    private void initializeTableDetail() {
        tablenic.setCellValueFactory(new PropertyValueFactory<>("nic"));
        tablename.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableaddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        tablegender.setCellValueFactory(new PropertyValueFactory<>("gender"));
    }
    //set select data to textfeals from tabale
    private void setSelectVisiterDetailsToTxt() {
        tableVesDetail.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txtnic.setText(newSelection.getNic());
                txtname.setText(newSelection.getName());
                txtaddress.setText(newSelection.getAddress());
                txtgender.setValue(newSelection.getGender());

            }
        });
    }
    @FXML
    private void dashOnAction(MouseEvent mouseEvent) throws IOException {
        isCapturing = false;
        Stage stage = (Stage) visitmanageanchor.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/dashboard.fxml"))));
        stage.setTitle("Dashboard");
        stage.centerOnScreen();
        stage.show();
    }
    @FXML
    private void visiitprimanage(ActionEvent actionEvent) throws IOException {
        isCapturing = false;
        Stage stage = (Stage) visitmanageanchor.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/prisonermanageform.fxml"))));
        stage.setTitle("Prisoner manage");
        stage.centerOnScreen();
        stage.show();
    }
    @FXML
    void visiitjailermanage(ActionEvent actionEvent) throws IOException {
        isCapturing = false;
        Stage stage = (Stage) visitmanageanchor.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/jailermanageform.fxml"))));
        stage.setTitle("Jailer manage");
        stage.centerOnScreen();
        stage.show();
    }
    @FXML
    void visiitstaffmanage(ActionEvent actionEvent) throws IOException {
        isCapturing = false;
        Stage stage = (Stage) visitmanageanchor.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/staffmanagerform.fxml"))));
        stage.setTitle("Staff manage");
        stage.centerOnScreen();
        stage.show();
    }
    @FXML
    void visiitcellsmanage(ActionEvent actionEvent) throws IOException {
        isCapturing = false;
        Stage stage = (Stage) visitmanageanchor.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/cellmanageform.fxml"))));
        stage.setTitle("Prison cells manage");
        stage.centerOnScreen();
        stage.show();
    }
    @FXML
    void visiitcasesmanage(ActionEvent actionEvent) throws IOException {
        isCapturing = false;
        Stage stage = (Stage) visitmanageanchor.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/casesmanageform.fxml"))));
        stage.setTitle("Cases manage");
        stage.centerOnScreen();
        stage.show();
    }
    @FXML
    void adminmanage(MouseEvent mouseEvent) throws IOException {
        isCapturing = false;
        Stage stage = (Stage) visitmanageanchor.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/adminform.fxml"))));
        stage.setTitle("Admin manage");
        stage.centerOnScreen();
        stage.show();

    }
    @FXML
    void logout(ActionEvent actionEvent) throws IOException {
        isCapturing = false;
        Stage stage = (Stage) visitmanageanchor.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/loginform.fxml"))));
        stage.setTitle("Login");
        stage.centerOnScreen();
        stage.show();
    }
//Visitor Actions
    @FXML
    void btnSave(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        lblsearch.setText("");
        btnVisit.setDisable(true);
        if(!txtnic.getText().isBlank() && !txtid.getText().isBlank() && !txtname.getText().isBlank() && selectdate.getValue() != null && !txtaddress.getText().isBlank() && txtgender.getValue() != null && !txttime.getText().isBlank()) {
            VisitorDetailDTO visitCheck = visitorBO.searchVisitors(txtnic.getText());
            if (visitCheck == null) {
                if (btnCapture.getText().equals("Open Camera") && panePhoto.getImage() != null) {
                    lblsearch.setText("");
                    String nic = txtnic.getText();
                    String id = txtid.getText();
                    String name = txtname.getText();
                    String address = txtaddress.getText();
                    String gender = txtgender.getValue();
                    LocalDate selectedDate = selectdate.getValue();
                    String date = selectedDate.toString();
                    String time = txttime.getText();
                    boolean isSaved = false;
                    try {
                        boolean prExists = false;
                        try {
                             prExists = visitorBO.existPrisoner(id);
                        } catch (SQLException | ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        if (prExists) {
                           isSaved = visitorBO.saveVisitors(new VisitorDTO(id, name, address, gender, date, time, nic));
                        }else {new Alert(Alert.AlertType.ERROR, "Prisoner Id Not Exist!").show();}

                        if (isSaved) {
                            new Alert(Alert.AlertType.CONFIRMATION, "Visitor Details saved!").show();
                            String imageName = nic + ".png"; // Construct the file name
                            File outputFile = new File(imageName);
                            try {
                                ImageIO.write(image, "png", outputFile);
                            } catch (IOException e) {
                                new Alert(Alert.AlertType.ERROR, "Failed to save the image!").show();
                                return;
                            }
                            getAllDetail();
                            initializeTableDetail();
                            getAllTime();
                            initializeTableTime();
                        }
                    } catch (SQLException e) {
                        new Alert(Alert.AlertType.ERROR, "Please Check Details & Try Again!").show();
                        txtClear();
                    }
                } else {
                    new Alert(Alert.AlertType.ERROR, "Please Take Picture For Save Details!").show();
                }
            }else{
                btnVisit.setDisable(false);
                new Alert(Alert.AlertType.ERROR, "Visitor Alredy Registerd! Pleace Cliq Visit Button To Confirm Visit").show();
            }
        }else{new Alert(Alert.AlertType.ERROR, "Please Fill Details!").show(); }
    }
    @FXML
    void btnSearch(ActionEvent actionEvent) {
        lblsearch.setText("");
        if(!txtnic.getText().isBlank()) {
            try {
                VisitorDetailDTO visitorDTO = visitorBO.searchVisitors(txtnic.getText());
                if (visitorDTO != null) {
                    search();
                    txtnic.setText(visitorDTO.getNic());
                    txtname.setText(visitorDTO.getName());
                    txtaddress.setText(visitorDTO.getAddress());
                    txtgender.setValue(visitorDTO.getGender());
                    lblsearch.requestFocus();

                    File file = new File(visitorDTO.getNic() + ".png");
                    if (file.exists()) {
                        System.out.println("Get image");
                        Image image = new Image(file.toURI().toString());
                        isCameraEnabled = false;
                        isCapturing = false;
                        btnCapture.setText("Open Camera");
                        btnCapture.setStyle("-fx-background-color:   #158392;");
                        panePhoto.setImage(image);
                        if (panePhoto.getParent() instanceof Pane) {
                            isCameraEnabled = false;
                            isCapturing = false;
                            btnCapture.setText("Open Camera");
                            btnCapture.setStyle("-fx-background-color:   #158392;");
                        }
                    } else {
                        panePhoto.setImage(null);
                    }

                } else {
                    new Alert(Alert.AlertType.ERROR, "Visitor NIC Not Exist!").show();
                }
            } catch (SQLException | ClassNotFoundException e) {
                new Alert(Alert.AlertType.ERROR, "Please Check Details & Try Again!").show();
                txtClear();
            }
        }else{new Alert(Alert.AlertType.ERROR, "Please Fill Visitor NIC!").show(); }
        btnVisit.setDisable(true);
    }
    private void search(){
        tableVesDetail.getItems().stream()
                .filter(item -> item.getNic().equals(txtnic.getText()) )
                .findAny()
                .ifPresent(item -> {
                    tableVesDetail.getSelectionModel().select(item);
                    tableVesDetail.scrollTo(item);
                });

    }
    @FXML
    void btnDelete(ActionEvent actionEvent) {
        lblsearch.setText("");
        String nic = txtnic.getText();
        if(!txtnic.getText().isBlank()) {
            try {
                boolean isDeleted = visitorBO.deleteVisitors(nic);
                if (isDeleted) {
                    String imageName = nic + ".png";
                    File outputFile = new File(imageName);
                    if (outputFile.delete()) {
                        System.out.println("Image file deleted successfully!");
                    } else {
                        System.out.println("Failed to delete image file!");
                    }
                    new Alert(Alert.AlertType.CONFIRMATION, "Visitor Deleted!").show();
                    txtClear();
                    getAllDetail();
                    initializeTableDetail();
                    getAllTime();
                    initializeTableTime();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Visitor NIC Not Exist!").show();
                }
            } catch (SQLException | ClassNotFoundException e) {
                new Alert(Alert.AlertType.ERROR, "Please Check Details & Try Again!").show();
                txtClear();
            }
        }else{new Alert(Alert.AlertType.ERROR, "Please Fill Visitor NIC!").show(); }
        btnVisit.setDisable(true);
    }
    @FXML
    void btnClear(ActionEvent actionEvent) {
        txtClear();
        btnVisit.setDisable(true);
    }
    @FXML
    void btnVisit(ActionEvent actionEvent) throws SQLException {
        if(!txtid.getText().isBlank() && !txtnic.getText().isBlank() && selectdate.getValue() != null && !txttime.getText().isBlank()) {
            String id = txtid.getText();
            String nic = txtnic.getText();
            LocalDate selectedDate = selectdate.getValue();
            String date = selectedDate.toString();
            String time = txttime.getText();

            lblsearch.setText("");
            try {
                boolean prisoner = visitorBO.existPrisoner(id);
                if (prisoner) {
                    boolean visitDate = visitorBO.existDate(nic,date);
                    if (visitDate) {
                        new Alert(Alert.AlertType.ERROR, "Visitor Alredy Visit Today!").show();
                    }else {
                        boolean isSaved = visitorBO.saveVisit(new VisitorDTO(id, nic, date, time));
                        if (isSaved) {
                            new Alert(Alert.AlertType.CONFIRMATION, "Visit Details saved!").show();
                            getAllTime();
                            initializeTableTime();
                        } else {
                            searchTxt();
                            txtClear();
                        }
                    }
                }else{
                    new Alert(Alert.AlertType.ERROR, "Prisoner Id Not Exist!").show();
                }
            } catch (SQLException | ClassNotFoundException e) {
                new Alert(Alert.AlertType.ERROR, "Please Check Details & Try Again!").show();
                txtClear();
            }

            btnVisit.setDisable(true);
        }else{new Alert(Alert.AlertType.ERROR, "Please Fill Visit Details!").show(); }
    }
    @FXML
    void btnUpdate(ActionEvent actionEvent) {
        lblsearch.setText("");
        String nic = txtnic.getText();
        String name = txtname.getText();
        String address = txtaddress.getText();
        String gender = txtgender.getValue();
        String id = null;
        String date = null;
        String time = null;

        if(!txtnic.getText().isBlank() && !txtname.getText().isBlank() && !txtaddress.getText().isBlank() && txtgender.getValue()!= null) {
            try {
                boolean isUpdated = visitorBO.updateVisitors(new VisitorDTO(id, name, address, gender, date, time, nic));
                if(isUpdated){
                    new Alert(Alert.AlertType.CONFIRMATION, "Visitor Details updated!").show();
                    getAllDetail();
                    initializeTableDetail();
                    getAllTime();
                    initializeTableTime();
                    VisitorDetailDTO visit = visitorBO.searchVisitors(txtnic.getText());
                    if (btnCapture.getText().equals("Open Camera") && panePhoto.getImage() != null){
                        String imageName = visit.getNic() +".png"; // Construct the file name
                        File outputFile = new File(imageName);
                        try {
                            ImageIO.write(image, "png", outputFile);
                        } catch (IOException e) {
                            new Alert(Alert.AlertType.ERROR, "Failed to save the image!").show();
                            return;
                        }
                    }
                }
                else {
                    new Alert(Alert.AlertType.ERROR, "Visitor Id Not Exist!").show();
                }
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Please Check Details & Try Again!").show();
                txtClear();
            }
        }else{new Alert(Alert.AlertType.ERROR, "Please Fill Details!").show(); }
        btnVisit.setDisable(true);
    }
    @SneakyThrows
    @FXML
    void txtClear() {
        txtnic.setText("");
        txtid.setText("");
        txtname.setText("");
        txtaddress.setText("");
        txtgender.setValue(null);
        selectdate.setValue(null);
        txttime.setText("");
        lblsearch.setText("");
        isCapturing = false;
        isCameraEnabled = false;
        panePhoto.setImage(null);
        grabber.stop();
        btnCapture.setText("Open Camera");
        btnCapture.setStyle("-fx-background-color:   #158392;");
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
    void nicOnAction(ActionEvent actionEvent) {
        if(!txtnic.getText().isBlank()) {
            try {
                VisitorDetailDTO visitorDTO = visitorBO.searchVisitors(txtnic.getText());
                if (visitorDTO != null) {
                    search();
                    txtnic.setText(visitorDTO.getNic());
                    txtname.setText(visitorDTO.getName());
                    txtaddress.setText(visitorDTO.getAddress());
                    txtgender.setValue(visitorDTO.getGender());
                    lblsearch.setText("Visitor Alredy Registerd! \n Pleace Cliq Visit Button To Confirm Visit");
                    btnVisit.setDisable(false);
                    searchTxt();
                } else {
                    lblsearch.setText("Visitor Not Exist! Please Fill Visitor Details");
                    searchTxt();
                }
            } catch (SQLException | ClassNotFoundException e) {lblsearch.setText("Please Check Details & Try Again!");
                txtClear();
            }
        }else{lblsearch.setText("Please Fill Visitor NIC!");}
    }

    @FXML
    void nameOnAction(ActionEvent actionEvent) {
        if (!txtname.getText().isBlank()){
            searchTxt();
        }
    }
    @FXML
    void addressOnAction(ActionEvent actionEvent) {
        if (!txtaddress.getText().isBlank()){
            searchTxt();
        }
    }
    @FXML
    void gendOnAction(ActionEvent actionEvent) {
        if (txtgender.getValue()!= null){
            searchTxt();
        }
    }
    @FXML
    void inmateOnaction(ActionEvent actionEvent) {
        if (!txtid.getText().isBlank()){
            lblsearch.setText("");
            try {
                PrisonerDTO prisonerDTO = visitorBO.searchPrisoner(txtid.getText());
                if (prisonerDTO != null) {
                    lblsearch.setText("");
                    searchTxt();
                } else {lblsearch.setText("Inmate Id Not Exist!");
                }
            } catch (SQLException | ClassNotFoundException e) {lblsearch.setText("Please Check Details\n & Try Again!");
                txtClear();
            }
        }
        else{ lblsearch.setText("Please Fill Inmate Id!");
        }
    }
    @FXML
    void dateOnAction(ActionEvent actionEvent) {
        if (selectdate.getValue() != null){
            searchTxt();
        }
    }
    @FXML
    void timeOnaction(ActionEvent actionEvent) {
        if (!txttime.getText().isBlank()){
            searchTxt();
        }

    }
    @FXML
    private void searchTxt(){
        int emptyFieldIndex = -1;
        switch (emptyFieldIndex) {
            case -1:
                if (txtnic.getText().isBlank()) {
                    txtnic.requestFocus();
                    emptyFieldIndex = 0;
                }
            case 0:
                if (emptyFieldIndex < 0 && (txtname.getText().isBlank())) {
                    txtname.requestFocus();
                    emptyFieldIndex = 1;
                }
            case 1:
                if (emptyFieldIndex < 0 && (txtaddress.getText().isBlank())) {
                    txtaddress.requestFocus();
                    emptyFieldIndex = 2;
                }
            case 2:
                if (emptyFieldIndex < 0 && (txtgender.getValue() == null)) {
                    txtgender.requestFocus();
                    emptyFieldIndex = 3;
                }
            case 3:
                if (emptyFieldIndex < 0 && (txtid.getText().isBlank())) {
                    txtid.requestFocus();
                    emptyFieldIndex = 4;
                }
            case 4:
                if (emptyFieldIndex < 0 && (selectdate.getValue() == null)) {
                    selectdate.requestFocus();
                    emptyFieldIndex = 5;
                }

            case 5:
                if (emptyFieldIndex < 0 && (txttime.getText().isBlank())) {
                    txttime.requestFocus();
                    emptyFieldIndex = 6;
                }
                break;
            default:
                break;
        }
    }

}
