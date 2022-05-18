package me.qcy.weibo;

import java.util.Calendar;
import java.util.Date;

/**
 * @author imqia
 * @date 2022-05-12 21:35
 */
public class DefaultTaskFilter implements TaskFilter {

    private final int maxPage;

    private final Date oldestTime;

    public DefaultTaskFilter(int maxPage, Date oldestTime) {
        this.maxPage = maxPage;
        this.oldestTime = oldestTime;
    }

    public DefaultTaskFilter(Date oldestTime) {
        this(-1, oldestTime);
    }

    public DefaultTaskFilter(int maxPage) {
        this(maxPage, null);
    }

    @Override
    public boolean isContinue(int page, String userId, Date oldestTime) {
        if (maxPage == -1) {
            return oldestTime.getTime() > this.oldestTime.getTime();
        } else if (this.oldestTime == null) {
            return page < this.maxPage;
        } else {
            return oldestTime.getTime() > this.oldestTime.getTime() && page < this.maxPage;
        }
    }
}
