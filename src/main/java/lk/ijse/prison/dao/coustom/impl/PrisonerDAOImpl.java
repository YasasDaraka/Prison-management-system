package lk.ijse.prison.dao.coustom.impl;

import lk.ijse.prison.dao.coustom.PrisonerDAO;
import lk.ijse.prison.dao.coustom.impl.util.SQLUtil;
import lk.ijse.prison.dto.PrisonerDTO;
import lk.ijse.prison.entity.Prisoner;

import java.sql.*;
import java.util.ArrayList;

public class PrisonerDAOImpl implements PrisonerDAO {
    @Override
    public boolean save(Prisoner dto) throws SQLException, ClassNotFoundException {
        return SQLUtil.execute("INSERT INTO Prisoner(InmateID,InmateName,Gender,DOB,Marital,Securety,BuildNO)" +
                "VALUES(?, ?, ?, ?, ?, ?, ?)", dto.getInmateID(), dto.getInmateName(), dto.getGender(), dto.getDOB(),
                dto.getMarital(), dto.getSecurity(), dto.getBuildNO());

    }
    @Override
    public Prisoner search(String id) throws SQLException, ClassNotFoundException {
        ResultSet rst = SQLUtil.execute("SELECT * FROM Prisoner WHERE InmateID = ?", id);
        if (rst.next()) {
            return new Prisoner(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5),
                    rst.getString(6),
                    rst.getString(7)
            );
        }
        return null;
    }
    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return SQLUtil.execute("DELETE FROM Prisoner WHERE InmateID = ?", id);

    }
    @Override
    public boolean update(Prisoner dto) throws SQLException, ClassNotFoundException {
        return SQLUtil.execute("UPDATE Prisoner SET InmateName = ?, Gender = ?, DOB = ?, Marital = ?, Securety = ?, BuildNO = ? WHERE InmateID = ?",
                dto.getInmateName(), dto.getGender(), dto.getDOB(), dto.getMarital(), dto.getSecurity(), dto.getBuildNO(), dto.getInmateID());
    }
    @Override
    public ArrayList<Prisoner> getAll() throws SQLException, ClassNotFoundException {
        ArrayList<Prisoner> all = new ArrayList<>();
        ResultSet rst = SQLUtil.execute("SELECT * FROM Prisoner");
        while (rst.next()) {
            all.add(new Prisoner(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5),
                    rst.getString(6),
                    rst.getString(7)
            ));
        }
        return all;
    }
    @Override
    public boolean exist(String id) throws SQLException, ClassNotFoundException {
        ResultSet rs = SQLUtil.execute("SELECT InmateID FROM Prisoner WHERE InmateID=?",id);
        return rs.next();
    }
}
