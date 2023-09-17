package lk.ijse.prison.dao.coustom;

import lk.ijse.prison.dao.SuperDAO;
import lk.ijse.prison.dao.coustom.impl.util.SQLUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface DashboardDAO extends SuperDAO {
     String getPriCount() throws SQLException, ClassNotFoundException;

     String getJailer() throws SQLException, ClassNotFoundException;

     String getStaff() throws SQLException, ClassNotFoundException;
}
