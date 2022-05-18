package me.qcy.weibo;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author imqia
 * @date 2022-05-10 19:14
 */
public class ItemParser {

    private String url;

    private Element itemElement;

    private String id;

    private final Logger log = LoggerFactory.getLogger(getClass());

    public ItemParser(Element itemElement) {
        this.itemElement = itemElement;
        id = itemElement.attr("id");
    }

    public ItemParser(String url) {
        this.url = url;
    }

    private void getPostElementFromUrl() throws IOException {
        itemElement = JsoupUtils.getDocumentFromUrl(url);
        String path = new URL(url).getPath();
        int idx = path.lastIndexOf("/");
        id = path.substring(idx + 1);
    }

    /**
     * 爬取数据
     * @return 微博爬取结果
     * @throws IOException 网络异常
     */
    public WeiboPost getWeiboPost() throws WeiboException {
        if (itemElement == null) {
            try {
                getPostElementFromUrl();
            } catch (IOException e) {
                log.error("无法获取单挑微博", e);
                throw new WeiboException("无法获取单挑微博", e);
            }
        }
        WeiboPost post = new WeiboPost();
        getMetadata(post);
        getContent(post);
        getFirstImage(post);
        post.setId(id);
        return post;
    }

    /**
     * 获取元数据
     * @param post 结果
     */
    private void getMetadata(WeiboPost post) {
        for (Element a : itemElement.getElementsByTag("a")) {
            String href = a.attr("href");
            long num = getNumber(a.text());
            if (href.contains("/attitude")) {
                post.setLike(num);
            }
            if (href.contains("/repost")) {
                post.setForward(num);
            }
            if (href.contains("/comment")) {
                post.setComment(num);
            }
        }
        getDate(post);
    }

    private void getFirstImage(WeiboPost weiboPost) {
        Elements ib = itemElement.getElementsByClass("ib");
        if (ib.size() > 0) {
            weiboPost.setPictureUrl(ib.get(0).attr("src"));
        }
    }

    private void getDate(WeiboPost post) {
        Elements ct = itemElement.getElementsByClass("ct");
        if (ct.size() > 0) {
            String time = ct.get(0).text().trim();
            if (time.contains("今天")) {
                Pattern pattern = Pattern.compile("([0-9]{2}):([0-9]{2})");
                Matcher matcher = pattern.matcher(time);
                if (matcher.find()) {
                    int minute = Integer.parseInt(matcher.group(2));
                    int hour = Integer.parseInt(matcher.group(1));
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.HOUR, hour);
                    calendar.set(Calendar.MINUTE, minute);
                    post.setPostDate(calendar.getTime());
                }
            } else if (time.contains("分钟前")) {
                Pattern pattern = Pattern.compile("([0-9]+)");
                Matcher matcher = pattern.matcher(time);
                if (matcher.find()) {
                    int minutesMinor = Integer.parseInt(matcher.group(1));
                    Calendar calendar = Calendar.getInstance();
                    calendar.add(Calendar.MINUTE, -minutesMinor);
                    post.setPostDate(calendar.getTime());
                }
            } else {
                Date date = null;
                if (time.contains("来自")) {
                    time = time.substring(0, time.indexOf(" 来自"));
                }
                try {
                    date = new SimpleDateFormat("MM月dd日 HH:mm").parse(time);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    calendar.set(Calendar.YEAR, 2022);
                    date = calendar.getTime();
                } catch (Exception e) {
                    try {
                        date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time);
                    } catch (Exception e1) {
                        log.error("无法解析日期: {}, ex: {}", time, e.getLocalizedMessage());
                    }
                }
                post.setPostDate(date);
            }
        }
    }

    /**
     * 获取点赞、评论、转发数量
     * @param s 文字
     * @return 数量
     */
    private long getNumber(String s) {
        if (s == null) {
            return 0;
        }
        int left = s.indexOf("[");
        int right = s.indexOf("]");
        if (left > 0 && right > 0 && left < right) {
            return Long.parseLong(s.substring(left + 1, right));
        }
        return 0;
    }

    /**
     * 获取微博内容
     * @param post 结果
     */
    private void getContent(WeiboPost post) {
        Elements cmt = itemElement.getElementsByClass("cmt");
        if (cmt.size() > 0) {
            if (cmt.get(0).text().trim().startsWith("转发了")) {
                post.setIsForward(Boolean.TRUE);
            }
        }

        Elements contents = itemElement.getElementsByClass("ctt");
        if (contents.size() == 0) {
            return;
        }
        Element content = contents.get(0);
        String originUrl = null;
        for (Element a : content.getElementsByTag("a")) {
            String href = a.attr("href");
            if (href.startsWith("/comment")) {
                originUrl = "https://weibo.cn/" + href;
                break;
            }
        }
        if (originUrl != null) {
            post.setContent(getContentByOriginUrl(originUrl));
        } else {
            post.setContent(getContentByElement(content));
        }
    }

    private String getContentByElement(Element content) {
        return content.text();
    }

    private String getContentByOriginUrl(String originUrl) {
        Document itemOriginPage;
        try {
            itemOriginPage = JsoupUtils.getDocumentFromUrl(originUrl);
        } catch (IOException e) {
            return null;
        }
        Elements contents = itemOriginPage.getElementsByClass("ctt");
        if (contents.size() == 0) {
            return null;
        }
        Element content = contents.get(0);
        return content.text();
    }
}
