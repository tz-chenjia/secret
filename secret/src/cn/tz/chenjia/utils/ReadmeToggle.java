package cn.tz.chenjia.utils;

import cn.tz.chenjia.configs.ConfigsUtils;

import java.io.File;

public class ReadmeToggle {

    private static final String SECRET_FILE_NAME="secret";

    private static final String README = "jpar";

    public static boolean isJar() {
        return README.equals("jar");
    }

    public static boolean exists() {
        if (isJar()) {
            return JarUtils.exists();
        } else {
            return FileRWUtils.exists(getSecret());
        }
    }

    public static void write(String content) {
        if (isJar()) {
            JarUtils.writeJarReadme(content);
        } else {
            FileRWUtils.write(new File(getSecret()), content);
        }
    }

    public static String read() {
        if (isJar()) {
            return JarUtils.readJarReadme();
        } else {
            return FileRWUtils.read(new File(getSecret()));
        }
    }

    private static String getSecret() {
        return ConfigsUtils.getRootPath() + SECRET_FILE_NAME;
    }
}
