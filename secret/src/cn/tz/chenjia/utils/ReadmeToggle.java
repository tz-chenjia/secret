package cn.tz.chenjia.utils;

public class ReadmeToggle {

    private static final String README = "jar";

    public static boolean isJar() {
        return README.equals("jar");
    }

    public static boolean exists() {
        if (isJar()) {
            return JarUtils.exists();
        } else {
            return ReadmeUtils.exists();
        }
    }

    public static void write(String content) {
        if (isJar()) {
            JarUtils.writeJarReadme(content);
        } else {
            ReadmeUtils.writeReadme(content);
        }
    }

    public static String read() {
        if (isJar()) {
            return JarUtils.readJarReadme();
        } else {
            return ReadmeUtils.readReadme();
        }
    }


}
