package lk.ijse.prison.dao.coustom.impl;

import javafx.scene.control.Label;
import lk.ijse.prison.dao.coustom.DashboardDAO;
import lk.ijse.prison.dao.coustom.impl.util.SQLUtil;
import lk.ijse.prison.db.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DashboardDAOImpl implements DashboardDAO {
    @Override
    public String getPriCount() throws SQLException, ClassNotFoundException {
        ResultSet rs = SQLUtil.execute("SELECT COUNT(*) AS PrisonerCount FROM Prisoner");
        String count = null;
        if (rs.next()) {
            int cou = rs.getInt("PrisonerCount");
            count = String.valueOf(cou);
            return count;
        }
        return count;
}
    @Override
    public String getJailer() throws SQLException, ClassNotFoundException {
        ResultSet rs = SQLUtil.execute("SELECT COUNT(*) AS JailerCount FROM Jailer");
        String count = null;
        if (rs.next()) {
            int cou = rs.getInt("JailerCount");
            count = String.valueOf(cou);
            return count;
        }
        return count;
        }

    @Override
    public String getStaff() throws SQLException, ClassNotFoundException {
        ResultSet rs = SQLUtil.execute("SELECT COUNT(*) AS StaffCount FROM Staff");
        String count = null;
        if (rs.next()) {
            int cou = rs.getInt("StaffCount");
            count = String.valueOf(cou);
            return count;
        }
        return count;
    }
}
