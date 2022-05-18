package me.qcy.weibo;

import java.util.Date;

/**
 * @author imqia
 * @date 2022-05-10 19:46
 */
public class WeiboPost {
    /** id **/
    private String id;

    /** 发布时间 **/
    private Date postDate;

    /** 内容 **/
    private String content;

    /** 转发数 **/
    private Long forward;

    /** 点赞数量 **/
    private Long like;

    /** 评论数 **/
    private Long comment;

    /** 图片url **/
    private String pictureUrl;

    /** 是否为转发 **/
    private Boolean isForward;

    private String userId;

    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean isForward() {
        return isForward;
    }

    public void setIsForward(Boolean isForward) {
        this.isForward = isForward;
    }

    public Date getPostDate() {
        return postDate;
    }

    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getForward() {
        return forward;
    }

    public void setForward(Boolean forward) {
        isForward = forward;
    }

    public void setForward(Long forward) {
        this.forward = forward;
    }

    public Long getLike() {
        return like;
    }

    public void setLike(Long like) {
        this.like = like;
    }

    public Long getComment() {
        return comment;
    }

    public void setComment(Long comment) {
        this.comment = comment;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    @Override
    public String toString() {
        return "WeiboPost{" +
                "postDate=" + postDate +
                ", content='" + content + '\'' +
                ", forward=" + forward +
                ", like=" + like +
                ", comment=" + comment +
                ", pictureUrl='" + pictureUrl + '\'' +
                ", isForward=" + isForward +
                ", userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
