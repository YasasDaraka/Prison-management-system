package lk.ijse.prison.dao.coustom.impl;

import lk.ijse.prison.dao.coustom.VisitDetailsDAO;
import lk.ijse.prison.dao.coustom.impl.util.SQLUtil;
import lk.ijse.prison.dto.VisitorDTO;
import lk.ijse.prison.entity.VisitTime;
import lk.ijse.prison.entity.VisitorDetail;

import java.sql.*;
import java.util.ArrayList;

public class VisitDetailsDAOImpl implements VisitDetailsDAO<String,String> {
    @Override
    public boolean save(VisitTime dto) throws SQLException, ClassNotFoundException {
        return SQLUtil.execute("INSERT INTO VisitorDetails(InmateID,NicNo,Date,time)" +
                        "VALUES(?,?,?,?)", dto.getInmateID(), dto.getNicNo(), dto.getDate(), dto.getTime());

    }

    @Override
    public ArrayList<VisitTime> getAll() throws SQLException, ClassNotFoundException {
        ArrayList<VisitTime> all = new ArrayList<>();
        ResultSet rst = SQLUtil.execute("SELECT * FROM VisitorDetails");
        while (rst.next()) {
            all.add(new VisitTime(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4)
            ));
        }
        return all;
    }

    @Override
    public boolean exist(String id) throws SQLException, ClassNotFoundException {
        throw new UnsupportedOperationException("This feature yet to be developed");
    }
    @Override
    public boolean existDate(String nic, String date) throws SQLException, ClassNotFoundException {
        ResultSet rs = SQLUtil.execute("SELECT Date FROM VisitorDetails WHERE NicNo = ? AND Date = ?",nic,date);
        return rs.next();
    }
    @Override
    public VisitTime search(String id) throws SQLException, ClassNotFoundException {
        throw new UnsupportedOperationException("This feature yet to be developed");
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        throw new UnsupportedOperationException("This feature yet to be developed");
    }

    @Override
    public boolean update(VisitTime dto) throws SQLException, ClassNotFoundException {
        throw new UnsupportedOperationException("This feature yet to be developed");
    }
}
