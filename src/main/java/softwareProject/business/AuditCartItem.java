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
public class AuditCartItem {

    private int auditCartItemsID;

    private String table_name;

    private String transaction_name;

    private int movie_id;

    private LocalDateTime transdate;
}
