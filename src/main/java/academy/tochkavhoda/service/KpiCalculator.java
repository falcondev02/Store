package academy.tochkavhoda.service;

import academy.tochkavhoda.dao.ProductDao;
import academy.tochkavhoda.dao.ProductReviewDao;
import academy.tochkavhoda.models.Product;
import academy.tochkavhoda.models.ProductReview;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class KpiCalculator implements CommandLineRunner {
    private final ProductReviewDao productReviewDao;
    private final ProductDao productDao;

    @Autowired
    public KpiCalculator(ProductReviewDao productReviewDao, ProductDao productDao) {
        this.productReviewDao = productReviewDao;
        this.productDao = productDao;
    }

    @Override
    public void run(String... args) {
        calculateAverageRating();
        calculateSatisfactionIndex();
        calculateAttractivenessIndex();
    }

    public void calculateKpiMetricsForProduct(String ean) {
        double avgRating = calculateAverageRatingById(ean);
        int satisfaction = calculateSatisfactionIndexById(ean);
        int attraction = calculateAttractivenessIndexById(ean);

        System.out.println("Расчет KPI метрик для продукта с EAN: " + ean);
        System.out.println("Средний рейтинг: " + avgRating);
        System.out.println("Индекс удовлетворенности: " + satisfaction);
        System.out.println("Индекс привлекательности: " + attraction);
    }

    public Map<String, Object> getKpiMetricsForProduct(String ean) {
        Map<String, Object> metrics = new HashMap<>();
        double averageRating = calculateAverageRatingById(ean);
        int satisfactionIndex = calculateSatisfactionIndexById(ean);
        int attractivenessIndex = calculateAttractivenessIndexById(ean);

        metrics.put("avgRating", averageRating);
        metrics.put("satisfaction", satisfactionIndex);
        metrics.put("attraction", attractivenessIndex);

        return metrics;
    }

    public double calculateAverageRatingById(String ean) {
        Product product = productDao.findById(ean);
        List<ProductReview> allReviews = productReviewDao.findAll();

        List<ProductReview> reviews = allReviews.stream()
                .filter(review -> review.getEan().equals(product.getEan())).toList();
        int sum = 0;
        for (ProductReview review : reviews) {
            sum += review.getRating();
        }
        double average = reviews.isEmpty() ? 0 : (double) sum / reviews.size();
        return average;
    }

    private void calculateAverageRating() {
        Map<String, Double> averageRatings = new HashMap<>();
        List<Product> productList = productDao.findAll();
        List<ProductReview> allReviews = productReviewDao.findAll();

        for (Product product : productList) {
            List<ProductReview> reviews = allReviews.stream()
                    .filter(review -> review.getEan().equals(product.getEan())).toList();
            int sum = 0;
            for (ProductReview review : reviews) {
                sum += review.getRating();
            }
            double average = reviews.isEmpty() ? 0 : (double) sum / reviews.size();
            averageRatings.put(product.getEan(), average);
        }
        System.out.println("Средний рейтинг для продуктов: " + averageRatings);
    }

    public int calculateSatisfactionIndexById(String ean) {
        Product product = productDao.findById(ean);
        List<ProductReview> allReviews = productReviewDao.findAll();

        List<ProductReview> reviews = allReviews.stream()
                .filter(review -> review.getEan().equals(product.getEan())).toList();
        double sum = 0;
        for (ProductReview review : reviews) {
            sum += review.isBrandLoyalty() ? review.getRating() * 0.8 : review.getRating() * 0.2;
        }
        double result = reviews.isEmpty() ? 0 : (sum / reviews.size()) * 100;
        return (int) result;
    }

    private void calculateSatisfactionIndex() {
        Map<String, Integer> satisfactionIndex = new HashMap<>();
        List<Product> productList = productDao.findAll();
        List<ProductReview> allReviews = productReviewDao.findAll();

        for (Product product : productList) {
            List<ProductReview> reviews = allReviews.stream()
                    .filter(review -> review.getEan().equals(product.getEan())).toList();
            double sum = 0;
            for (ProductReview review : reviews) {
                sum += review.isBrandLoyalty() ? review.getRating() * 0.8 : review.getRating() * 0.2;
            }
            double result = reviews.isEmpty() ? 0 : (sum / reviews.size()) * 100;
            satisfactionIndex.put(product.getEan(), (int) Math.round(result));
        }
        System.out.println("Индекс удовлетворенности продукта: " + satisfactionIndex);
    }

    public int calculateAttractivenessIndexById(String ean) {
        Product product = productDao.findById(ean);
        List<ProductReview> allReviews = productReviewDao.findAll();

        List<ProductReview> reviews = allReviews.stream()
                .filter(review -> review.getEan().equals(product.getEan())).toList();

        int sum = 0;
        for (ProductReview review : reviews) {
            sum += review.isWillRecommend() ? 1 : 0;
        }
        double average = reviews.isEmpty() ? 0 : (double) sum / reviews.size();
        return (int) Math.round(average * 100);
    }

    private void calculateAttractivenessIndex() {
        Map<String, Integer> attractivenessIndex = new HashMap<>();
        List<Product> productList = productDao.findAll();
        List<ProductReview> allReviews = productReviewDao.findAll();

        for (Product product : productList) {
            List<ProductReview> reviews = allReviews.stream()
                    .filter(review -> review.getEan().equals(product.getEan())).toList();

            int sum = 0;
            for (ProductReview review : reviews) {
                sum += review.isWillRecommend() ? 1 : 0;
            }
            double average = reviews.isEmpty() ? 0 : (double) sum / reviews.size();
            attractivenessIndex.put(product.getEan(), (int) Math.round(average * 100));
        }
        System.out.println("Индекс привлекательности продукта: " + attractivenessIndex);
    }

}
