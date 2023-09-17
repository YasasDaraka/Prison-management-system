package lk.ijse.prison.dao.coustom;

import lk.ijse.prison.dao.CrudDAO;
import lk.ijse.prison.dto.VisitorDTO;
import lk.ijse.prison.entity.VisitTime;

import java.sql.SQLException;

public interface VisitDetailsDAO<N,D> extends CrudDAO<VisitTime,String> {

     boolean existDate(N nic, D date) throws SQLException, ClassNotFoundException;
}
