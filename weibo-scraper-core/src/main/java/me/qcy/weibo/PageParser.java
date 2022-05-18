package me.qcy.weibo;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author imqia
 * @date 2022-05-10 19:14
 */
public class PageParser {

    private Element pageElement;

    private String url;

    private Consumer<WeiboPost> weiboConsumer;

    private Logger logger = Logger.getLogger(getClass());

    public PageParser(String url) {
        this.url = url;
    }

    public PageParser(Element pageElement) {
        this.pageElement = pageElement;
    }

    public void setWeiboConsumer(Consumer<WeiboPost> weiboConsumer) {
        this.weiboConsumer = weiboConsumer;
    }

    private void getPageElementFromUrl() throws WeiboException {
        try {
            pageElement = JsoupUtils.getDocumentFromUrl(url);
        } catch (IOException e) {
            logger.error("获取页面失败", e);
            throw new WeiboException("获取页面失败", e);
        }
    }

    public List<Element> getPageRecords() throws WeiboException {
        if (pageElement == null) {
            getPageElementFromUrl();
        }
        Elements cs = pageElement.getElementsByClass("c");
        List<Element> pageElements = new ArrayList<>();
        for (Element elem : cs) {
            if (elem.hasAttr("id")) {
                pageElements.add(elem);
            }
        }
        return pageElements;
    }

    public List<WeiboPost> getPagePosts() throws WeiboException {
        if (pageElement == null) {
            getPageElementFromUrl();
        }
        Elements ut = pageElement.getElementsByClass("ut");
        String userId = null;
        String username = null;
        if (ut.size() > 0) {
            Elements ctt = ut.get(0).getElementsByClass("ctt");
            if (ctt.size() > 0) {
                String text = ctt.get(0).text();
                int idx = text.indexOf(' ');
                if (idx > 0) {
                    username = text.substring(0, idx);
                }
            }
            Elements as = ut.get(0).getElementsByTag("a");
            for (Element a : as) {
                String href = a.attr("href");
                if (href.contains("/info")) {
                    int last = href.lastIndexOf("/");
                    userId = href.substring(1, last);
                }
            }
        }

        List<Element> pageRecords = getPageRecords();
        List<WeiboPost> weiboPosts = new ArrayList<>(pageRecords.size());
        for (Element element : pageRecords) {
            WeiboPost post = new ItemParser(element).getWeiboPost();
            post.setUserId(userId);
            post.setUsername(username);
            weiboPosts.add(post);
        }

        return weiboPosts;
    }
}
