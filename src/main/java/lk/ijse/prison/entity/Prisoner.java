package lk.ijse.prison.entity;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Prisoner {
    private String InmateID;
    private String InmateName;
    private String Gender;
    private String DOB;
    private String Marital;
    private String Security;
    private String BuildNO;

}
