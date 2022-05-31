package pl.sk.comment;

import lombok.Data;
import lombok.NonNull;
import javax.persistence.*;

@Entity
@Table(name="comments")
@Data
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String nickname;

    private String text;

    @NonNull
    @Column(name="stationId")
    private Long stationId;

    public Comment() {
    }
}
