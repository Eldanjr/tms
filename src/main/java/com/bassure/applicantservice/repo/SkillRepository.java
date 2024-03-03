package com.bassure.applicantservice.repo;

import com.bassure.applicantservice.model.Skill;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Integer> {
     @Query(value = "select id from job_posting_skill where job_posting_id = :id ",nativeQuery = true)
    public List<Integer> jobSkillList(int id);
    
    @Transactional
    @Modifying
    @Query (value = "delete from job_posting_skill where id = :id" ,nativeQuery = true )
    public void deleteJobPostingSkillBYJobId(int id);
}
