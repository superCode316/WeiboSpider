package me.qcy.weibo;

import java.util.Date;

/**
 * @author imqia
 * @date 2022-05-12 20:40
 */
public interface TaskFilter {
    boolean isContinue(int page, String userId, Date oldestTime);
}
