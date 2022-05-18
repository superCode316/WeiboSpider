package me.qcy.weibo;

import java.io.IOException;
import java.sql.SQLException;

/**
 * @author imqia
 * @date 2022-05-10 19:33
 */
public class Main {
    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();
        ScraperTask task = new ScraperTask("1642512402", new DefaultTaskFilter(100), new DefaultWeiboConsumer("root", "QIANcaoyu316", "jdbc:mysql://localhost:3306/biyesheji"));
        task.start();
        System.out.println(System.currentTimeMillis() - start);
    }
}
