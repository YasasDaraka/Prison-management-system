package lk.ijse.prison.bo.coustom;

import lk.ijse.prison.bo.SuperBO;
import lk.ijse.prison.dto.CaseDTO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface CaseBO extends SuperBO {

     boolean save(CaseDTO cas) throws SQLException, ClassNotFoundException;

     CaseDTO searchBy(String id , Integer num) throws SQLException, ClassNotFoundException;

     boolean delete(String id) throws SQLException, ClassNotFoundException;

     boolean update(CaseDTO dto) throws SQLException, ClassNotFoundException;

     ArrayList<CaseDTO> getAll() throws SQLException, ClassNotFoundException;

     boolean exist(String id) throws SQLException, ClassNotFoundException;

     boolean existPrisoner(String id) throws SQLException, ClassNotFoundException;

}
