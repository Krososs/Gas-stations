package pl.sk.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository  extends JpaRepository<Comment,Long> {

    List<Comment> findBystationId(Long id);
    Optional<Comment> findById(Long id);
    Long countBystationId(Long id);
    void deleteById(Long id);
}
