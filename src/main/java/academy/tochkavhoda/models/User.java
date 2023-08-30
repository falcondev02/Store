package academy.tochkavhoda.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {
    private int id;
    private String birthday;
    private String sex;
    private String cardNumber;
    private boolean hasKids;
    private String married;
    private String education;
    private String residencePlace;
}
