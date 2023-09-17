package lk.ijse.prison.bo.coustom;

import lk.ijse.prison.bo.SuperBO;
import lk.ijse.prison.dto.CellDTO;
import lk.ijse.prison.dto.EmergencyDTO;
import lk.ijse.prison.dto.PrisonerDTO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface PrisonerBO extends SuperBO {

     boolean save(PrisonerDTO prisonerDTO) throws SQLException, ClassNotFoundException;

     PrisonerDTO search(String id) throws SQLException, ClassNotFoundException;

     boolean delete(String id) throws SQLException, ClassNotFoundException;

     boolean update(PrisonerDTO dto) throws SQLException, ClassNotFoundException;

     ArrayList<PrisonerDTO> getAll() throws SQLException, ClassNotFoundException;

     boolean exist(String id) throws SQLException, ClassNotFoundException;

     boolean existCell(String id) throws SQLException, ClassNotFoundException;

     CellDTO searchCell(String id) throws SQLException, ClassNotFoundException;

     boolean saveEmergency(EmergencyDTO emergencyDTO) throws SQLException, ClassNotFoundException;

     EmergencyDTO searchEmergency(String id) throws SQLException, ClassNotFoundException;

     boolean deleteEmergency(String id) throws SQLException, ClassNotFoundException;

     boolean updateEmergency(EmergencyDTO dto) throws SQLException, ClassNotFoundException;

     ArrayList<EmergencyDTO> getAllEmergency() throws SQLException, ClassNotFoundException;

     boolean existEmergency(String id) throws SQLException, ClassNotFoundException;
}
