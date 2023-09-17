package lk.ijse.prison.bo.coustom.impl;

import lk.ijse.prison.bo.coustom.CaseBO;
import lk.ijse.prison.dao.DAOFactory;
import lk.ijse.prison.dao.coustom.CaseDAO;
import lk.ijse.prison.dao.coustom.PrisonerDAO;
import lk.ijse.prison.dto.CaseDTO;
import lk.ijse.prison.entity.Case;

import java.sql.SQLException;
import java.util.ArrayList;

public class CaseBOImpl implements CaseBO {

    CaseDAO caseDAO = DAOFactory.getDAOFactory().getDAO(DAOFactory.DAOTypes.CASE);
    PrisonerDAO prisonerDAO = DAOFactory.getDAOFactory().getDAO(DAOFactory.DAOTypes.PRISONER);
    @Override
    public boolean save(CaseDTO cas) throws SQLException, ClassNotFoundException {
        return caseDAO.save(new Case(cas.getTxtcaseid(),cas.getTxtprid(),cas.getTxtcrytype(),cas.getTxtsenten(),cas.getTxttimest(),cas.getTxttimeend()));
    }

    @Override
    public CaseDTO searchBy(String id , Integer num) throws SQLException, ClassNotFoundException {
        Case c =  caseDAO.searchBy(id,num);
        if(c != null) {
            return new CaseDTO(c.getCaseID(), c.getInmateID(), c.getCrime(), c.getSentence(), c.getTimeStart(), c.getTimeEnd());
        }
        return null;
    }
    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return caseDAO.delete(id);
    }
    @Override
    public boolean update(CaseDTO dto) throws SQLException, ClassNotFoundException {
        return caseDAO.update(new Case(dto.getTxtcaseid(),dto.getTxtprid(),dto.getTxtcrytype(),dto.getTxtsenten(),dto.getTxttimest(),dto.getTxttimeend()));
    }
    @Override
    public ArrayList<CaseDTO> getAll() throws SQLException, ClassNotFoundException {
            ArrayList<Case> cas =  caseDAO.getAll();
            ArrayList<CaseDTO>  dto = new ArrayList<>();
        for (Case c : cas) {
            dto.add(new CaseDTO(c.getCaseID(),c.getInmateID(),c.getCrime(),c.getSentence(),c.getTimeStart(),c.getTimeEnd()));
        }
        return dto;
    }
    @Override
    public boolean exist(String id) throws SQLException, ClassNotFoundException {
        return caseDAO.exist(id);
    }
    @Override
    public boolean existPrisoner(String id) throws SQLException, ClassNotFoundException {
        return prisonerDAO.exist(id);
    }
}
