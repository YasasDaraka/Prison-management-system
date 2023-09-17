package lk.ijse.prison.dao.coustom.impl;

import lk.ijse.prison.dao.coustom.EmergencyDAO;
import lk.ijse.prison.dao.coustom.impl.util.SQLUtil;
import lk.ijse.prison.dto.EmergencyDTO;
import lk.ijse.prison.entity.Emergency;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class EmergencyDAOImpl implements EmergencyDAO {
    @Override
    public  boolean save(Emergency dto) throws SQLException, ClassNotFoundException {
        return SQLUtil.execute("INSERT INTO Emergency(EmId,InmateID,Name,Relation,Contact)" +
                "VALUES(?,?,?,?,?)", dto.getEmId(),dto.getInmateID(), dto.getName(), dto.getRelation(), dto.getContact());
    }
    @Override
    public Emergency search(String id) throws SQLException, ClassNotFoundException {
        ResultSet rst = SQLUtil.execute("SELECT * FROM Emergency WHERE InmateID = ?", id);
        if (rst.next()) {
            return new Emergency(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getInt(5));
        }
        return null;
    }
    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return SQLUtil.execute("DELETE FROM Emergency WHERE EmId = ?", id);
    }
    @Override
    public  boolean update(Emergency dto) throws SQLException, ClassNotFoundException {
        return SQLUtil.execute("UPDATE Emergency SET InmateID = ?, Name = ?, Relation = ?, Contact = ? WHERE EmId = ?",
                dto.getInmateID(), dto.getName(), dto.getRelation(), dto.getContact(), dto.getEmId());

    }
    @Override
    public ArrayList<Emergency> getAll() throws SQLException, ClassNotFoundException {
        ArrayList<Emergency> all = new ArrayList<>();
        ResultSet rst = SQLUtil.execute("SELECT * FROM Emergency");
        while (rst.next()) {
            all.add(new Emergency(rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getInt(5) ));
        }
        return all;
    }
    @Override
    public boolean exist(String id) throws SQLException, ClassNotFoundException {
        ResultSet rs = SQLUtil.execute("SELECT InmateID FROM Emergency WHERE InmateID=?",id);
        return rs.next();
    }
}
