package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * @author ChenJie 读取配置文件
 */
public class DLDEConfiguration {

	private Logger logger = Logger.getLogger(DLDEConfiguration.class);
	
	private static DLDEConfiguration config = null;
	private String file;
	boolean flag=false;

	private DLDEConfiguration() {

	}

	private DLDEConfiguration(String fileName) {
		this.file = fileName;
	}
	private DLDEConfiguration(String fileName,boolean flag) {
		this.file = fileName;
		this.flag=flag;
	}
	synchronized public static DLDEConfiguration getInstance(String fileName) {
		if (config == null) {
			return new DLDEConfiguration(fileName,false);
		} else
			return config;
	}
	
	synchronized public static DLDEConfiguration getInstance4Maven(String fileName) {
		if (config == null) {
			return new DLDEConfiguration(fileName,true);
		} else
			return config;
	}

	/**
	 * get the value in the config.properties
	 * 
	 * @param key
	 * @return
	 * @throws Exception 
	 */
	synchronized public String getValue(String key){
		Properties properties = new Properties();
		String value = null;
		try {
			String filePath =getClass().getClassLoader().getResource(file).getPath();
			File configFile = new File(filePath);
			if(configFile.exists()){
				logger.info("config file Path:" + filePath);
				FileInputStream fis = new FileInputStream(new File(filePath));
				properties.load(fis);
				fis.close();
			}else{
				properties.load(this.getClass().getClassLoader().getResourceAsStream(file)); 
			}
			value = (String) properties.get(key);
		} catch (FileNotFoundException e) {
			logger.error(key + " value cant find in " + file);
		} catch (IOException e) {
			logger.error(file + "  config file io exception");
		} catch (Exception e) {
			logger.error(file + "  config file get value error !");
		} finally {
			if (value == null) {
				logger.info("can't get the right value of " + key+ " from " + file+" , you will get null .");
			}
		}
		return value;
	}
}