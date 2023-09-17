package lk.ijse.prison.entity;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class VisitTime {
    private String inmateID;
    private String nicNo ;
    private String date;
    private String time;

}
