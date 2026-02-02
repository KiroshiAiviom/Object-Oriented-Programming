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

/**
 * DAO class (required).
 * <p>
 * Requirements satisfied:
 * - PreparedStatement used for all SQL
 * - INSERT methods for each entity type (insertShirt, insertJacket)
 * - getAll(), getById(), and filtered SELECT (getByType)
 * - UPDATE methods for each entity type
 * - DELETE by ID
 * - Search by name (ILIKE with %), search by numeric field (price)
 * <p>
 * Code Quality:
 * - try/catch/finally in each method
 * - resources closed in finally (connection/resultset/statement)
 */
public class ClothingItemDAO {

    // ------------------ CREATE (INSERT) ------------------
    // Defense format explicitly wants insert method per entity type.

    public boolean insertShirt(Shirt shirt) {
        String sql = "INSERT INTO clothing_items (item_id, type, name, size, price, sleeve_type, season) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DatabaseConnection.getConnection();
            ps = conn.prepareStatement(sql);

            ps.setInt(1, shirt.getItemId());
            ps.setString(2, "SHIRT");
            ps.setString(3, shirt.getName());
            ps.setString(4, shirt.getSize());
            ps.setDouble(5, shirt.getPrice());
            ps.setString(6, shirt.getSleeveType());
            ps.setNull(7, Types.VARCHAR);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("DB insertShirt error: " + e.getMessage());
            return false;

        } finally {
            DatabaseConnection.closeStatement(ps);
            DatabaseConnection.closeConnection(conn);
        }
    }

    public boolean insertJacket(Jacket jacket) {
        String sql = "INSERT INTO clothing_items (item_id, type, name, size, price, sleeve_type, season) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DatabaseConnection.getConnection();
            ps = conn.prepareStatement(sql);

            ps.setInt(1, jacket.getItemId());
            ps.setString(2, "JACKET");
            ps.setString(3, jacket.getName());
            ps.setString(4, jacket.getSize());
            ps.setDouble(5, jacket.getPrice());
            ps.setNull(6, Types.VARCHAR);
            ps.setString(7, jacket.getSeason());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("DB insertJacket error: " + e.getMessage());
            return false;

        } finally {
            DatabaseConnection.closeStatement(ps);
            DatabaseConnection.closeConnection(conn);
        }
    }

    // ------------------ READ (SELECT) ------------------

    public List<ClothingItem> getAll() {
        String sql = "SELECT item_id, type, name, size, price, sleeve_type, season " +
                "FROM clothing_items ORDER BY item_id";

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        List<ClothingItem> items = new ArrayList<>();

        try {
            conn = DatabaseConnection.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                items.add(mapRow(rs));
            }

        } catch (SQLException e) {
            System.out.println("DB getAll error: " + e.getMessage());

        } finally {
            DatabaseConnection.closeResultSet(rs);
            DatabaseConnection.closeStatement(ps);
            DatabaseConnection.closeConnection(conn);
        }

        return items;
    }

    /**
     * Required by defence format: getById().
     * Returns single object or null if not found.
     */
    public ClothingItem getById(int itemId) {
        String sql = "SELECT item_id, type, name, size, price, sleeve_type, season " +
                "FROM clothing_items WHERE item_id = ?";

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, itemId);

            rs = ps.executeQuery();

            if (rs.next()) {
                return mapRow(rs);
            }
            return null;

        } catch (SQLException e) {
            System.out.println("DB getById error: " + e.getMessage());
            return null;

        } finally {
            DatabaseConnection.closeResultSet(rs);
            DatabaseConnection.closeStatement(ps);
            DatabaseConnection.closeConnection(conn);
        }
    }

    /**
     * Filtered SELECT (required at least one filtered SELECT).
     * Used to display only shirts or only jackets.
     */
    public List<ClothingItem> getByType(String type) {
        String sql = "SELECT item_id, type, name, size, price, sleeve_type, season " +
                "FROM clothing_items WHERE type = ? ORDER BY item_id";

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        List<ClothingItem> items = new ArrayList<>();

        try {
            conn = DatabaseConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, type);

            rs = ps.executeQuery();

            while (rs.next()) {
                items.add(mapRow(rs));
            }

        } catch (SQLException e) {
            System.out.println("DB getByType error: " + e.getMessage());

        } finally {
            DatabaseConnection.closeResultSet(rs);
            DatabaseConnection.closeStatement(ps);
            DatabaseConnection.closeConnection(conn);
        }

        return items;
    }

    // ------------------ UPDATE ------------------

    public boolean updateShirt(int itemId, String name, String size, double price, String sleeveType) {
        String sql = "UPDATE clothing_items " +
                "SET name = ?, size = ?, price = ?, sleeve_type = ?, season = NULL " +
                "WHERE item_id = ? AND type = 'SHIRT'";

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DatabaseConnection.getConnection();
            ps = conn.prepareStatement(sql);

            ps.setString(1, name);
            ps.setString(2, size);
            ps.setDouble(3, price);
            ps.setString(4, sleeveType);
            ps.setInt(5, itemId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("DB updateShirt error: " + e.getMessage());
            return false;

        } finally {
            DatabaseConnection.closeStatement(ps);
            DatabaseConnection.closeConnection(conn);
        }
    }

    public boolean updateJacket(int itemId, String name, String size, double price, String season) {
        String sql = "UPDATE clothing_items " +
                "SET name = ?, size = ?, price = ?, season = ?, sleeve_type = NULL " +
                "WHERE item_id = ? AND type = 'JACKET'";

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DatabaseConnection.getConnection();
            ps = conn.prepareStatement(sql);

            ps.setString(1, name);
            ps.setString(2, size);
            ps.setDouble(3, price);
            ps.setString(4, season);
            ps.setInt(5, itemId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("DB updateJacket error: " + e.getMessage());
            return false;

        } finally {
            DatabaseConnection.closeStatement(ps);
            DatabaseConnection.closeConnection(conn);
        }
    }

    // ------------------ DELETE ------------------

    public boolean deleteById(int itemId) {
        String sql = "DELETE FROM clothing_items WHERE item_id = ?";

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DatabaseConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, itemId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("DB deleteById error: " + e.getMessage());
            return false;

        } finally {
            DatabaseConnection.closeStatement(ps);
            DatabaseConnection.closeConnection(conn);
        }
    }

    // ------------------ SEARCH ------------------

    public List<ClothingItem> searchByName(String namePart) {
        // ILIKE + %...% = case-insensitive partial search in PostgreSQL
        String sql = "SELECT item_id, type, name, size, price, sleeve_type, season " +
                "FROM clothing_items WHERE name ILIKE ? ORDER BY item_id";

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        List<ClothingItem> items = new ArrayList<>();

        try {
            conn = DatabaseConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + namePart + "%");

            rs = ps.executeQuery();

            while (rs.next()) {
                items.add(mapRow(rs));
            }

        } catch (SQLException e) {
            System.out.println("DB searchByName error: " + e.getMessage());

        } finally {
            DatabaseConnection.closeResultSet(rs);
            DatabaseConnection.closeStatement(ps);
            DatabaseConnection.closeConnection(conn);
        }

        return items;
    }

    public List<ClothingItem> searchByPriceRange(double minPrice, double maxPrice) {
        // Numeric search using BETWEEN, ordered by price DESC
        String sql = "SELECT item_id, type, name, size, price, sleeve_type, season " +
                "FROM clothing_items WHERE price BETWEEN ? AND ? ORDER BY price DESC";

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        List<ClothingItem> items = new ArrayList<>();

        try {
            conn = DatabaseConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setDouble(1, minPrice);
            ps.setDouble(2, maxPrice);

            rs = ps.executeQuery();

            while (rs.next()) {
                items.add(mapRow(rs));
            }

        } catch (SQLException e) {
            System.out.println("DB searchByPriceRange error: " + e.getMessage());

        } finally {
            DatabaseConnection.closeResultSet(rs);
            DatabaseConnection.closeStatement(ps);
            DatabaseConnection.closeConnection(conn);
        }

        return items;
    }

    public List<ClothingItem> searchByMinPrice(double minPrice) {
        // Numeric search using >=, ordered by price DESC
        String sql = "SELECT item_id, type, name, size, price, sleeve_type, season " +
                "FROM clothing_items WHERE price >= ? ORDER BY price DESC";

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        List<ClothingItem> items = new ArrayList<>();

        try {
            conn = DatabaseConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setDouble(1, minPrice);

            rs = ps.executeQuery();

            while (rs.next()) {
                items.add(mapRow(rs));
            }

        } catch (SQLException e) {
            System.out.println("DB searchByMinPrice error: " + e.getMessage());

        } finally {
            DatabaseConnection.closeResultSet(rs);
            DatabaseConnection.closeStatement(ps);
            DatabaseConnection.closeConnection(conn);
        }

        return items;
    }

    // ------------------ Helper: map row -> correct object ------------------

    private ClothingItem mapRow(ResultSet rs) throws SQLException {
        int id = rs.getInt("item_id");
        String type = rs.getString("type");
        String name = rs.getString("name");
        String size = rs.getString("size");
        double price = rs.getDouble("price");

        if ("SHIRT".equalsIgnoreCase(type)) {
            String sleeve = rs.getString("sleeve_type");
            return new Shirt(id, name, size, price, sleeve);
        }

        if ("JACKET".equalsIgnoreCase(type)) {
            String season = rs.getString("season");
            return new Jacket(id, name, size, price, season);
        }

        throw new SQLException("Unknown item type in DB: " + type);
    }
}
