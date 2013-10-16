package org.nestframework.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import cn.xddai.chardet.CharsetDetector;


public class ProjectInitTool
{
	public static String HandleExts = ",java,xml,properties,jsp,html,htm,js,css,mf,component,prefs,classpath,project,txt,tld,";
    
	public static String NohandleDirs=",ext-2.1,images,jquery,lib,";
	
    public static void handle(File dir, String[][] rp) {
    	File[] files = dir.listFiles();
    	for (int i = 0; i < files.length; i++) {
    		File file = files[i];
    		if (file.isFile()) {
    			try {
					replace(file, rp);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
    		} else if (file.isDirectory()) {
    			String filename = file.getName();
    	    	if (NohandleDirs.indexOf(',' + filename + ',') > -1) {
    	    		continue;
    	    	}
    			handle(file, rp);
    		}
    	}
    }
    
    private static void replace(File file, String[][] rp) throws IOException {
    	String filename = file.getName();
    	String fileext = filename.substring(filename.lastIndexOf('.') + 1);
    	if (HandleExts.indexOf(',' + fileext + ',') == -1) {
    		return;
    	}
    	//判断文件编码
    	CharsetDetector charDect = new CharsetDetector();      
    	InputStream ios = new FileInputStream(file);
    	String fileCharset="GBK";  
    	String[] probableSet = charDect.detectChineseCharset(ios);
    	fileCharset=probableSet[0];

    	String content = readFile(file,fileCharset);
    	for (int i = 0; i < rp.length; i++) {
    		content = content.replaceAll(rp[i][0], rp[i][1]);
    	}
    	writeFile(file,content,fileCharset);
    	
    }
    public static String readFile(File f,String fileCharset) {
    	String fileContent = "";
		try {
			if (f.isFile() && f.exists()) {
				InputStreamReader read = new InputStreamReader(
						new FileInputStream(f), fileCharset);
		    	BufferedReader reader = new BufferedReader(read);
				String line;
				while ((line = reader.readLine()) != null) {
					fileContent += line + "\r\n";
				}
				read.close();
			}
		} catch (Exception e) {
			System.out.println("读取文件内容操作出错");
			e.printStackTrace();
		}
    	return fileContent;
    }
    
    public static void writeFile(File f, String fileContent, String fileCharset) {
    	try {
    	   if (!f.exists()) {
    		   f.createNewFile();
    	   }
    	   OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(f),fileCharset);
    	   BufferedWriter writer=new BufferedWriter(write);  
    	   writer.write(fileContent);
    	   writer.close();
    	} catch (Exception e) {
    	   System.out.println("写文件内容操作出错");
    	   e.printStackTrace();
    	}
    }

    /**
     * @param args
     */
    public static void main(String[] args)
    {
    	String[][] rp = new String[args.length][2];
    	File dir = new File(".");
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            int p = arg.indexOf('=');
            String left = arg.substring(0, p);
            String right = arg.substring(p + 1);
            if ("-ext".equalsIgnoreCase(left)) {
            	HandleExts += right + ",";
            } else if ("-dir".equalsIgnoreCase(left)) {
            	dir = new File(right);
            }
            rp[i] = new String[] {left, right};
        }
        
        handle(dir, rp);
    }

}
