package cn.tz.chenjia.utils;

import cn.tz.chenjia.rule.EMsg;

import java.io.*;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;

public class JarUtils {

    private static final String JAR_NAME = "secret.jar";
    private static final String README_PATH = "secret";

    public static void writeJarReadme(String content) {
        boolean readmeExists = false;
        JarFile file = null;
        JarOutputStream jos = null;
        try {
            file = new JarFile(jarPath());
            TreeMap tm = new TreeMap();
            Enumeration<JarEntry> entries = file.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                byte[] b = getByte(file.getInputStream(entry));
                tm.put(entry.getName(), b);
            }
            jos = new JarOutputStream(new FileOutputStream(jarPath()));
            Set set = tm.entrySet();
            Iterator it = set.iterator();
            while (it.hasNext()) {
                Map.Entry me = (Map.Entry) it.next();
                String name = (String) me.getKey();
                JarEntry jeNew = new JarEntry(name);
                jos.putNextEntry(jeNew);
                byte[] b = (byte[]) me.getValue();
                if (name.contains(README_PATH)) {
                    b = content.getBytes();
                    readmeExists = true;
                }
                jos.write(b, 0, b.length);
            }
            if (!readmeExists) {
                JarEntry entry = new JarEntry(README_PATH);
                jos.putNextEntry(entry);
                jos.write(content.getBytes("UTF-8"));
            }
        } catch (IOException e) {
            handlingError(e);
        } finally {
            if (jos != null) {
                try {
                    jos.close();
                } catch (IOException e) {
                    handlingError(e);
                }
            }
        }
    }

    public static String readJarReadme() {
        StringBuffer sb = new StringBuffer();
        try {
            JarFile file = new JarFile(jarPath());
            ZipEntry entry = file.getEntry(README_PATH);
            InputStream stream = file.getInputStream(entry);
            BufferedReader br = new BufferedReader(new InputStreamReader(stream));
            String str;
            while ((str = br.readLine()) != null) {
                sb.append(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
            handlingError(e);
        }

        return sb.toString();
    }

    private static String jarPath() {
        return System.getProperty("user.dir") + File.separator + JAR_NAME;
    }

    public static boolean exists() {
        boolean exists = false;
        JarFile file = null;
        try {
            file = new JarFile(jarPath());
            ZipEntry entry = file.getEntry(README_PATH);
            if (entry == null) {
                writeJarReadme("");
            } else {
                exists = true;
            }
        } catch (IOException e) {
            handlingError(e);
        }
        return exists;
    }

    //从输入取字节
    public static byte[] getByte(InputStream s) {
        byte[] buffer = new byte[0];
        byte[] chunk = new byte[4096];
        int count;
        try {
            while ((count = s.read(chunk)) >= 0) {
                byte[] t = new byte[buffer.length + count];
                System.arraycopy(buffer, 0, t, 0, buffer.length);
                System.arraycopy(chunk, 0, t, buffer.length, count);
                buffer = t;
            }
            s.close();
        } catch (Exception e) {
            handlingError(e);
        }
        return buffer;
    }

    private static void handlingError(Throwable e){
        EMsg.println(e.getMessage());
        System.exit(1);
    }
    
}
