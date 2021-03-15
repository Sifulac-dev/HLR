package fr.sifulac.plugin;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBase {

	public Connection connection;

	private String _dbName;

	private File _dataFolder;

	private String createTableQueryHoppers = "CREATE TABLE IF NOT EXISTS hoppers ('identifiant' varchar(255), 'chunkX' INTEGER, 'chunkZ' INTEGER, 'locationX' INTEGER, 'locationY' INTEGER, 'locationZ' INTEGER, 'regionX' INTEGER, 'regionZ' INTEGER, 'world' varchar(255));";

	public DataBase(String dbName, File dataFolder) {

		_dbName = dbName;
		_dataFolder = dataFolder;

	}

	public void connect() {

		if (isConnect())
			return;

		if(!Main.getInstance().getDataFolder().exists()) {
			Main.getInstance().getDataFolder().mkdir();
		}
		
		File f = new File(_dataFolder, _dbName + ".db");
		if (!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		try {
			connection = DriverManager.getConnection("jdbc:sqlite:" + f);
			Statement s = connection.createStatement();
			s.executeUpdate(createTableQueryHoppers);
			s.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void disconnect() {

		if (isConnect()) {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	public boolean isConnect() {
		return connection != null;
	}

	public void addHopper(HopperObject hopper, int regionX, int regionZ, String worldName) {
		try {
			PreparedStatement q = connection.prepareStatement(
					"INSERT INTO hoppers (identifiant, chunkX, chunkZ, locationX, locationY, locationZ, regionX, regionZ, world) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
			String identifiant = String.valueOf(hopper.getLocationX() + hopper.getLocationY() + hopper.getLocationZ());
			q.setString(1, identifiant);
			q.setInt(2, hopper.getChunkX());
			q.setInt(3, hopper.getChunkZ());
			q.setInt(4, hopper.getLocationX());
			q.setInt(5, hopper.getLocationY());
			q.setInt(6, hopper.getLocationZ());
			q.setInt(7, regionX);
			q.setInt(8, regionZ);
			q.setString(9, worldName);
			q.execute();
			q.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void removeHopper(HopperObject hopper) {
		try {
			String identifiant = String.valueOf(hopper.getLocationX() + hopper.getLocationY() + hopper.getLocationZ());
			PreparedStatement q = connection.prepareStatement("DELETE FROM hoppers WHERE identifiant = ?");
			q.setString(1, identifiant);
			q.executeUpdate();
			q.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void loadHoppers() {

		try {
			PreparedStatement q = connection.prepareStatement("SELECT * FROM hoppers");
			ResultSet rs = q.executeQuery();
			while (rs.next()) {

				int chunkX = rs.getInt("chunkX");
				int chunkZ = rs.getInt("chunkZ");

				int locationX = rs.getInt("locationX");
				int locationY = rs.getInt("locationY");
				int locationZ = rs.getInt("locationZ");

				int regX = rs.getInt("regionX");
				int regZ = rs.getInt("regionZ");

				String world = rs.getString("world");

				Region region = Reflections.getRegions().stream()
						.filter(r -> r.getId().equals(String.valueOf(regX + regZ))).findFirst().orElse(null);

				if (region != null) {

					region.addHopper(new HopperObject(locationX, locationY, locationZ, chunkX, chunkZ, world));

				} else {

					Region r = new Region(String.valueOf(regX + regZ));
					if (Boolean.TRUE.equals(
							r.addHopper(new HopperObject(locationX, locationY, locationZ, chunkX, chunkZ, world)))) {
						Reflections.getRegions().add(r);
					}
				}
			}
			rs.close();
			q.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
