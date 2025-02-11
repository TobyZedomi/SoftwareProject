package softwareProject.business;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Subscription {


    @EqualsAndHashCode.Include
    private String username;
    @EqualsAndHashCode.Exclude
    private int  subscription_plan_id;
    private LocalDateTime subscription_startDate;
    private LocalDateTime subscription_endDate;

}
