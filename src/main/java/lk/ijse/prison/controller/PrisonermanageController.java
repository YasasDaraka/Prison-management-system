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
import lk.ijse.prison.bo.coustom.PrisonerBO;
import lk.ijse.prison.dto.CellDTO;
import lk.ijse.prison.dto.EmergencyDTO;
import lk.ijse.prison.dto.PrisonerDTO;
import lk.ijse.prison.view.tdm.EmergencyTM;
import lk.ijse.prison.view.tdm.PrisonerDetailTM;
import lombok.SneakyThrows;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameGrabber;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class PrisonermanageController implements Initializable , Runnable{


    @FXML
    private JFXComboBox <String> txtmarital;
    @FXML
    private JFXComboBox  <String> txtgender;
    @FXML
    private Label lbltime;
    @FXML
    private Label lblDate;
    @FXML
    private Label lblsearch;
    @FXML
    private Label emlblsearch;
    @FXML
    private AnchorPane prisonachor;
    //Tables
    public TableView <PrisonerDetailTM> tablePrDetail;
    public TableView <EmergencyTM> tableEmDetail;
    //prisoner detail txt
    @FXML
    private TextField txtmatecode;
    @FXML
    private TextField txtsecurity;
    @FXML
    private TextField txtmatename;
    @FXML
    private DatePicker txtbirth;
    @FXML
    private TextField txtblno;

    //Prisoner Table col
    @FXML
    private TableColumn incode;
    @FXML
    private TableColumn name;
    @FXML
    private TableColumn gender;
    @FXML
    private TableColumn birthday;
    @FXML
    private TableColumn marital;
    @FXML
    private TableColumn seclevel;
    @FXML
    private TableColumn bildno;
    //Emergency details
    @FXML
    private TextField txtemid;
    @FXML
    private TextField txtpername;
    @FXML
    private TextField txtrelation;
    @FXML
    private TextField txtcontactno;
    @FXML
    private TextField txtemcode;

    //Emergency Table
    @FXML
    private TableColumn emcode;
    @FXML
    private TableColumn emId;
    @FXML
    private TableColumn emName;
    @FXML
    private TableColumn emRelation;
    @FXML
    private TableColumn emContact;

    PrisonerBO prisonerBO = BoFactory.getBoFactory().getBO(BoFactory.BOTypes.PRISONER);

    private Boolean isCameraEnabled = true;
    public ImageView panePhoto;
    @FXML
    private JFXButton btnCapture;
    private volatile boolean isCapturing;
    private FrameGrabber grabber;
    private Java2DFrameConverter converter;
    private BufferedImage image;

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
                Image fxImage = SwingFXUtils.toFXImage(image, null); // convert to JavaFX Image
                panePhoto.setImage(fxImage); // set Image to ImageView pane
            }
            grabber.stop();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    @FXML
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> gen = FXCollections.observableArrayList("Male","Female");
        txtgender.setItems(gen);

        ObservableList<String> marital = FXCollections.observableArrayList("Married","Unmarried");
        txtmarital.setItems(marital);

        grabber = new OpenCVFrameGrabber(0);
        converter = new Java2DFrameConverter();

        getAllPrDetail();
        setCellValuePrDetail();
        getAllEmergency();
        setCellValueEmergency();
        setSelectPrDetailToTxt();
        setSelectEmergencyToTxt();
        setDate();
        setTime();
    }

//Emergency details

//Get all Emergeancy details from database
    private void getAllEmergency() {
        try {
            ObservableList<EmergencyTM> obList = FXCollections.observableArrayList();
            List<EmergencyDTO> EmList = prisonerBO.getAllEmergency();

            for (EmergencyDTO emergencyDTO : EmList) {
                obList.add(new EmergencyTM(
                        emergencyDTO.getId(),
                        emergencyDTO.getPrId(),
                        emergencyDTO.getName(),
                        emergencyDTO.getRelation(),
                        emergencyDTO.getContact()

                ));
            }
            tableEmDetail.setItems(obList);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "SQL Error!").show();
        }
    }
//set Emergency details to table from database
    private void setCellValueEmergency() {
        emcode.setCellValueFactory(new PropertyValueFactory<>("prId"));
        emId.setCellValueFactory(new PropertyValueFactory<>("id"));
        emName.setCellValueFactory(new PropertyValueFactory<>("name"));
        emRelation.setCellValueFactory(new PropertyValueFactory<>("relation"));
        emContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
    }
    //set Select Emergency item to textfealds from table
    private void setSelectEmergencyToTxt() {
        tableEmDetail.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txtemcode.setText(newSelection.getPrId());
                txtemid.setText(newSelection.getId());
                txtpername.setText(newSelection.getName());
                txtrelation.setText(newSelection.getRelation());
                txtcontactno.setText(String.valueOf(newSelection.getContact()));
            }
        });
    }

    //Get all Prisoner details from database
    private void getAllPrDetail() {
        try {
            ObservableList<PrisonerDetailTM> obList = FXCollections.observableArrayList();
            List<PrisonerDTO> prList = prisonerBO.getAll();

            for (PrisonerDTO prisonerDTO : prList) {
                obList.add(new PrisonerDetailTM(
                        prisonerDTO.getTxtmatecode(),
                        prisonerDTO.getTxtmatename(),
                        prisonerDTO.getTxtgender(),
                        prisonerDTO.getTxtbirth(),
                        prisonerDTO.getTxtmarital(),
                        prisonerDTO.getTxtsecurity(),
                        prisonerDTO.getTxtblno()
                ));
            }
            tablePrDetail.setItems(obList);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "SQL Error!").show();
        }
    }
    //set Prisoner details to table from database
    @FXML
    private void setCellValuePrDetail(){
        incode.setCellValueFactory(new PropertyValueFactory<>("txtmatecode"));
        name.setCellValueFactory(new PropertyValueFactory<>("txtmatename"));
        gender.setCellValueFactory(new PropertyValueFactory<>("txtgender"));
        birthday.setCellValueFactory(new PropertyValueFactory<>("txtbirth"));
        marital.setCellValueFactory(new PropertyValueFactory<>("txtmarital"));
        seclevel.setCellValueFactory(new PropertyValueFactory<>("txtsecurity"));
        bildno.setCellValueFactory(new PropertyValueFactory<>("txtblno"));
    }
    //set Select Prisoner detais to textfealds from table
    @FXML
    private void setSelectPrDetailToTxt() {
        tablePrDetail.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txtmatecode.setText(newSelection.getTxtmatecode());
                txtgender.setValue(newSelection.getTxtgender());
                txtsecurity.setText(newSelection.getTxtsecurity());
                txtmatename.setText(newSelection.getTxtmatename());
                txtbirth.setValue(LocalDate.parse(newSelection.getTxtbirth()));
                txtmarital.setValue(newSelection.getTxtmarital());
                txtblno.setText(newSelection.getTxtblno());
            }
        });
    }
    //navigate
    @FXML
    private void dashOnAction(MouseEvent mouseEvent) throws IOException {
        isCapturing = false;
        Stage stage = (Stage) prisonachor.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/dashboard.fxml"))));
        stage.setTitle("Dashboard");
        stage.centerOnScreen();
        stage.show();
    }
    @FXML
    void jlmanage(ActionEvent actionEvent) throws IOException {
        isCapturing = false;
        Stage stage = (Stage) prisonachor.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/jailermanageform.fxml"))));
        stage.setTitle("Jailer manage");
        stage.centerOnScreen();
        stage.show();
    }
    @FXML
    void stmanage(ActionEvent actionEvent) throws IOException {
        isCapturing = false;
        Stage stage = (Stage) prisonachor.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/staffmanagerform.fxml"))));
        stage.setTitle("Staff manage");
        stage.centerOnScreen();
        stage.show();
    }
    @FXML
    void vismanage(ActionEvent actionEvent) throws IOException {
        isCapturing = false;
        Stage stage = (Stage) prisonachor.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/visitormanageform.fxml"))));
        stage.setTitle("Visit manage");
        stage.centerOnScreen();
        stage.show();

    }
    @FXML
    void celmanage(ActionEvent actionEvent) throws IOException {
        isCapturing = false;
        Stage stage = (Stage) prisonachor.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/cellmanageform.fxml"))));
        stage.setTitle("Prisoner cells manage");
        stage.centerOnScreen();
        stage.show();
    }
    @FXML
    void pcasemanage(ActionEvent actionEvent) throws IOException {
        isCapturing = false;
        Stage stage = (Stage) prisonachor.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/casesmanageform.fxml"))));
        stage.setTitle("Cases manage");
        stage.centerOnScreen();
        stage.show();
    }
    @FXML
    void adminmanage(MouseEvent mouseEvent) throws IOException {
        isCapturing = false;
        Stage stage = (Stage) prisonachor.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/adminform.fxml"))));
        stage.setTitle("Admin manage");
        stage.centerOnScreen();
        stage.show();

    }
    @FXML
    void logout(ActionEvent actionEvent) throws IOException {
        isCapturing = false;
        Stage stage = (Stage) prisonachor.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/loginform.fxml"))));
        stage.setTitle("Login");
        stage.centerOnScreen();
        stage.show();
    }
    //start prisoner actions
    @FXML
    void btnSave(ActionEvent actionEvent) throws Exception {
        lblsearch.setText("");
       if(!txtmatecode.getText().isBlank() && !txtmatename.getText().isBlank() && txtgender.getValue()!= null && txtbirth.getValue() != null && txtmarital.getValue()!= null && !txtsecurity.getText().isBlank() && !txtblno.getText().isBlank()) {

        if (btnCapture.getText().equals("Open Camera") && panePhoto.getImage() != null){
           String id = txtmatecode.getText();
           String name = txtmatename.getText();
           String gender = txtgender.getValue();
           LocalDate selectedDate = txtbirth.getValue();
           String birth = selectedDate.toString();
           String marital = txtmarital.getValue();
           String security = txtsecurity.getText();
           String buildNo = txtblno.getText();

           try {
               boolean build = prisonerBO.existCell(buildNo);
               if (build) {
                   boolean isSaved = prisonerBO.save(new PrisonerDTO(id, name, gender, birth, marital, security, buildNo));
                   if (isSaved) {
                       new Alert(Alert.AlertType.CONFIRMATION, "Prisoner saved!").show();
                       String imageName = id + ".png"; // Construct the file name
                       File outputFile = new File(imageName);
                       try {
                           ImageIO.write(image, "png", outputFile);
                       } catch (IOException e) {
                           new Alert(Alert.AlertType.ERROR, "Failed to save the image!").show();
                           return;
                       }

                       getAllPrDetail();
                       setCellValuePrDetail();
                   }
               }else{new Alert(Alert.AlertType.ERROR, "Building ID Not Exist!").show();}
           } catch (SQLException e) {
               new Alert(Alert.AlertType.ERROR, "Please Check Details & Try Again!").show();
               txtClear();
           }
       }else{new Alert(Alert.AlertType.ERROR, "Please Take Picture For Save Details!").show(); }
       }else{new Alert(Alert.AlertType.ERROR, "Please Fill Details!").show(); }

    }
    @FXML
    void btnSearch(ActionEvent actionEvent) throws Exception {
        lblsearch.setText("");

       if(!txtmatecode.getText().isBlank()) {
            try {
                PrisonerDTO prisonerDTO = prisonerBO.search(txtmatecode.getText());
                if (prisonerDTO != null) {
                    searchPrDetails();
                    txtmatecode.setText(prisonerDTO.getTxtmatecode());
                    txtmatename.setText(prisonerDTO.getTxtmatename());
                    txtgender.setValue(prisonerDTO.getTxtgender());
                    txtbirth.setValue(LocalDate.parse(prisonerDTO.getTxtbirth()));
                    txtmarital.setValue(prisonerDTO.getTxtmarital());
                    txtsecurity.setText(prisonerDTO.getTxtsecurity());
                    txtblno.setText(prisonerDTO.getTxtblno());
                    lblsearch.requestFocus();

                    File file = new File(prisonerDTO.getTxtmatecode() + ".png");
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
                    new Alert(Alert.AlertType.ERROR, "Prisoner Id Not Exist!").show();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "Please Check Details & Try Again!").show();
                txtClear();
            }
        }else{new Alert(Alert.AlertType.ERROR, "Please Fill Prisoner Id!").show(); }
    }

    private void searchPrDetails(){
        tablePrDetail.getItems().stream()
                .filter(item -> item.getTxtmatecode().equals(txtmatecode.getText()) )
                .findAny()
                .ifPresent(item -> {
                    tablePrDetail.getSelectionModel().select(item);
                    tablePrDetail.scrollTo(item);
                });

    }
    @FXML
    void btnDelete(ActionEvent actionEvent) {
        lblsearch.setText("");
        String prId = txtmatecode.getText();
        if(!txtmatecode.getText().isBlank()) {
            try {
                boolean isDeleted = prisonerBO.delete(prId);
                if (isDeleted) {
                    String imageName = txtmatecode.getText() + ".png";
                    File outputFile = new File(imageName);
                    if (outputFile.delete()) {
                        System.out.println("Image file deleted successfully!");
                    } else {
                        System.out.println("Failed to delete image file!");
                    }

                    new Alert(Alert.AlertType.CONFIRMATION, "Prisoner Deleted!").show();
                    txtClear();
                    getAllPrDetail();
                    setCellValuePrDetail();
                    getAllEmergency();
                    setCellValueEmergency();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Prisoner Id Not Exist!").show();
                }
            } catch (SQLException | ClassNotFoundException e) {
                new Alert(Alert.AlertType.ERROR, "Please Check Details & Try Again!").show();
                txtClear();
            }
        }else{new Alert(Alert.AlertType.ERROR, "Please Fill Prisoner Id!").show(); }
    }
    @FXML
    void btnClear(ActionEvent actionEvent) {
        txtClear();
    }
    @FXML
    void btnUpdate(ActionEvent actionEvent) throws Exception {
        lblsearch.setText("");
        if(!txtmatecode.getText().isBlank() && !txtmatename.getText().isBlank() && txtgender.getValue()!= null && txtbirth.getValue() != null && txtmarital.getValue()!= null && !txtsecurity.getText().isBlank() && !txtblno.getText().isBlank()) {
            String id = txtmatecode.getText();
            String name = txtmatename.getText();
            String gender = txtgender.getValue();
            LocalDate selectedDate = txtbirth.getValue();
            String birth = selectedDate.toString();
            String marital = txtmarital.getValue();
            String security = txtsecurity.getText();
            String buildNo = txtblno.getText();

            try {
                boolean isUpdated = prisonerBO.update(new PrisonerDTO(id,name,gender,birth,marital,security,buildNo));
                if(isUpdated){
                    new Alert(Alert.AlertType.CONFIRMATION, "Prisoner updated!").show();
                    getAllPrDetail();
                    setCellValuePrDetail();
                    PrisonerDTO pris = prisonerBO.search(txtmatecode.getText());
                    if (btnCapture.getText().equals("Open Camera") && panePhoto.getImage() != null){
                        String imageName = pris.getTxtmatecode() +".png"; // Construct the file name
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
                    new Alert(Alert.AlertType.ERROR, "Prisoner Id Not Exist!").show();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Building Id Not Exist!").show();
            }
        }else{new Alert(Alert.AlertType.ERROR, "Please Fill Details!").show(); }
    }

    //start emergancy actions
    @FXML
    void emSave(ActionEvent actionEvent) {
        emlblsearch.setText("");
            if (!txtemid.getText().isBlank() && !txtemcode.getText().isBlank() && !txtpername.getText().isBlank() && !txtrelation.getText().isBlank() && !txtcontactno.getText().isBlank()) {
                if (txtcontactno.getText().matches("\\d+")) {
                String id = txtemid.getText();
                String prId = txtemcode.getText();
                String name = txtpername.getText();
                String relation = txtrelation.getText();
                int contact = Integer.parseInt(txtcontactno.getText());
                try {
                    boolean available = prisonerBO.existEmergency(id);
                    if (available) {
                            new Alert(Alert.AlertType.ERROR, "Prisoner Emergency Contact Alredy added!").show();
                    }else {
                        boolean isSaved = prisonerBO.saveEmergency(new EmergencyDTO(id, prId, name, relation, contact));
                        if (isSaved) {
                            new Alert(Alert.AlertType.CONFIRMATION, "Prisoner Emergency Contact saved!").show();
                            getAllEmergency();
                            setCellValueEmergency();
                        }
                    }
                } catch (SQLException | ClassNotFoundException e) {
                    new Alert(Alert.AlertType.ERROR, "Please Check Details & Try Again!").show();
                    emTxtClear();

                }
            } else {emlblsearch.setText("Please Fill Correct Contact Number!");
            }
        }else{new Alert(Alert.AlertType.ERROR, "Please Fill Details!").show();}
    }

    @FXML
    void emSearch(ActionEvent actionEvent) {
        emlblsearch.setText("");
        if(!txtemcode.getText().isBlank()) {
            try {
                    EmergencyDTO emergencyDTO = prisonerBO.searchEmergency(txtemcode.getText());
                    if (emergencyDTO != null) {
                        searchEm();
                        txtemid.setText(emergencyDTO.getId());
                        txtemcode.setText(emergencyDTO.getPrId());
                        txtpername.setText(emergencyDTO.getName());
                        txtrelation.setText(emergencyDTO.getRelation());
                        txtcontactno.setText(String.valueOf(emergencyDTO.getContact()));
                    } else {
                        new Alert(Alert.AlertType.ERROR, "Prisoner Id Not Exist!").show();
                    }
            } catch (SQLException | ClassNotFoundException e) {
                new Alert(Alert.AlertType.ERROR, "Please Check Details & Try Again!").show();
                emTxtClear();
            }
        }else{new Alert(Alert.AlertType.ERROR, "Please Fill Prisoner Id!").show(); }
    }
    @FXML
    void emDelete(ActionEvent actionEvent) {
        emlblsearch.setText("");
        String prId = txtemid.getText();
        if(!txtemid.getText().isBlank()) {
            try {
                boolean isDeleted = prisonerBO.deleteEmergency(prId);
                if (isDeleted) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Prisoner Emergency Contact Deleted!").show();
                    emTxtClear();
                    getAllEmergency();
                    setCellValueEmergency();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Person NIC Not Exist!").show();
                }
            } catch (SQLException | ClassNotFoundException e) {
                new Alert(Alert.AlertType.ERROR, "Please Check Details & Try Again!").show();
                emTxtClear();
            }
        }else{new Alert(Alert.AlertType.ERROR, "Please Fill Person NIC").show(); }
    }
    @FXML
    void emClear(ActionEvent actionEvent) {
        emTxtClear();
    }
    @FXML
    void btnEmUpdate(ActionEvent actionEvent) {
        emlblsearch.setText("");
            if (!txtemid.getText().isBlank() && !txtemcode.getText().isBlank() && !txtpername.getText().isBlank() && !txtrelation.getText().isBlank() && !txtcontactno.getText().isBlank()) {
                if (txtcontactno.getText().matches("\\d+")) {
                String id = txtemid.getText();
                String prId = txtemcode.getText();
                String name = txtpername.getText();
                String relation = txtrelation.getText();
                int contact = Integer.parseInt(txtcontactno.getText());

                try {
                    boolean isUpdated = prisonerBO.updateEmergency(new EmergencyDTO(id, prId, name, relation, contact));
                    if (isUpdated) {
                        new Alert(Alert.AlertType.CONFIRMATION, "Emergency Details updated!").show();
                        getAllEmergency();
                        setCellValueEmergency();
                    } else {
                        new Alert(Alert.AlertType.ERROR, "Person NIC Not Exist!").show();
                    }
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                    new Alert(Alert.AlertType.ERROR, "Prisoner Id Not Exist!").show();
                    txtClear();
                }
            } else {emlblsearch.setText("Please Fill Correct Contact No!");}

        } else{new Alert(Alert.AlertType.ERROR, "Please Fill Details!").show();}
    }
    @FXML
    private void searchEm(){
        tableEmDetail.getItems().stream()
                .filter(item -> item.getPrId().equals(txtemcode.getText()) )
                .findAny()
                .ifPresent(item -> {
                    tableEmDetail.getSelectionModel().select(item);
                    tableEmDetail.scrollTo(item);
                });

    }
    @SneakyThrows
    @FXML
    void txtClear() {
        txtmatecode.setText("");
        txtmatename.setText("");
        txtgender.setValue(null);
        txtbirth.setValue(null);
        txtmarital.setValue(null);
        txtsecurity.setText("");
        txtblno.setText("");
        lblsearch.setText("");
        isCapturing = false;
        isCameraEnabled = false;
        grabber.stop();
        panePhoto.setImage(null);
        btnCapture.setText("Open Camera");
        btnCapture.setStyle("-fx-background-color:   #158392;");

    }
    @FXML
    void emTxtClear() {
        txtemid.setText("");
        txtemcode.setText("");
        txtpername.setText("");
        txtrelation.setText("");
        txtcontactno.setText("");
        emlblsearch.setText("");
    }
    @FXML
    void setDate() {
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
        if(!txtmatecode.getText().isBlank()) {
            try {
                PrisonerDTO prisonerDTO = prisonerBO.search(txtmatecode.getText());
                if (prisonerDTO != null) {
                    searchPrDetails();
                    txtmatecode.setText(prisonerDTO.getTxtmatecode());
                    txtmatename.setText(prisonerDTO.getTxtmatename());
                    txtgender.setValue(prisonerDTO.getTxtgender());
                    txtbirth.setValue(LocalDate.parse(prisonerDTO.getTxtbirth()));
                    txtmarital.setValue(prisonerDTO.getTxtmarital());
                    txtsecurity.setText(prisonerDTO.getTxtsecurity());
                    txtblno.setText(prisonerDTO.getTxtblno());
                    lblsearch.setText("Prisoner ID Alredy Registerd!");
                    lblsearch.requestFocus();
                } else {
                    lblsearch.setText("Prisoner Not Exist!\nPlease Fill Prisoner Details");
                    prdetailTxt();
                }
            } catch (SQLException | ClassNotFoundException e) {lblsearch.setText("Please Check Details & \nTry Again!");
                txtClear();
            }
        }else{lblsearch.setText("Please Fill Prisoner ID!");}
    }

    @FXML
    void nameOnAction(ActionEvent actionEvent) {
        if (!txtmatename.getText().isBlank()){
            prdetailTxt();
        }
    }

    @FXML
    void maritalOnAction(ActionEvent actionEvent) {
        if (txtmarital.getValue()!= null){
            prdetailTxt();
        }
    }

    @FXML
    void buildOnAction(ActionEvent actionEvent) {
       if(!txtblno.getText().isBlank()){
            try {
                CellDTO cellDTO = prisonerBO.searchCell(txtblno.getText());
                if (cellDTO != null) {
                    lblsearch.setText("");
                    prdetailTxt();
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
    void genOnAction(ActionEvent actionEvent) {
        if (txtgender.getValue() != null){
            prdetailTxt();
        }
    }

    @FXML
    void securityOnAction(ActionEvent actionEvent) {
        if (!txtsecurity.getText().isBlank()){
            prdetailTxt();
        }
    }

    @FXML
    void birthOnAction(ActionEvent actionEvent) {
        if (txtbirth.getValue() != null){
            prdetailTxt();
        }
    }

    @FXML
    void emnameOnAction(ActionEvent actionEvent) {
        if (!txtpername.getText().isBlank()){
            emdetailTxt();
        }
    }

    @FXML
    void emrelaionOnAction(ActionEvent actionEvent) {
        if (!txtrelation.getText().isBlank()){
            emdetailTxt();
        }
    }

    @FXML
    void emcontactOnAction(ActionEvent actionEvent) {
        if (!txtcontactno.getText().isBlank()){
            if (txtcontactno.getText().matches("\\d+")) {
                emdetailTxt();
            }else { txtcontactno.setText("");
                emlblsearch.setText("Please Fill Correct Contact No!");}
        }
    }
    @FXML
    void emidOnAction(ActionEvent actionEvent) {
        if (!txtemid.getText().isBlank()){
            emdetailTxt();
        }
    }
    @FXML
    void emcodeOnAction(ActionEvent actionEvent) {
        emlblsearch.setText("");
        if(!txtemcode.getText().isBlank()) {
            try {
                boolean prisoner = prisonerBO.exist(txtemcode.getText());
                if (prisoner) {
                    EmergencyDTO emergencyDTO = prisonerBO.searchEmergency(txtemcode.getText());
                    if (emergencyDTO != null) {
                        searchEm();
                        txtemid.setText(emergencyDTO.getId());
                        txtemcode.setText(emergencyDTO.getPrId());
                        txtpername.setText(emergencyDTO.getName());
                        txtrelation.setText(emergencyDTO.getRelation());
                        txtcontactno.setText(String.valueOf(emergencyDTO.getContact()));
                        emlblsearch.setText("Emergency Details Alredy \nRegisterd!");
                        emlblsearch.requestFocus();
                    } else {
                            emlblsearch.setText("Emergency Details Not Exist!");
                        if (emlblsearch.getText().equals("Emergency Details Not Exist!")) {
                            emdetailTxt();
                        }
                    }
                }else{
                    emlblsearch.setText("Prisoner Id Not Exist!");
                }
            } catch (SQLException | ClassNotFoundException e) {emlblsearch.setText("Please Check Details & \nTry Again!");
                emTxtClear();
            }
        }else{emlblsearch.setText("Please Fill Inmate Code!");}
    }

    @FXML
    private void prdetailTxt(){
        int emptyFieldIndex = -1;
        switch (emptyFieldIndex) {
            case -1:
                if (txtmatecode.getText().isBlank()) {
                    txtmatecode.requestFocus();
                    emptyFieldIndex = 0;
                }
            case 0:
                if (emptyFieldIndex < 0 && (txtmatename.getText().isBlank())) {
                    txtmatename.requestFocus();
                    emptyFieldIndex = 1;
                }
            case 1:
                if (emptyFieldIndex < 0 && (txtgender.getValue()== null)) {
                    txtgender.requestFocus();
                    emptyFieldIndex = 2;
                }
            case 2:
                if (emptyFieldIndex < 0 && (txtbirth.getValue() == null)) {
                    txtbirth.requestFocus();
                    emptyFieldIndex = 3;
                }
            case 3:
                if (emptyFieldIndex < 0 && (txtmarital.getValue()== null)) {
                    txtmarital.requestFocus();
                    emptyFieldIndex = 4;
                }
            case 4:
                if (emptyFieldIndex < 0 && (txtsecurity.getText().isBlank())) {
                    txtsecurity.requestFocus();
                    emptyFieldIndex = 5;
                }

            case 5:
                if (emptyFieldIndex < 0 && (txtblno.getText().isBlank())) {
                    txtblno.requestFocus();
                    emptyFieldIndex = 6;
                }
                break;
            default:
                break;
        }
    }
    @FXML
    private void emdetailTxt() {
        int emptyFieldIndex = -1;
        switch (emptyFieldIndex) {
            case -1:
                if (txtemcode.getText().isBlank()) {
                    txtemcode.requestFocus();
                    emptyFieldIndex = 0;
                }
            case 0:
                if (emptyFieldIndex < 0 && (txtemid.getText().isBlank())) {
                    txtemid.requestFocus();
                    emptyFieldIndex = 1;
                }
            case 1:
                if (emptyFieldIndex < 0 && (txtpername.getText().isBlank())) {
                    txtpername.requestFocus();
                    emptyFieldIndex = 2;
                }
            case 2:
                if (emptyFieldIndex < 0 && (txtrelation.getText().isBlank())) {
                    txtrelation.requestFocus();
                    emptyFieldIndex = 3;
                }
            case 3:
                if (emptyFieldIndex < 0 && (txtcontactno.getText().isBlank())) {
                    txtcontactno.requestFocus();
                    emptyFieldIndex = 4;
                }
                break;
            default:
                break;
        }

    }

    public void btnReport(ActionEvent actionEvent) {
        try{
            JasperReport compileReport=(JasperReport) JRLoader.loadObject(this.getClass().getResource("/view/Prisoner.jasper"));
            JasperPrint jasperPrint= JasperFillManager.fillReport(compileReport,null,getCon());
            JasperViewer.viewReport(jasperPrint,false);
        }catch (JRException e){
            e.printStackTrace();
        }
    }

    public Connection getCon(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/Prison","root","1234");

        }catch (ClassNotFoundException|SQLException e){
            e.printStackTrace();
        }
        return null;
    }
}
