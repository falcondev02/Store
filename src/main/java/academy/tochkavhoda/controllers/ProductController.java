package academy.tochkavhoda.controllers;

import academy.tochkavhoda.dao.ProductDao;
import academy.tochkavhoda.dao.ProductReviewDao;
import academy.tochkavhoda.exception.ProductNotFoundException;
import academy.tochkavhoda.models.Product;
import academy.tochkavhoda.models.ProductReview;
import academy.tochkavhoda.service.KpiCalculator;
import academy.tochkavhoda.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductDao productDao;
    private final ProductReviewDao productReviewDao;
    private final KpiCalculator kpiCalculator;
    private final ProductService productService;

    @Autowired
    public ProductController(ProductDao productDao, ProductReviewDao productReviewDao, KpiCalculator kpiCalculator, ProductService productService) {
        this.productDao = productDao;
        this.productReviewDao = productReviewDao;
        this.kpiCalculator = kpiCalculator;
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        boolean isUniqueEan = productService.checkUniqueEan(product.getEan());

        if (!isUniqueEan) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        Product createdProduct = productDao.insert(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }


    @PostMapping("/{ean}/reviews")
    public ResponseEntity<ProductReview> createReview(@PathVariable String ean, @RequestBody ProductReview review) {
        if (!productDao.existsById(ean)) {
            return ResponseEntity.notFound().build();
        }

        review.setEan(ean);
        ProductReview createdReview = productReviewDao.insert(review);
        return new ResponseEntity<>(createdReview, HttpStatus.CREATED);
    }

    @PutMapping("/{ean}")
    public ResponseEntity<Product> updateProduct(@PathVariable String ean, @Valid @RequestBody Product product) {
        try {
            productService.checkUniqueEan(ean); // Проверка уникальности EAN
        } catch (ProductNotFoundException ex) {
            return ResponseEntity.notFound().build(); // Если продукт с таким EAN не существует, возвращаем 404
        }

        Product existingProduct = productDao.findById(ean);

        if (existingProduct == null) {
            return ResponseEntity.notFound().build();
        }

        existingProduct.setPrice(product.getPrice());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setCountry(product.getCountry());
        existingProduct.setManufacturerAddress(product.getManufacturerAddress());

        Product updatedProduct = productDao.update(existingProduct);

        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{ean}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String ean) {
        if (productDao.existsById(ean)) {
            productDao.deleteById(ean);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{ean}/metrics/kpi")
    public ResponseEntity<?> calculateKpiMetrics(@PathVariable String ean) {
        if (!productDao.existsById(ean)) {
            return ResponseEntity.notFound().build();
        }

        kpiCalculator.calculateKpiMetricsForProduct(ean);
        return ResponseEntity.status(HttpStatus.CREATED).body(Collections.singletonMap("status", "creating"));
    }

    @GetMapping("/{ean}/metrics/kpi")
    public ResponseEntity<Map<String, Object>> getKpiMetrics(@PathVariable String ean) {
        if (!productDao.existsById(ean)) {
            return ResponseEntity.notFound().build();
        }

        Map<String, Object> metrics = kpiCalculator.getKpiMetricsForProduct(ean); // Получение KPI метрик для продукта с заданным EAN
        return ResponseEntity.ok(metrics);
    }
}
