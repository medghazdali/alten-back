package alten.text.demo.controller;

import alten.text.demo.model.Cart;
import alten.text.demo.model.User;
import alten.text.demo.service.CartService;
import alten.text.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<Cart> getCart() {
        User user = getCurrentUser();
        return ResponseEntity.ok(cartService.getOrCreateCart(user));
    }

    @PostMapping("/items")
    public ResponseEntity<Cart> addToCart(
            @RequestParam Long productId,
            @RequestParam(defaultValue = "1") Integer quantity) {
        User user = getCurrentUser();
        return ResponseEntity.ok(cartService.addToCart(user, productId, quantity));
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<Cart> removeFromCart(@PathVariable Long productId) {
        User user = getCurrentUser();
        return ResponseEntity.ok(cartService.removeFromCart(user, productId));
    }

    @PatchMapping("/items/{productId}")
    public ResponseEntity<Cart> updateQuantity(
            @PathVariable Long productId,
            @RequestParam Integer quantity) {
        User user = getCurrentUser();
        return ResponseEntity.ok(cartService.updateQuantity(user, productId, quantity));
    }

    @DeleteMapping
    public ResponseEntity<Void> clearCart() {
        User user = getCurrentUser();
        cartService.clearCart(user);
        return ResponseEntity.ok().build();
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
} 