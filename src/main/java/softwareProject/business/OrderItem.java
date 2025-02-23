package softwareProject.business;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItem {

    private int order_items_id;

    private double price;

    private int order_id;

    private int movie_id;

}
