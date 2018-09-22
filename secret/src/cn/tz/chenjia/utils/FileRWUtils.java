package cn.tz.chenjia.utils;

import cn.tz.chenjia.rule.EMsg;

import java.io.*;

public class FileRWUtils {

    public static boolean exists(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            return true;
        } else {
            try {
                file.createNewFile();
            } catch (IOException e) {
                handlingError(e);
            }
            return false;
        }
    }

    public static String read(File file) {
        InputStreamReader isr = null;
        BufferedReader br = null;
        StringBuffer str = new StringBuffer();
        try {
            isr = new InputStreamReader(new FileInputStream(file));
            br = new BufferedReader(isr);
            String lineStr;
            while ((lineStr = br.readLine()) != null) {
                str.append(lineStr);
            }
        } catch (IOException e) {
            handlingError(e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    handlingError(e);
                }
            }
            if (isr != null) {
                try {
                    isr.close();
                } catch (IOException e) {
                    handlingError(e);
                }
            }
        }
        return str.toString();
    }

    public static void write(File file, String content) {
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new FileWriter(file));
            out.write(content);
            out.flush(); // 把缓存区内容压入文件
            out.close(); // 最后记得关闭文件
        } catch (IOException e) {
            handlingError(e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    handlingError(e);
                }
            }
        }

    }

    private static void handlingError(Throwable e){
        EMsg.println(e.getMessage());
        System.exit(1);
    }

}