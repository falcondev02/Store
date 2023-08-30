package academy.tochkavhoda.models;

import academy.tochkavhoda.validator.UniqueEan;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @NotBlank
    private String ean;
    @NotNull
    @Positive
    private double price;
    private String description;
    @NotBlank
    private String country;
    @NotBlank
    private String company;
    private String manufacturerAddress;

}
