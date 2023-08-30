package academy.tochkavhoda.dao;

import academy.tochkavhoda.models.Product;
import academy.tochkavhoda.models.ProductReview;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ProductReviewDao implements Dao<ProductReview, Integer> {
    private Map<Integer, ProductReview> storage = new ConcurrentHashMap<>();

    @Override
    public ProductReview findById(Integer id) {
        return storage.get(id);
    }

    @Override
    public List<ProductReview> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public ProductReview insert(ProductReview obj) {
        storage.put(obj.getId(), obj);
        return obj;
    }

}
