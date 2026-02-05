package dao;

import db.DatabaseConnection;
import model.ClothingItem;
import model.Jacket;
import model.Shirt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class ClothingItemDAO {

    private static final String SELECT_COLUMNS =
            "item_id, type, name, size, price, sleeve_type, season";

    public boolean insertShirt(Shirt shirt) throws SQLException {
        String sql = "INSERT INTO clothing_items (item_id, type, name, size, price, sleeve_type, season) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, shirt.getItemId());
            statement.setString(2, shirt.getType());
            statement.setString(3, shirt.getName());
            statement.setString(4, shirt.getSize());
            statement.setDouble(5, shirt.getPrice());
            statement.setString(6, shirt.getSleeveType());
            statement.setNull(7, Types.VARCHAR);

            return statement.executeUpdate() > 0;
        }
    }

    public boolean insertJacket(Jacket jacket) throws SQLException {
        String sql = "INSERT INTO clothing_items (item_id, type, name, size, price, sleeve_type, season) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, jacket.getItemId());
            statement.setString(2, jacket.getType());
            statement.setString(3, jacket.getName());
            statement.setString(4, jacket.getSize());
            statement.setDouble(5, jacket.getPrice());
            statement.setNull(6, Types.VARCHAR);
            statement.setString(7, jacket.getSeason());

            return statement.executeUpdate() > 0;
        }
    }

    public List<ClothingItem> getAll() throws SQLException {
        List<ClothingItem> items = new ArrayList<>();
        String sql = "SELECT " + SELECT_COLUMNS + " FROM clothing_items ORDER BY item_id";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                items.add(mapRow(resultSet));
            }
        }

        return items;
    }

    public ClothingItem getById(int itemId) throws SQLException {
        String sql = "SELECT " + SELECT_COLUMNS + " FROM clothing_items WHERE item_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, itemId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapRow(resultSet);
                }
            }
        }

        return null;
    }

    public List<ClothingItem> getByType(String type) throws SQLException {
        List<ClothingItem> items = new ArrayList<>();
        String sql = "SELECT " + SELECT_COLUMNS + " FROM clothing_items WHERE type = ? ORDER BY item_id";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, type);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    items.add(mapRow(resultSet));
                }
            }
        }

        return items;
    }

    public boolean updateShirt(int itemId, String name, String size, double price, String sleeveType) throws SQLException {
        String sql = "UPDATE clothing_items " +
                "SET name = ?, size = ?, price = ?, sleeve_type = ?, season = NULL " +
                "WHERE item_id = ? AND type = 'SHIRT'";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, name);
            statement.setString(2, size);
            statement.setDouble(3, price);
            statement.setString(4, sleeveType);
            statement.setInt(5, itemId);

            return statement.executeUpdate() > 0;
        }
    }

    public boolean updateJacket(int itemId, String name, String size, double price, String season) throws SQLException {
        String sql = "UPDATE clothing_items " +
                "SET name = ?, size = ?, price = ?, season = ?, sleeve_type = NULL " +
                "WHERE item_id = ? AND type = 'JACKET'";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, name);
            statement.setString(2, size);
            statement.setDouble(3, price);
            statement.setString(4, season);
            statement.setInt(5, itemId);

            return statement.executeUpdate() > 0;
        }
    }

    public boolean deleteById(int itemId) throws SQLException {
        String sql = "DELETE FROM clothing_items WHERE item_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, itemId);
            return statement.executeUpdate() > 0;
        }
    }

    public List<ClothingItem> searchByName(String namePart) throws SQLException {
        List<ClothingItem> items = new ArrayList<>();
        String sql = "SELECT " + SELECT_COLUMNS + " FROM clothing_items WHERE name ILIKE ? ORDER BY item_id";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, "%" + namePart + "%");

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    items.add(mapRow(resultSet));
                }
            }
        }

        return items;
    }

    public List<ClothingItem> searchByPriceRange(double minPrice, double maxPrice) throws SQLException {
        List<ClothingItem> items = new ArrayList<>();
        String sql = "SELECT " + SELECT_COLUMNS + " FROM clothing_items " +
                "WHERE price BETWEEN ? AND ? ORDER BY price DESC";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setDouble(1, minPrice);
            statement.setDouble(2, maxPrice);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    items.add(mapRow(resultSet));
                }
            }
        }

        return items;
    }

    public List<ClothingItem> searchByMinPrice(double minPrice) throws SQLException {
        List<ClothingItem> items = new ArrayList<>();
        String sql = "SELECT " + SELECT_COLUMNS + " FROM clothing_items " +
                "WHERE price >= ? ORDER BY price DESC";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setDouble(1, minPrice);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    items.add(mapRow(resultSet));
                }
            }
        }

        return items;
    }

    private ClothingItem mapRow(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("item_id");
        String type = resultSet.getString("type");
        String name = resultSet.getString("name");
        String size = resultSet.getString("size");
        double price = resultSet.getDouble("price");

        if (type != null && type.equalsIgnoreCase("SHIRT")) {
            String sleeve = resultSet.getString("sleeve_type");
            return new Shirt(id, name, size, price, sleeve);
        }

        String season = resultSet.getString("season");
        return new Jacket(id, name, size, price, season);
    }
}
