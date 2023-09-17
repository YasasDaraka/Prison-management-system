package lk.ijse.prison.bo.coustom;

import lk.ijse.prison.bo.SuperBO;
import lk.ijse.prison.dto.CellDTO;
import lk.ijse.prison.dto.JailerDTO;
import lk.ijse.prison.dto.ScheduleDTO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface JailerBO extends SuperBO {

     boolean save(JailerDTO jailerDTO) throws SQLException, ClassNotFoundException;

     JailerDTO search(String id) throws SQLException, ClassNotFoundException;

     boolean delete(String id) throws SQLException, ClassNotFoundException;

     boolean update(JailerDTO dto) throws SQLException, ClassNotFoundException;

     ArrayList<JailerDTO> getAll() throws SQLException, ClassNotFoundException;

     boolean exist(String id) throws SQLException, ClassNotFoundException;

     boolean existCell(String id) throws SQLException, ClassNotFoundException;

     CellDTO searchCell(String id) throws SQLException, ClassNotFoundException;

     boolean saveSchedule(ScheduleDTO scheduleDTO) throws SQLException, ClassNotFoundException;

     public ScheduleDTO searchSchedule(String jailerId, String buildNo) throws SQLException, ClassNotFoundException;

     boolean deleteSchedule(String jailerId, String buldId) throws SQLException, ClassNotFoundException;

     boolean updateSchedule(ScheduleDTO dto) throws SQLException, ClassNotFoundException;

     ArrayList<ScheduleDTO> getAllSchedule() throws SQLException, ClassNotFoundException;

     boolean existSchedule(String id,String buildId) throws SQLException, ClassNotFoundException;
}
