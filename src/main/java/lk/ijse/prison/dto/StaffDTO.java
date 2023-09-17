package lk.ijse.prison.dto;

import lombok.*;

import java.time.LocalDate;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class StaffDTO {
   private String id ;
   private String name;
   private String gender;
   private String birth;
   private String address;
   private String nic;
   private String position;
   private String buildNo;

}
