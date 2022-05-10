package me.qcy.weibo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * @author imqia
 * @date 2022-05-10 19:17
 */
public class JsoupUtils {
    public static Document getDocumentFromUrl(String url) throws IOException {
        return Jsoup.connect(url).cookie("_T_WM", GlobalConfig.getInstance().getWm())
                .cookie("SUB", GlobalConfig.getInstance().getSUB())
                .cookie("SUBP", GlobalConfig.getInstance().getSUBP())
                .post();
    }
}
