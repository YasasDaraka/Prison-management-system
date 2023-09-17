package lk.ijse.prison.bo.coustom;

import lk.ijse.prison.bo.SuperBO;
import lk.ijse.prison.dto.CellDTO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface CellBO extends SuperBO {

         boolean save(CellDTO cells) throws SQLException, ClassNotFoundException;

         CellDTO search(String id) throws SQLException, ClassNotFoundException ;

         boolean delete(String id) throws SQLException, ClassNotFoundException;

         boolean update(CellDTO dto) throws SQLException, ClassNotFoundException;

         ArrayList<CellDTO> getAll() throws SQLException, ClassNotFoundException;

         boolean exist(String id) throws SQLException, ClassNotFoundException;

}