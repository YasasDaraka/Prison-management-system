package lk.ijse.prison.bo.coustom;

import lk.ijse.prison.bo.SuperBO;
import lk.ijse.prison.dto.AdminDTO;

import java.sql.SQLException;

public interface LoginBO extends SuperBO {

     AdminDTO search(String id) throws SQLException, ClassNotFoundException;
}
