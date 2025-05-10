package alten.text.demo.service;

import alten.text.demo.model.Wishlist;
import alten.text.demo.model.Product;
import alten.text.demo.model.User;
import alten.text.demo.repository.WishlistRepository;
import alten.text.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WishlistService {

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public Wishlist getOrCreateWishlist(User user) {
        return wishlistRepository.findByUser(user)
                .orElseGet(() -> {
                    Wishlist wishlist = new Wishlist();
                    wishlist.setUser(user);
                    return wishlistRepository.save(wishlist);
                });
    }

    @Transactional
    public Wishlist addToWishlist(User user, Long productId) {
        Wishlist wishlist = getOrCreateWishlist(user);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        if (!wishlist.getProducts().contains(product)) {
            wishlist.getProducts().add(product);
            return wishlistRepository.save(wishlist);
        }
        return wishlist;
    }

    @Transactional
    public Wishlist removeFromWishlist(User user, Long productId) {
        Wishlist wishlist = getOrCreateWishlist(user);
        wishlist.getProducts().removeIf(product -> product.getId().equals(productId));
        return wishlistRepository.save(wishlist);
    }

    @Transactional
    public void clearWishlist(User user) {
        Wishlist wishlist = getOrCreateWishlist(user);
        wishlist.getProducts().clear();
        wishlistRepository.save(wishlist);
    }
} 