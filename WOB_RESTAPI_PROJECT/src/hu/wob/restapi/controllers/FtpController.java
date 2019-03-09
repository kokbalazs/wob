package hu.wob.restapi.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import hu.wob.restapi.models.Ftp;

/**
 * FTP csatlakozást, fájl feltöltését kezelõ osztály.
 * @author Kókay Balázs
 *
 */
public class FtpController {
	
	private Ftp ftp;
	private FTPClient ftpClient;
	
	public FtpController(Ftp ftp) {
		this.ftp = ftp;
	}
	
	/**
	 * Csatlakozik az FTP-hez.
	 * @return Sikeres-e a csatlakozás.
	 */
	public Boolean connect() {
		ftpClient = new FTPClient();
		try {
			ftpClient.connect(ftp.getFtpIp(), ftp.getFtpPort());
			ftpClient.login(ftp.getFtpUser(), ftp.getFtpPassword());
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			ftpClient.enterLocalPassiveMode();
			
			return true;
		} catch (SocketException e) {
			System.err.println("Socket hiba FTP csatlakozáskor: " + e.getMessage());
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			System.err.println("IO hiba FTP csatlakozáskor: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Feltölti a bejövõ fájlt az FTP-re.
	 * @param file - Feltöltendõ fájl
	 * @return - Sikeres-e a feltöltés.
	 */
	public Boolean uploadFile(File file) {
		try {
			
			if (file.exists()) {
				
				String remoteFile = file.getName();
				InputStream inputStream = new FileInputStream(file);
				
				try {
					return ftpClient.storeFile(remoteFile, inputStream);
				} finally {
					inputStream.close();
				}
				
			} else {
				System.out.println("Nem létezõ fájl: " + file);
				return false;
			}
			
		} catch (IOException e) {
			System.err.println("IO hiba a fájl feltöltésekor: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
		
	}
	
	/**
	 * Csatlakozás zárása.
	 */
	public void disconnect() {
		try {
			if (ftpClient.isConnected()) {
                ftpClient.logout();
                ftpClient.disconnect();
            }
		} catch (IOException e) {
			System.err.println("IO hiba FTP csatlakozás zárásakor: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public Ftp getFtp() {
		return ftp;
	}

	public void setFtp(Ftp ftp) {
		this.ftp = ftp;
	}

	public FTPClient getFtpClient() {
		return ftpClient;
	}

	public void setFtpClient(FTPClient ftpClient) {
		this.ftpClient = ftpClient;
	}
	

}
