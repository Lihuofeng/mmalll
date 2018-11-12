package com.bees360;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * file util
 * 
 * @author shoushan.zhao
 *
 */
public class FileUtil {

    public static void  downLoadFromUrl(String urlStr, String fileName, String savePath, int type) throws IOException{
    	byte[] bytes = HttpsUtil.doGet(urlStr);
        FileOutputStream fos = new FileOutputStream(savePath + File.separator + fileName);
        fos.write(bytes);
        fos.close();
    }
	
	/**
     * Download files from network Url
     * @param urlStr
     * @param fileName
     * @param savePath
     * @throws IOException
     */
    public static void  downLoadFromUrl(String urlStr,String fileName,String savePath) throws IOException{
        URL url = new URL(urlStr);  
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();  
        // Set the extra time to 3 seconds.
        conn.setConnectTimeout(3*1000);
        // Prevent shield from crawling and return 403 error.
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

        // Get input stream
        InputStream inputStream = conn.getInputStream();  
        // Get your own array
        byte[] getData = readInputStream(inputStream);    

        // File save location
        File saveDir = new File(savePath);
        if(!saveDir.exists()){
            saveDir.mkdir();
        }
        File file = new File(saveDir+File.separator+fileName);    
        FileOutputStream fos = new FileOutputStream(file);     
        fos.write(getData); 
        if(fos!=null){
            fos.close();  
        }
        if(inputStream!=null){
            inputStream.close();
        }
    }

    /**
     * Get byte array from input stream
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static  byte[] readInputStream(InputStream inputStream) throws IOException {  
        byte[] buffer = new byte[1024];  
        int len = 0;  
        ByteArrayOutputStream bos = new ByteArrayOutputStream();  
        while((len = inputStream.read(buffer)) != -1) {  
            bos.write(buffer, 0, len);  
        }  
        bos.close();  
        return bos.toByteArray();  
    }  

    public static void main(String[] args) {
        try{
            downLoadFromUrl("https://maps.googleapis.com/maps/api/staticmap?center=28.86595496237607,-101.92337654610367&zoom=12&size=440x440&format=jpg&maptype=satellite&key=AIzaSyAdfVMg3bZKotcsxUDWZ6lXftymKzzIpkM",
                    "abc.jpg","E:", 1);
        }catch (Exception e) {
            // TODO: handle exception
        }
    }
}
