package hu.wob.restapi.application;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Konfiguráció betöltése.
 * @author Kókay Balázs
 *
 */
public class Config {
	
	private static java.util.Properties prop = null;
	
	/**
	 * Property fájlt betölti.
	 * @param configFile - Property fájl helye
	 * @return - Sikeres-e a betöltés
	 */
	public static boolean load(String configFile) {
		
		FileInputStream stream = null;
		prop = new java.util.Properties();
		File propFile = new File(configFile);
		
		try {
			stream = new FileInputStream(propFile);
			prop.load(stream);
						
			
			stream.close();
		} catch (IOException ex) {
			System.err.println("Hiba a config beolvasásakor: " + ex.getMessage());
			return false;
		}
		
		return true;
	}
	
	public static String getProperty(String propId) {
		
		return prop.getProperty(propId);
		
	}

}
