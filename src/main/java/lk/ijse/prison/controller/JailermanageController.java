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
import lk.ijse.prison.bo.coustom.JailerBO;
import lk.ijse.prison.dto.CellDTO;
import lk.ijse.prison.dto.JailerDTO;
import lk.ijse.prison.dto.ScheduleDTO;
import lk.ijse.prison.view.tdm.JailerTM;
import lk.ijse.prison.view.tdm.ScheduleTM;
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
import java.util.List;
import java.util.ResourceBundle;

public class JailermanageController implements Initializable , Runnable{
    //Tables
    public TableView <JailerTM>tableJailer;
    public TableView <ScheduleTM>tableJailerSchedule;
    @FXML
    private Label lbltime;
    @FXML
    private Label lblDate;
    @FXML
    private Label lblsearch;
    @FXML
    private Label shlblsearch;
    @FXML
    private AnchorPane jaileranchorpane;
    //jailer add textfealds
    @FXML
    private TextField txtjlid;
    @FXML
    private JFXComboBox<String> txtgender;
    @FXML
    private TextField txtname;
    @FXML
    private DatePicker txtbirth;
    @FXML
    private TextField txtaddress;
    @FXML
    private TextField txtnic;
    @FXML
    private TextField txtrank;
    //jailer schedule txtfealds
    @FXML
    private TextField txtjailerid;
    @FXML
    private TextField txtbulno;
    @FXML
    private TextField txtshiflen;
    @FXML
    private TextField txtweptype;
    //jailer Table
    @FXML
    private TableColumn<JailerDTO, String> tablejid;
    @FXML
    private TableColumn<JailerDTO, String> tablename;
    @FXML
    private TableColumn<JailerDTO, String> tablegender;
    @FXML
    private TableColumn<JailerDTO, LocalDate> tablebirthday;
    @FXML
    private TableColumn<JailerDTO, String> tableaddress;
    @FXML
    private TableColumn<JailerDTO, String> tablenic;
    @FXML
    private TableColumn<JailerDTO, String> tablerank;
    //jailer Schedule Table
    @FXML
    private TableColumn tableShId;
    @FXML
    private TableColumn tableShBlNo;
    @FXML
    private TableColumn tableShShift;
    @FXML
    private TableColumn tableShWeapon;

    private Boolean isCameraEnabled = true;
    public ImageView panePhoto;
    @FXML
    private JFXButton btnCapture;
    private volatile boolean isCapturing;

    private FrameGrabber grabber;
    private Java2DFrameConverter converter;
    private BufferedImage image;
    //DI
    JailerBO jailerBO = BoFactory.getBoFactory().getBO(BoFactory.BOTypes.JAILER);

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
                btnCapture.setStyle("-fx-background-color:    #158392;");

            }}
    }
    public void run() {
        try {
            // Continuously capture photos until isCapturing is set to false
            grabber.start();
            while (isCapturing) {
                Frame frame = grabber.grab();
                image = converter.convert(frame);
                Image fxImage = SwingFXUtils.toFXImage(image, null);
                panePhoto.setImage(fxImage); // set Image to ImageView pane
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

        getAll();
        initializeTableColumns();
        getAllSchedule();
        initializeTableShedule();
        setSelectJlDetailToTxt();
        setSelectJlScheduleToTxt();
        setDate();
        setTime();
    }
    private void setSelectJlScheduleToTxt() {
        tableJailerSchedule.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txtjailerid.setText(newSelection.getId());
                txtbulno.setText(newSelection.getBuildNo());
                txtshiflen.setText(newSelection.getShift());
                txtweptype.setText(newSelection.getWeapon());

            }
        });
    }
    private void getAllSchedule() {
        try {
            ObservableList<ScheduleTM> obList = FXCollections.observableArrayList();
            List<ScheduleDTO> sheduleList = jailerBO.getAllSchedule();

            for (ScheduleDTO scheduleDTO : sheduleList) {
                obList.add(new ScheduleTM(
                        scheduleDTO.getId(),
                        scheduleDTO.getBuildNo(),
                        scheduleDTO.getShift(),
                        scheduleDTO.getWeapon()
                ));
            }
            tableJailerSchedule.setItems(obList);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "SQL Error!").show();
        }
    }

    private void initializeTableShedule() {
        tableShId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableShBlNo.setCellValueFactory(new PropertyValueFactory<>("buildNo"));
        tableShShift.setCellValueFactory(new PropertyValueFactory<>("shift"));
        tableShWeapon.setCellValueFactory(new PropertyValueFactory<>("weapon"));
    }
//get all jailer database data
    private void getAll() {
        try {
            ObservableList<JailerTM> obList = FXCollections.observableArrayList();
            List<JailerDTO> jailerDTOList = jailerBO.getAll();

            for (JailerDTO jailerDTO : jailerDTOList) {
                obList.add(new JailerTM(
                        jailerDTO.getId(),
                        jailerDTO.getName(),
                        jailerDTO.getGender(),
                        jailerDTO.getBirth(),
                        jailerDTO.getAdress(),
                        jailerDTO.getNic(),
                        jailerDTO.getRank()
                ));
            }
            tableJailer.setItems(obList);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "SQL Error!").show();
        }
    }
    //set data to tables
    private void initializeTableColumns() {
        tablejid.setCellValueFactory(new PropertyValueFactory<>("id"));
        tablename.setCellValueFactory(new PropertyValueFactory<>("name"));
        tablegender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        tablebirthday.setCellValueFactory(new PropertyValueFactory<>("birth"));
        tableaddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        tablenic.setCellValueFactory(new PropertyValueFactory<>("nic"));
        tablerank.setCellValueFactory(new PropertyValueFactory<>("rank"));

    }
    //set select data to textfeald from database
    private void setSelectJlDetailToTxt() {
        tableJailer.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txtjlid.setText(newSelection.getId());
                txtgender.setValue(newSelection.getGender());
                txtname.setText(newSelection.getName());
                txtbirth.setValue(LocalDate.parse(newSelection.getBirth()));
                txtaddress.setText(newSelection.getAddress());
                txtnic.setText(newSelection.getNic());
                txtrank.setText(newSelection.getRank());
            }
        });
    }

    //navigate
    @FXML
    private void dashOnAction(MouseEvent mouseEvent) throws IOException {
        isCapturing = false;
        Stage stage = (Stage) jaileranchorpane.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/dashboard.fxml"))));
        stage.setTitle("Dashboard");
        stage.centerOnScreen();
        stage.show();
    }
    @FXML
    private void jailerprimanage(ActionEvent actionEvent) throws IOException {
        isCapturing = false;
        Stage stage = (Stage) jaileranchorpane.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/prisonermanageform.fxml"))));
        stage.setTitle("Prisoner manage");
        stage.centerOnScreen();
        stage.show();
    }

    @FXML
    private void jailerstaffmanage(ActionEvent actionEvent) throws IOException {
        isCapturing = false;
        Stage stage = (Stage) jaileranchorpane.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/staffmanagerform.fxml"))));
        stage.setTitle("Staff manage");
        stage.centerOnScreen();
        stage.show();
    }

    @FXML
    private void jailervisitmanage(ActionEvent actionEvent) throws IOException {
        isCapturing = false;
        Stage stage = (Stage) jaileranchorpane.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/visitormanageform.fxml"))));
        stage.setTitle("Visit manage");
        stage.centerOnScreen();
        stage.show();
    }

    @FXML
    private void jailercellmanage(ActionEvent actionEvent) throws IOException {
        isCapturing = false;
        Stage stage = (Stage) jaileranchorpane.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/cellmanageform.fxml"))));
        stage.setTitle("Prison cells manage");
        stage.centerOnScreen();
        stage.show();
    }

    @FXML
    private void jailercasemanage(ActionEvent actionEvent) throws IOException {
        isCapturing = false;
        Stage stage = (Stage) jaileranchorpane.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/casesmanageform.fxml"))));
        stage.setTitle("Cases manage");
        stage.centerOnScreen();
        stage.show();
    }
    @FXML
    private void adminmanage(MouseEvent mouseEvent) throws IOException {
        isCapturing = false;
        Stage stage = (Stage) jaileranchorpane.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/adminform.fxml"))));
        stage.setTitle("Admin manage");
        stage.centerOnScreen();
        stage.show();

    }
    @FXML
    private void logout(ActionEvent actionEvent) throws IOException {
        isCapturing = false;
        Stage stage = (Stage) jaileranchorpane.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/loginform.fxml"))));
        stage.setTitle("Login");
        stage.centerOnScreen();
        stage.show();
    }
//jailer add actions
    @FXML
    void btnSave(ActionEvent actionEvent) {
        lblsearch.setText("");
        if(!txtjlid.getText().isBlank() && !txtname.getText().isBlank() && txtgender.getValue()!= null && txtbirth.getValue() != null && !txtaddress.getText().isBlank() && !txtnic.getText().isBlank() && !txtrank.getText().isBlank() ) {
          if (btnCapture.getText().equals("Open Camera") && panePhoto.getImage() != null){
            String id = txtjlid.getText();
            String name = txtname.getText();
            String gender =txtgender.getValue();
            LocalDate selectedDate = txtbirth.getValue();
            String birth = selectedDate.toString();
            String address = txtaddress.getText();
            String nic = txtnic.getText();
            String rank = txtrank.getText();

            try {
                boolean isSaved = jailerBO.save(new JailerDTO(id,name,gender,birth,address,nic,rank));
                if (isSaved) {
                    String imageName = id +".png"; // Construct the file name
                    File outputFile = new File(imageName);
                    try {
                        ImageIO.write(image, "png", outputFile);
                    } catch (IOException e) {
                        new Alert(Alert.AlertType.ERROR, "Failed to save the image!").show();
                        return;
                    }
                    new Alert(Alert.AlertType.CONFIRMATION, "Jailer Details saved!").show();
                    getAll();
                    initializeTableColumns();
                }
            } catch (SQLException | ClassNotFoundException e) {
                new Alert(Alert.AlertType.ERROR, "Jailer Already Saved!").show();
                txtClear();
            }
          }else{new Alert(Alert.AlertType.ERROR, "Please Take Picture For Save Details!").show(); }
        }else{new Alert(Alert.AlertType.ERROR, "Please Fill Details!").show(); }
    }
    @FXML
    void btnSearch(ActionEvent actionEvent) {
        lblsearch.setText("");
        if(!txtjlid.getText().isBlank()) {
            try {
                JailerDTO jailerDTO = jailerBO.search(txtjlid.getText());
                if (jailerDTO != null) {
                    searchJl();
                    txtjlid.setText(jailerDTO.getId());
                    txtname.setText(jailerDTO.getName());
                    txtgender.setValue(jailerDTO.getGender());
                    txtbirth.setValue(LocalDate.parse(jailerDTO.getBirth()));
                    txtaddress.setText(jailerDTO.getAdress());
                    txtnic.setText(jailerDTO.getNic());
                    txtrank.setText(jailerDTO.getRank());
                    lblsearch.requestFocus();

                    File file = new File(jailerDTO.getId() + ".png");
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
                    new Alert(Alert.AlertType.ERROR, "Jailer Id Not Exist!").show();
                }
            } catch (SQLException | ClassNotFoundException e) {
                new Alert(Alert.AlertType.ERROR, "Please Check Details & Try Again!").show();
                txtClear();
            }
        }else{new Alert(Alert.AlertType.ERROR, "Please Fill Jailer Id!").show(); }
    }
    private void searchJl(){
        tableJailer.getItems().stream()
                .filter(item -> item.getId().equals(txtjlid.getText()) )
                .findAny()
                .ifPresent(item -> {
                    tableJailer.getSelectionModel().select(item);
                    tableJailer.scrollTo(item);
                });

    }
    @FXML
    void btnDelete(ActionEvent actionEvent) {
        lblsearch.setText("");
        String jailerId = txtjlid.getText();
        if(!txtjlid.getText().isBlank()) {
            try {
                boolean isDeleted = jailerBO.delete(jailerId);
                if (isDeleted) {
                    String imageName = txtjlid.getText() + ".png";
                    File outputFile = new File(imageName);
                    if (outputFile.delete()) {
                        System.out.println("Image file deleted successfully!");
                    } else {
                        System.out.println("Failed to delete image file!");
                    }
                    new Alert(Alert.AlertType.CONFIRMATION, "Jailer Deleted!").show();
                    txtClear();
                    getAll();
                    initializeTableColumns();
                    getAllSchedule();
                    initializeTableShedule();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Jailer Id Not Exist!").show();
                }
            } catch (SQLException | ClassNotFoundException e) {
                new Alert(Alert.AlertType.ERROR, "Please Check Details & Try Again!").show();
                txtClear();
            }
        }else{new Alert(Alert.AlertType.ERROR, "Please Fill Jailer Id!").show(); }
    }
    @FXML
    void btnUpdate(ActionEvent actionEvent) {
        lblsearch.setText("");
        if(!txtjlid.getText().isBlank() && !txtname.getText().isBlank() && txtgender.getValue()!= null && txtbirth.getValue() != null && !txtaddress.getText().isBlank() && !txtnic.getText().isBlank() && !txtrank.getText().isBlank()) {
            String id = txtjlid.getText();
            String name = txtname.getText();
            String gender =txtgender.getValue();
            LocalDate selectedDate = txtbirth.getValue();
            String birth = selectedDate.toString();
            String address = txtaddress.getText();
            String nic = txtnic.getText();
            String rank = txtrank.getText();

            try {
                boolean isUpdated = jailerBO.update(new JailerDTO(id,name,gender,birth,address,nic,rank));
                if(isUpdated){
                new Alert(Alert.AlertType.CONFIRMATION, "Jailer Details updated!").show();
                    getAll();
                    initializeTableColumns();
                    JailerDTO jail = jailerBO.search(txtjlid.getText());
                    if (btnCapture.getText().equals("Open Camera") && panePhoto.getImage() != null){
                        String imageName = jail.getId() +".png"; // Construct the file name
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
                    new Alert(Alert.AlertType.ERROR, "Jailer Id Not Exist!").show();
                }
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Please Check Details & Try Again!").show();
                txtClear();
            }
        }else{new Alert(Alert.AlertType.ERROR, "Please Fill Details!").show(); }
    }
    @FXML
    void btnClear(ActionEvent actionEvent) {
        txtClear();
    }
//jailer schedule action
    @FXML
    void shSave(ActionEvent actionEvent) {
        shlblsearch.setText("");
        String id = txtjailerid.getText();
        String buildNo = txtbulno.getText();
        String shift = txtshiflen.getText();
        String weapon = txtweptype.getText();

        if(!txtjailerid.getText().isBlank() && !txtbulno.getText().isBlank() && !txtshiflen.getText().isBlank() && !txtweptype.getText().isBlank()) {
            try {
                boolean jailer = jailerBO.exist(id);
                boolean build = jailerBO.existCell(buildNo);
                if(jailer == false && build) {new Alert(Alert.AlertType.ERROR, "Jailer Id Not Exist!").show();}

                if(jailer && build == false)  {new Alert(Alert.AlertType.ERROR, "Building ID Not Exist!").show();}
                if(!jailer && !build) {
                    new Alert(Alert.AlertType.ERROR, "Building ID & Jailer Id Not Exist!").show();
                }
                if(jailer && build) {
                    boolean exist = jailerBO.existSchedule(id,buildNo);
                    if(exist) {
                        shlblsearch.setText("Schedule Details Alredy \nRegisterd!");
                    }
                    else {
                        boolean isSaved = jailerBO.saveSchedule(new ScheduleDTO(id,buildNo,shift,weapon));
                        if (isSaved) {
                            new Alert(Alert.AlertType.CONFIRMATION, "Jailer Schedule saved!").show();
                            getAllSchedule();
                            initializeTableShedule();
                        }
                    }
                }
            } catch (SQLException | ClassNotFoundException e) {
            }
        }else{new Alert(Alert.AlertType.ERROR, "Please Fill Details!").show(); }

    }
    @FXML
    void shSearch(ActionEvent actionEvent) {
        shlblsearch.setText("");
        if(!txtjailerid.getText().isBlank() && !txtbulno.getText().isBlank()) {
            try {
                ScheduleDTO scheduleDTO = jailerBO.searchSchedule(txtjailerid.getText(),txtbulno.getText());
                if (scheduleDTO != null) {
                    searchSh();
                    txtjailerid.setText(scheduleDTO.getId());
                    txtbulno.setText(scheduleDTO.getBuildNo());
                    txtshiflen.setText(scheduleDTO.getShift());
                    txtweptype.setText(scheduleDTO.getWeapon());

                } else {
                    new Alert(Alert.AlertType.ERROR, "Jailer Id or Building Id Not Exist!").show();
                }
            } catch (SQLException | ClassNotFoundException e) {
                new Alert(Alert.AlertType.ERROR, "Please Check Details & Try Again!").show();
                shtxtClear();
            }
        }else{new Alert(Alert.AlertType.ERROR, "Please Fill Jailer Id & Building Id!").show(); }

    }
    private void searchSh(){
        tableJailerSchedule.getItems().stream()
                .filter(item -> item.getId().equals(txtjailerid.getText()) )
                .findAny()
                .ifPresent(item -> {
                    tableJailerSchedule.getSelectionModel().select(item);
                    tableJailerSchedule.scrollTo(item);
                });

    }
    @FXML
    void shDelete(ActionEvent actionEvent) {
        shlblsearch.setText("");
        String jailerId = txtjailerid.getText();
        String buldId = txtbulno.getText();
        if(!txtjailerid.getText().isBlank() && !txtbulno.getText().isBlank()) {
            try {
                boolean isDeleted = jailerBO.deleteSchedule(jailerId,buldId);
                if (isDeleted) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Schedule Deleted!").show();
                    shtxtClear();
                    getAllSchedule();
                    initializeTableShedule();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Jailer Id or Building Id Not Exist!").show();
                }
            } catch (SQLException | ClassNotFoundException e) {
                new Alert(Alert.AlertType.ERROR, "Please Check Details & Try Again!").show();
                shtxtClear();
            }
        }else{new Alert(Alert.AlertType.ERROR, "Please Fill Jailer Id & Building Id!").show(); }

    }
    @FXML
    void shUpdate(ActionEvent actionEvent) {
        shlblsearch.setText("");
        String id = txtjailerid.getText();
        String buildNo = txtbulno.getText();
        String shift = txtshiflen.getText();
        String weapon = txtweptype.getText();

        if(!txtjailerid.getText().isBlank() && !txtbulno.getText().isBlank() && !txtshiflen.getText().isBlank() && !txtweptype.getText().isBlank()) {
            try {
                boolean isUpdated = jailerBO.updateSchedule(new ScheduleDTO(id,buildNo,shift,weapon));
                if(isUpdated){
                    new Alert(Alert.AlertType.CONFIRMATION, "Jailer Schedule updated!").show();
                    getAllSchedule();
                    initializeTableShedule();
                }
                else {
                    new Alert(Alert.AlertType.ERROR, "Jailer Schedule Not Exist!").show();
                }
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Please Check Details & Try Again!").show();
                txtClear();
            }
        }else{new Alert(Alert.AlertType.ERROR, "Please Fill Details!").show(); }

    }
    @FXML
    void shClear(ActionEvent actionEvent) {
        shtxtClear();
    }

    @SneakyThrows
    @FXML
    private void txtClear() {
        txtjlid.setText("");
        txtname.setText("");
        txtgender.setValue(null);
        txtbirth.setValue(null);
        txtaddress.setText("");
        txtnic.setText("");
        txtrank.setText("");
        lblsearch.setText("");
        isCapturing = false;
        isCameraEnabled = false;
        grabber.stop();
        panePhoto.setImage(null);
        btnCapture.setText("Open Camera");
        btnCapture.setStyle("-fx-background-color:    #158392;");

    }
    @FXML
    private void shtxtClear() {
        txtjailerid.setText("");
        txtbulno.setText("");
        txtshiflen.setText("");
        txtweptype.setText("");
        shlblsearch.setText("");
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
    void jlIdOnAction(ActionEvent actionEvent) {
        if(!txtjlid.getText().isBlank()) {
            try {
                JailerDTO jailerDTO = jailerBO.search(txtjlid.getText());
                if (jailerDTO != null) {
                    searchJl();
                    txtjlid.setText(jailerDTO.getId());
                    txtname.setText(jailerDTO.getName());
                    txtgender.setValue(jailerDTO.getGender());
                    txtbirth.setValue(LocalDate.parse(jailerDTO.getBirth()));
                    txtaddress.setText(jailerDTO.getAdress());
                    txtnic.setText(jailerDTO.getNic());
                    txtrank.setText(jailerDTO.getRank());
                    lblsearch.setText("Jailer Details Alredy \nRegisterd!");
                    lblsearch.requestFocus();
                } else {lblsearch.setText("Jailer Id Not Exist!");
                    jldetailTxt();
                }
            } catch (SQLException | ClassNotFoundException e) {lblsearch.setText("Please Check Details & \nTry Again!");
                txtClear();
            }
        }else{lblsearch.setText("Please Fill Jailer Id!"); }
    }

    @FXML
    void nameOnAction(ActionEvent actionEvent) {
        if (!txtname.getText().isBlank()){
            jldetailTxt();
        }
    }
    @FXML
    void genderOnAction(ActionEvent actionEvent) {
        if (txtgender.getValue() != null){
            jldetailTxt();
        }
    }
    @FXML
    void BirthOnAction(ActionEvent actionEvent) {
        if (txtbirth.getValue() != null){
            jldetailTxt();
        }
    }
    @FXML
    void addressOnAction(ActionEvent actionEvent) {
        if (!txtaddress.getText().isBlank()){
            jldetailTxt();
        }
    }
    @FXML
    void nicOnAction(ActionEvent actionEvent) {
        if (!txtnic.getText().isBlank()){
            jldetailTxt();
        }
    }
    @FXML
    void rankOnAction(ActionEvent actionEvent) {
        if (!txtrank.getText().isBlank()){
            jldetailTxt();
        }
    }
    @FXML
    private void jldetailTxt(){
        int emptyFieldIndex = -1;
        switch (emptyFieldIndex) {
            case -1:
                if (txtjlid.getText().isBlank()) {
                    txtjlid.requestFocus();
                    emptyFieldIndex = 0;
                }
            case 0:
                if (emptyFieldIndex < 0 && (txtname.getText().isBlank())) {
                    txtname.requestFocus();
                    emptyFieldIndex = 1;
                }
            case 1:
                if (emptyFieldIndex < 0 && (txtgender.getValue() == null)) {
                    txtgender.requestFocus();
                    emptyFieldIndex = 2;
                }
            case 2:
                if (emptyFieldIndex < 0 && (txtbirth.getValue() == null)) {
                    txtbirth.requestFocus();
                    emptyFieldIndex = 3;
                }
            case 3:
                if (emptyFieldIndex < 0 && (txtaddress.getText().isBlank())) {
                    txtaddress.requestFocus();
                    emptyFieldIndex = 4;
                }
            case 4:
                if (emptyFieldIndex < 0 && (txtnic.getText().isBlank())) {
                    txtnic.requestFocus();
                    emptyFieldIndex = 5;
                }

            case 5:
                if (emptyFieldIndex < 0 && (txtrank.getText().isBlank())) {
                    txtrank.requestFocus();
                    emptyFieldIndex = 6;
                }
                break;
            default:
                break;
        }
    }
    @FXML
    private void shdetailTxt(){
        int emptyFieldIndex = -1;
        switch (emptyFieldIndex) {
            case -1:
                if (txtjailerid.getText().isBlank()) {
                    txtjailerid.requestFocus();
                    emptyFieldIndex = 0;
                }
            case 0:
                if (emptyFieldIndex < 0 && (txtbulno.getText().isBlank())) {
                    txtbulno.requestFocus();
                    emptyFieldIndex = 1;
                }
            case 1:
                if (emptyFieldIndex < 0 && (txtshiflen.getText().isBlank())) {
                    txtshiflen.requestFocus();
                    emptyFieldIndex = 2;
                }
            case 2:
                if (emptyFieldIndex < 0 && (txtweptype.getText().isBlank())) {
                    txtweptype.requestFocus();
                    emptyFieldIndex = 3;
                }
                break;
            default:
                break;
        }
    }

    @FXML
    void buildOnAction(ActionEvent actionEvent) {
        if(!txtbulno.getText().isBlank()){
            try {
                CellDTO cellDTO = jailerBO.searchCell(txtbulno.getText());
                if (cellDTO != null) {
                    shlblsearch.setText("");
                    enterSearch();shdetailTxt();
                } else {shlblsearch.setText("Building Id Not Exist!");
                }
            } catch (SQLException | ClassNotFoundException e) {shlblsearch.setText("Please Check Details\n & Try Again!");
                txtClear();
            }
        }
        else{shlblsearch.setText("Please Fill Building NO!");
        }
    }

    @FXML
    void shiftOnAction(ActionEvent actionEvent) {
        if (!txtshiflen.getText().isBlank()){
            shdetailTxt();
        }
    }

    @FXML
    void weaponOnAction(ActionEvent actionEvent) {
        if (!txtweptype.getText().isBlank()){
            shdetailTxt();
        }
    }

    @FXML
    void shidOnAction(ActionEvent actionEvent) {
        if(!txtjailerid.getText().isBlank()){
            shlblsearch.setText("");
                try {
                    JailerDTO jailerDTO = jailerBO.search(txtjailerid.getText());
                    if (jailerDTO != null) {
                        shlblsearch.setText("");
                        enterSearch();shdetailTxt();
                    } else {shlblsearch.setText("Jailer Id Not Exist!");
                    }
                } catch (SQLException | ClassNotFoundException e) {shlblsearch.setText("Please Check Details\n & Try Again!");
                    txtClear();
                }
        }
        else{ shlblsearch.setText("Please Fill Jailer Id!");
        }
    }
    @FXML
    void enterSearch() {
        if(!txtjailerid.getText().isBlank() && !txtbulno.getText().isBlank()) {
            try {
                ScheduleDTO scheduleDTO = jailerBO.searchSchedule(txtjailerid.getText(),txtbulno.getText());
                if (scheduleDTO != null) {
                    searchSh();
                    txtjailerid.setText(scheduleDTO.getId());
                    txtbulno.setText(scheduleDTO.getBuildNo());
                    txtshiflen.setText(scheduleDTO.getShift());
                    txtweptype.setText(scheduleDTO.getWeapon());
                    shlblsearch.setText("Schedule Details Alredy \nRegisterd!");
                    shlblsearch.requestFocus();
                } else {
                    shdetailTxt();
                }
            } catch (SQLException | ClassNotFoundException e) {}
        }else{ shdetailTxt(); }
    }


}