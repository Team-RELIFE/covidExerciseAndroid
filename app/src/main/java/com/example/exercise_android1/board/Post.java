package com.example.exercise_android1.board;

import java.util.ArrayList;

public class Post {
    //declare private data instead of public to ensure the privacy of data field of each class
    private String id;
    private String title;
    private String content;
    private String writer;

    public Post(String postId, String writer, String title, String content) {
        this.id = postId;
        this.title = title;
        this.writer = writer;
        this.content = content;
    }

    public String getId() { return id; }

    public String getTitle() {
        return title;
    }

    public String getWriter() {
        return writer;
    }

    public String getContent() {
        return content;
    }

    public static ArrayList<Post> getPosts() {
        GetPostsActivity getPostsActivity = new GetPostsActivity();
        return getPostsActivity.posts;
    }
}
