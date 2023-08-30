package academy.tochkavhoda.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductReview {
    private int id;
    @NotBlank
    private String Ean;
    @NotBlank
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must not exceed 5")
    private int rating;
    private boolean willRecommend;
    private boolean brandLoyalty;
    @NotBlank
    private User user;
}
