package me.qcy.weibo;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.PreparedStatement;

/**
 * @author imqia
 * @date 2022-05-12 21:33
 */
public class DefaultWeiboConsumer implements WeiboConsumer {

    private JdbcTemplate jdbcTemplate;

    private final String sql = "INSERT INTO weibo(id, comment_num, content, original, publish_time, retweet_num, up_num, user_id) " +
            "value (?, ?, ?, ?, ?, ?, ?, ?) " +
            "ON DUPLICATE KEY UPDATE comment_num = ?, retweet_num = ?, up_num = ?;";

    public DefaultWeiboConsumer(String username, String password, String url) {
        MysqlDataSource mysqlDataSource = new MysqlDataSource();
        mysqlDataSource.setURL(url);
        mysqlDataSource.setUser(username);
        mysqlDataSource.setPassword(password);
        jdbcTemplate = new JdbcTemplate(mysqlDataSource);
    }

    @Override
    public void accept(WeiboPost weiboPost) {
        jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement = null;
            preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, weiboPost.getId());
            preparedStatement.setLong(2, weiboPost.getComment());
            preparedStatement.setString(3, weiboPost.getContent());
            preparedStatement.setBoolean(4, Boolean.TRUE.equals(weiboPost.isForward()));
            preparedStatement.setDate(5, new java.sql.Date(weiboPost.getPostDate().getTime()));
            preparedStatement.setLong(6, weiboPost.getForward());
            preparedStatement.setLong(7, weiboPost.getLike());
            preparedStatement.setString(8, weiboPost.getUserId());
            preparedStatement.setLong(9, weiboPost.getComment());
            preparedStatement.setLong(10, weiboPost.getForward());
            preparedStatement.setLong(11, weiboPost.getLike());
            return preparedStatement;
        });
    }
}
