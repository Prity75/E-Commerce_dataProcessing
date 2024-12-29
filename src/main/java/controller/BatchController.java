package controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BatchController {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job job;

    @GetMapping("/run-batch")
    public String runBatchJob(){
        try{
            jobLauncher.run(job, new JobParameters());
            return "Batch Job Started";
        }catch (JobExecutionException e){
            return "Batch Job Failed: "+ e.getMessage();
        }
    }
}
