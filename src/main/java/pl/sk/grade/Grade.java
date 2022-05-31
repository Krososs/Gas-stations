package pl.sk.grade;


import lombok.Data;
import lombok.NonNull;

import javax.persistence.*;

@Entity
@Table(name="grades")
@Data
public class Grade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private Long userId;

    @NonNull
    private Long stationId;

    @NonNull
    private Integer grade;

    public Grade() {
    }
}
