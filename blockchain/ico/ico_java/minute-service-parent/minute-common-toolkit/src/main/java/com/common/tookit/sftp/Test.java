package com.common.tookit.sftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.jcraft.jsch.SftpException;

public class Test {
	public static void main(String[] args) throws FileNotFoundException, SftpException {
		SFTPClient client = new SFTPClient("root", "udqirvmcgk", "106.2.3.36", 22);
		client.login();
		client.upload("alibaba", "test", "index.txt", new FileInputStream(new File("D:\\index.txt")));
		client.logout();

	}

}
