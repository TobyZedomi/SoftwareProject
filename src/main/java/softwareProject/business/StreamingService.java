package softwareProject.business;

import lombok.*;
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StreamingService {

    @EqualsAndHashCode.Include
    private int streamingServiceId;
    private String movieId;
    private String movieServiceName;
    private String movieServiceLink;
    private double cost;


}
