package alten.text.demo.service;

import alten.text.demo.model.Product;
import alten.text.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Product createProduct(Product product) {
        if (productRepository.existsByCode(product.getCode())) {
            throw new IllegalArgumentException("Product with code " + product.getCode() + " already exists");
        }
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, Product productDetails) {
        return productRepository.findById(id)
                .map(existingProduct -> {
                    existingProduct.setName(productDetails.getName());
                    existingProduct.setDescription(productDetails.getDescription());
                    existingProduct.setImage(productDetails.getImage());
                    existingProduct.setCategory(productDetails.getCategory());
                    existingProduct.setPrice(productDetails.getPrice());
                    existingProduct.setQuantity(productDetails.getQuantity());
                    existingProduct.setInternalReference(productDetails.getInternalReference());
                    existingProduct.setShellId(productDetails.getShellId());
                    existingProduct.setInventoryStatus(productDetails.getInventoryStatus());
                    existingProduct.setRating(productDetails.getRating());
                    return productRepository.save(existingProduct);
                })
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + id));
    }

    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new IllegalArgumentException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }
} 