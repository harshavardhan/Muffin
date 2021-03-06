package org.muffin.muffin.daoimplementations;


import javafx.util.Pair;
import org.muffin.muffin.beans.Seat;
import org.muffin.muffin.beans.Theatre;


import org.muffin.muffin.daos.TheatreDAO;
import org.muffin.muffin.db.DBConfig;

import java.sql.*;
import java.util.*;


public class TheatreDAOImpl implements TheatreDAO {

    @Override
    public List<Theatre> getByCinemaBuilding(int cinemaBuildingId, int cinemaBuildingOwnerId) {

        List<Theatre> theatresList = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DBConfig.URL, DBConfig.USERNAME, DBConfig.PASSWORD);
             PreparedStatement preparedStmt = conn.prepareStatement("SELECT theatre.id, theatre.cinema_building_id, theatre.screen_no FROM theatre WHERE theatre.cinema_building_id = ? AND theatre.cinema_building_id IN (SELECT  cinema_building.id FROM  cinema_building WHERE cinema_building.owner_id = ?); ")) {
            preparedStmt.setInt(1, cinemaBuildingId);
            preparedStmt.setInt(2, cinemaBuildingOwnerId);
            ResultSet result = preparedStmt.executeQuery();
            while (result.next()) {
                Theatre theatre = new Theatre(
                        result.getInt(1),
                        result.getInt(2),
                        result.getInt(3)
                );
                theatresList.add(theatre);
            }
            return theatresList;
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }


    }

    @Override
    public Optional<Theatre> get(int cinemaBuildingId, int screenNo) {
        try (Connection conn = DriverManager.getConnection(DBConfig.URL, DBConfig.USERNAME, DBConfig.PASSWORD);
             PreparedStatement preparedStmt = conn.prepareStatement("SELECT id,cinema_building_id,screen_no FROM theatre WHERE cinema_building_id = ? AND screen_no = ?");) {
            preparedStmt.setInt(1, cinemaBuildingId);
            preparedStmt.setInt(2, screenNo);
            ResultSet result = preparedStmt.executeQuery();
            if (result.next()) {
                Theatre theatre = new Theatre(result.getInt(1), result.getInt(2), result.getInt(3));
                return Optional.of(theatre);
            }
            return Optional.empty();
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public Optional<Theatre> getByOwner(int theatreId, int cinemaBuildingOwnerId) {

        try (Connection conn = DriverManager.getConnection(DBConfig.URL, DBConfig.USERNAME, DBConfig.PASSWORD);
             PreparedStatement preparedStmt = conn.prepareStatement("SELECT theatre.id,cinema_building_id,screen_no FROM theatre,cinema_building WHERE theatre.id = ? AND theatre.cinema_building_id = cinema_building.id AND cinema_building.owner_id = ?");) {
            preparedStmt.setInt(2, cinemaBuildingOwnerId);
            preparedStmt.setInt(1, theatreId);
            ResultSet result = preparedStmt.executeQuery();
            if (result.next()) {
                Theatre theatre = new Theatre(result.getInt(1), result.getInt(2), result.getInt(3));
                return Optional.of(theatre);
            }
            return Optional.empty();
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }

    }

    @Override
    public Optional<Theatre> create(int cinemaBuildingId, int screenNo, int cinemaBuildingOwnerId) {
        try (Connection conn = DriverManager.getConnection(DBConfig.URL, DBConfig.USERNAME, DBConfig.PASSWORD);
             PreparedStatement preparedStmt = conn.prepareStatement("INSERT INTO theatre(cinema_building_id,screen_no) SELECT id,? FROM cinema_building WHERE id = ? AND owner_id = ? RETURNING id, cinema_building_id, screen_no;")) {
            preparedStmt.setInt(1, screenNo);
            preparedStmt.setInt(2, cinemaBuildingId);
            preparedStmt.setInt(3, cinemaBuildingOwnerId);
            ResultSet rs = preparedStmt.executeQuery();
            if (rs.next()) {
                Theatre theatre = new Theatre(rs.getInt(1), rs.getInt(2), rs.getInt(3));
                return Optional.of(theatre);
            }
            return Optional.empty();
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public boolean delete(int theatreId, int cinemaBuildingOwnerId) {

        try (Connection conn = DriverManager.getConnection(DBConfig.URL, DBConfig.USERNAME, DBConfig.PASSWORD);
             PreparedStatement preparedStmt = conn.prepareStatement("DELETE FROM theatre WHERE id = ? AND cinema_building_id IN (SELECT  cinema_building.id FROM  cinema_building WHERE cinema_building.owner_id = ?);")) {
            preparedStmt.setInt(1, theatreId);
            preparedStmt.setInt(2, cinemaBuildingOwnerId);
            int result = preparedStmt.executeUpdate();
            return result == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Seat> getSeatsOf(int theatreID) {
        List<Seat> seats = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DBConfig.URL, DBConfig.USERNAME, DBConfig.PASSWORD);
             PreparedStatement preparedStmt = conn.prepareStatement("SELECT id, theatre_id, x, y FROM seat WHERE theatre_id = ?")) {
            preparedStmt.setInt(1, theatreID);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()) {
                Seat seat = new Seat(
                        resultSet.getInt(1),
                        resultSet.getInt(2),
                        resultSet.getInt(3),
                        resultSet.getInt(4)
                );
                seats.add(seat);
            }
            return seats;
        } catch (SQLException e) {
            e.printStackTrace();
            return seats;
        }
    }

    @Override
    public boolean createSeatsOfTheatre(int theatreID, Set<Pair<Integer, Integer>> seatsXY) {
        try (Connection conn = DriverManager.getConnection(DBConfig.URL, DBConfig.USERNAME, DBConfig.PASSWORD);) {
            PreparedStatement preparedStmt = conn.prepareStatement("INSERT INTO seat(theatre_id,x,y) VALUES(?,?,?);");
            conn.setAutoCommit(false);
            for (Pair<Integer, Integer> seatXY : seatsXY) {
                preparedStmt.setInt(1, theatreID);
                preparedStmt.setInt(2, seatXY.getKey());
                preparedStmt.setInt(3, seatXY.getValue());
                preparedStmt.executeUpdate();
            }
            conn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Optional<Theatre> getById(int theatreId) {
        try (Connection conn = DriverManager.getConnection(DBConfig.URL, DBConfig.USERNAME, DBConfig.PASSWORD);
             PreparedStatement preparedStmt = conn.prepareStatement("SELECT id,cinema_building_id,screen_no FROM theatre WHERE id = ?");) {
            preparedStmt.setInt(1, theatreId);
            ResultSet result = preparedStmt.executeQuery();
            if (result.next()) {
                Theatre theatre = new Theatre(result.getInt(1), result.getInt(2), result.getInt(3));
                return Optional.of(theatre);
            }
            return Optional.empty();
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}

