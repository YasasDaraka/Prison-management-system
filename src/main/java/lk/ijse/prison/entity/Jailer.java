package lk.ijse.prison.entity;

public class Jailer {
    private String jailerID;
    private String jailerName;
    private String gender;
    private String dOB;
    private String address;
    private String nicNo;
    private String jailerRank;

    public Jailer(String id, String name, String gender, String birth, String address, String nic, String rank) {
        this.jailerID = id;
        this.jailerName = name;
        this.gender = gender;
        this.dOB = birth;
        this.address = address;
        this.nicNo = nic;
        this.jailerRank = rank;
    }

    public String getJailerID() {
        return jailerID;
    }

    public void setJailerID(String jailerID) {
        this.jailerID = jailerID;
    }

    public String getJailerName() {
        return jailerName;
    }

    public void setJailerName(String jailerName) {
        this.jailerName = jailerName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getdOB() {
        return dOB;
    }

    public void setdOB(String dOB) {
        this.dOB = dOB;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNicNo() {
        return nicNo;
    }

    public void setNicNo(String nicNo) {
        this.nicNo = nicNo;
    }

    public String getJailerRank() {
        return jailerRank;
    }

    public void setJailerRank(String jailerRank) {
        this.jailerRank = jailerRank;
    }
}
