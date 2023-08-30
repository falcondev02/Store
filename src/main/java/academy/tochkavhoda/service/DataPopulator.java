package academy.tochkavhoda.service;

import academy.tochkavhoda.dao.ProductDao;
import academy.tochkavhoda.dao.ProductReviewDao;
import academy.tochkavhoda.dao.UserDao;
import academy.tochkavhoda.models.Product;
import academy.tochkavhoda.models.ProductReview;
import academy.tochkavhoda.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataPopulator implements CommandLineRunner {
    private final ProductService productService;
    private final ProductDao productDao;
    private final UserDao userDao;
    private final ProductReviewDao productReviewDao;

    @Autowired
    public DataPopulator(ProductService productService, ProductDao productDao, UserDao userDao, ProductReviewDao productReviewDao) {
        this.productService = productService;
        this.productDao = productDao;
        this.userDao = userDao;
        this.productReviewDao = productReviewDao;
    }


    @Override
    public void run(String... args) {
        createTestDataForUsers();
        createTestDataForProducts();
        createTestDataForProductReviews();

    }

    private void createTestDataForProducts() {
        Product product1 = new Product("1001001001", 3.5, "Product 1 description", "USA", "Company 1", "123 Main St");
        Product product2 = new Product("2002002002", 2.9, "Product 2 description", "Germany", "Company 2", "456 Second Ave");
        Product product3 = new Product("3003003003", 4.8, "Product 3 description", "Japan", "Company 3", "789 Third Blvd");

        try {
            productService.checkUniqueEan(product1.getEan());
            productDao.insert(product1);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        try {
            productService.checkUniqueEan(product2.getEan());
            productDao.insert(product2);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        try {
            productService.checkUniqueEan(product3.getEan());
            productDao.insert(product3);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void createTestDataForUsers() {
        User user1 = new User(1, "1/10/1988", "M", "1001", true, "Married", "College", "City 1");
        User user2 = new User(2, "15/12/1985", "F", "1002", false, "Single", "High School", "City 2");
        User user3 = new User(3, "7/11/2000", "M", "1003", true, "Married", "University", "City 3");

        userDao.insert(user1);
        userDao.insert(user2);
        userDao.insert(user3);
    }

    private void createTestDataForProductReviews() {
        ProductReview review1 = new ProductReview(1, "1001001001", 5, true, true, userDao.findById(1));
        ProductReview review2 = new ProductReview(2, "1001001001", 3, false, true, userDao.findById(2));
        ProductReview review3 = new ProductReview(3, "2002002002", 2, false, false, userDao.findById(1));
        ProductReview review4 = new ProductReview(4, "3003003003", 5, true, true, userDao.findById(2));
        ProductReview review5 = new ProductReview(5, "3003003003", 5, true, false, userDao.findById(3));

        productReviewDao.insert(review1);
        productReviewDao.insert(review2);
        productReviewDao.insert(review3);
        productReviewDao.insert(review4);
        productReviewDao.insert(review5);
    }
}
