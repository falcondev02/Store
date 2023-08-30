package academy.tochkavhoda.dao;

import academy.tochkavhoda.models.Product;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ProductDao implements Dao<Product, String> {

    private Map<String, Product> storage = new ConcurrentHashMap<>();

    @Override
    public Product findById(String id) {
        return storage.get(id);
    }

    @Override
    public List<Product> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public Product insert(Product product) {
        storage.put(product.getEan(), product);
        return product;
    }


    public Product update(Product product) {
        String ean = product.getEan();
        if (storage.containsKey(ean)) {
            storage.put(ean, product);
            return product;
        } else {
            throw new IllegalArgumentException("Product with EAN " + ean + " does not exist.");
        }
    }

    public boolean existsById(String id) {
        return storage.containsKey(id);
    }

    public void deleteById(String id) {
        storage.remove(id);
    }


}
