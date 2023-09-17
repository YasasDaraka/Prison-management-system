package lk.ijse.prison.bo.coustom.impl;

import lk.ijse.prison.bo.coustom.LoginBO;
import lk.ijse.prison.dao.DAOFactory;
import lk.ijse.prison.dao.coustom.AdminDAO;
import lk.ijse.prison.dto.AdminDTO;
import lk.ijse.prison.entity.Admin;

import java.sql.SQLException;

public class LoginBOImpl implements LoginBO {
    AdminDAO adminDAO = DAOFactory.getDAOFactory().getDAO(DAOFactory.DAOTypes.ADMIN);

    @Override
    public AdminDTO search(String id) throws SQLException, ClassNotFoundException {
        Admin a = adminDAO.search(id);
        return new AdminDTO(a.getUserName(),a.getPassword());
    }
}
