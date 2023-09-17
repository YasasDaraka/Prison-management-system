package lk.ijse.prison.dao.coustom;

import lk.ijse.prison.dao.CrudDAO;
import lk.ijse.prison.dto.CaseDTO;
import lk.ijse.prison.entity.Case;

import java.sql.SQLException;

public interface CaseDAO<ID,N> extends CrudDAO<Case,String> {

    public Case searchBy(ID id , N num) throws SQLException, ClassNotFoundException;
}
