package com.smartcart.backend.service;

import com.smartcart.backend.dto.ChatRequest;
import com.smartcart.backend.dto.ChatResponse;
import com.smartcart.backend.model.Product;
import com.smartcart.backend.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {
    
    private final ProductRepository productRepository;
    
    // In-memory conversation storage (in production, use Redis or database)
    private final Map<String, List<String>> conversationHistory = new ConcurrentHashMap<>();
    
    // Common categories and their variations
    // IMPORTANT: Keys must match exact values stored in Product.category
    private final Map<String, List<String>> categoryKeywords = Map.of(
        "Electronics", Arrays.asList(
            "electronics", "electronic", "phone", "mobile", "smartphone", "laptop", "computer",
            "tablet", "headphone", "earphone", "earbuds", "speaker", "camera", "tv", "television",
            "mouse", "keyboard", "monitor", "wireless", "bluetooth", "headphones"
        ),
        "Clothing", Arrays.asList(
            "clothing", "clothes", "cloth", "cloths", "apparel", "shirt", "t-shirt", "tee", 
            "pants", "jeans", "dress", "shoes", "sneakers", "jacket", "hoodie", "sweater",
            "men's", "women's", "kids", "baby", "fashion", "wear", "outfit"
        ),
        "Books", Arrays.asList(
            "books", "book", "novel", "fiction", "non-fiction", "textbook", "magazine", "comic",
            "reading", "literature", "story", "stories"
        ),
        "Furniture", Arrays.asList(
            "furniture", "sofa", "couch", "chair", "table", "desk", "bed", "wardrobe", "cabinet",
            "wooden", "study", "dining", "office", "home", "decor"
        ),
        "Sports", Arrays.asList(
            "sports", "sport", "fitness", "gym", "running", "football", "basketball", "tennis",
            "yoga", "exercise", "racket", "bat", "ball", "badminton", "athletic", "training"
        ),
        "Beauty", Arrays.asList(
            "beauty", "cosmetics", "makeup", "skincare", "perfume", "shampoo", "soap", "cream",
            "care", "personal", "hygiene"
        ),
        "Food", Arrays.asList(
            "food", "foods", "grocery", "groceries", "snack", "snacks", "drink", "drinks",
            "beverage", "beverages", "fruit", "fruits", "vegetable", "vegetables", "meat", "bread",
            "cake", "biscuits", "cookies", "chips", "noodles", "rice", "eating", "meal", "meals"
        )
    );
    
    public ChatResponse processMessage(ChatRequest request) {
        String conversationId = request.getConversationId();
        String message = request.getMessage().toLowerCase();
        
        // Store conversation history
        conversationHistory.computeIfAbsent(conversationId, k -> new ArrayList<>()).add(request.getMessage());
        
        // Analyze message and generate response
        ChatResponse.ChatResponseBuilder responseBuilder = ChatResponse.builder()
            .conversationId(conversationId);
        
        // Extract price range from message
        PriceRange priceRange = extractPriceRange(message);
        
        // Extract category from message
        String category = extractCategory(message);
        
        // Debug logging
        log.info("Processing message: '{}' -> Category: '{}', PriceRange: {}", 
            request.getMessage(), category, priceRange != null ? 
            String.format("₹%.0f - ₹%.0f", priceRange.minPrice.doubleValue(), priceRange.maxPrice.doubleValue()) : "null");
        
        // Generate appropriate response based on message content
        if (category != null && priceRange != null) {
            // Category + Price range
            List<Product> products = productRepository.findByCategoryAndPriceRange(
                category, priceRange.minPrice, priceRange.maxPrice);
            log.info("Found {} products for category '{}' in price range ₹{} - ₹{}", 
                products.size(), category, priceRange.minPrice, priceRange.maxPrice);
            responseBuilder.reply(generateCategoryPriceResponse(category, priceRange, products.size()));
            responseBuilder.products(convertToChatProducts(products));
            
        } else if (category != null) {
            // Category only
            List<Product> products = productRepository.findByCategory(category);
            log.info("Found {} products for category '{}'", products.size(), category);
            responseBuilder.reply(generateCategoryResponse(category, products.size()));
            responseBuilder.products(convertToChatProducts(products));
            
        } else if (priceRange != null) {
            // Price range only
            List<Product> products = productRepository.findByPriceRange(priceRange.minPrice, priceRange.maxPrice);
            log.info("Found {} products in price range ₹{} - ₹{}", 
                products.size(), priceRange.minPrice, priceRange.maxPrice);
            responseBuilder.reply(generatePriceResponse(priceRange, products.size()));
            responseBuilder.products(convertToChatProducts(products));
            
        } else if (isGreeting(message)) {
            // Greeting
            responseBuilder.reply("Hello! I'm your SmartCart AI assistant. I can help you find products by category, price range, or specific features. What are you looking for today?");
            responseBuilder.products(Collections.emptyList());
            
        } else if (isHelpRequest(message)) {
            // Help request
            responseBuilder.reply("I can help you find products in various categories like Electronics, Clothing, Books, Furniture, Sports, Beauty, and Food. You can also specify price ranges like 'under 20000' or 'between 1000 and 5000'. What would you like to explore?");
            responseBuilder.products(Collections.emptyList());
            
        } else {
            // General search or fallback
            List<Product> products = productRepository.searchProducts(request.getMessage());
            log.info("General search for '{}' returned {} products", request.getMessage(), products.size());
            if (products.isEmpty()) {
                responseBuilder.reply("I couldn't find any products matching your request. Could you try being more specific? For example, you could ask for 'electronics under 20000' or 'clothing in the 1000-5000 range'.");
            } else {
                responseBuilder.reply("Here are some products that might interest you:");
            }
            responseBuilder.products(convertToChatProducts(products));
        }
        
        return responseBuilder.build();
    }
    
    private PriceRange extractPriceRange(String message) {
        // Pattern for "under X" or "below X" - more flexible
        Pattern underPattern = Pattern.compile("(?:under|below|less\\s+than|upto|up\\s+to)\\s*(?:₹|rs\\.?|rupees?)?\\s*(\\d+(?:,\\d{3})*(?:\\.\\d{2})?)", Pattern.CASE_INSENSITIVE);
        Matcher underMatcher = underPattern.matcher(message);
        if (underMatcher.find()) {
            double maxPrice = parsePrice(underMatcher.group(1));
            return new PriceRange(BigDecimal.ZERO, BigDecimal.valueOf(maxPrice));
        }
        
        // Pattern for "above X" or "over X" - more flexible
        Pattern abovePattern = Pattern.compile("(?:above|over|more\\s+than|greater\\s+than)\\s*(?:₹|rs\\.?|rupees?)?\\s*(\\d+(?:,\\d{3})*(?:\\.\\d{2})?)", Pattern.CASE_INSENSITIVE);
        Matcher aboveMatcher = abovePattern.matcher(message);
        if (aboveMatcher.find()) {
            double minPrice = parsePrice(aboveMatcher.group(1));
            return new PriceRange(BigDecimal.valueOf(minPrice), BigDecimal.valueOf(999999));
        }
        
        // Pattern for "between X and Y" or "X to Y" - more flexible
        Pattern betweenPattern = Pattern.compile("(?:between\\s+)?(?:₹|rs\\.?|rupees?)?\\s*(\\d+(?:,\\d{3})*(?:\\.\\d{2})?)\\s*(?:and|to|-)\\s*(?:₹|rs\\.?|rupees?)?\\s*(\\d+(?:,\\d{3})*(?:\\.\\d{2})?)", Pattern.CASE_INSENSITIVE);
        Matcher betweenMatcher = betweenPattern.matcher(message);
        if (betweenMatcher.find()) {
            double minPrice = parsePrice(betweenMatcher.group(1));
            double maxPrice = parsePrice(betweenMatcher.group(2));
            return new PriceRange(BigDecimal.valueOf(minPrice), BigDecimal.valueOf(maxPrice));
        }
        
        // Pattern for specific price like "20k", "5k", "1.5k"
        Pattern specificPattern = Pattern.compile("(\\d+(?:\\.\\d+)?)\\s*k", Pattern.CASE_INSENSITIVE);
        Matcher specificMatcher = specificPattern.matcher(message);
        if (specificMatcher.find()) {
            double price = Double.parseDouble(specificMatcher.group(1)) * 1000;
            return new PriceRange(BigDecimal.valueOf(price * 0.8), BigDecimal.valueOf(price * 1.2)); // ±20% range
        }
        
        // Pattern for standalone numbers that might be price limits
        Pattern standalonePattern = Pattern.compile("\\b(\\d+(?:,\\d{3})*(?:\\.\\d{2})?)\\b", Pattern.CASE_INSENSITIVE);
        Matcher standaloneMatcher = standalonePattern.matcher(message);
        if (standaloneMatcher.find()) {
            double price = parsePrice(standaloneMatcher.group(1));
            // If it's a reasonable price (not too high), treat as "under X"
            if (price < 100000) { // Reasonable upper limit
                return new PriceRange(BigDecimal.ZERO, BigDecimal.valueOf(price));
            }
        }
        
        return null;
    }
    
    private double parsePrice(String priceStr) {
        return Double.parseDouble(priceStr.replaceAll(",", ""));
    }
    
    private String extractCategory(String message) {
        // Convert message to lowercase for case-insensitive matching
        String lowerMessage = message.toLowerCase();
        
        for (Map.Entry<String, List<String>> entry : categoryKeywords.entrySet()) {
            String category = entry.getKey();
            List<String> keywords = entry.getValue();
            
            for (String keyword : keywords) {
                String lowerKeyword = keyword.toLowerCase();
                
                // Check for exact word match or phrase match
                if (lowerMessage.contains(lowerKeyword)) {
                    // Additional validation for single letters to avoid false matches
                    if (lowerKeyword.length() > 1 || lowerMessage.matches(".*\\b" + lowerKeyword + "\\b.*")) {
                        return category;
                    }
                }
            }
        }
        return null;
    }
    
    private boolean isGreeting(String message) {
        List<String> greetings = Arrays.asList("hello", "hi", "hey", "good morning", "good afternoon", "good evening", "greetings");
        return greetings.stream().anyMatch(message::contains);
    }
    
    private boolean isHelpRequest(String message) {
        List<String> helpKeywords = Arrays.asList("help", "what can you do", "how can you help", "assist", "support");
        return helpKeywords.stream().anyMatch(message::contains);
    }
    
    private String generateCategoryPriceResponse(String category, PriceRange priceRange, int productCount) {
        if (productCount == 0) {
            return String.format("I couldn't find any %s products in the price range ₹%.0f - ₹%.0f. Would you like to try a different price range or category?", 
                category, priceRange.minPrice.doubleValue(), priceRange.maxPrice.doubleValue());
        } else if (productCount == 1) {
            return String.format("I found 1 %s product in the price range ₹%.0f - ₹%.0f:", 
                category, priceRange.minPrice.doubleValue(), priceRange.maxPrice.doubleValue());
        } else {
            return String.format("I found %d %s products in the price range ₹%.0f - ₹%.0f:", 
                productCount, category, priceRange.minPrice.doubleValue(), priceRange.maxPrice.doubleValue());
        }
    }
    
    private String generateCategoryResponse(String category, int productCount) {
        if (productCount == 0) {
            return String.format("I couldn't find any %s products at the moment. Would you like to try a different category?", category);
        } else if (productCount == 1) {
            return String.format("I found 1 %s product:", category);
        } else {
            return String.format("I found %d %s products:", productCount, category);
        }
    }
    
    private String generatePriceResponse(PriceRange priceRange, int productCount) {
        if (productCount == 0) {
            return String.format("I couldn't find any products in the price range ₹%.0f - ₹%.0f. Would you like to try a different price range?", 
                priceRange.minPrice.doubleValue(), priceRange.maxPrice.doubleValue());
        } else if (productCount == 1) {
            return String.format("I found 1 product in the price range ₹%.0f - ₹%.0f:", 
                priceRange.minPrice.doubleValue(), priceRange.maxPrice.doubleValue());
        } else {
            return String.format("I found %d products in the price range ₹%.0f - ₹%.0f:", 
                productCount, priceRange.minPrice.doubleValue(), priceRange.maxPrice.doubleValue());
        }
    }
    
    private List<ChatResponse.ChatProduct> convertToChatProducts(List<Product> products) {
        return products.stream()
            .limit(6) // Limit to 6 products for better UX
            .map(product -> ChatResponse.ChatProduct.builder()
                .id(product.getId())
                .title(product.getTitle())
                .price(product.getPrice().doubleValue())
                .imageUrl(product.getImageUrl())
                .category(product.getCategory())
                .build())
            .collect(Collectors.toList());
    }
    
    // Helper class for price range
    private static class PriceRange {
        final BigDecimal minPrice;
        final BigDecimal maxPrice;
        
        PriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
            this.minPrice = minPrice;
            this.maxPrice = maxPrice;
        }
    }
}

