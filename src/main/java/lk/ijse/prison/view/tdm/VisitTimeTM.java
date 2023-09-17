package lk.ijse.prison.view.tdm;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class VisitTimeTM {
   private String priId;
   private String nic ;
   private String date;
   private String time;

 /*   public VisitTimeTM(String priId, String nic, String date, String time) {
        this.priId = priId;
        this.nic = nic;
        this.date = date;
        this.time = time;
    }

    public String getPriId() {
        return priId;
    }

    public void setPriId(String priId) {
        this.priId = priId;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
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

    @Override
    public String toString() {
        return "VisitTime{" +
                "priId='" + priId + '\'' +
                ", nic='" + nic + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                '}';
    }*/
}
