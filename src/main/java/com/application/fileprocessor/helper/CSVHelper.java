package com.application.fileprocessor.helper;

import com.application.fileprocessor.model.RevenueAmount;
import com.opencsv.bean.CsvToBeanBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class CSVHelper {

    private static Logger logger = LogManager.getLogger(CSVHelper.class);

    private static String CSV_TYPE = ".csv";
    private final static String FOLDER_PATH = "/home/amit.agarwal/Downloads/input_data_small1/";

    public static File[] getFiles() {
        File[] files = new File(FOLDER_PATH).listFiles(path -> {
            if (path.isFile() && CSVHelper.hasCSVFormat(path.getName())) {
                return true;
            }
            return false;
        });
        return files;
    }

    private static boolean hasCSVFormat(String file) {
        if (!file.endsWith(CSV_TYPE)) {
            return false;
        }
        return true;
    }

    public static List<RevenueAmount> csvToRevenueAmount(File file) {
        List<RevenueAmount> revenueList = new ArrayList<>();
        try {
            revenueList = new CsvToBeanBuilder(new FileReader(file))
                    .withType(RevenueAmount.class)
                    .build()
                    .parse();
        } catch (Exception e) {
            logger.error("Failed to parse CSV file: " + e.getMessage());
        }
        return revenueList;
    }

}
