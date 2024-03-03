package com.bassure.applicantservice.repo;

import com.bassure.applicantservice.model.JobPosting;
import com.bassure.applicantservice.model.JobRecruiter;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

@Repository
public interface JobRecruiterRepository extends JpaRepository<JobRecruiter, Integer> {

    @Query(value = "select jp.id,jp.title from job_posting as jp join job_recruiter as jr on jr.job_posting_id = jp.id where recruiter_id =:id and jr.status NOT LIKE 'CLOSE' ", nativeQuery = true)
    public List<Map<Integer, String>> findByrecruiter(int id);

    public Optional<JobRecruiter> findByrecruiterId(int id);

    public List<JobRecruiter> findByRecruiterId(int id);

    @Query(value = "SELECT jr.id,jp.title,jp.id FROM job_recruiter as jr join job_posting as jp on jr.job_posting_id = jp.id where jr.job_posting_id = :jobId and jr.recruiter_id = :recruiterId", nativeQuery = true)
    public List<Object[]> getByJobPostingAndRecruiterId(@Param("jobId") int jobId, @Param("recruiterId") int recruiterId);

    public JobRecruiter findByJobPostingIdAndRecruiterId(int jobPostingId, int recruiterId);

    @Query(value = "select jp.id,jp.title from job_posting as jp join job_recruiter as jr on jr.job_posting_id = jp.id where recruiter_id =:id and jr.status NOT LIKE 'CLOSE' ", nativeQuery = true)
    public List<Map<Integer, String>> findRecruiterJob(int id);

    @Transactional
    @Modifying
    @Query(value = "delete from job_recruiter where job_posting_id = :id", nativeQuery = true)
    public void deleteJobRecruiterByJobId(int id);

    @Query(value = "select recruiter_id from job_recruiter where job_posting_id = :id", nativeQuery = true)
    public List<Integer> findByJobPostingById(int id);

    @Query(value = "select jp.id,jp.title from job_posting as jp join job_recruiter as jr on jr.job_posting_id = jp.id where recruiter_id =:id and jr.status NOT LIKE 'CLOSE' ", nativeQuery = true)
    public List<Map<Integer, String>> findByrecruiterJob(int id);

    @Query(value = "select id from job_recruiter where recruiter_id = :id", nativeQuery = true)
    public List<Integer> findByEmployeeId(int id);

    @Query(value = "select assigned_counts from job_recruiter where status NOT LIKE 'CLOSED' and recruiter_id = :id", nativeQuery = true)
    public List<Integer> findByEmployeeTaskCount(int id);

    @Query(value = "select assigned_counts from job_recruiter where status NOT LIKE 'INPROGRESS' and status NOT LIKE 'CLOSE' and recruiter_id = :id", nativeQuery = true)
    public List<Integer> findByEmployeeTaskCountTotal(int id);

    @Query(value = "SELECT\n" +
            "    jp.title,\n" +
            "    jp.no_of_vaccancies,\n" +
            "    jp.type,\n" +
            "    jp.status,\n" +
            "    jp.location,\n" +
            "    jp.open_date,\n" +
            "    jp.close_date,\n" +
            "    jp.created_by,\n" +
            "    jp.id,\n" +
            "    jr.id AS job_recruiter_id,\n" +
            "    jr.job_posting_id,\n" +
            "    jr.recruiter_id\n" +
            "FROM\n" +
            "    job_posting jp\n" +
            "JOIN\n" +
            "    job_recruiter jr ON jp.id = jr.job_posting_id\n" +
            "WHERE\n" +
            "    jr.recruiter_id = :id\n" +
            "    AND (\n" +
            "        jp.status = :status\n" +
            "        OR jp.title LIKE :title%\n" +
            "    )", nativeQuery = true)
    public List<Object[]> searchByNameandStatus(int id, String status, String title);

}
