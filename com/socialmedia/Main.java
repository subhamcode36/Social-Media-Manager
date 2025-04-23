package com.socialmedia;

import com.socialmedia.analytics.AnalyticsProcessor;
import com.socialmedia.dashboard.Dashboard;
import com.socialmedia.model.Profile;
import com.socialmedia.util.DataProcessor;
import com.socialmedia.util.InvalidDataException;
import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        try {
            // Process input data
            System.out.println("Reading data from CSV file...");
            DataProcessor processor = new DataProcessor("social_media_data.csv");
            Map<String, Profile> profiles = processor.readCSV();
            
            System.out.println("Loaded " + profiles.size() + " profiles.");
            
            // Process analytics in parallel for each profile
            System.out.println("Processing analytics...");
            List<AnalyticsProcessor> processors = new ArrayList<>();
            List<Thread> threads = new ArrayList<>();
            Map<String, Map<String, Object>> allResults = new HashMap<>();
            
            for (Profile profile : profiles.values()) {
                AnalyticsProcessor analyticsProcessor = new AnalyticsProcessor(profile);
                processors.add(analyticsProcessor);
                Thread thread = new Thread(analyticsProcessor);
                threads.add(thread);
                thread.start();
            }
            
            // Wait for all threads to complete
            for (int i = 0; i < threads.size(); i++) {
                threads.get(i).join();
                String profileName = processors.get(i).getProfileName();
                allResults.put(profileName, processors.get(i).getResults());
            }
            
            System.out.println("Analytics processing complete.");
            
            // Write results to CSV
            processor.writeAnalyticsToCSV(profiles, "analytics_results.csv");
            System.out.println("\nResults have been written to analytics_results.csv");
            
            // Interactive user input
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("\nEnter profile number (1-50) to analyze or 'exit' to quit:");
                String input = scanner.nextLine().trim();
                
                if (input.equalsIgnoreCase("exit")) {
                    break;
                }
                
                try {
                    int profileNum = Integer.parseInt(input);
                    if (profileNum < 1 || profileNum > 50) {
                        System.out.println("Please enter a number between 1-50");
                        continue;
                    }
                    
                    String targetProfile = "profile" + profileNum;
                    if (allResults.containsKey(targetProfile)) {
                        // Display analytics
                        Dashboard.displayProfileAnalytics(targetProfile, allResults.get(targetProfile));
                        
                        // NEW: Display recommendations
                        List<String> hashtags = processors.stream()
                                .filter(p -> p.getProfileName().equals(targetProfile))
                                .findFirst()
                                .get()
                                .getRecommendedHashtags();
                                
                       
                                
                        Dashboard.displayRecommendations(targetProfile, hashtags, null);
                    } else {
                        System.out.println("Profile not found: " + targetProfile);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number or 'exit'");
                }
            }
            scanner.close();

        } catch (InvalidDataException e) {
            System.err.println("Error: Invalid data in CSV file. " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error: IO exception. " + e.getMessage());
        } catch (InterruptedException e) {
            System.err.println("Error: Thread interrupted. " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
