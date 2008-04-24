package org.nestframework.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;


public class ProjectInitTool
{
	public static String HandleExts = ",java,xml,properties,jsp,html,htm,js,css,mf,component,prefs,classpath,project,txt,";
    
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
    	StringWriter writer = new StringWriter(102400);
    	char[] buf = new char[1024];
    	FileReader reader = new FileReader(file);
    	int len = 0;
    	while ((len = reader.read(buf)) != -1) {
    		writer.write(buf, 0, len);
    	}
    	String content = writer.toString();
    	reader.close();
    	writer.close();
    	for (int i = 0; i < rp.length; i++) {
    		content = content.replaceAll(rp[i][0], rp[i][1]);
    	}
    	FileWriter fw = new FileWriter(file);
    	fw.write(content);
    	fw.close();
    	
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
