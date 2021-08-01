package com.application.fileprocessor.service;

import com.application.fileprocessor.callable.ArraySumCallable;
import com.application.fileprocessor.helper.CSVHelper;
import com.application.fileprocessor.model.RevenueAmount;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class FileProcessorServiceImpl implements FileProcessorService {

    private static Logger logger = LogManager.getLogger(FileProcessorServiceImpl.class);

    private ExecutorService executorService;

    @PostConstruct
    void init() {
        executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);
    }

    @PreDestroy
    public void beanDestroy() {
        try {
            executorService.shutdown();
        } catch (Exception ex) {
            logger.error("Error while stopping the thread pool", ex);
        }
    }

    @Override
    public void processFiles() {

        File[] files = CSVHelper.getFiles();

        List<Future<List<RevenueAmount>>> futuresList = new ArrayList<>();
        List<List<RevenueAmount>> revenueList = new ArrayList<>();
        for (File file : files) {
            Future<List<RevenueAmount>> future =
                    executorService.submit(() -> {
                        return CSVHelper.csvToRevenueAmount(file);
                    });
            futuresList.add(future);
        }

        for (Future<List<RevenueAmount>> future : futuresList) {
            List<RevenueAmount> list = new ArrayList<>();
            try {
                list = future.get();
            } catch (Exception e) {
                logger.error("Exception occured while getting list of revenue from CSV files : ", e);
            }
            revenueList.add(list);
        }

        List<Long> sumOfEachCSV = calculateSumOfMultipleLists(revenueList);
        System.out.println("Final Output Sum of Revenue : " + calculateSumOfList(sumOfEachCSV));
    }

    private Long calculateSumOfList(List<Long> revenues) {
        AtomicLong total = new AtomicLong(0);
        for (Long revenue : revenues) {
            Long value = revenue;
            executorService.submit(() -> {
                total.addAndGet(value);
                return;
            });
        }
        executorService.shutdown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
        }
        return total.get();
    }


    public List<Long> calculateSumOfMultipleLists(List<List<RevenueAmount>> revenueLists) {

        List<Callable<Long>> callables = new ArrayList<>(revenueLists.size());
        List<Long> arraySum = new ArrayList<>(revenueLists.size());
        for (List<RevenueAmount> longValues : revenueLists) {
            Callable<Long> callable = new ArraySumCallable(longValues);
            callables.add(callable);
        }
        try {
            List<Future<Long>> futures = executorService.invokeAll(callables);

            for (Future<Long> future : futures) {
                Long sum = future.get();
                arraySum.add(sum);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            logger.error("Exception while calculating sum : ", e);
        }
        return arraySum;
    }

}



