package lk.ijse.prison.entity;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Case {
    private String caseID;
    private String inmateID;
    private String crime;
    private String sentence;
    private String timeStart;
    private String timeEnd;

}

