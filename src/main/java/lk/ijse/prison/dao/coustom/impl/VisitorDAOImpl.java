package lk.ijse.prison.dao.coustom.impl;

import lk.ijse.prison.dao.coustom.VisitorDAO;
import lk.ijse.prison.dao.coustom.impl.util.SQLUtil;
import lk.ijse.prison.dto.VisitorDTO;
import lk.ijse.prison.entity.VisitorDetail;

import java.sql.*;
import java.util.ArrayList;

public class VisitorDAOImpl implements VisitorDAO {
    @Override
    public boolean save(VisitorDetail dto) throws SQLException, ClassNotFoundException {
        return SQLUtil.execute("INSERT INTO Visitor(NicNo,InmateID,Name,Address,Gender)" +
                "VALUES(?,?,?,?,?)", dto.getNicNo(), dto.getInmateID(), dto.getName(), dto.getAddress(), dto.getGender());

    }
    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return SQLUtil.execute("DELETE FROM Visitor WHERE NicNo = ?", id);
    }
    @Override
    public VisitorDetail search(String id) throws SQLException, ClassNotFoundException {
        ResultSet rst = SQLUtil.execute("SELECT * FROM Visitor WHERE NicNo = ?", id);
        if (rst.next()) {
            return new VisitorDetail(
                    rst.getString(2),
                    rst.getString(1),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5)
            );
        }
        return null;
    }
    @Override
    public boolean update(VisitorDetail dto) throws SQLException, ClassNotFoundException {
        return SQLUtil.execute("UPDATE Visitor SET Address = ?, Name = ?, Gender = ? WHERE NicNo = ?",
        dto.getAddress(), dto.getName(), dto.getGender(), dto.getNicNo());

    }
    @Override
    public ArrayList<VisitorDetail> getAll() throws SQLException, ClassNotFoundException {
        ArrayList<VisitorDetail> all = new ArrayList<>();
        ResultSet rst = SQLUtil.execute("SELECT * FROM Visitor");
        while (rst.next()) {
            all.add(new VisitorDetail(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5)
            ));
        }
        return all;
    }

    @Override
    public boolean exist(String id) throws SQLException, ClassNotFoundException {
        throw new UnsupportedOperationException("This feature yet to be developed");
    }
}
