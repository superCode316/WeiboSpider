package me.qcy.weibo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

/**
 * @author imqia
 * @date 2022-05-10 19:40
 */
public class GlobalConfig {
    private String wm;
    private String SUB;
    private String SUBP;

    public String getWm() {
        return wm;
    }

    public void setWm(String wm) {
        this.wm = wm;
    }

    public String getSUB() {
        return SUB;
    }

    public void setSUB(String SUB) {
        this.SUB = SUB;
    }

    public String getSUBP() {
        return SUBP;
    }

    public void setSUBP(String SUBP) {
        this.SUBP = SUBP;
    }

    private static GlobalConfig globalConfig;

    public static GlobalConfig getInstance() throws IOException {
        if (globalConfig == null) {
            globalConfig = new GlobalConfig();
        }
        return globalConfig;
    }

    private GlobalConfig() throws IOException {
        URL resource = Main.class.getClassLoader().getResource("application.properties");
        BufferedReader file = new BufferedReader(new FileReader(resource.getFile()));
        String line;

        while ((line = file.readLine()) != null) {
            int first = line.indexOf('=');
            String key = line.substring(0, first);
            String value = line.substring(first + 1);
            if ("me.qcy.weibo.cookie.wm".equals(key)) {
                wm = value;
            }
            if ("me.qcy.weibo.cookie.SUB".equals(key)) {
                SUB = value;
            }
            if ("me.qcy.weibo.cookie.SUBP".equals(key)) {
                SUBP = value;
            }
        }
    }
}
