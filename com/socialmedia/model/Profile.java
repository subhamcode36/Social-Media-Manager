package com.socialmedia.model;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class Profile {
    private String name;
    private List<Post> posts;
    private Map<LocalDateTime, Integer> followerGrowth;

    public Profile(String name) {
        this.name = name;
        this.posts = new ArrayList<>();
        this.followerGrowth = new TreeMap<>();
    }

    public void addPost(Post post) {
        posts.add(post);
        followerGrowth.put(post.getPostDateTime(), post.getFollowers());
    }

    public LocalTime calculateOptimalPostingTime() {
        return calculateOptimalPostingTime(posts);
    }

    public LocalTime calculateOptimalPostingTime(Post.ContentType contentType) {
        List<Post> filteredPosts = posts.stream()
                .filter(p -> p.getContentType() == contentType)
                .collect(Collectors.toList());
        return calculateOptimalPostingTime(filteredPosts);
    }

    private LocalTime calculateOptimalPostingTime(List<Post> postList) {
        Map<LocalTime, Double> engagementByHour = new HashMap<>();
        for (Post post : postList) {
            LocalTime hour = post.getPostDateTime().toLocalTime().truncatedTo(ChronoUnit.HOURS);
            double engagement = calculateEngagement(post);
            engagementByHour.merge(hour, engagement, Double::sum);
        }
        return engagementByHour.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(LocalTime.NOON);
    }

    private double calculateEngagement(Post post) {
        if (post.getFollowers() == 0) return 0;
        return (post.getLikes() + post.getShares() + post.getComments()) / (double) post.getFollowers();
    }

    public Map<Post.ContentType, Double> analyzeFollowerGrowthByContentType() {
        Map<Post.ContentType, Double> growthByType = new HashMap<>();
        Map<Post.ContentType, List<Post>> postsByType = posts.stream()
                .collect(Collectors.groupingBy(Post::getContentType));

        for (Map.Entry<Post.ContentType, List<Post>> entry : postsByType.entrySet()) {
            Post.ContentType type = entry.getKey();
            List<Post> typePosts = entry.getValue();

            if (typePosts.size() < 2) continue;

            typePosts.sort(Comparator.comparing(Post::getPostDateTime));

            double totalGrowth = 0;
            int growthPoints = 0;

            for (int i = 0; i < typePosts.size() - 1; i++) {
                Post current = typePosts.get(i);
                Post next = typePosts.get(i + 1);

                int growth = next.getFollowers() - current.getFollowers();
                if (growth > 0) {
                    totalGrowth += growth;
                    growthPoints++;
                }
            }

            double averageGrowth = growthPoints > 0 ? totalGrowth / growthPoints : 0;
            growthByType.put(type, averageGrowth);
        }
        return growthByType;
    }

    public String getName() { return name; }
    public List<Post> getPosts() { return posts; }
}
