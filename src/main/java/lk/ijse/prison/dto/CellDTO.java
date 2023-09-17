package lk.ijse.prison.dto;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CellDTO {
    private String txtbulno;
    private int txtammcell;
    private String txttype;
    private String txtlevel;

}
