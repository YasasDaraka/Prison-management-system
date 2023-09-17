package lk.ijse.prison.dao.coustom.impl;

import lk.ijse.prison.dao.coustom.JailerDAO;
import lk.ijse.prison.dao.coustom.impl.util.SQLUtil;
import lk.ijse.prison.dto.JailerDTO;
import lk.ijse.prison.entity.Jailer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class JailerDAOImpl implements JailerDAO {
    @Override
    public boolean save(Jailer jailer) throws SQLException, ClassNotFoundException {
        return SQLUtil.execute("INSERT INTO Jailer(JailerID,JailerName,Gender,DOB,Address,NicNo,JailerRank)" +
                "VALUES(?,?,?,?,?,?,?)", jailer.getJailerID(), jailer.getJailerName(), jailer.getGender(), jailer.getdOB(),
                jailer.getAddress(), jailer.getNicNo(), jailer.getJailerRank());
    }
    @Override
    public Jailer search(String id) throws SQLException, ClassNotFoundException {
        ResultSet rst = SQLUtil.execute("SELECT * FROM Jailer WHERE JailerID = ?", id);
        if (rst.next()) {
            return new Jailer(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5),
                    rst.getString(6),
                    rst.getString(7)
            ); }
        return null;
    }
    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return SQLUtil.execute("DELETE FROM Jailer WHERE JailerID = ?", id);
    }
    @Override
    public boolean update(Jailer dto) throws SQLException, ClassNotFoundException {
        return SQLUtil.execute("UPDATE Jailer SET JailerName = ?, Gender = ?, DOB = ?, Address = ?, NicNo = ?, JailerRank = ? WHERE JailerID = ?",
                dto.getJailerName(), dto.getGender(), dto.getdOB(), dto.getAddress(), dto.getNicNo(), dto.getJailerRank(), dto.getJailerID());

    }
    @Override
    public ArrayList<Jailer> getAll() throws SQLException, ClassNotFoundException {
        ArrayList<Jailer> all = new ArrayList<>();
        ResultSet rst = SQLUtil.execute("SELECT * FROM Jailer");
        while (rst.next()) {
            all.add(new Jailer(
                    rst.getString("JailerID"),
                    rst.getString("JailerName"),
                    rst.getString("Gender"),
                    rst.getString("DOB"),
                    rst.getString("Address"),
                    rst.getString("NicNo"),
                    rst.getString("JailerRank")
            )); }
        return all;
    }
    @Override
    public boolean exist(String id) throws SQLException, ClassNotFoundException {
        ResultSet rs = SQLUtil.execute("SELECT JailerID FROM Jailer WHERE JailerID=?",id);
        return rs.next();
    }
}
