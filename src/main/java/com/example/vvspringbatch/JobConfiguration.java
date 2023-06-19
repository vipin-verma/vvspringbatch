package com.example.vvspringbatch;

import javax.sql.DataSource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
@EnableBatchProcessing
@Slf4j
public class JobConfiguration {

    // Bean to define the CSV reader
    @Bean
    public FlatFileItemReader<Employee> reader() {
        log.info("********************************************************************************************************************");
        log.info("********************************************************************************************************************");
        log.info("FlatFileItemReader");
        log.info("********************************************************************************************************************");
        log.info("********************************************************************************************************************");

        return new FlatFileItemReaderBuilder<Employee>()
                .name("employeeItemReader")
                .resource(new ClassPathResource("employees.csv"))
                .delimited()
                .names("firstName", "lastName", "email", "phone", "position", "department")
                .linesToSkip(1)
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                    setTargetType(Employee.class);
                }})
                .build();
    }

    // Bean to define the processor
    @Bean
    public ItemProcessor<Employee, Employee> processor() {
        log.info("********************************************************************************************************************");
        log.info("********************************************************************************************************************");
        log.info("ItemProcessor");
        log.info("********************************************************************************************************************");
        log.info("********************************************************************************************************************");
        return new EmployeeItemProcessor();
    }

    // Bean to define the writer for MySQL database
    @Bean
    public JdbcBatchItemWriter<Employee> writer(DataSource dataSource) {
        log.info("********************************************************************************************************************");
        log.info("********************************************************************************************************************");
        log.info("JdbcBatchItemWriter");
        log.info("********************************************************************************************************************");
        log.info("********************************************************************************************************************");
        return new JdbcBatchItemWriterBuilder<Employee>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO employee (first_name, last_name, email, phone , position, department) VALUES (:firstName, :lastName, :email, :phone, :position, :department)")
                .dataSource(dataSource)
                .build();
    }

    // Bean to define the Job with a listener and a step
    @Bean
    public Job importEmployeeJob(JobCompletionNotificationListener listener, Step step1) {
        log.info("********************************************************************************************************************");
        log.info("********************************************************************************************************************");
        log.info("JdbcBatchItemWriter");
        log.info("********************************************************************************************************************");
        log.info("********************************************************************************************************************");
        return jobBuilderFactory.get("importEmployeeJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1)
                .end()
                .build();
    }

    // Bean to define the Step with reader, processor, and writer
    @Bean
    public Step step1(JdbcBatchItemWriter<Employee> writer) {
        log.info("********************************************************************************************************************");
        log.info("********************************************************************************************************************");
        log.info("step1");
        log.info("********************************************************************************************************************");
        log.info("********************************************************************************************************************");
        return stepBuilderFactory.get("step1")
                .<Employee, Employee>chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer)
                .build();
    }

    // JobBuilderFactory and StepBuilderFactory are autowired in the constructor
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    public JobConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        log.info("********************************************************************************************************************");
        log.info("********************************************************************************************************************");
        log.info("JobConfiguration");
        log.info("********************************************************************************************************************");
        log.info("********************************************************************************************************************");
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }
}
