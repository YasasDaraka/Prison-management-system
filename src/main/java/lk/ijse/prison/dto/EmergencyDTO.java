package lk.ijse.prison.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class EmergencyDTO {
    private String id;
    private String prId;
    private String name;
    private String relation;
    private int contact;

}
