package cn.tyson.simplefilemanager.utils;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.*;

public class FileUtils {

    public static String writeTxt(String text, String fileName) {
        String dir = FileUtils.class.getClassLoader().getResource("").getPath() + "files/";
        File parent = new File(dir);
        if (!parent.exists()) {
            parent.mkdirs();
        }
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        try {
            fos = new FileOutputStream(new File(dir + fileName));
            bos = new BufferedOutputStream(fos);
            bos.write(text.getBytes("UTF-8"));
            bos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                bos.close();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return dir + fileName;
    }

    public static ResponseEntity<byte[]> buildResponseEntity(String path) throws UnsupportedEncodingException {
        File file =  new File(path);
        byte[] body = null;
        InputStream is = null;
        try {
            is = new FileInputStream(file);
            body = new byte[is.available()];
            is.read(body);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attchement;filename=" + new String(file.getName().getBytes("utf-8"), "ISO8859-1"));
        HttpStatus statusCode = HttpStatus.OK;
        return new ResponseEntity<>(body, headers, statusCode);
    }

}
