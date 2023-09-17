package lk.ijse.prison.entity;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Emergency {
    private String emId;
    private String inmateID;
    private String name;
    private String relation;
    private int contact;

}
