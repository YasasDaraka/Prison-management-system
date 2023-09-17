package lk.ijse.prison.bo.coustom;

import lk.ijse.prison.bo.SuperBO;
import lk.ijse.prison.dto.CellDTO;
import lk.ijse.prison.dto.StaffDTO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface StaffBO extends SuperBO {

     boolean save(StaffDTO staffDTO) throws SQLException, ClassNotFoundException;

     StaffDTO search(String id) throws SQLException, ClassNotFoundException;

     boolean delete(String id) throws SQLException, ClassNotFoundException;

     boolean update(StaffDTO dto) throws SQLException, ClassNotFoundException;

     ArrayList<StaffDTO> getAll() throws SQLException, ClassNotFoundException;

     boolean existCell(String id) throws SQLException, ClassNotFoundException;

     CellDTO searchCell(String id) throws SQLException, ClassNotFoundException;
}
