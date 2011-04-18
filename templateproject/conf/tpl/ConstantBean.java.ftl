package ${hss_base_package}.common;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

public class ConstantBean {

	private String propertiesFile;
	private String propertiesFileDB;

	private static Map<String, String> propertiesMap =new HashMap<String, String>();
	private static Map<String, String> propertiesMapDB =new HashMap<String, String>();
	
	public String getPropertiesFile() {
		return propertiesFile;
	}

	public void setPropertiesFile(String propertiesFile) {
		this.propertiesFile = propertiesFile;
	}
	
	public String getPropertiesFileDB() {
		return propertiesFileDB;
	}

	public void setPropertiesFileDB(String propertiesFileDB) {
		this.propertiesFileDB = propertiesFileDB;
	}

	public String get(String propertyName){
		return propertiesMap.get(propertyName);
	}
	
	public void init(){
		// ∂¡»°≈‰÷√Œƒº˛
		Properties prop = new Properties();
		Properties propDB = new Properties();
		try {
			InputStream in = getClass().getResourceAsStream("/" + propertiesFile);

			prop.load(in);
			Iterator<Object> iterator = prop.keySet().iterator();
			while(iterator.hasNext()){				
				String pName =(String)iterator.next();
				propertiesMap.put(pName, new String(prop.getProperty(pName).getBytes("ISO-8859-1"),"utf8"));
			}
			in.close();
			
			InputStream inDB = getClass().getResourceAsStream("/" + propertiesFileDB);

			propDB.load(inDB);
			Iterator<Object> iteratorDB = propDB.keySet().iterator();
			while(iteratorDB.hasNext()){				
				String pNameDB =(String)iteratorDB.next();
				propertiesMapDB.put(pNameDB, new String(propDB.getProperty(pNameDB).getBytes("ISO-8859-1"),"utf8"));
			}
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
