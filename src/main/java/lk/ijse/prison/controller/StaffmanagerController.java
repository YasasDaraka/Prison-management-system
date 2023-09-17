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
import lk.ijse.prison.bo.coustom.StaffBO;
import lk.ijse.prison.dto.CellDTO;
import lk.ijse.prison.dto.StaffDTO;
import lk.ijse.prison.view.tdm.StaffTM;
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

public class StaffmanagerController implements Initializable , Runnable{
    @FXML
    private AnchorPane staffanchor;
    public TableView<StaffTM> tableStaff;
    @FXML
    private Label lbltime;
    @FXML
    private Label lblsearch;
    @FXML
    private Label lblDate;
    //staff Textfealds
    @FXML
    private TextField txtemid;
    @FXML
    private JFXComboBox<String> txtegender;
    @FXML
    private TextField txtemname;
    @FXML
    private DatePicker selectbirth;
    @FXML
    private TextField txtaddress;
    @FXML
    private TextField txtnic;
    @FXML
    private TextField txtposition;
    @FXML
    private TextField txtbuildno;
    //Staff Table
    @FXML
    private TableColumn tableempid;
    @FXML
    private TableColumn tablename;
    @FXML
    private TableColumn tablegender;
    @FXML
    private TableColumn tablebirthday;
    @FXML
    private TableColumn tableaddress;
    @FXML
    private TableColumn tablenic;
    @FXML
    private TableColumn tablepos;
    @FXML
    private TableColumn tablebildno;

    private Boolean isCameraEnabled = true;
    public ImageView panePhoto;
    @FXML
    private JFXButton btnCapture;
    private volatile boolean isCapturing;
    private FrameGrabber grabber;
    private Java2DFrameConverter converter;
    private BufferedImage image;

    StaffBO staffBO = BoFactory.getBoFactory().getBO(BoFactory.BOTypes.STAFF);
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
        txtegender.setItems(gen);

        grabber = new OpenCVFrameGrabber(0);
        converter = new Java2DFrameConverter();

        initializeTableColumns();
        setSelectStaffToTxt();
        getAll();
        setDate();
        setTime();
    }

    private void getAll() {
        try {
            ObservableList<StaffTM> obList = FXCollections.observableArrayList();
            List<StaffDTO> stList = staffBO.getAll();

            for (StaffDTO staffDTO : stList) {
                obList.add(new StaffTM(
                        staffDTO.getId(),
                        staffDTO.getName(),
                        staffDTO.getGender(),
                        staffDTO.getBirth(),
                        staffDTO.getAddress(),
                        staffDTO.getNic(),
                        staffDTO.getPosition(),
                        staffDTO.getBuildNo()
                ));
            }
            tableStaff.setItems(obList);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "SQL Error!").show();
        }
    }
//set database details to table
    private void initializeTableColumns() {
        tableempid.setCellValueFactory(new PropertyValueFactory<>("id"));
        tablename.setCellValueFactory(new PropertyValueFactory<>("name"));
        tablegender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        tablebirthday.setCellValueFactory(new PropertyValueFactory<>("birth"));
        tableaddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        tablenic.setCellValueFactory(new PropertyValueFactory<>("nic"));
        tablepos.setCellValueFactory(new PropertyValueFactory<>("position"));
        tablebildno.setCellValueFactory(new PropertyValueFactory<>("buildNo"));
    }
    //set select data to textfealds from table
    private void setSelectStaffToTxt() {
        tableStaff.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txtemid.setText(newSelection.getId());
                txtegender.setValue(newSelection.getGender());
                txtemname.setText(newSelection.getName());
                selectbirth.setValue(LocalDate.parse(newSelection.getBirth()));
                txtaddress.setText(newSelection.getAddress());
                txtnic.setText(newSelection.getNic());
                txtposition.setText(newSelection.getPosition());
                txtbuildno.setText(newSelection.getBuildNo());

            }
        });
    }
    //Navigate
    @FXML
    private void dashOnAction(MouseEvent mouseEvent) throws IOException {
        isCapturing = false;
        Stage stage = (Stage) staffanchor.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/dashboard.fxml"))));
        stage.setTitle("Dashboard");
        stage.centerOnScreen();
        stage.show();
    }
    @FXML
     void staffprmanage(ActionEvent actionEvent) throws IOException {
        isCapturing = false;
        Stage stage = (Stage) staffanchor.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/prisonermanageform.fxml"))));
        stage.setTitle("Prisoner manage");
        stage.centerOnScreen();
        stage.show();
    }
    @FXML
    void staffjailermanage(ActionEvent actionEvent) throws IOException {
        isCapturing = false;
        Stage stage = (Stage) staffanchor.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/jailermanageform.fxml"))));
        stage.setTitle("Jailer manage");
        stage.centerOnScreen();
        stage.show();
    }
    @FXML
    void staffvisitmanage(ActionEvent actionEvent) throws IOException {
        isCapturing = false;
        Stage stage = (Stage) staffanchor.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/visitormanageform.fxml"))));
        stage.setTitle("Visit manage");
        stage.centerOnScreen();
        stage.show();
    }
    @FXML
    void staffcellmanage(ActionEvent actionEvent) throws IOException {
        isCapturing = false;
        Stage stage = (Stage) staffanchor.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/cellmanageform.fxml"))));
        stage.setTitle("Prison cells manage");
        stage.centerOnScreen();
        stage.show();
    }
    @FXML
    void staffcasesmanage(ActionEvent actionEvent) throws IOException {
        isCapturing = false;
        Stage stage = (Stage) staffanchor.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/casesmanageform.fxml"))));
        stage.setTitle("Cases manage");
        stage.centerOnScreen();
        stage.show();
    }
    @FXML
    void adminmanage(MouseEvent mouseEvent) throws IOException {
        isCapturing = false;
        Stage stage = (Stage) staffanchor.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/adminform.fxml"))));
        stage.setTitle("Admin manage");
        stage.centerOnScreen();
        stage.show();
    }
    @FXML
    void logout(ActionEvent actionEvent) throws IOException {
        isCapturing = false;
            Stage stage = (Stage) staffanchor.getScene().getWindow();
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/loginform.fxml"))));
            stage.setTitle("Login");
            stage.centerOnScreen();
            stage.show();
    }
//staff Actions
    @FXML
    void btnSave(ActionEvent actionEvent) {
        lblsearch.setText("");
        if(!txtemid.getText().isBlank() && !txtemname.getText().isBlank() && txtegender.getValue()!= null && selectbirth.getValue() != null && !txtaddress.getText().isBlank() && !txtnic.getText().isBlank() && !txtposition.getText().isBlank() && !txtbuildno.getText().isBlank()) {
            if (btnCapture.getText().equals("Open Camera") && panePhoto.getImage() != null){
            String id = txtemid.getText();
            String name = txtemname.getText();
            String gender =txtegender.getValue();
            LocalDate selectedDate = selectbirth.getValue();
            String birth = selectedDate.toString();
            String address = txtaddress.getText();
            String nic = txtnic.getText();
            String position = txtposition.getText();
            String buildNo = txtbuildno.getText();

            try {
                boolean build = staffBO.existCell(buildNo);
                if (build) {
                    boolean isSaved = staffBO.save(new StaffDTO(id,name,gender,birth,address,nic,position,buildNo));
                    if (isSaved) {
                        String imageName = id + ".png"; // Construct the file name
                        File outputFile = new File(imageName);
                        try {
                            ImageIO.write(image, "png", outputFile);
                        } catch (IOException e) {
                            new Alert(Alert.AlertType.ERROR, "Failed to save the image!").show();
                            return;
                        }
                        new Alert(Alert.AlertType.CONFIRMATION, "Employee saved!").show();
                        getAll();
                        initializeTableColumns();
                    }
                }if(build == false)  {new Alert(Alert.AlertType.ERROR, "Building ID Not Exist!").show();}
            } catch (SQLException | ClassNotFoundException e) {
                new Alert(Alert.AlertType.ERROR, "Please Fill Valid Details!").show();
                txtClear();
            }
          }else{new Alert(Alert.AlertType.ERROR, "Please Take Picture For Save Details!").show(); }
        }else{new Alert(Alert.AlertType.ERROR, "Please Fill Details!").show(); }
    }
    @FXML
    void btnSearch(ActionEvent actionEvent) {
        lblsearch.setText("");
        if(!txtemid.getText().isBlank()) {
            try {
                StaffDTO staffDTO = staffBO.search(txtemid.getText());
                if (staffDTO != null) {
                    search();
                    txtemid.setText(staffDTO.getId());
                    txtemname.setText(staffDTO.getName());
                    txtegender.setValue(staffDTO.getGender());
                    selectbirth.setValue(LocalDate.parse(staffDTO.getBirth()));
                    txtaddress.setText(staffDTO.getAddress());
                    txtnic.setText(staffDTO.getNic());
                    txtposition.setText(staffDTO.getPosition());
                    txtbuildno.setText(staffDTO.getBuildNo());
                    lblsearch.requestFocus();

                    File file = new File(staffDTO.getId() + ".png");
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
                    new Alert(Alert.AlertType.ERROR, "Employee Id Not Exist!").show();
                }
            } catch (SQLException | ClassNotFoundException e) {
                new Alert(Alert.AlertType.ERROR, "Please Check Details & Try Again!").show();
                txtClear();
            }
        }else{new Alert(Alert.AlertType.ERROR, "Please Fill Employee Id!").show(); }
    }
    private void search(){
        tableStaff.getItems().stream()
                .filter(item -> item.getId().equals(txtemid.getText()) )
                .findAny()
                .ifPresent(item -> {
                    tableStaff.getSelectionModel().select(item);
                    tableStaff.scrollTo(item);
                });

    }
    @FXML
    void btnDelete(ActionEvent actionEvent) {
        lblsearch.setText("");
        String empId = txtemid.getText();
        if(!txtemid.getText().isBlank()) {
            try {
                boolean isDeleted = staffBO.delete(empId);
                if (isDeleted) {
                    String imageName = empId + ".png";
                    File outputFile = new File(imageName);
                    if (outputFile.delete()) {
                        System.out.println("Image file deleted successfully!");
                    } else {
                        System.out.println("Failed to delete image file!");
                    }
                    new Alert(Alert.AlertType.CONFIRMATION, "Employee Deleted!").show();
                    txtClear();
                    getAll();
                    initializeTableColumns();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Employee Id Not Exist!").show();
                }
            } catch (SQLException | ClassNotFoundException e) {
                new Alert(Alert.AlertType.ERROR, "Please Check Details & Try Again!").show();
                txtClear();
            }
        }else{new Alert(Alert.AlertType.ERROR, "Please Fill Employee Id!").show(); }
    }
    @FXML
    void btnClear(ActionEvent actionEvent) {
        txtClear();
    }

    @FXML
    void btnUpdate(ActionEvent actionEvent) {
        lblsearch.setText("");
        if(!txtemid.getText().isBlank() && !txtemname.getText().isBlank() && txtegender.getValue()!= null && selectbirth.getValue() != null && !txtaddress.getText().isBlank() && !txtnic.getText().isBlank() && !txtposition.getText().isBlank() && !txtbuildno.getText().isBlank()) {
            String id = txtemid.getText();
            String name = txtemname.getText();
            String gender =txtegender.getValue();
            LocalDate selectedDate = selectbirth.getValue();
            String birth = selectedDate.toString();
            String address = txtaddress.getText();
            String nic = txtnic.getText();
            String position = txtposition.getText();
            String buildNo = txtbuildno.getText();

            try {
                boolean isUpdated = staffBO.update(new StaffDTO(id,name,gender,birth,address,nic,position,buildNo));
                if(isUpdated){
                    new Alert(Alert.AlertType.CONFIRMATION, "Staff Details updated!").show();
                    getAll();
                    initializeTableColumns();
                    StaffDTO staf = staffBO.search(txtemid.getText());
                        if (btnCapture.getText().equals("Open Camera") && panePhoto.getImage() != null){
                            String imageName = staf.getId() +".png"; // Construct the file name
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
                    new Alert(Alert.AlertType.ERROR, "Employee Id Not Exist!").show();
                }
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Building Id Not Exist!").show();
                txtClear();
            }
        }else{new Alert(Alert.AlertType.ERROR, "Please Fill Details!").show(); }
    }
    @SneakyThrows
    @FXML
    void txtClear() {
        txtemid.setText("");
        txtemname.setText("");
        txtegender.setValue(null);
        selectbirth.setValue(null);
        txtaddress.setText("");
        txtnic.setText("");
        txtposition.setText("");
        txtbuildno.setText("");
        lblsearch.setText("");
        isCapturing = false;
        isCameraEnabled = false;
        grabber.stop();
        panePhoto.setImage(null);
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
    void idOnAction(ActionEvent actionEvent) {
        if(!txtemid.getText().isBlank()) {
            try {
                StaffDTO staffDTO = staffBO.search(txtemid.getText());
                if (staffDTO != null) {
                    search();
                    txtemid.setText(staffDTO.getId());
                    txtemname.setText(staffDTO.getName());
                    txtegender.setValue(staffDTO.getGender());
                    selectbirth.setValue(LocalDate.parse(staffDTO.getBirth()));
                    txtaddress.setText(staffDTO.getAddress());
                    txtnic.setText(staffDTO.getNic());
                    txtposition.setText(staffDTO.getPosition());
                    txtbuildno.setText(staffDTO.getBuildNo());
                    lblsearch.setText("Employee Alredy Registerd!");
                    lblsearch.requestFocus();
                } else {lblsearch.setText("Employee Not Exist!\nPlease Fill Employee Details");
                    detailTxt();
                }
            } catch (SQLException | ClassNotFoundException e) {
                lblsearch.setText("Please Check Details & Try Again!");
                txtClear();
            }
        }else{lblsearch.setText("Please Fill Employee Id!");}
    }

    @FXML
    void nameOnAction(ActionEvent actionEvent) {
        if (!txtemname.getText().isBlank()){
            detailTxt();
        }
    }
    @FXML
    void genOnAction(ActionEvent actionEvent) {
        if (txtegender.getValue() != null){
            detailTxt();
        }
    }
    @FXML
    void birthOnAction(ActionEvent actionEvent) {
        if (selectbirth.getValue() != null){
            detailTxt();
        }
    }
    @FXML
    void addressOnAction(ActionEvent actionEvent) {
        if (!txtaddress.getText().isBlank()){
            detailTxt();
        }
    }
    @FXML
    void nicOnAction(ActionEvent actionEvent) {
        if (!txtnic.getText().isBlank()){
            detailTxt();
        }
    }
    @FXML
    void posOnAction(ActionEvent actionEvent) {
        if (!txtposition.getText().isBlank()){
            detailTxt();
        }
    }
    @FXML
    void buildOnAction(ActionEvent actionEvent) {
        if(!txtbuildno.getText().isBlank()){
            try {
                CellDTO cellDTO = staffBO.searchCell(txtbuildno.getText());
                if (cellDTO != null) {
                   lblsearch.setText("");
                    detailTxt();
                } else {lblsearch.setText("Building Id Not Exist!");
                }
            } catch (SQLException | ClassNotFoundException e) {lblsearch.setText("Please Check Details\n & Try Again!");
                txtClear();
            }
        }
        else{lblsearch.setText("Please Fill Building NO!");
        }
    }

    @FXML
    private void detailTxt(){
        int emptyFieldIndex = -1;
        switch (emptyFieldIndex) {
            case -1:
                if (txtemid.getText().isBlank()) {
                    txtemid.requestFocus();
                    emptyFieldIndex = 0;
                }
            case 0:
                if (emptyFieldIndex < 0 && (txtemname.getText().isBlank())) {
                    txtemname.requestFocus();
                    emptyFieldIndex = 1;
                }
            case 1:
                if (emptyFieldIndex < 0 && (txtegender.getValue() == null)) {
                    txtegender.requestFocus();
                    emptyFieldIndex = 2;
                }
            case 2:
                if (emptyFieldIndex < 0 && (selectbirth.getValue() == null)) {
                    selectbirth.requestFocus();
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
                if (emptyFieldIndex < 0 && (txtposition.getText().isBlank())) {
                    txtposition.requestFocus();
                    emptyFieldIndex = 6;
                }
            case 6:
                if (emptyFieldIndex < 0 && (txtbuildno.getText().isBlank())) {
                    txtbuildno.requestFocus();
                    emptyFieldIndex = 7;
                }
                break;
            default:
                break;
        }
    }
}
