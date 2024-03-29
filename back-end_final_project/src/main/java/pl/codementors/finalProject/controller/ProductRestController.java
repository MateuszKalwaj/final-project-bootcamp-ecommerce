package pl.codementors.finalProject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.codementors.finalProject.models.Product;
import pl.codementors.finalProject.services.ProductService;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "/products")
public class ProductRestController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public List<Product> getProducts() {
        return productService.findAll();
    }

    @GetMapping("/json")
    public List<Product> getProductsForJson() {
        return productService.findAllForJsonView();
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productService.findOne(id);
    }

    @PostMapping("/add/{id}")
    public Product addProduct(@PathVariable Long id,
                              @RequestBody Product userProduct) {
        return productService.addProduct(userProduct, id);
    }

    @PutMapping("/edit/{id}")
    public Product editProduct(@PathVariable Long id,
                               @RequestBody Product product) {
        return productService.editProduct(id, product);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }

    @PutMapping("/{productId}/addToUser/{userId}")
    public Product addProductToUser(@PathVariable("productId") Long productId,
                                    @PathVariable("userId") Long userId){
        return productService.addProductToUser(productId, userId);
    }
}