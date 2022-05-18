package me.qcy.weibo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @author imqia
 * @date 2022-05-12 20:39
 */
public class ScraperTask {

    private final String userId;

    private final TaskFilter taskFilter;

    private final WeiboConsumer weiboConsumer;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public ScraperTask(String userId, TaskFilter taskFilter, WeiboConsumer weiboConsumer) {
        this.userId = userId;
        this.taskFilter = taskFilter;
        this.weiboConsumer = weiboConsumer;
    }

    public void start() throws Exception {
        int i = 1;
        Date date = new Date();
        logger.info("开始爬取用户 {}", userId);
        while (taskFilter.isContinue(i, userId, date)) {
            PageParser pageParser = new PageParser("https://weibo.cn/" + userId + "?page=" + i++);
            List<WeiboPost> pagePosts = pageParser.getPagePosts();
            if (pagePosts.size() == 0) {
                break;
            }
            for (int j = pagePosts.size() - 1; j >= 0; j--) {
                WeiboPost weiboPost = pagePosts.get(j);
                if (weiboPost.getPostDate() != null) {
                    date = weiboPost.getPostDate();
                }
            }
            for (WeiboPost weiboPost : pagePosts) {
                weiboConsumer.accept(weiboPost);
            }
            try {
                Thread.sleep(getWaitTime());
            } catch (InterruptedException e) {
                logger.info("等待时被中断，停止爬取");
                return;
            }
            logger.info("成功爬取用户 {} 第 {} 页", userId, i - 1);
        }
    }

    protected long getWaitTime() {
        return (long) (Math.random() * 1000 + 2000);
    }
}
