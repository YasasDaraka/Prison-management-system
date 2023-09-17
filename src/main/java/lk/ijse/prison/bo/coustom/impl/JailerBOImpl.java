package lk.ijse.prison.bo.coustom.impl;

import lk.ijse.prison.bo.coustom.JailerBO;
import lk.ijse.prison.dao.DAOFactory;
import lk.ijse.prison.dao.coustom.CellDAO;
import lk.ijse.prison.dao.coustom.JailerDAO;
import lk.ijse.prison.dao.coustom.ScheduleDAO;
import lk.ijse.prison.dto.CaseDTO;
import lk.ijse.prison.dto.CellDTO;
import lk.ijse.prison.dto.JailerDTO;
import lk.ijse.prison.dto.ScheduleDTO;
import lk.ijse.prison.entity.Case;
import lk.ijse.prison.entity.Cell;
import lk.ijse.prison.entity.Jailer;
import lk.ijse.prison.entity.Schedule;

import java.sql.SQLException;
import java.util.ArrayList;

public class JailerBOImpl implements JailerBO {
    JailerDAO jailerDAO = DAOFactory.getDAOFactory().getDAO(DAOFactory.DAOTypes.JAILER);
    CellDAO cellDAO = DAOFactory.getDAOFactory().getDAO(DAOFactory.DAOTypes.CELL);
    ScheduleDAO scheduleDAO = DAOFactory.getDAOFactory().getDAO(DAOFactory.DAOTypes.SCHEDULE);
    @Override
    public boolean save(JailerDTO dto) throws SQLException, ClassNotFoundException {
        return jailerDAO.save(new Jailer(dto.getId(),dto.getName(),dto.getGender(),dto.getBirth(),dto.getAdress(),dto.getNic(),dto.getRank()));
    }
    @Override
    public JailerDTO search(String id) throws SQLException, ClassNotFoundException {
        Jailer j = jailerDAO.search(id);
        if(j != null) {
            return new JailerDTO(j.getJailerID(),j.getJailerName(),j.getGender(),j.getdOB(),j.getAddress(),j.getNicNo(),j.getJailerRank());
        }
        return null;
    }
    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return jailerDAO.delete(id);
    }
    @Override
    public boolean update(JailerDTO dto) throws SQLException, ClassNotFoundException {
        return jailerDAO.update(new Jailer(dto.getId(),dto.getName(),dto.getGender(),dto.getBirth(),dto.getAdress(),dto.getNic(),dto.getRank()));
    }
    @Override
    public ArrayList<JailerDTO> getAll() throws SQLException, ClassNotFoundException {
        ArrayList<Jailer> jail = jailerDAO.getAll();
        ArrayList<JailerDTO>  dto = new ArrayList<>();
        for (Jailer j : jail) {
            dto.add(new JailerDTO(j.getJailerID(),j.getJailerName(),j.getGender(),j.getdOB(),j.getAddress(),j.getNicNo(),j.getJailerRank()));
        }
        return dto;
    }
    @Override
    public boolean exist(String id) throws SQLException, ClassNotFoundException {
        return jailerDAO.exist(id);
    }
    @Override
    public boolean existCell(String id) throws SQLException, ClassNotFoundException {
        return cellDAO.exist(id);
    }
    @Override
    public CellDTO searchCell(String id) throws SQLException, ClassNotFoundException {
        Cell c = cellDAO.search(id);
        if(c != null) {
            return new CellDTO(c.getBuildNO(), c.getCells(), c.getType(), c.getLevel());
        }
        return null;
    }
    @Override
    public boolean saveSchedule(ScheduleDTO dto) throws SQLException, ClassNotFoundException {
        return scheduleDAO.save(new Schedule(dto.getId(),dto.getBuildNo(),dto.getShift(),dto.getWeapon()));
    }
    @Override
    public ScheduleDTO searchSchedule(String jailerId, String buildNo) throws SQLException, ClassNotFoundException {
        Schedule s = scheduleDAO.searchSchedule(jailerId,buildNo);
        if(s != null) {
            return new ScheduleDTO(s.getJailerID(), s.getBuildNO(), s.getShift(), s.getWeapons());
        }
        return null;
    }
    @Override
    public boolean deleteSchedule(String jailerId, String buldId) throws SQLException, ClassNotFoundException {
        return scheduleDAO.deleteSchedule(jailerId,buldId);
    }
    @Override
    public boolean updateSchedule(ScheduleDTO dto) throws SQLException, ClassNotFoundException {
       return scheduleDAO.update(new Schedule(dto.getId(),dto.getBuildNo(),dto.getShift(),dto.getWeapon()));
    }
    @Override
    public ArrayList<ScheduleDTO> getAllSchedule() throws SQLException, ClassNotFoundException {
        ArrayList<Schedule> shedule = scheduleDAO.getAll();
        ArrayList<ScheduleDTO>  dto = new ArrayList<>();
        for (Schedule s : shedule) {
            dto.add(new ScheduleDTO(s.getJailerID(),s.getBuildNO(),s.getShift(),s.getWeapons()));
        }
        return dto;
    }
    @Override
    public boolean existSchedule(String id,String buildId) throws SQLException, ClassNotFoundException {
        return scheduleDAO.existSchedule(id,buildId);
    }
}
