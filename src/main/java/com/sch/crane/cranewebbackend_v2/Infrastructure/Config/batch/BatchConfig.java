package com.sch.crane.cranewebbackend_v2.Infrastructure.Config.batch;

import com.sch.crane.cranewebbackend_v2.Service.Service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.DuplicateJobException;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class BatchConfig extends DefaultBatchConfiguration {

    private final ReservationService reservationService;

    //초기 예약 1주일치 생성
    @Bean
    public Job createReservationJob(JobRepository jobRepository,
                                    PlatformTransactionManager transactionManager
                                    ) throws DuplicateJobException {
        Job job = new JobBuilder("createReservation",jobRepository)
                .start(createReservationStep(jobRepository, transactionManager))
                .build();
        return job;
    }

    public Step createReservationStep(JobRepository jobRepository,
                                      PlatformTransactionManager transactionManager){
        Step step = new StepBuilder("createReservationStep", jobRepository)
                .tasklet(createReservationTasklet(), transactionManager)
                .build();
        return step;
    }

    public Tasklet createReservationTasklet(){
        return ((contribution, chunkContext) -> {
            System.out.println("***** Batch *****");

            reservationService.createReservationAfterNDays(8);

            return RepeatStatus.FINISHED;
        });
    }


    //매일 저녁 11시 예약 생성
    @Bean
    public Job createInitReservationJob(JobRepository jobRepository,
                                    PlatformTransactionManager transactionManager
                                    ) throws DuplicateJobException {
        Job job = new JobBuilder("createInitReservationJob",jobRepository)
                .start(createInitReservationStep(jobRepository, transactionManager))
                .build();
        return job;
    }

    public Step createInitReservationStep(JobRepository jobRepository,
                                      PlatformTransactionManager transactionManager){
        Step step = new StepBuilder("createInitReservationStep", jobRepository)
                .tasklet(createInitReservationTasklet(), transactionManager)
                .build();
        return step;
    }

    public Tasklet createInitReservationTasklet(){
        return ((contribution, chunkContext) -> {
            System.out.println("***** Batch *****");
            reservationService.initReservation();

            return RepeatStatus.FINISHED;
        });
    }


    //매일 밤 12시 다음주 합주 예약 오픈
    @Bean
    public Job openEnsembleJob(JobRepository jobRepository,
                                        PlatformTransactionManager transactionManager
    ) throws DuplicateJobException {
        Job job = new JobBuilder("openEnsembleJob",jobRepository)
                .start(openEnsembleStep(jobRepository, transactionManager))
                .build();
        return job;
    }

    public Step openEnsembleStep(JobRepository jobRepository,
                                          PlatformTransactionManager transactionManager){
        Step step = new StepBuilder("openEnsembleStep", jobRepository)
                .tasklet(openEnsembleTasklet(), transactionManager)
                .build();
        return step;
    }

    public Tasklet openEnsembleTasklet(){
        return ((contribution, chunkContext) -> {
            System.out.println("***** Batch *****");
            reservationService.openEnsembleAfterNDays(7);

            return RepeatStatus.FINISHED;
        });
    }


    //매일 낮 12시 다음주 악기 예약 오픈
    @Bean
    public Job openInstJob(JobRepository jobRepository,
                               PlatformTransactionManager transactionManager
    ) throws DuplicateJobException {
        Job job = new JobBuilder("openInstJob",jobRepository)
                .start(openInstStep(jobRepository, transactionManager))
                .build();
        return job;
    }

    public Step openInstStep(JobRepository jobRepository,
                                 PlatformTransactionManager transactionManager){
        Step step = new StepBuilder("openInstStep", jobRepository)
                .tasklet(openInstTasklet(), transactionManager)
                .build();
        return step;
    }

    public Tasklet openInstTasklet(){
        return ((contribution, chunkContext) -> {
            System.out.println("***** Batch *****");
            reservationService.openInstAfterNDays(7);

            return RepeatStatus.FINISHED;
        });
    }


//    @Bean
//    public Job testJob(JobRepository jobRepository,
//                                    PlatformTransactionManager transactionManager
//    ) throws DuplicateJobException {
//        Job job = new JobBuilder("testJob",jobRepository)
//                .start(testStep(jobRepository, transactionManager))
//                .build();
//        return job;
//    }
//
//    public Step testStep(JobRepository jobRepository,
//                                      PlatformTransactionManager transactionManager){
//        Step step = new StepBuilder("testStep", jobRepository)
//                .tasklet(testTaskLet(), transactionManager)
//                .build();
//        return step;
//    }
//
//    public Tasklet testTaskLet(){
//        return ((contribution, chunkContext) -> {
//            System.out.println("***** Batch *****");
//            System.out.println("RUN BATCH");
//
//            return RepeatStatus.FINISHED;
//        });
//    }
}
