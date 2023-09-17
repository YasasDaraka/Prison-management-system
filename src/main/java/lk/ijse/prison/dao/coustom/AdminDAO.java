package lk.ijse.prison.dao.coustom;

import lk.ijse.prison.dao.CrudDAO;
import lk.ijse.prison.entity.Admin;

import java.sql.SQLException;

public interface AdminDAO<T,ID> extends CrudDAO<Admin,String> {

      Admin search(String id) throws SQLException, ClassNotFoundException;
      boolean Update(T admin, ID pass) throws SQLException, ClassNotFoundException;
      boolean checkPassword(ID pass) throws SQLException, ClassNotFoundException;
}
