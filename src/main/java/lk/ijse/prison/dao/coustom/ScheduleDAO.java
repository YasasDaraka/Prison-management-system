package lk.ijse.prison.dao.coustom;

import lk.ijse.prison.dao.CrudDAO;
import lk.ijse.prison.entity.Schedule;

import java.sql.SQLException;

public interface ScheduleDAO<T,B> extends CrudDAO<Schedule,String> {
     Schedule searchSchedule(T jailerId, B buildNo) throws SQLException, ClassNotFoundException;

     boolean deleteSchedule(T jailerId, B buldId) throws SQLException, ClassNotFoundException;

     boolean existSchedule(T id,B buildId) throws SQLException, ClassNotFoundException;
}
