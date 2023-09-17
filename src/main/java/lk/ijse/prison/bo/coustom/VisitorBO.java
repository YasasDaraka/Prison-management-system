package lk.ijse.prison.bo.coustom;

import lk.ijse.prison.bo.SuperBO;
import lk.ijse.prison.dto.PrisonerDTO;
import lk.ijse.prison.dto.VisitTimeDTO;
import lk.ijse.prison.dto.VisitorDTO;
import lk.ijse.prison.dto.VisitorDetailDTO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface VisitorBO extends SuperBO {

     boolean saveVisit(VisitorDTO visitorDTO) throws SQLException, ClassNotFoundException;

     ArrayList<VisitTimeDTO> getAllVisit() throws SQLException, ClassNotFoundException;

     boolean existDate(String nic, String date) throws SQLException, ClassNotFoundException;

     boolean saveVisitors(VisitorDTO visitorDTO) throws SQLException, ClassNotFoundException;

     boolean deleteVisitors(String id) throws SQLException, ClassNotFoundException;

     VisitorDetailDTO searchVisitors(String id) throws SQLException, ClassNotFoundException;

     boolean updateVisitors(VisitorDTO dto) throws SQLException, ClassNotFoundException;

     ArrayList<VisitorDTO> getAllVisitors() throws SQLException, ClassNotFoundException;

     boolean existPrisoner(String id) throws SQLException, ClassNotFoundException;

     PrisonerDTO searchPrisoner(String id) throws SQLException, ClassNotFoundException;
}
