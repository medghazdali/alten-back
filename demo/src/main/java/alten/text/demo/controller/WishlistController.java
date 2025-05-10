package alten.text.demo.controller;

import alten.text.demo.model.Wishlist;
import alten.text.demo.model.User;
import alten.text.demo.service.WishlistService;
import alten.text.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<Wishlist> getWishlist() {
        User user = getCurrentUser();
        return ResponseEntity.ok(wishlistService.getOrCreateWishlist(user));
    }

    @PostMapping("/items/{productId}")
    public ResponseEntity<Wishlist> addToWishlist(@PathVariable Long productId) {
        User user = getCurrentUser();
        return ResponseEntity.ok(wishlistService.addToWishlist(user, productId));
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<Wishlist> removeFromWishlist(@PathVariable Long productId) {
        User user = getCurrentUser();
        return ResponseEntity.ok(wishlistService.removeFromWishlist(user, productId));
    }

    @DeleteMapping
    public ResponseEntity<Void> clearWishlist() {
        User user = getCurrentUser();
        wishlistService.clearWishlist(user);
        return ResponseEntity.ok().build();
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
} 