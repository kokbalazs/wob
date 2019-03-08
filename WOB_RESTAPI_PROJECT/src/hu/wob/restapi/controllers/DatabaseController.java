package hu.wob.restapi.controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

import hu.wob.restapi.application.Config;
import hu.wob.restapi.models.Database;

/**
 * Adatb�zis csatlakoz�st �s lek�rdez�sek futtat�s�t kezel� oszt�ly.
 * @author K�kay Bal�zs
 *
 */
public class DatabaseController {
	
	private Connection connection;
	private PreparedStatement ps;
	private Database db;
	
	public DatabaseController(Database db) {
		this.db = db;
	}

	/**
	 * Csatlakozik az adatb�zishoz.
	 * @return - Sikeres csatlakoz�s
	 */
	public boolean connect() {
		
		try {
			connection = DriverManager.getConnection(db.getDbUrl(), db.getDbUser(), db.getDbPassword());
			connection.setAutoCommit(false);
			return true;
		}catch (SQLException e) {
			System.err.println("SQL hiba csatlakoz�skor: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
		
	} 

	/**
	 * PreparedStatement-nek �tadja a bej�v� Stringet.
	 * @param insertQuery - Lek�rdez�s
	 */
	public void prepareQuery(String insertQuery) {
		try {
			ps = connection.prepareStatement(insertQuery);
		} catch (SQLException e) {
			System.err.println("SQL hiba statement felk�sz�t�sekor: " + e.getMessage());
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Hozz�adja a Statement-hez az objektumot.
	 * @param position - melyik helyen
	 * @param value - milyen objektumot
	 */
	public void addToStatement (int position, Object value) {
		
		try {
			if (value instanceof Integer) {
				ps.setInt(position, (Integer) value);
			} else if (value instanceof Long) {
				ps.setLong(position, (Long) value);
			} else if (value instanceof String) {
				ps.setString(position, (String) value); 
			} else if (value instanceof Double) {
				ps.setDouble(position, (Double) value);
			} else if (value instanceof Date ) {
				ps.setObject(position, ((Date) value).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
			} else if (value instanceof UUID) {
				ps.setString(position, ((UUID) value).toString());
			} else if (value == null) {
				ps.setNull(position, Types.VARCHAR);
			} else {
				System.err.println("V�t: " + position + ", " + value);
			}
		}  catch (SQLException e) {
			System.err.println("SQL hiba a statementhez ad�skor: " + e.getMessage());
			e.printStackTrace();
		}
		
	}
	
	/**
	 * V�grehajtja a Statement-et.
	 */
	public void executeStatement() {
		try {
			ps.executeUpdate();
		} catch (SQLException e) {
			System.err.println("SQL hiba a statement v�grehajt�sakor: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Commitolja a Statement-et.
	 */
	public void commit() {
		try {
			connection.commit();
		} catch (SQLException e) {
			System.err.println("SQL hiba a statement commitol�skor: " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Csatlakoz�s z�r�sa.
	 */
	public void disconnect() {
		
		System.out.println("Adatb�zis csatlakoz�s z�r�sa.");
		
		try {
			ps.close();
		} catch (SQLException e) {
			System.err.println("SQL hiba a Statement z�r�sakor: " + e.getMessage());
			e.printStackTrace();
		}
		try {
			connection.close();
		} catch (SQLException e) {
			System.err.println("SQL hiba az SQL kapcsolat z�r�sakor: " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Lek�rdez�st v�grehajtja �s visszaadja a ResultSet-et.
	 * @param query - lek�rdez�s
	 * @return - a lek�rdezett ResultSet
	 */
	public ResultSet executeQuery(String query) {
		try {
			ps = connection.prepareStatement(query);
			return ps.executeQuery();
		} catch (SQLException e) {
			System.err.println("SQL hiba a query v�grehajt�sakor: " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

}
