package lk.ijse.prison.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class VisitTimeDTO{
    private String priId;
    private String nic ;
    private String date;
    private String time;

}
