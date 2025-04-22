package softwareProject.business;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor


public class MovieReview {
    private String author;
    private String content;
    private String created_at;
}
