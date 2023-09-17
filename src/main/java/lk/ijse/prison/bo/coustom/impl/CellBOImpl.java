package lk.ijse.prison.bo.coustom.impl;

import lk.ijse.prison.bo.coustom.CellBO;
import lk.ijse.prison.dao.DAOFactory;
import lk.ijse.prison.dao.coustom.CellDAO;
import lk.ijse.prison.dto.CellDTO;
import lk.ijse.prison.entity.Cell;

import java.sql.SQLException;
import java.util.ArrayList;

public class CellBOImpl implements CellBO {
    CellDAO cellDAO = DAOFactory.getDAOFactory().getDAO(DAOFactory.DAOTypes.CELL);

    @Override
    public boolean save(CellDTO cells) throws SQLException, ClassNotFoundException {
        return cellDAO.save(new Cell(cells.getTxtbulno(),cells.getTxtammcell(),cells.getTxttype(),cells.getTxtlevel()));
    }
    @Override
    public CellDTO search(String id) throws SQLException, ClassNotFoundException {
        Cell c = cellDAO.search(id);
        if(c != null) {
            return new CellDTO(c.getBuildNO(), c.getCells(), c.getType(), c.getLevel());
        }
        return null;
    }
    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return cellDAO.delete(id);
    }
    @Override
    public boolean update(CellDTO dto) throws SQLException, ClassNotFoundException {
        return cellDAO.update(new Cell(dto.getTxtbulno(),dto.getTxtammcell(),dto.getTxttype(),dto.getTxtlevel()));
    }
    @Override
    public ArrayList<CellDTO> getAll() throws SQLException, ClassNotFoundException {
        ArrayList<Cell> cells = cellDAO.getAll();
        ArrayList<CellDTO> dto = new ArrayList<>();
        for (Cell c: cells) {
            dto.add(new CellDTO(c.getBuildNO(),c.getCells(),c.getType(),c.getLevel()));
        }
        return dto;
    }
    @Override
    public boolean exist(String id) throws SQLException, ClassNotFoundException {
        return cellDAO.exist(id);
    }
}
