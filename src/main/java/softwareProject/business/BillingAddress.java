package softwareProject.business;
import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BillingAddress {

    private int billing_address_id;

    private String username;

    private String fullName;

    private String email;

    private String address;
    private String city;

    private String county;

    private String postcode;
}
