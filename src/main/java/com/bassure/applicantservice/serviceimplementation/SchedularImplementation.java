package com.bassure.applicantservice.serviceimplementation;

import com.bassure.applicantservice.model.JobPosting;
import com.bassure.applicantservice.model.JobPostingStatus;
import com.bassure.applicantservice.repo.JobPostingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableScheduling
public class SchedularImplementation {

    @Autowired
    JobPostingRepository jobRepo;

    public void jobStatusChangeSchedular(long delay, int jobId) {
        ScheduledExecutorService schedular = Executors.newSingleThreadScheduledExecutor();
        schedular.schedule(() -> {
            JobPosting job = jobRepo.findById(jobId).get();

            if (job.getStatus().equals(JobPostingStatus.OPEN)) {
                job.setStatus(JobPostingStatus.CLOSE);
                jobRepo.save(job);

            } else if (job.getStatus().equals(JobPostingStatus.INPROCESS)) {
                job.setStatus(JobPostingStatus.CLOSE);
                jobRepo.save(job);
                System.out.println("Jod Id " + jobId + " will be " + JobPostingStatus.CLOSE);

            }
        }, delay, TimeUnit.MINUTES);
        schedular.shutdown();
    }

}
