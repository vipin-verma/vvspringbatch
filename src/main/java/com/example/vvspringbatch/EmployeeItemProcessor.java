package com.example.vvspringbatch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
@Slf4j
public class EmployeeItemProcessor implements ItemProcessor<Employee, Employee> {

    @Override
    public Employee process(final Employee employee) throws Exception {
        log.info("********************************************************************************************************************");
        log.info("********************************************************************************************************************");
        log.info("process");
        log.info("********************************************************************************************************************");
        log.info("********************************************************************************************************************");
        return employee;
    }
}
