package lk.ijse.prison.dao.coustom.impl;

import lk.ijse.prison.dao.coustom.CaseDAO;
import lk.ijse.prison.dao.coustom.impl.util.SQLUtil;
import lk.ijse.prison.dto.CaseDTO;
import lk.ijse.prison.entity.Case;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CaseDAOImpl implements CaseDAO<String,Integer> {
    @Override
    public boolean save(Case cas) throws SQLException, ClassNotFoundException {
        return SQLUtil.execute("INSERT INTO Cases(CaseID, InmateID, Crime, Sentence, TimeStart, TimeEnd) " +
                "VALUES (?, ?, ?, ?, ?, ?)",cas.getCaseID(),cas.getInmateID(),cas.getCrime(),cas.getSentence(),cas.getTimeStart(),cas.getTimeEnd());
    }

    @Override
    public Case search(String id) throws SQLException, ClassNotFoundException {
        throw new UnsupportedOperationException("This feature yet to be developed");
    }
    @Override
    public Case searchBy(String id , Integer num) throws SQLException, ClassNotFoundException {
        String sql = null;
        if (num == 1) {
            sql = "SELECT * FROM Cases WHERE CaseID = ?";
        } else if (num == 2) {
            sql = "SELECT * FROM Cases WHERE InmateID = ?";
        }
        ResultSet rst = SQLUtil.execute(sql, id);
        if (rst.next()) {
            return new Case(rst.getString(1),rst.getString(2), rst.getString(3), rst.getString(4),
                    rst.getString(5),
                    rst.getString(6)
            );
        }
        return null;
    }
    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return SQLUtil.execute("DELETE FROM Cases WHERE CaseID = ?", id);
    }
    @Override
    public boolean update(Case dto) throws SQLException, ClassNotFoundException {
        return SQLUtil.execute("UPDATE Cases SET InmateID = ?, Crime = ?, Sentence = ?, TimeStart = ?,TimeEnd = ? WHERE CaseID = ?",
               dto.getInmateID(), dto.getCrime(), dto.getSentence(), dto.getTimeStart(), dto.getTimeEnd(), dto.getCaseID());
    }
    @Override
    public ArrayList<Case> getAll() throws SQLException, ClassNotFoundException {
        ArrayList<Case> all = new ArrayList<>();
        ResultSet rst = SQLUtil.execute("SELECT * FROM Cases");
        while (rst.next()) {
            all.add(new Case(rst.getString(1), rst.getString(2), rst.getString(3),rst.getString(4),rst.getString(5),rst.getString(6)));
        }
        return all;
    }
    @Override
    public boolean exist(String id) throws SQLException, ClassNotFoundException {
        ResultSet rs = SQLUtil.execute("SELECT InmateID FROM Cases WHERE InmateID=?",id);
        return rs.next();
    }
}
