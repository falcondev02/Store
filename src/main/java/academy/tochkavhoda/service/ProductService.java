package academy.tochkavhoda.service;

import academy.tochkavhoda.dao.ProductDao;
import academy.tochkavhoda.exception.ProductNotFoundException;
import academy.tochkavhoda.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductDao productDao;

    @Autowired
    public ProductService(ProductDao productDao) {
        this.productDao = productDao;
    }

    public boolean checkUniqueEan(String ean) {
        return productDao.existsById(ean);
    }

}
