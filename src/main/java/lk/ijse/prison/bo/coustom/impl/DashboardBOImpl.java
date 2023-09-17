package lk.ijse.prison.bo.coustom.impl;

import lk.ijse.prison.bo.coustom.DashboardBO;
import lk.ijse.prison.dao.DAOFactory;
import lk.ijse.prison.dao.coustom.DashboardDAO;
import lk.ijse.prison.dao.coustom.impl.DashboardDAOImpl;
import lk.ijse.prison.dao.coustom.impl.util.SQLUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DashboardBOImpl implements DashboardBO {
    DashboardDAO dashboardDAO = DAOFactory.getDAOFactory().getDAO(DAOFactory.DAOTypes.DASHBOARD);

    @Override
    public String getPriCount() throws SQLException, ClassNotFoundException {
        return dashboardDAO.getPriCount();
    }

    @Override
    public String getJailer() throws SQLException, ClassNotFoundException {
        return dashboardDAO.getJailer();
    }

    @Override
    public String getStaff() throws SQLException, ClassNotFoundException {
        return dashboardDAO.getStaff();
    }
}
