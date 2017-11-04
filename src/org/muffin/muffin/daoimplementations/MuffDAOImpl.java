package org.muffin.muffin.daoimplementations;

import org.muffin.muffin.beans.Actor;
import org.muffin.muffin.beans.Muff;
import org.muffin.muffin.daos.MuffDAO;
import org.muffin.muffin.db.DBConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MuffDAOImpl implements MuffDAO {

    @Override
    public boolean create(String handle, String name, String password) {
        try (Connection conn = DriverManager.getConnection(DBConfig.URL, DBConfig.USERNAME, DBConfig.PASSWORD);
             PreparedStatement createMuff = conn.prepareStatement("INSERT INTO muff(handle, name) VALUES (?, ?);");
             PreparedStatement getMuffId = conn.prepareStatement("SELECT id FROM muff WHERE handle = ?;");
             PreparedStatement createMuffPassword = conn.prepareStatement("INSERT INTO muff_password(id, password) VALUES (?, ?);");) {
            // createMuff
            createMuff.setString(1, handle);
            createMuff.setString(2, name);
            int result = createMuff.executeUpdate();
            if (result != 1) {
                return false;
            }
            // getCreatedMuff
            getMuffId.setString(1, handle);
            ResultSet rs = getMuffId.executeQuery();
            if (rs.next()) {
                int id = rs.getInt(1);
                createMuffPassword.setInt(1, id);
                createMuffPassword.setString(2, password);
                result = createMuffPassword.executeUpdate();
                return result == 1;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean exists(String handle, String password) {
        try (Connection conn = DriverManager.getConnection(DBConfig.URL, DBConfig.USERNAME, DBConfig.PASSWORD);
             PreparedStatement preparedStmt = conn.prepareStatement("SELECT count(*) FROM muff, muff_password WHERE muff.id = muff_password.id AND handle=? AND password=?;")) {
            preparedStmt.setString(1, handle);
            preparedStmt.setString(2, password);
            ResultSet result = preparedStmt.executeQuery();
            result.next();
            return (result.getInt(1) > 0);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Optional<Muff> get(String handle) {
        try (Connection conn = DriverManager.getConnection(DBConfig.URL, DBConfig.USERNAME, DBConfig.PASSWORD);
             PreparedStatement preparedStmt = conn.prepareStatement("SELECT id, handle, name, level, joined_on FROM muff WHERE handle = ?")) {
            preparedStmt.setString(1, handle);
            ResultSet result = preparedStmt.executeQuery();
            if (result.next()) {
                Muff muff = new Muff(result.getInt(1),
                        result.getString(2),
                        result.getString(3),
                        result.getInt(4),
                        result.getTimestamp(5).toLocalDateTime());
                return Optional.of(muff);
            }
            return Optional.empty();
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public Optional<Muff> get(int id) {
        try (Connection conn = DriverManager.getConnection(DBConfig.URL, DBConfig.USERNAME, DBConfig.PASSWORD);
             PreparedStatement preparedStmt = conn.prepareStatement("SELECT id, handle, name, level, joined_on FROM muff WHERE id = ?")) {
            preparedStmt.setInt(1, id);
            ResultSet result = preparedStmt.executeQuery();
            if (result.next()) {
                Muff muff = new Muff(result.getInt(1),
                        result.getString(2),
                        result.getString(3),
                        result.getInt(4),
                        result.getTimestamp(5).toLocalDateTime());
                return Optional.of(muff);
            }
            return Optional.empty();
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public List<Muff> search(String searchKey) {
        List<Muff> muffs = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DBConfig.URL, DBConfig.USERNAME, DBConfig.PASSWORD);
             PreparedStatement preparedStmt = conn.prepareStatement("SELECT id, handle, name, level, joined_on FROM muff WHERE name ILIKE ? OR handle ILIKE ?")) {
            preparedStmt.setString(1, "%" + searchKey + "%");
            preparedStmt.setString(2, "%" + searchKey + "%");
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()) {
                Muff muff = new Muff(resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getInt(4),
                        resultSet.getTimestamp(5).toLocalDateTime());
                muffs.add(muff);
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return muffs;
    }

    @Override
    public List<Muff> userfollows(int id) {
        List<Muff> muffs = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DBConfig.URL, DBConfig.USERNAME, DBConfig.PASSWORD);
             PreparedStatement preparedStmt = conn.prepareStatement("SELECT id, handle, name, level, joined_on FROM follows,muff WHERE name follows.id1 = ?  and follows.id2 = muff.id")) {
            preparedStmt.setInt(1, id);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()) {
                Muff muff = new Muff(resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getInt(4),
                        resultSet.getTimestamp(5).toLocalDateTime());
                muffs.add(muff);
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return muffs;
    }

    @Override
    public boolean follow(int uid1, int uid2) {
        try (Connection conn = DriverManager.getConnection(DBConfig.URL, DBConfig.USERNAME, DBConfig.PASSWORD);
             PreparedStatement preparedStmt = conn.prepareStatement("insert into follows(id1,id2) values (?,?)")) {
            preparedStmt.setInt(1, uid1);
            preparedStmt.setInt(2, uid2);
            int result = preparedStmt.executeUpdate();
            return result == 1;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
