package ap1.josemaria.valenzuela.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("huggingface_queries")
public class HuggingFaceQuery {

    @Id
    private Long id;

    @Column("input_text")
    private String inputText;

    @Column("candidate_labels")
    private String candidateLabels;

    @Column("top_label")
    private String topLabel;

    @Column("top_score")
    private Double topScore;

    @Column("full_response")
    private String fullResponse;

    @Column("model_used")
    private String modelUsed;

    @CreatedDate
    @Column("created_at")
    private LocalDateTime createdAt;
}
