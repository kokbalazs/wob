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
 * FTP csatlakoz�st, f�jl felt�lt�s�t kezel� oszt�ly.
 * @author K�kay Bal�zs
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
	 * @return Sikeres-e a csatlakoz�s.
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
			System.err.println("Socket hiba FTP csatlakoz�skor: " + e.getMessage());
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			System.err.println("IO hiba FTP csatlakoz�skor: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Felt�lti a bej�v� f�jlt az FTP-re.
	 * @param file - Felt�ltend� f�jl
	 * @return - Sikeres-e a felt�lt�s.
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
				System.out.println("Nem l�tez� f�jl: " + file);
				return false;
			}
			
		} catch (IOException e) {
			System.err.println("IO hiba a f�jl felt�lt�sekor: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
		
	}
	
	/**
	 * Csatlakoz�s z�r�sa.
	 */
	public void disconnect() {
		try {
			if (ftpClient.isConnected()) {
                ftpClient.logout();
                ftpClient.disconnect();
            }
		} catch (IOException e) {
			System.err.println("IO hiba FTP csatlakoz�s z�r�sakor: " + e.getMessage());
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
