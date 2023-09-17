package lk.ijse.prison.entity;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Cell {
    private String buildNO;
    private int cells;
    private String type;
    private String level;

}
