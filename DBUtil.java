import java.sql.*;
import java.util.*;

public class DBUtil {
    private String dsn;
    private String user;
    private String password;
    private Connection dbh;

    public DBUtil(String dsn, String user, String password) {
        this.dsn = dsn;
        this.user = user;
        this.password = password;
        this.dbh = null;
    }

    public void connect() throws Exception {
        if (dbh != null) {
            return;
        }
        try {
            dbh = DriverManager.getConnection(dsn, user, password);
            dbh.setAutoCommit(true);
        } catch (SQLException e) {
            throw new Exception("Could not connect to database: " + e.getMessage());
        }
    }

    public void disconnect() throws Exception {
        if (dbh == null) {
            return;
        }
        try {
            dbh.close();
            dbh = null;
        } catch (SQLException e) {
            throw new Exception("Could not disconnect from database: " + e.getMessage());
        }
    }

    public List<Map<String, Object>> executeQuery(String query, Object... params) throws Exception {
        if (dbh == null) {
            throw new Exception("No database handle exists");
        }
        if (query == null) {
            throw new Exception("Missing required input: query");
        }
        try {
            PreparedStatement stmt = dbh.prepareStatement(query);
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            ResultSet rs = stmt.executeQuery();
            List<Map<String, Object>> rows = new ArrayList<>();
            ResultSetMetaData meta = rs.getMetaData();
            int numColumns = meta.getColumnCount();
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= numColumns; i++) {
                    String columnName = meta.getColumnName(i);
                    Object value = rs.getObject(i);
                    row.put(columnName, value);
                }
                rows.add(row);
            }
            return rows;
        } catch (SQLException e) {
            throw new Exception("Could not execute query: " + e.getMessage());
        }
    }

    public List<String> getColumnNames(String tableName) throws Exception {
        if (dbh == null) {
            throw new Exception("No database handle exists");
        }
        if (tableName == null) {
            throw new Exception("Missing required input: tableName");
        }
        try {
            String quotedTableName = dbh.getMetaData().getIdentifierQuoteString() + tableName + dbh.getMetaData().getIdentifierQuoteString();
            String query = "SELECT column_name FROM all_tab_columns WHERE table_name = ?";
            PreparedStatement stmt = dbh.prepareStatement(query);
            stmt.setString(1, tableName);
            ResultSet rs = stmt.executeQuery();
            List<String> columnNames = new ArrayList<>();
            while (rs.next()) {
                String columnName = rs.getString("column_name");
                columnNames.add(columnName);
            }
            return columnNames;
        } catch (SQLException e) {
            throw new Exception("Could not execute query: " + e.getMessage());
        }
    }

    public List<String> getTableNames() throws Exception {
        if (dbh == null) {
            throw new Exception("No database handle exists");
        }
        try {
            String query = "SELECT table_name FROM all_tables";
            PreparedStatement stmt = dbh.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            List<String> tableNames = new ArrayList<>();
            while (rs.next()) {
                String tableName = rs.getString("table_name");
                tableNames.add(tableName);
            }
            return tableNames;
        } catch (SQLException e) {
            throw new Exception("Could not execute query: " + e.getMessage());
        }
    }

    public void insertRow(String tableName, Map<String, Object> data) throws Exception {
        if (dbh == null) {
            throw new Exception("No database handle exists");
        }
        if (tableName == null) {
            throw new Exception("Missing required input: tableName");
        }
        if (data == null) {
            throw new Exception("Missing required input: data");
        }
        try {
            String quotedTableName = dbh.getMetaData().getIdentifierQuoteString() + tableName + dbh.getMetaData().getIdentifierQuoteString();
            String columnNames = String.join(", ", data.keySet());
            String placeholders = String.join(", ", Collections.nCopies(data.size(), "?"));
            String query = "INSERT INTO " + quotedTableName + " (" + columnNames + ") VALUES (" + placeholders + ")";
            PreparedStatement stmt = dbh.prepareStatement(query);
            int i = 1;
            for (Object value : data.values()) {
                stmt.setObject(i++, value);
            }
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new Exception("Could not execute query: " + e.getMessage());
        }
    }

    public void updateRow(String tableName, Map<String, Object> data, Map<String, Object> where) throws Exception {
        if (dbh == null) {
            throw new Exception("No database handle exists");
        }
        if (tableName == null) {
            throw new Exception("Missing required input: tableName");
        }
        if (data == null) {
            throw new Exception("Missing required input: data");
        }
        if (where == null) {
            throw new Exception("Missing required input: where");
        }
        try {
            String quotedTableName = dbh.getMetaData().getIdentifierQuoteString() + tableName + dbh.getMetaData().getIdentifierQuoteString();
            String setClause = String.join(", ", data.keySet().stream().map(columnName -> dbh.getMetaData().getIdentifierQuoteString() + columnName + dbh.getMetaData().getIdentifierQuoteString() + " = ?").toArray(String[]::new));
            String whereClause = String.join(" AND ", where.keySet().stream().map(columnName -> dbh.getMetaData().getIdentifierQuoteString() + columnName + dbh.getMetaData().getIdentifierQuoteString() + " = ?").toArray(String[]::new));
            String query = "UPDATE " + quotedTableName + " SET " + setClause + " WHERE " + whereClause;
            PreparedStatement stmt = dbh.prepareStatement(query);
            int i = 1;
            for (Object value : data.values()) {
                stmt.setObject(i++, value);
            }
            for (Object value : where.values()) {
                stmt.setObject(i++, value);
            }
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new Exception("Could not execute query: " + e.getMessage());
        }
    }

    public void deleteRow(String tableName, Map<String, Object> where) throws Exception {
        if (dbh == null) {
            throw new Exception("No database handle exists");
        }
        if (tableName == null) {
            throw new Exception("Missing required input: tableName");
        }
        if (where == null) {
            throw new Exception("Missing required input: where");
        }
        try {
            String quotedTableName = dbh.getMetaData().getIdentifierQuoteString() + tableName + dbh.getMetaData().getIdentifierQuoteString();
            String whereClause = String.join(" AND ", where.keySet().stream().map(columnName -> dbh.getMetaData().getIdentifierQuoteString() + columnName + dbh.getMetaData().getIdentifierQuoteString() + " = ?").toArray(String[]::new));
            String query = "DELETE FROM " + quotedTableName + " WHERE " + whereClause;
            PreparedStatement stmt = dbh.prepareStatement(query);
            int i = 1;
            for (Object value : where.values()) {
                stmt.setObject(i++, value);
            }
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new Exception("Could not execute query: " + e.getMessage());
        }
    }

    public List<Map<String, Object>> partition(String tableName, String columnName, int numPartitions) throws Exception {
        if (dbh == null) {
            throw new Exception("No database handle exists");
        }
        if (tableName == null) {
            throw new Exception("Missing required input: tableName");
        }
        if (columnName == null) {
            throw new Exception("Missing required input: columnName");
        }
        if (numPartitions <= 0) {
            throw new Exception("Invalid input: numPartitions must be greater than 0");
        }
        try {
            String quotedTableName = dbh.getMetaData().getIdentifierQuoteString() + tableName + dbh.getMetaData().getIdentifierQuoteString();
            String query = "SELECT MIN(" + dbh.getMetaData().getIdentifierQuoteString() + columnName + dbh.getMetaData().getIdentifierQuoteString() + "), MAX(" + dbh.getMetaData().getIdentifierQuoteString() + columnName + dbh.getMetaData().getIdentifierQuoteString() + ") FROM " + quotedTableName;
            PreparedStatement stmt = dbh.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            double minValue = rs.getDouble(1);
            double maxValue = rs.getDouble(2);
            double range = (maxValue - minValue) / numPartitions;
            List<Map<String, Object>> partitions = new ArrayList<>();
            for (int i = 0; i < numPartitions; i++) {
                double startValue = minValue + i * range;
                double endValue = minValue + (i + 1) * range;
                String whereClause = dbh.getMetaData().getIdentifierQuoteString() + columnName + dbh.getMetaData().getIdentifierQuoteString() + " >= ? AND " + dbh.getMetaData().getIdentifierQuoteString() + columnName + dbh.getMetaData().getIdentifierQuoteString() + " < ?";
                Map<String, Object> partition = new HashMap<>();
                partition.put("name", "partition_" + i);
                partition.put("where", whereClause);
                partition.put("params", new Object[] { startValue, endValue });
                partitions.add(partition);
            }
            return partitions;
        } catch (SQLException e) {
            throw new Exception("Could not execute query: " + e.getMessage());
        }
    }
}