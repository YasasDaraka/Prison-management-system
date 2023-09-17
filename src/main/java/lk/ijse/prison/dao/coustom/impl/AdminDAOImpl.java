package lk.ijse.prison.dao.coustom.impl;

import lk.ijse.prison.dao.coustom.AdminDAO;
import lk.ijse.prison.dao.coustom.impl.util.SQLUtil;
import lk.ijse.prison.entity.Admin;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AdminDAOImpl implements AdminDAO<Admin,String> {

    @Override
    public Admin search(String id) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = SQLUtil.execute("SELECT * FROM Admin");
        lk.ijse.prison.entity.Admin result = null;
        while (resultSet.next()) {

            String username = resultSet.getString("UserName");
            String password = resultSet.getString("Password");

            result = new lk.ijse.prison.entity.Admin(username,password);
        }
        return result;
    }
    @Override
    public boolean Update(Admin admin, String pass) throws SQLException, ClassNotFoundException {
        return SQLUtil.execute("UPDATE Admin SET UserName = ?, Password = ? WHERE Password = ?", admin.getUserName(), admin.getPassword(),pass);

    }
    @Override
    public boolean checkPassword(String pass) throws SQLException, ClassNotFoundException {
        ResultSet rs = SQLUtil.execute("SELECT * FROM Admin WHERE LOWER(Password) = LOWER(?)",pass);
        return rs.next();
    }
    @Override
    public boolean save(Admin dto) throws SQLException, ClassNotFoundException {
        throw new UnsupportedOperationException("This feature yet to be developed");
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        throw new UnsupportedOperationException("This feature yet to be developed");
    }

    @Override
    public boolean update(Admin dto) throws SQLException, ClassNotFoundException {
        throw new UnsupportedOperationException("This feature yet to be developed");
    }

    @Override
    public ArrayList<Admin> getAll() throws SQLException, ClassNotFoundException {
        throw new UnsupportedOperationException("This feature yet to be developed");
    }

    @Override
    public boolean exist(String id) throws SQLException, ClassNotFoundException {
        throw new UnsupportedOperationException("This feature yet to be developed");
    }

}
