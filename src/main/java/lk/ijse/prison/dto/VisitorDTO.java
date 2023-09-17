package lk.ijse.prison.dto;

import javafx.scene.control.TextField;

import java.time.LocalDate;

public class VisitorDTO {
    private String priId;
    private String name;
    private String address;
    private String gender;
    private String date;
    private String time;
    private String nic ;

    public VisitorDTO(String priId, String name, String address, String gender, String date, String time, String nic) {
        this.priId = priId;
        this.name = name;
        this.address = address;
        this.gender = gender;
        this.date = date;
        this.time = time;
        this.nic = nic;
    }

    public VisitorDTO(String priId, String nic, String date, String time) {
        this.priId = priId;
        this.date = date;
        this.time = time;
        this.nic = nic;
    }

    public VisitorDTO(String nic, String inmatecode, String name, String address, String gender) {
        this.nic = nic;
        this.name = name;
        this.address = address;
        this.gender = gender;
        this.priId = inmatecode;
    }

    public String getPriId() {
        return priId;
    }

    public void setPriId(String priId) {
        this.priId = priId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    @Override
    public String toString() {
        return "Visitor{" +
                "priId='" + priId + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", gender='" + gender + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", nic='" + nic + '\'' +
                '}';
    }
}
