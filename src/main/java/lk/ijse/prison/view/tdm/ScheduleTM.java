package lk.ijse.prison.view.tdm;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ScheduleTM{
   private String id;
   private String buildNo;
   private String shift;
   private String weapon;

  /*  public ScheduleTM(String id, String buildNo, String shift, String weapon) {
        this.id = id;
        this.buildNo = buildNo;
        this.shift = shift;
        this.weapon = weapon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBuildNo() {
        return buildNo;
    }

    public void setBuildNo(String buildNo) {
        this.buildNo = buildNo;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public String getWeapon() {
        return weapon;
    }

    public void setWeapon(String weapon) {
        this.weapon = weapon;
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "id='" + id + '\'' +
                ", buildNo='" + buildNo + '\'' +
                ", shift='" + shift + '\'' +
                ", weapon='" + weapon + '\'' +
                '}';
    }*/
}
