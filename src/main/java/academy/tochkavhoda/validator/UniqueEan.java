package academy.tochkavhoda.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueEanValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueEan {

    String message() default "EAN must be unique";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}