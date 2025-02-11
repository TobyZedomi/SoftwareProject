package softwareProject.business;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubscriptionPlan {
    @EqualsAndHashCode.Include
    private int subscription_plan_id;

    private String  subscription_plan_name;

    private double cost;
}
