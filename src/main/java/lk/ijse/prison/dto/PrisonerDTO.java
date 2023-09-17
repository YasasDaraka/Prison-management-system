package lk.ijse.prison.dto;

import javafx.scene.control.TextField;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class PrisonerDTO {
    private String txtmatecode;
    private String txtmatename;
    private String txtgender;
    private String txtbirth;
    private String txtmarital;
    private String txtsecurity;
    private String txtblno;

}
