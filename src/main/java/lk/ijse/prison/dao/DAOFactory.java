package lk.ijse.prison.dao;

import lk.ijse.prison.dao.coustom.impl.*;

public class DAOFactory {
    private static DAOFactory daoFactory;

    private DAOFactory() {
    }

    public static DAOFactory getDAOFactory() {
        return (daoFactory == null) ? daoFactory = new DAOFactory() : daoFactory;
    }

    public enum DAOTypes {
        ADMIN, CASE, CELL, DASHBOARD, EMERGENCY,JAILER,PRISONER,QUERY,SCHEDULE,STAFF,VISITOR,VISSITDETAILS
    }

    public <T extends SuperDAO> T getDAO(DAOTypes res) {
        switch (res) {
            case ADMIN:
                return (T) new AdminDAOImpl();
            case CASE:
                return (T) new CaseDAOImpl();
            case CELL:
                return (T) new CellDAOImpl();
            case DASHBOARD:
                return (T) new DashboardDAOImpl();
            case EMERGENCY:
                return (T) new EmergencyDAOImpl();
            case JAILER:
                return (T) new JailerDAOImpl();
            case PRISONER:
                return (T) new PrisonerDAOImpl();
            case QUERY:
                return (T) new QueryDAOImpl();
            case SCHEDULE:
                return (T) new ScheduleDAOImpl();
            case STAFF:
                return (T) new StaffDAOImpl();
            case VISITOR:
                return (T) new VisitorDAOImpl();
            case VISSITDETAILS:
                return (T) new VisitDetailsDAOImpl();
            default:
                return null;
        }
    }


}
