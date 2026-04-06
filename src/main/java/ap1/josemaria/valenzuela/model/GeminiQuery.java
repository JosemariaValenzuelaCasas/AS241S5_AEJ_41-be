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
@Table("gemini_queries")
public class GeminiQuery {

    @Id
    private Long id;

    @Column("prompt")
    private String prompt;

    @Column("response")
    private String response;

    @Column("model_used")
    private String modelUsed;

    @Column("tokens_used")
    private Integer tokensUsed;

    @CreatedDate
    @Column("created_at")
    private LocalDateTime createdAt;
}
