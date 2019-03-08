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
 * Adatbázis csatlakozást és lekérdezések futtatását kezelõ osztály.
 * @author Kókay Balázs
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
	 * Csatlakozik az adatbázishoz.
	 * @return - Sikeres csatlakozás
	 */
	public boolean connect() {
		
		try {
			connection = DriverManager.getConnection(db.getDbUrl(), db.getDbUser(), db.getDbPassword());
			connection.setAutoCommit(false);
			return true;
		}catch (SQLException e) {
			System.err.println("SQL hiba csatlakozáskor: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
		
	} 

	/**
	 * PreparedStatement-nek átadja a bejövõ Stringet.
	 * @param insertQuery - Lekérdezés
	 */
	public void prepareQuery(String insertQuery) {
		try {
			ps = connection.prepareStatement(insertQuery);
		} catch (SQLException e) {
			System.err.println("SQL hiba statement felkészítésekor: " + e.getMessage());
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Hozzáadja a Statement-hez az objektumot.
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
				System.err.println("Vát: " + position + ", " + value);
			}
		}  catch (SQLException e) {
			System.err.println("SQL hiba a statementhez adáskor: " + e.getMessage());
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Végrehajtja a Statement-et.
	 */
	public void executeStatement() {
		try {
			ps.executeUpdate();
		} catch (SQLException e) {
			System.err.println("SQL hiba a statement végrehajtásakor: " + e.getMessage());
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
			System.err.println("SQL hiba a statement commitoláskor: " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Csatlakozás zárása.
	 */
	public void disconnect() {
		
		System.out.println("Adatbázis csatlakozás zárása.");
		
		try {
			ps.close();
		} catch (SQLException e) {
			System.err.println("SQL hiba a Statement zárásakor: " + e.getMessage());
			e.printStackTrace();
		}
		try {
			connection.close();
		} catch (SQLException e) {
			System.err.println("SQL hiba az SQL kapcsolat zárásakor: " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Lekérdezést végrehajtja és visszaadja a ResultSet-et.
	 * @param query - lekérdezés
	 * @return - a lekérdezett ResultSet
	 */
	public ResultSet executeQuery(String query) {
		try {
			ps = connection.prepareStatement(query);
			return ps.executeQuery();
		} catch (SQLException e) {
			System.err.println("SQL hiba a query végrehajtásakor: " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

}
