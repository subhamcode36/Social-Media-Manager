package com.socialmedia.util;

import com.socialmedia.model.Post;
import com.socialmedia.model.Profile;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class DataProcessor {
    private String filePath;

    public DataProcessor(String filePath) {
        this.filePath = filePath;
    }

    public Map<String, Profile> readCSV() throws InvalidDataException, IOException {
        Map<String, Profile> profiles = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line = reader.readLine(); // header
            while ((line = reader.readLine()) != null) {
                try {
                    String[] data = line.split(",");
                    if (data.length < 9) {
                        throw new InvalidDataException("Invalid data format in CSV file: " + line);
                    }
                    String profileName = data[0];
                    String postId = data[1];
                    LocalDate postDate = LocalDate.parse(data[2]);
                    LocalTime postTime = LocalTime.parse(data[3]);
                    Integer likes = Integer.parseInt(data[4]);
                    Integer shares = Integer.parseInt(data[5]);
                    Integer followers = Integer.parseInt(data[6]);
                    Integer comments = Integer.parseInt(data[7]);
                    Post.ContentType contentType = Post.ContentType.valueOf(data[8]);
                    Profile profile = profiles.computeIfAbsent(profileName, Profile::new);
                    LocalDateTime postDateTime = LocalDateTime.of(postDate, postTime);
                    Post post = new Post(postId, profileName, postDateTime, likes, shares, followers, comments, contentType);
                    if (data.length > 9) {
                        post.addCommentTexts(data[9]);
                    }
                    profile.addPost(post);
                } catch (Exception e) {
                    throw new InvalidDataException("Error processing line: " + line + " - " + e.getMessage());
                }
            }
        } catch (FileNotFoundException e) {
            throw new InvalidDataException("File not found: " + filePath);
        }
        return profiles;
    }

    public void writeAnalyticsToCSV(Map<String, Profile> profiles, String outputPath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
            writer.write("profile_name,optimal_posting_time,photo_growth,video_growth,reel_growth,text_growth\n");
            for (Map.Entry<String, Profile> entry : profiles.entrySet()) {
                String profileName = entry.getKey();
                Profile profile = entry.getValue();
                java.time.LocalTime optimalTime = profile.calculateOptimalPostingTime();
                Map<Post.ContentType, Double> growthByType = profile.analyzeFollowerGrowthByContentType();
                double photoGrowth = growthByType.getOrDefault(Post.ContentType.PHOTO, 0.0);
                double videoGrowth = growthByType.getOrDefault(Post.ContentType.VIDEO, 0.0);
                double reelGrowth = growthByType.getOrDefault(Post.ContentType.REEL, 0.0);
                double textGrowth = growthByType.getOrDefault(Post.ContentType.TEXT, 0.0);
                writer.write(String.format("%s,%s,%.2f,%.2f,%.2f,%.2f\n",
                        profileName, optimalTime, photoGrowth, videoGrowth, reelGrowth, textGrowth));
            }
        }
    }
}
