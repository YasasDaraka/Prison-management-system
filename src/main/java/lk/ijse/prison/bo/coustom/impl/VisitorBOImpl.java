package lk.ijse.prison.bo.coustom.impl;

import lk.ijse.prison.bo.coustom.VisitorBO;
import lk.ijse.prison.dao.DAOFactory;
import lk.ijse.prison.dao.coustom.PrisonerDAO;
import lk.ijse.prison.dao.coustom.VisitorDAO;
import lk.ijse.prison.dao.coustom.VisitDetailsDAO;
import lk.ijse.prison.db.DBConnection;

import lk.ijse.prison.dto.*;

import lk.ijse.prison.entity.Cell;
import lk.ijse.prison.entity.Prisoner;
import lk.ijse.prison.entity.VisitTime;
import lk.ijse.prison.entity.VisitorDetail;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class VisitorBOImpl implements VisitorBO {

    VisitorDAO visitorDAO = DAOFactory.getDAOFactory().getDAO(DAOFactory.DAOTypes.VISITOR);
    VisitDetailsDAO visitDetailsDAO = DAOFactory.getDAOFactory().getDAO(DAOFactory.DAOTypes.VISSITDETAILS);
    PrisonerDAO prisonerDAO = DAOFactory.getDAOFactory().getDAO(DAOFactory.DAOTypes.PRISONER);

    @Override
    public boolean saveVisit(VisitorDTO dto) throws SQLException, ClassNotFoundException {
        return visitDetailsDAO.save(new VisitTime(dto.getPriId(),dto.getNic(),dto.getDate(),dto.getTime()));
    }

    @Override
    public ArrayList<VisitTimeDTO> getAllVisit() throws SQLException, ClassNotFoundException {
        ArrayList<VisitTime> time = visitDetailsDAO.getAll();
        ArrayList<VisitTimeDTO> dto = new ArrayList<>();
        for (VisitTime t: time) {
            dto.add(new VisitTimeDTO(t.getInmateID(),t.getNicNo(),t.getDate(),t.getTime()));
        }
        return dto;
    }
    @Override
    public boolean existDate(String nic, String date) throws SQLException, ClassNotFoundException {
        return visitDetailsDAO.existDate(nic,date);
    }
    @Override
    public boolean saveVisitors(VisitorDTO dto) throws SQLException, ClassNotFoundException {
        boolean isSaved = false;
        boolean isUpdated = false;
        Connection con = null;
        try {
            con = DBConnection.getInstance().getConnection();
            con.setAutoCommit(false);

             isSaved = visitorDAO.save(new VisitorDetail(dto.getPriId(),dto.getNic(),dto.getName(),dto.getAddress(),dto.getGender()));
             isUpdated = visitDetailsDAO.save(new VisitTime(dto.getPriId(),dto.getNic(),dto.getDate(),dto.getTime()));
            con.commit();

            if(isSaved && isUpdated){
                return true;
            }
            return false;
        } catch (SQLException er) {
            con.rollback();
            return false;
        } finally {
            System.out.println("finally");
            con.setAutoCommit(true);
        }
    }

    @Override
    public boolean deleteVisitors(String id) throws SQLException, ClassNotFoundException {
        return visitorDAO.delete(id);
    }
    @Override
    public VisitorDetailDTO searchVisitors(String id) throws SQLException, ClassNotFoundException {
        VisitorDetail v = visitorDAO.search(id);
        if(v != null) {
            return new VisitorDetailDTO(v.getNicNo(),v.getName(),v.getAddress(),v.getGender());
        }
        return null;
    }
    @Override
    public boolean updateVisitors(VisitorDTO dto) throws SQLException, ClassNotFoundException {
        return visitorDAO.update(new VisitorDetail(dto.getPriId(),dto.getNic(),dto.getName(),dto.getAddress(),dto.getGender()));
    }
    @Override
    public ArrayList<VisitorDTO> getAllVisitors() throws SQLException, ClassNotFoundException {

        ArrayList<VisitorDetail> det = visitorDAO.getAll();
        ArrayList<VisitorDTO> dto = new ArrayList<>();
        for (VisitorDetail v: det) {
            dto.add(new VisitorDTO(v.getNicNo(),v.getInmateID(),v.getName(),v.getAddress(),v.getGender()));
        }
        return dto;
    }
    @Override
    public boolean existPrisoner(String id) throws SQLException, ClassNotFoundException {
        return prisonerDAO.exist(id);
    }
    @Override
    public PrisonerDTO searchPrisoner(String id) throws SQLException, ClassNotFoundException {
        Prisoner p = prisonerDAO.search(id);
        if(p != null) {
            return new PrisonerDTO(p.getInmateID(),p.getInmateName(),p.getGender(),p.getDOB(),p.getMarital(),p.getSecurity(),p.getBuildNO());
        }
        return null;

    }

}
