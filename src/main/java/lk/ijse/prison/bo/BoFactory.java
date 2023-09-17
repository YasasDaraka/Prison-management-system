package lk.ijse.prison.bo;
import lk.ijse.prison.bo.coustom.impl.*;
import lk.ijse.prison.dao.coustom.impl.*;

public class BoFactory {
    private static BoFactory boFactory;

    private BoFactory() {

    }

    public static BoFactory getBoFactory() {
        return (boFactory == null) ? boFactory = new BoFactory() : boFactory;
    }

    public enum BOTypes {
        ADMIN, CASE, CELL, DASHBOARD,JAILER,LOGIN,PRISONER,STAFF,VISITOR
    }

    public <T extends SuperBO> T getBO(BOTypes boTypes) {
        switch (boTypes) {
            case ADMIN:
                return (T) new AdminBOImpl();
            case CASE:
                return (T) new CaseBOImpl();
            case CELL:
                return (T) new CellBOImpl();
            case DASHBOARD:
                return (T) new DashboardBOImpl();
            case JAILER:
                return (T) new JailerBOImpl();
            case LOGIN:
                return (T) new LoginBOImpl();
            case PRISONER:
                return (T) new PrisonerBOImpl();
            case STAFF:
                return (T) new StaffBOImpl();
            case VISITOR:
                return (T) new VisitorBOImpl();
            default:
                return null;
        }
    }

}
