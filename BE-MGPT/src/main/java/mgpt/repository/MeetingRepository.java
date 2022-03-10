package mgpt.repository;

import mgpt.dao.Meeting;
import mgpt.dao.PermissionOfRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Integer> {
}
