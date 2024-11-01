package com.sch.crane.cranewebbackend_v2.Infrastructure.Config.batch;


import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.launch.JobInstanceAlreadyExistsException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
public class BatchScheduler {

    private final JobLauncher jobLauncher;
    private final JobRegistry jobRegistry;

    @Bean
    public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor(){
        JobRegistryBeanPostProcessor jobProcessor = new JobRegistryBeanPostProcessor();
        jobProcessor.setJobRegistry(jobRegistry);;
        return jobProcessor;
    }

    @Scheduled(cron = "0 0 23 * * ? ")
    public void runJob(){
        String time = LocalDateTime.now().toString();

        try{
            Job job = jobRegistry.getJob("createReservation");
            JobParametersBuilder jobParam = new JobParametersBuilder().addString("time", time);
            jobLauncher.run(job, jobParam.toJobParameters());
        }catch(NoSuchJobException e){
            throw new RuntimeException(e);
        }catch(JobInstanceAlreadyCompleteException |
               JobExecutionAlreadyRunningException |
               JobParametersInvalidException |
               JobRestartException e){
            throw new RuntimeException("작업 실행 중 오류 발생: " + e.getMessage(), e);
        }
    }

    @Scheduled(cron = "0 0 0 * * ? ")
    public void runEnsembleOpenJob(){
        String time = LocalDateTime.now().toString();

        try{
            Job job = jobRegistry.getJob("openEnsembleJob");
            JobParametersBuilder jobParam = new JobParametersBuilder().addString("time", time);
            jobLauncher.run(job, jobParam.toJobParameters());
        }catch(NoSuchJobException e){
            throw new RuntimeException(e);
        }catch(JobInstanceAlreadyCompleteException |
               JobExecutionAlreadyRunningException |
               JobParametersInvalidException |
               JobRestartException e){
            throw new RuntimeException("작업 실행 중 오류 발생: " + e.getMessage(), e);
        }
    }

    @Scheduled(cron = "0 0 12 * * ? ")
    public void runInstOpenJob(){
        String time = LocalDateTime.now().toString();

        try{
            Job job = jobRegistry.getJob("openInstJob");
            JobParametersBuilder jobParam = new JobParametersBuilder().addString("time", time);
            jobLauncher.run(job, jobParam.toJobParameters());
        }catch(NoSuchJobException e){
            throw new RuntimeException(e);
        }catch(JobInstanceAlreadyCompleteException |
               JobExecutionAlreadyRunningException |
               JobParametersInvalidException |
               JobRestartException e){
            throw new RuntimeException("작업 실행 중 오류 발생: " + e.getMessage(), e);
        }
    }

    //처음 시작될 때 한번 실행
    @EventListener(ApplicationReadyEvent.class)
    public void runInitJob(){
        String time = LocalDateTime.now().toString();

        try{
            Job job = jobRegistry.getJob("createInitReservationJob");
            JobParametersBuilder jobParam = new JobParametersBuilder().addString("time", time);
            jobLauncher.run(job, jobParam.toJobParameters());
        }catch(NoSuchJobException e){
            throw new RuntimeException("Job이 존재하지 않습니다: " + e.getMessage(), e);
        }catch(JobInstanceAlreadyCompleteException |
               JobExecutionAlreadyRunningException |
               JobParametersInvalidException |
               JobRestartException e){
            throw new RuntimeException("작업 실행 중 오류 발생: " + e.getMessage(), e);
        }
    }

//batch test
//    @Scheduled(cron = "*/10 * * * * * ")
//    public void testJob(){
//        String time = LocalDateTime.now().toString();
//
//        try{
//            Job job = jobRegistry.getJob("testJob");
//            JobParametersBuilder jobParam = new JobParametersBuilder().addString("time", time);
//            jobLauncher.run(job, jobParam.toJobParameters());
//        }catch(NoSuchJobException e){
//            throw new RuntimeException(e);
//        }catch(JobInstanceAlreadyCompleteException |
//               JobExecutionAlreadyRunningException |
//               JobParametersInvalidException |
//               JobRestartException e){
//            throw new RuntimeException(e);
//        }
//    }

}
