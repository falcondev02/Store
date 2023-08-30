package academy.tochkavhoda.validator;

import academy.tochkavhoda.dao.ProductDao;
import academy.tochkavhoda.exception.ProductNotFoundException;
import academy.tochkavhoda.models.Product;
import academy.tochkavhoda.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueEanValidator implements ConstraintValidator<UniqueEan, String> {
    private final ProductService productService;

    public UniqueEanValidator(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public void initialize(UniqueEan constraintAnnotation) {
        // Можно выполнить инициализацию, если это необходимо
    }

    @Override
    public boolean isValid(String ean, ConstraintValidatorContext context) {
        if (ean == null || !ean.matches("[0-9]{1,15}")) {
            return false;
        }
        return productService.checkUniqueEan(ean);
    }
}