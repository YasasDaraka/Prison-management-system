package lk.ijse.prison.view.tdm;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class EmergencyTM{
    private String id;
    private String prId;
    private String name;
    private String relation;
    private int contact;

  /*  public EmergencyTM(String id, String prId, String name, String relation, int contact) {
        this.id = id;
        this.prId = prId;
        this.name = name;
        this.relation = relation;
        this.contact = contact;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrId() {
        return prId;
    }

    public void setPrId(String prId) {
        this.prId = prId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public int getContact() {
        return contact;
    }

    public void setContact(int contact) {
        this.contact = contact;
    }

    @Override
    public String toString() {
        return "Emergency{" +
                "id='" + id + '\'' +
                ", prId='" + prId + '\'' +
                ", name='" + name + '\'' +
                ", relation='" + relation + '\'' +
                ", contact='" + contact + '\'' +
                '}';
    }*/
}
