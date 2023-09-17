package lk.ijse.prison.bo.coustom.impl;

import lk.ijse.prison.bo.coustom.StaffBO;
import lk.ijse.prison.dao.DAOFactory;
import lk.ijse.prison.dao.coustom.CellDAO;
import lk.ijse.prison.dao.coustom.StaffDAO;
import lk.ijse.prison.dto.CellDTO;
import lk.ijse.prison.dto.StaffDTO;
import lk.ijse.prison.entity.Cell;
import lk.ijse.prison.entity.Staff;

import java.sql.SQLException;
import java.util.ArrayList;

public class StaffBOImpl implements StaffBO {
    StaffDAO staffDAO = DAOFactory.getDAOFactory().getDAO(DAOFactory.DAOTypes.STAFF);
    CellDAO cellDAO = DAOFactory.getDAOFactory().getDAO(DAOFactory.DAOTypes.CELL);

    @Override
    public boolean save(StaffDTO dto) throws SQLException, ClassNotFoundException {
        return staffDAO.save(new Staff(dto.getId(),dto.getName(),dto.getGender(),dto.getBirth(),dto.getAddress(),dto.getNic(),dto.getPosition(),dto.getBuildNo()));
    }
    @Override
    public StaffDTO search(String id) throws SQLException, ClassNotFoundException {
        Staff s = staffDAO.search(id);
        if(s != null) {
            return new StaffDTO(s.getEmpID(),s.getName(),s.getGender(),s.getDOB(),s.getAddress(),s.getNic(),s.getPosition(),s.getBuildNo());
        }
        return null;
    }
    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return staffDAO.delete(id);
    }
    @Override
    public boolean update(StaffDTO dto) throws SQLException, ClassNotFoundException {
        return staffDAO.update(new Staff(dto.getId(),dto.getName(),dto.getGender(),dto.getBirth(),dto.getAddress(),dto.getNic(),dto.getPosition(),dto.getBuildNo()));
    }
    @Override
    public ArrayList<StaffDTO> getAll() throws SQLException, ClassNotFoundException {
        ArrayList<Staff> staff = staffDAO.getAll();
        ArrayList<StaffDTO> dto = new ArrayList<>();
        for (Staff s: staff) {
            dto.add(new StaffDTO(s.getEmpID(),s.getName(),s.getGender(),s.getDOB(),s.getAddress(),s.getNic(),s.getPosition(),s.getBuildNo()));
        }
        return dto;
    }
    @Override
    public boolean existCell(String id) throws SQLException, ClassNotFoundException {
        return cellDAO.exist(id);
    }
    @Override
    public CellDTO searchCell(String id) throws SQLException, ClassNotFoundException {
        Cell c = cellDAO.search(id);
        return new CellDTO(c.getBuildNO(),c.getCells(),c.getType(),c.getLevel());
    }

}
