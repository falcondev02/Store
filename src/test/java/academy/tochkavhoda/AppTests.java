package academy.tochkavhoda;

import academy.tochkavhoda.dao.ProductDao;
import academy.tochkavhoda.dao.ProductReviewDao;
import academy.tochkavhoda.dao.UserDao;
import academy.tochkavhoda.models.Product;
import academy.tochkavhoda.models.ProductReview;
import academy.tochkavhoda.models.User;
import academy.tochkavhoda.service.KpiCalculator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
@ActiveProfiles("test")
class AppTests {

    @Autowired
    private ProductDao productDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private ProductReviewDao productReviewDao;

    @Autowired
    private KpiCalculator kpiCalculator;

    @Test
    void productDaoTest() {
        List<Product> products = productDao.findAll();
        assertNotNull(products);
        assertFalse(products.isEmpty());

        Product product = products.get(0);
        assertEquals(product, productDao.findById(product.getEan()));
    }

    @Test
    void userDaoTest() {
        List<User> users = userDao.findAll();
        assertNotNull(users);
        assertFalse(users.isEmpty());

        User user = users.get(0);
        assertEquals(user, userDao.findById(user.getId()));
    }

    @Test
    void productReviewDaoTest() {
        List<ProductReview> reviewList = productReviewDao.findAll();
        assertNotNull(reviewList);
        assertFalse(reviewList.isEmpty());

        ProductReview review = reviewList.get(0);
        assertEquals(review, productReviewDao.findById(review.getId()));
    }

    @Test
    void kpiCalculatorTest() {
        assertNotNull(kpiCalculator);
        kpiCalculator.run();
    }

}