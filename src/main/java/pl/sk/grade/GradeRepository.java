package pl.sk.grade;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface GradeRepository extends JpaRepository<Grade,Long> {

    List<Grade> findByStationId(Long id);

    @Transactional
    @Query(value="SELECT g FROM Grade g WHERE g.userId = :userId AND g.stationId = :stationId")
    Optional<Grade> checkIfAssessed(@Param("userId") Long userId, @Param("stationId") Long stationId);


}
