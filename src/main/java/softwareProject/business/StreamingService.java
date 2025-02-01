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
    private int streaming_service_id;
    private int movie_id;
    private String streaming_service_name;
    private String streaming_service_link;
    private double cost;


}
