package mgpt.repository;

import mgpt.dao.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Integer> {

    List<Meeting> findAllByProject_ProjectId(int projectId);

    Meeting findMeetingByMeetingId(int meetingId);
}
