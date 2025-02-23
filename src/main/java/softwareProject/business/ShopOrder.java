package softwareProject.business;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShopOrder {

    private int order_id;

    private String username;

    private int billing_address_id;

    private LocalDateTime order_date;

    private double total_price;

    private String order_status;
}
