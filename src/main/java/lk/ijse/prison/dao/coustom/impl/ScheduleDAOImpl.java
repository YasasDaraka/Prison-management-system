package lk.ijse.prison.dao.coustom.impl;

import lk.ijse.prison.dao.coustom.ScheduleDAO;
import lk.ijse.prison.dao.coustom.impl.util.SQLUtil;
import lk.ijse.prison.dto.ScheduleDTO;
import lk.ijse.prison.entity.Schedule;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ScheduleDAOImpl implements ScheduleDAO<String,String> {
    @Override
    public boolean save(Schedule dto) throws SQLException, ClassNotFoundException {
        return SQLUtil.execute("INSERT INTO Schedule(JailerID,BuildNO,Shift,Weapons)" +
                        "VALUES(?,?,?,?)", dto.getJailerID(), dto.getBuildNO(), dto.getShift(), dto.getWeapons());
    }
    @Override
    public Schedule searchSchedule(String jailerId, String buildNo) throws SQLException, ClassNotFoundException {
        ResultSet rst = SQLUtil.execute("SELECT * FROM Schedule WHERE JailerID = ? AND BuildNO = ?", jailerId,buildNo);
        if (rst.next()) {
            return new Schedule(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4)
            );
        }
        return null;
    }
    @Override
    public boolean deleteSchedule(String jailerId, String buldId) throws SQLException, ClassNotFoundException {
        return SQLUtil.execute("DELETE FROM Schedule WHERE JailerID = ? AND BuildNO = ?", jailerId,buldId);

    }
    @Override
    public boolean update(Schedule dto) throws SQLException, ClassNotFoundException {
        return SQLUtil.execute("UPDATE Schedule SET BuildNO = ?, Shift = ?, Weapons = ? WHERE JailerID = ? AND BuildNO = ?",
                dto.getBuildNO(), dto.getShift(), dto.getWeapons(), dto.getJailerID(), dto.getBuildNO());

    }
    @Override
    public ArrayList<Schedule> getAll() throws SQLException, ClassNotFoundException {
        ArrayList<Schedule> all = new ArrayList<>();
        ResultSet rst = SQLUtil.execute("SELECT * FROM Schedule");
        while (rst.next()) {
            all.add(new Schedule(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4)
            ));
        }
        return all;
    }
    @Override
    public boolean existSchedule(String id,String buildId) throws SQLException, ClassNotFoundException {
        ResultSet rs = SQLUtil.execute("SELECT * FROM Schedule WHERE JailerID = ? AND BuildNO = ?",id,buildId);
        return rs.next();
    }
    @Override
    public boolean exist(String id) throws SQLException, ClassNotFoundException {
        throw new UnsupportedOperationException("This feature yet to be developed");
    }
    @Override
    public Schedule search(String id) throws SQLException, ClassNotFoundException {
        throw new UnsupportedOperationException("This feature yet to be developed");
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        throw new UnsupportedOperationException("This feature yet to be developed");
    }

}
