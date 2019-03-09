package hu.wob.restapi.application;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Konfigur�ci� bet�lt�se.
 * @author K�kay Bal�zs
 *
 */
public class Config {
	
	private static java.util.Properties prop = null;
	
	/**
	 * Property f�jlt bet�lti.
	 * @param configFile - Property f�jl helye
	 * @return - Sikeres-e a bet�lt�s
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
			System.err.println("Hiba a config beolvas�sakor: " + ex.getMessage());
			return false;
		}
		
		return true;
	}
	
	public static String getProperty(String propId) {
		
		return prop.getProperty(propId);
		
	}

}
