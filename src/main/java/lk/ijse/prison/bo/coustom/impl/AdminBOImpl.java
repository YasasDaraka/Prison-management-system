package lk.ijse.prison.bo.coustom.impl;

import lk.ijse.prison.bo.coustom.AdminBO;
import lk.ijse.prison.dao.DAOFactory;
import lk.ijse.prison.dao.coustom.AdminDAO;
import lk.ijse.prison.dto.AdminDTO;
import lk.ijse.prison.entity.Admin;

import java.sql.SQLException;

public class AdminBOImpl implements AdminBO {

    AdminDAO adminDAO = DAOFactory.getDAOFactory().getDAO(DAOFactory.DAOTypes.ADMIN);
    @Override
    public boolean Update(AdminDTO adminDTO, String pass) throws SQLException, ClassNotFoundException {
        return adminDAO.Update(new Admin(adminDTO.getUsername(), adminDTO.getPassword()),pass);
    }
    @Override
    public boolean checkPassword(String pass) throws SQLException, ClassNotFoundException {
        return adminDAO.checkPassword(pass);
    }
}