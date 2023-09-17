package lk.ijse.prison.dao.coustom.impl;

import lk.ijse.prison.dao.coustom.StaffDAO;
import lk.ijse.prison.dao.coustom.impl.util.SQLUtil;
import lk.ijse.prison.dto.StaffDTO;
import lk.ijse.prison.entity.Staff;

import java.sql.*;
import java.util.ArrayList;

public class StaffDAOImpl implements StaffDAO {
    @Override
    public boolean save(Staff dto) throws SQLException, ClassNotFoundException {
       return SQLUtil.execute("INSERT INTO Staff(EmpID,EmName,Gender,DOB,Address,Nic,Pos,BuildNO)" +
                       "VALUES(?,?,?,?,?,?,?,?)", dto.getEmpID(), dto.getName(), dto.getGender(), dto.getDOB(),
               dto.getAddress(), dto.getNic(), dto.getPosition(), dto.getBuildNo());

    }
    @Override
    public Staff search(String id) throws SQLException, ClassNotFoundException {
        ResultSet rst = SQLUtil.execute("SELECT * FROM Staff WHERE EmpID = ?", id);
        if (rst.next()) {
            return new Staff(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5),
                    rst.getString(6),
                    rst.getString(7),
                    rst.getString(8)
            );
        }
        return null;
    }
    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return SQLUtil.execute("DELETE FROM Staff WHERE EmpID = ?", id);

    }
    @Override
    public boolean update(Staff dto) throws SQLException, ClassNotFoundException {
        return SQLUtil.execute("UPDATE Staff SET Gender = ?, EmName = ?, DOB = ?, Address = ?, Nic = ?, Pos = ?, BuildNO = ? WHERE EmpID = ?",
dto.getGender(), dto.getName(), dto.getDOB(), dto.getAddress(), dto.getNic(), dto.getPosition(), dto.getBuildNo(), dto.getEmpID());

    }
    @Override
    public ArrayList<Staff> getAll() throws SQLException, ClassNotFoundException {
        ArrayList<Staff> all = new ArrayList<>();
        ResultSet rst = SQLUtil.execute("SELECT * FROM Staff");
        while (rst.next()) {
            all.add(new Staff(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5),
                    rst.getString(6),
                    rst.getString(7),
                    rst.getString(8)
            ));
        }
        return all;
    }

    @Override
    public boolean exist(String id) throws SQLException, ClassNotFoundException {
        throw new UnsupportedOperationException("This feature yet to be developed");
    }
}
