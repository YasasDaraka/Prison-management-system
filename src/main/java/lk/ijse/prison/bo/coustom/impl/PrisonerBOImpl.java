package lk.ijse.prison.bo.coustom.impl;

import lk.ijse.prison.bo.coustom.PrisonerBO;
import lk.ijse.prison.dao.DAOFactory;
import lk.ijse.prison.dao.coustom.CellDAO;
import lk.ijse.prison.dao.coustom.EmergencyDAO;
import lk.ijse.prison.dao.coustom.PrisonerDAO;
import lk.ijse.prison.dto.CaseDTO;
import lk.ijse.prison.dto.CellDTO;
import lk.ijse.prison.dto.EmergencyDTO;
import lk.ijse.prison.dto.PrisonerDTO;
import lk.ijse.prison.entity.Case;
import lk.ijse.prison.entity.Cell;
import lk.ijse.prison.entity.Emergency;
import lk.ijse.prison.entity.Prisoner;

import java.sql.SQLException;
import java.util.ArrayList;

public class PrisonerBOImpl implements PrisonerBO {
    PrisonerDAO prisonerDAO = DAOFactory.getDAOFactory().getDAO(DAOFactory.DAOTypes.PRISONER);
    CellDAO cellDAO = DAOFactory.getDAOFactory().getDAO(DAOFactory.DAOTypes.CELL);
    EmergencyDAO emergencyDAO = DAOFactory.getDAOFactory().getDAO(DAOFactory.DAOTypes.EMERGENCY);
    @Override
    public boolean save(PrisonerDTO dto) throws SQLException, ClassNotFoundException {
        return prisonerDAO.save(new Prisoner(dto.getTxtmatecode(),dto.getTxtmatename(),dto.getTxtgender(),dto.getTxtbirth(),dto.getTxtmarital(),dto.getTxtsecurity(),dto.getTxtblno()));
    }
    @Override
    public PrisonerDTO search(String id) throws SQLException, ClassNotFoundException {
        Prisoner p = prisonerDAO.search(id);
        if(p != null) {
            return new PrisonerDTO(p.getInmateID(),p.getInmateName(),p.getGender(),p.getDOB(),p.getMarital(),p.getSecurity(),p.getBuildNO());
        }
        return null;
    }
    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return prisonerDAO.delete(id);
    }
    @Override
    public boolean update(PrisonerDTO dto) throws SQLException, ClassNotFoundException {
        return prisonerDAO.update(new Prisoner(dto.getTxtmatecode(),dto.getTxtmatename(),dto.getTxtgender(),dto.getTxtbirth(),dto.getTxtmarital(),dto.getTxtsecurity(),dto.getTxtblno()));
    }
    @Override
    public ArrayList<PrisonerDTO> getAll() throws SQLException, ClassNotFoundException {
        ArrayList<Prisoner> pris =  prisonerDAO.getAll();
        ArrayList<PrisonerDTO>  dto = new ArrayList<>();
        for (Prisoner p : pris) {
            dto.add(new PrisonerDTO(p.getInmateID(),p.getInmateName(),p.getGender(),p.getDOB(),p.getMarital(),p.getSecurity(),p.getBuildNO()));
        }
        return dto;
    }
    @Override
    public boolean exist(String id) throws SQLException, ClassNotFoundException {
        return prisonerDAO.exist(id);
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
    @Override
    public boolean saveEmergency(EmergencyDTO dto) throws SQLException, ClassNotFoundException {
        return emergencyDAO.save(new Emergency(dto.getId(),dto.getPrId(),dto.getName(),dto.getRelation(),dto.getContact()));
    }
    @Override
    public EmergencyDTO searchEmergency(String id) throws SQLException, ClassNotFoundException {
        Emergency e =  emergencyDAO.search(id);
        if(e != null) {
            return new EmergencyDTO(e.getEmId(),e.getInmateID(),e.getName(),e.getRelation(),e.getContact());
        }
        return null;
    }
    @Override
    public boolean deleteEmergency(String id) throws SQLException, ClassNotFoundException {
        return emergencyDAO.delete(id);
    }
    @Override
    public  boolean updateEmergency(EmergencyDTO dto) throws SQLException, ClassNotFoundException {
        return emergencyDAO.update(new Emergency(dto.getId(),dto.getPrId(),dto.getName(),dto.getRelation(),dto.getContact()));
    }
    @Override
    public ArrayList<EmergencyDTO> getAllEmergency() throws SQLException, ClassNotFoundException {
        ArrayList<Emergency> emer = emergencyDAO.getAll();
        ArrayList<EmergencyDTO>  dto = new ArrayList<>();
        for (Emergency e : emer) {
            dto.add(new EmergencyDTO(e.getEmId(),e.getInmateID(),e.getName(),e.getRelation(),e.getContact()));
        }
        return dto;
    }
    @Override
    public boolean existEmergency(String id) throws SQLException, ClassNotFoundException {
        return emergencyDAO.exist(id);
    }
}
