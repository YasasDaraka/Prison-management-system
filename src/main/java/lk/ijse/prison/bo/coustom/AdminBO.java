package lk.ijse.prison.bo.coustom;

import lk.ijse.prison.bo.SuperBO;
import lk.ijse.prison.dto.AdminDTO;

import java.sql.SQLException;

public interface AdminBO extends SuperBO {

     boolean Update(AdminDTO adminDTO, String pass) throws SQLException, ClassNotFoundException;

     boolean checkPassword(String pass) throws SQLException, ClassNotFoundException;

}
