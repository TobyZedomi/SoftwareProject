package softwareProject.business;

import lombok.*;
import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class GiftedMovie {
    private int giftId;
    private String senderUsername;
    private String recipientUsername;
    private int movieId;
    private Timestamp giftedAt;
}