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
public class AuditPurchasedItems {
    private int auditPurchasedItemsID;

    private String movie_name;

    private String username;

    private int order_id;

    private double price;

    private LocalDateTime created_at;
}
