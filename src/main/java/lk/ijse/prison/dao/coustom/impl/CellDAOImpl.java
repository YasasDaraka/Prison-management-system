package lk.ijse.prison.dao.coustom.impl;

import lk.ijse.prison.dao.coustom.CellDAO;
import lk.ijse.prison.dao.coustom.impl.util.SQLUtil;
import lk.ijse.prison.dto.CellDTO;
import lk.ijse.prison.entity.Cell;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CellDAOImpl implements CellDAO {
    @Override
    public boolean save(Cell cells) throws SQLException, ClassNotFoundException {
        return SQLUtil.execute("INSERT INTO Building(BuildNO,Cells,Type,Level)" +
                "VALUES(?,?,?,?)",cells.getBuildNO(),cells.getCells(),cells.getType(),cells.getLevel());
    }
    @Override
    public Cell search(String id) throws SQLException, ClassNotFoundException {
        ResultSet rst = SQLUtil.execute("SELECT * FROM Building WHERE BuildNO = ?", id);
        if (rst.next()) {
            return new Cell(rst.getString(1), rst.getInt(2), rst.getString(3),rst.getString(4));
        }
        return null;
    }
    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return SQLUtil.execute("DELETE FROM Building WHERE BuildNO = ?", id);
    }
    @Override
    public boolean update(Cell dto) throws SQLException, ClassNotFoundException {
        return SQLUtil.execute("UPDATE Building SET Cells = ?, Type = ?, Level = ? WHERE BuildNO = ?", dto.getCells(), dto.getType(), dto.getLevel(), dto.getBuildNO());

    }
    @Override
    public ArrayList<Cell> getAll() throws SQLException, ClassNotFoundException {
        ArrayList<Cell> allbuild = new ArrayList<>();
        ResultSet rst = SQLUtil.execute("SELECT * FROM Building");
        while (rst.next()) {
            allbuild.add(new Cell(rst.getString(1), rst.getInt(2), rst.getString(3),rst.getString(4)));
        }
        return allbuild;
    }
    @Override
    public boolean exist(String id) throws SQLException, ClassNotFoundException {
        ResultSet rs = SQLUtil.execute("SELECT BuildNO FROM Building WHERE BuildNO=?",id);
        return rs.next();
    }
}
