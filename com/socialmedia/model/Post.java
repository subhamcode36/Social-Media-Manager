package com.socialmedia.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Post {
    private String postId;
    private String profileName;
    private LocalDateTime postDateTime;
    private Integer likes;
    private Integer shares;
    private Integer followers;
    private Integer comments;
    private ContentType contentType;
    private List<String> commentTexts;

    public Post(String postId, String profileName, LocalDateTime postDateTime, 
                Integer likes, Integer shares, Integer followers, Integer comments, 
                ContentType contentType) {
        this.postId = postId;
        this.profileName = profileName;
        this.postDateTime = postDateTime;
        this.likes = likes;
        this.shares = shares;
        this.followers = followers;
        this.comments = comments;
        this.contentType = contentType;
        this.commentTexts = new ArrayList<>();
    }

    public Post(String postId, String profileName, LocalDateTime postDateTime, ContentType contentType) {
        this(postId, profileName, postDateTime, 0, 0, 0, 0, contentType);
    }

    public void addCommentTexts(String... comments) {
        for (String comment : comments) {
            commentTexts.add(comment);
        }
    }

    public String getPostId() { return postId; }
    public String getProfileName() { return profileName; }
    public LocalDateTime getPostDateTime() { return postDateTime; }
    public Integer getLikes() { return likes; }
    public Integer getShares() { return shares; }
    public Integer getFollowers() { return followers; }
    public Integer getComments() { return comments; }
    public ContentType getContentType() { return contentType; }
    public List<String> getCommentTexts() { return commentTexts; }

    public static enum ContentType {
        PHOTO, VIDEO, REEL, TEXT
    }
}
