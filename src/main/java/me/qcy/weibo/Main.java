package me.qcy.weibo;

import org.jsoup.nodes.Element;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * @author imqia
 * @date 2022-05-10 19:33
 */
public class Main {

    public static void main(String[] args) throws IOException {
        PageParser pageParser = new PageParser("https://weibo.cn/1050395697?page=1");
        pageParser.getPagePosts().forEach(System.out::println);
    }
}
