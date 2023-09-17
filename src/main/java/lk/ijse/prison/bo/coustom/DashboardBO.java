package lk.ijse.prison.bo.coustom;

import lk.ijse.prison.bo.SuperBO;
import lk.ijse.prison.dao.coustom.impl.util.SQLUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface DashboardBO extends SuperBO {

     String getPriCount() throws SQLException, ClassNotFoundException;

     String getJailer() throws SQLException, ClassNotFoundException;

     String getStaff() throws SQLException, ClassNotFoundException;
}
