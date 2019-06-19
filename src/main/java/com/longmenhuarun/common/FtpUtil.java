package com.longmenhuarun.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

public class FtpUtil {
	
	public static FTPClient getConnectedClient(String ip, Integer port, String user, String pwd) throws IOException {
		
		FTPClient ftpClient = new FTPClient();
		ftpClient.connect(ip, port);
		ftpClient.setControlEncoding("GBK");
		
		if (FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
			if (ftpClient.login(user, pwd)) {
				return ftpClient; // login success.
			}
		} 
		
		// login failure
		disconnect(ftpClient);
		
		return null;
	}
	
	public static void disconnect(FTPClient ftpClient) throws IOException {
		if (null != ftpClient && ftpClient.isConnected()) {
			ftpClient.disconnect();
		}
	}
	
	public static boolean download(FTPClient ftpClient, String remote, String local) throws IOException {
		
		// 设置被动模式
		ftpClient.enterLocalPassiveMode();
		// 设置以二进制方式传输
		ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

		// 检查远程文件是否存在
		FTPFile[] files = ftpClient.listFiles(new String(remote.getBytes("GBK"), "iso-8859-1"));
		if (files.length != 1) {
//System.out.println("远程文件不存在！");
			return false;
		}

		File f = new File(local);
		
		OutputStream out = new FileOutputStream(f);
		InputStream in = ftpClient.retrieveFileStream(new String(remote.getBytes("GBK"), "iso-8859-1"));
		int c;
		while ((c = in.read()) != -1) {
			out.write(c);
		}
		in.close();
		out.close();
		
		return ftpClient.completePendingCommand();
	}
	
	public static boolean checkRemoteFileIsExist(FTPClient ftpClient, String remote) throws IOException {
		
		// 设置被动模式
		ftpClient.enterLocalPassiveMode();
		// 设置以二进制方式传输
		ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

		// 检查远程文件是否存在
		FTPFile[] files = ftpClient.listFiles(new String(remote.getBytes("GBK"), "iso-8859-1"));
		if (files.length != 1) {
//System.out.println("远程文件不存在！");
			return false;
		}
		
		return true;
	}
	
	public static boolean upload(FTPClient ftpClient, String local, String remote) throws Exception {
		
		//设置PassiveMode传输   
        ftpClient.enterLocalPassiveMode();   
        //设置以二进制流的方式传输   
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);   
        ftpClient.setControlEncoding("GBK");   
        
        boolean result = false;
        
        //对远程目录的处理   
        String remoteFileName = remote;   
        if(remote.contains("/")) {   
            remoteFileName = remote.substring(remote.lastIndexOf("/")+1);   
            //创建服务器远程目录结构，创建失败直接返回   
            if(createDirecroty(remote, ftpClient)==false) {   
                return false;   
            }   
        }   
           
        //检查远程是否存在文件   
        FTPFile[] files = ftpClient.listFiles(new String(remoteFileName.getBytes("GBK"), "iso-8859-1"));   
        if(files.length == 1) {   
            ftpClient.deleteFile(remote);
        }      
        	
        result = uploadFile(remoteFileName, new File(local), ftpClient);   
        
        return result; 
	}
	
	private static boolean createDirecroty(String remote,FTPClient ftpClient) throws IOException{   
        String directory = remote.substring(0,remote.lastIndexOf("/")+1);   
        if(!directory.equalsIgnoreCase("/")&&!ftpClient.changeWorkingDirectory(new String(directory.getBytes("GBK"),"iso-8859-1"))){   
            //如果远程目录不存在，则递归创建远程服务器目录   
            int start=0;   
            int end = 0;   
            if(directory.startsWith("/")){   
                start = 1;   
            }else{   
                start = 0;   
            }   
            end = directory.indexOf("/",start);   
            while(true){   
                String subDirectory = new String(remote.substring(start,end).getBytes("GBK"),"iso-8859-1");   
                if(!ftpClient.changeWorkingDirectory(subDirectory)){   
                    if(ftpClient.makeDirectory(subDirectory)){   
                        ftpClient.changeWorkingDirectory(subDirectory);   
                    }else {   
                        return false;   
                    }   
                }   
                   
                start = end + 1;   
                end = directory.indexOf("/",start);   
                   
                //检查所有目录是否创建完毕   
                if(end <= start){   
                    break;   
                }   
            }   
        }   
        return true;   
    } 
	
	private static boolean uploadFile(String remoteFile, File localFile, FTPClient ftpClient) throws IOException {   
		
        RandomAccessFile raf = new RandomAccessFile(localFile, "r");   
        OutputStream out = ftpClient.appendFileStream(new String(remoteFile.getBytes("GBK"),"iso-8859-1"));
        
        byte[] bytes = new byte[(int)raf.length()];
        int c;   
        while((c = raf.read(bytes))!= -1){   
            out.write(bytes,0,c);   
        }   
        out.flush();   
        raf.close();   
        out.close();   
        
        return ftpClient.completePendingCommand();   
    }
}
