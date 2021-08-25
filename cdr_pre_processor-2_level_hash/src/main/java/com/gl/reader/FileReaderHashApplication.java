package com.gl.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;

import com.gl.reader.bean.Book;
import com.gl.reader.configuration.ConnectionConfiguration;
import com.gl.reader.configuration.PropertiesReader;
import com.gl.reader.constants.Alerts;
import java.io.PrintWriter;
import java.io.StringWriter;

@EnableAsync
@SpringBootConfiguration
@EnableAutoConfiguration
@SpringBootApplication(scanBasePackages = {"com.gl.reader"})
public class FileReaderHashApplication {

    private static final Logger logger = Logger.getLogger(FileReaderHashApplication.class);
    static long duplicate = 0;
    static long error = 0;
    static long inSet = 0;
    static long totalCount = 0;
    static long iduplicate = 0;
    static long ierror = 0;
    static long iinSet = 0;
    static long itotalCount = 0;
    static String type;
    static long value;
    static long processed = 0;
    static String fileName;
    static String extension;
    static String servername;
    static Integer sleep;
    static String folderName;
    static String sourceName;
    static String eventTime;
    static String errorFlag;
    static long errorDuplicate = 0;
    static long inErrorSet = 0;
    static String inputLocation;
    static String outputLocation;
    static Long timeTaken;
    static Float Tps;
    static Integer returnCount;
    static long inputOffset = 0;
    static long outputOffset = 0;
    static String tag;
    static Integer fileCount = 0;
    static Integer headCount = 0;

    static Set<Book> errorFile = new HashSet<Book>();
    static Set<String> set = new HashSet<String>();
    static List<String> pattern = new ArrayList<String>();
    public static HashMap<String, HashMap<String, Book>> BookHashMap = new HashMap<>();
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
    static Clock offsetClock = Clock.offset(Clock.systemUTC(), Duration.ofHours(+7));
    static Instant start = Instant.now(offsetClock);
    static LocalDate currentdate = LocalDate.now();
    static Integer day = currentdate.getDayOfMonth();
    static Month month = currentdate.getMonth();
    static Integer year = currentdate.getYear();

    static PropertiesReader propertiesReader = null;
    static ConnectionConfiguration connectionConfiguration = null;

    public static void main(String[] args) {
        File file = null;
        try {
            sourceName = args[0];
            folderName = args[1];
            ApplicationContext context = SpringApplication.run(FileReaderHashApplication.class, args);
            propertiesReader = (PropertiesReader) context.getBean("propertiesReader");
            connectionConfiguration = (ConnectionConfiguration) context.getBean("connectionConfiguration");
            DateTimeFormatter tagDtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            LocalDateTime tagNow = LocalDateTime.now();
            tag = tagDtf.format(tagNow);
            type = propertiesReader.typeOfProcess;
            value = propertiesReader.filesCount;
            extension = propertiesReader.extension;
            sleep = propertiesReader.sleepTime;
            inputLocation = propertiesReader.inputLocation;
            outputLocation = propertiesReader.outputLocation;
            errorFlag = propertiesReader.errorReportFlag;
            returnCount = propertiesReader.rowCountForSplit;
            servername = propertiesReader.servername;
            if (!"sm_ims".equals(folderName)) {
                set.addAll(propertiesReader.reportType);
                if (set.contains("null")) {
                    set = new HashSet<String>();
                }
            }
            if (value == -1) {
                File directory = new File(inputLocation + "/" + sourceName + "/" + folderName);
                value = directory.list().length;
                logger.info("File Count:" + value    +    " "  +  new File(directory, "null").exists());
                
//                if (value == 0  || (value == 1 &&   new File(directory, "null").exists() )  ) {
                if (value == 0    ) {
                    logger.info("No file present");
                    insertReportv2("O", "0", Long.valueOf(0), Long.valueOf(0), Long.valueOf(0), Long.valueOf(0), "0", "0", Float.valueOf(0), Float.valueOf(0), sourceName, folderName, Long.valueOf(0), tag, 0, headCount);
                    System.exit(0);
                }
            }
            Instant startTimeOutput = Instant.now(offsetClock);
            while (true) {
                if ("File".equalsIgnoreCase(type)) {
                    if (processed < value) {
                        File folder = new File(inputLocation + "/" + sourceName + "/" + folderName);
                        File[] listOfFiles = folder.listFiles();
                        if (listOfFiles.length <= 0) {
                            logger.info("No file present");
                            System.exit(0);
                            //sleep 5sec
                            Thread.sleep(sleep);
                            continue;
                        } else {
                            for (int j = 0; j < listOfFiles.length; j++) {
//                                        File file = listOfFiles[j];
                                file = listOfFiles[j];
                                Instant startTime = Instant.now(offsetClock);
                                if ("".equals(extension)) {
                                    if (file.isFile()) {
                                        if (processed < value) {
                                            logger.info("Content: " + file.getName());
                                            fileName = file.getName();
                                            if ("sm_ims".equals(folderName)) {
                                                boolean check = readBooksFromCSV_ims(file.getName());
                                                if (!check) {
                                                    processed++;
                                                    move(fileName);
                                                    continue;
                                                }
                                            } else {
                                                boolean check = readBooksFromCSV(file.getName());
                                                if (!check) {
                                                    processed++;
                                                    move(fileName);
                                                    continue;
                                                }
                                            }
                                            //check source directory 
                                            Path pathSource = Paths.get(outputLocation + "/" + sourceName);

                                            if (!Files.exists(pathSource)) {

                                                Files.createDirectory(pathSource);
                                                logger.info("Directory created");
                                            }
                                            //check folder
                                            Path pathFolder = Paths.get(outputLocation + "/" + sourceName + "/" + folderName);

                                            if (!Files.exists(pathFolder)) {

                                                Files.createDirectory(pathFolder);
                                                logger.info("Directory created");
                                            }
                                            //check directory
                                            Path path = Paths.get(outputLocation + "/" + sourceName + "/" + folderName + "/processed/");

                                            if (!Files.exists(path)) {

                                                Files.createDirectory(path);
                                                logger.info("Directory created");
                                            }
                                            //year
                                            Path pathYear = Paths.get(outputLocation + "/" + sourceName + "/" + folderName + "/processed/" + year);

                                            if (!Files.exists(pathYear)) {

                                                Files.createDirectory(pathYear);
                                                logger.info("Directory created");
                                            }
                                            //month
                                            Path pathMonth = Paths.get(outputLocation + "/" + sourceName + "/" + folderName + "/processed/" + year + "/" + month);

                                            if (!Files.exists(pathMonth)) {

                                                Files.createDirectory(pathMonth);
                                                logger.info("Directory created");
                                            }
                                            //day
                                            Path pathDay = Paths.get(outputLocation + "/" + sourceName + "/" + folderName + "/processed/" + year + "/" + month + "/" + day);

                                            if (!Files.exists(pathDay)) {

                                                Files.createDirectory(pathDay);
                                                logger.info("Directory created");
                                            }
                                            //rename file
                                            if (Files.exists(Paths.get(outputLocation + "/" + sourceName + "/" + folderName + "/processed/" + year + "/" + month + "/" + day + "/" + fileName))) {
                                                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                                                File sourceFile = new File(outputLocation + "/" + sourceName + "/" + folderName + "/processed/" + year + "/" + month + "/" + day + "/" + fileName);
                                                String newName = fileName + "-" + sdf.format(timestamp);
                                                File destFile = new File(outputLocation + "/" + sourceName + "/" + folderName + "/processed/" + year + "/" + month + "/" + day + "/" + newName);
                                                if (sourceFile.renameTo(destFile)) {
                                                    logger.info("File renamed successfully");
                                                } else {
                                                    logger.info("Failed to rename file");
                                                }
                                            }
                                            //move file
                                            Path temp = Files.move(Paths.get(inputLocation + "/" + sourceName + "/" + folderName + "/" + file.getName()), Paths.get(outputLocation + "/" + sourceName + "/" + folderName + "/processed/" + year + "/" + month + "/" + day + "/" + fileName));
                                            if (temp != null) {
                                                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                                                LocalDateTime now = LocalDateTime.now();
                                                Instant endTime = Instant.now(offsetClock);
                                                timeTaken = Duration.between(startTime, endTime).toMillis();
                                                Float timeTakenF = ((float) timeTaken / 1000);
                                                if (timeTakenF == 0.0) {
                                                    timeTakenF = (float) 0.001;
                                                }
                                                Tps = itotalCount / timeTakenF;
                                                logger.info("Input File Report");
                                                logger.info("FileName: " + fileName + ", Date: " + dtf.format(now) + ", Start Time: " + startTime + ", End Time: " + endTime + ", Time Taken: " + timeTakenF + ", Operator Name: " + sourceName + ", Source Name: " + folderName + ", TPS: " + Tps + ", Error: " + ierror + ", inSet: " + iinSet + ", totalCount: " + itotalCount + ", duplicate: " + iduplicate + ", volume: " + inputOffset + ", tag: " + tag);
                                                fileCount++;
                                                insertReportv2("I", fileName, itotalCount, ierror, iduplicate, iinSet, startTime.toString(), endTime.toString(), timeTakenF, Tps, sourceName, folderName, inputOffset, tag, 1, headCount);
                                                headCount = 0;
                                                ierror = 0;
                                                iinSet = 0;
                                                itotalCount = 0;
                                                iduplicate = 0;
                                                inputOffset = 0;
                                                logger.info("File moved successfully");
                                                processed++;
                                            } else {
                                                logger.info("Failed to move the file");
                                            }
                                        } else {
                                            makeCsv();
                                            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                                            LocalDateTime now = LocalDateTime.now();
                                            Instant endTimeOutput = Instant.now(offsetClock);
                                            timeTaken = Duration.between(startTimeOutput, endTimeOutput).toMillis();
                                            Float timeTakenF = ((float) timeTaken / 1000);
                                            if (timeTakenF == 0.0) {
                                                timeTakenF = (float) 0.001;
                                            }
                                            Tps = totalCount / timeTakenF;
                                            logger.info("Output File Report");
                                            logger.info("FileName: " + fileName + ", Date: " + dtf.format(now) + ", Start Time: " + startTimeOutput + ", End Time: " + endTimeOutput + ", Time Taken: " + timeTakenF + ", Operator Name: " + sourceName + ", Source Name: " + folderName + ", TPS: " + Tps + ", Error: " + error + ", inSet: " + inSet + ", totalCount: " + totalCount + ", duplicate: " + duplicate + ", volume: " + outputOffset + ", tag: " + tag);
                                            insertReportv2("O", fileName, totalCount, error, duplicate, inSet, startTimeOutput.toString(), endTimeOutput.toString(), timeTakenF, Tps, sourceName, folderName, outputOffset, tag, fileCount, headCount);
                                            headCount = 0;
                                            error = 0;
                                            inSet = 0;
                                            totalCount = 0;
                                            duplicate = 0;
                                            outputOffset = 0;
                                            fileCount = 0;
                                            processed = 0;
                                            BookHashMap.clear();
                                            makeErrorCsv();
                                            logger.info("Output Error File Report");
                                            logger.info("FileName: " + fileName + ", Date: " + dtf.format(now) + ", Error: " + errorDuplicate + ", inFile: " + inErrorSet);
                                            errorDuplicate = 0;
                                            inErrorSet = 0;
                                            errorFile.clear();
                                            System.exit(0);
                                        }
                                    } else {
                                        logger.info("No file or Incorrect file format preset");
                                        processed++;
                                        //sleep 5sec
                                        //System.exit(0);
                                        //Thread.sleep(sleep);
                                        continue;
                                    }
                                } else {
                                    if (file.isFile() && !file.getName().endsWith(extension)) {
                                        if (processed < value) {
                                            logger.info("Content: " + file.getName());
                                            fileName = file.getName();
                                            if ("sm_ims".equals(folderName)) {
                                                //for ims
                                                boolean check = readBooksFromCSV_ims(file.getName());
                                                if (!check) {
                                                    processed++;
                                                    move(fileName);
                                                    continue;
                                                }
                                            } else {
                                                //for others
                                                boolean check = readBooksFromCSV(file.getName());
                                                if (!check) {
                                                    processed++;
                                                    move(fileName);
                                                    continue;
                                                }
                                            }
                                            //check source directory
                                            Path pathSource = Paths.get(outputLocation + "/" + sourceName);
                                            if (!Files.exists(pathSource)) {
                                                Files.createDirectory(pathSource);
                                                logger.info("Directory created");
                                            }
                                            //check folder
                                            Path pathFolder = Paths.get(outputLocation + "/" + sourceName + "/" + folderName);

                                            if (!Files.exists(pathFolder)) {

                                                Files.createDirectory(pathFolder);
                                                logger.info("Directory created");
                                            }
                                            //check directory
                                            Path path = Paths.get(outputLocation + "/" + sourceName + "/" + folderName + "/processed/");

                                            if (!Files.exists(path)) {

                                                Files.createDirectory(path);
                                                logger.info("Directory created");
                                            }
                                            //year
                                            Path pathYear = Paths.get(outputLocation + "/" + sourceName + "/" + folderName + "/processed/" + year);

                                            if (!Files.exists(pathYear)) {

                                                Files.createDirectory(pathYear);
                                                logger.info("Directory created");
                                            }
                                            //month
                                            Path pathMonth = Paths.get(outputLocation + "/" + sourceName + "/" + folderName + "/processed/" + year + "/" + month);

                                            if (!Files.exists(pathMonth)) {

                                                Files.createDirectory(pathMonth);
                                                logger.info("Directory created");
                                            }
                                            //day
                                            Path pathDay = Paths.get(outputLocation + "/" + sourceName + "/" + folderName + "/processed/" + year + "/" + month + "/" + day);

                                            if (!Files.exists(pathDay)) {

                                                Files.createDirectory(pathDay);
                                                logger.info("Directory created");
                                            }
                                            //rename file
                                            if (Files.exists(Paths.get(outputLocation + "/" + sourceName + "/" + folderName + "/processed/" + year + "/" + month + "/" + day + "/" + fileName))) {
                                                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                                                File sourceFile = new File(outputLocation + "/" + sourceName + "/" + folderName + "/processed/" + year + "/" + month + "/" + day + "/" + fileName);
                                                String newName = fileName + "-" + sdf.format(timestamp);
                                                File destFile = new File(outputLocation + "/" + sourceName + "/" + folderName + "/processed/" + year + "/" + month + "/" + day + "/" + newName);
                                                if (sourceFile.renameTo(destFile)) {
                                                    logger.info("File renamed successfully");
                                                } else {
                                                    logger.info("Failed to rename file");
                                                }
                                            }
                                            //move file
                                            Path temp = Files.move(Paths.get(inputLocation + "/" + sourceName + "/" + folderName + "/" + file.getName()), Paths.get(outputLocation + "/" + sourceName + "/" + folderName + "/processed/" + year + "/" + month + "/" + day + "/" + fileName));
                                            if (temp != null) {
                                                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                                                LocalDateTime now = LocalDateTime.now();
                                                Instant endTime = Instant.now(offsetClock);
                                                timeTaken = Duration.between(startTime, endTime).toMillis();
                                                Float timeTakenF = ((float) timeTaken / 1000);
                                                if (timeTakenF == 0.0) {
                                                    timeTakenF = (float) 0.001;
                                                }
                                                Tps = itotalCount / timeTakenF;
                                                logger.info("Input File Report");
                                                logger.info("FileName: " + fileName + ", Date: " + dtf.format(now) + ", Start Time: " + startTime + ", End Time: " + endTime + ", Time Taken: " + timeTakenF + ", Operator Name: " + sourceName + ", Source Name: " + folderName + ", TPS: " + Tps + ", Error: " + ierror + ", inSet: " + iinSet + ", totalCount: " + itotalCount + ", duplicate: " + iduplicate + ", volume: " + inputOffset + ", tag: " + tag);
                                                fileCount++;
                                                insertReportv2("I", fileName, itotalCount, ierror, iduplicate, iinSet, startTime.toString(), endTime.toString(), timeTakenF, Tps, sourceName, folderName, inputOffset, tag, 1, headCount);
                                                headCount = 0;
                                                ierror = 0;
                                                iinSet = 0;
                                                itotalCount = 0;
                                                iduplicate = 0;
                                                inputOffset = 0;
                                                logger.info("File moved successfully");
                                                processed++;
                                            } else {
                                                logger.info("Failed to move the file");
                                            }
                                        } else {
                                            makeCsv();
                                            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                                            LocalDateTime now = LocalDateTime.now();
                                            Instant endTimeOutput = Instant.now(offsetClock);
                                            timeTaken = Duration.between(startTimeOutput, endTimeOutput).toMillis();
                                            Float timeTakenF = ((float) timeTaken / 1000);
                                            if (timeTakenF == 0.0) {
                                                timeTakenF = (float) 0.001;
                                            }
                                            Tps = totalCount / timeTakenF;
                                            logger.info("Output File Report");
                                            logger.info("FileName: " + fileName + ", Date: " + dtf.format(now) + ", Start Time: " + startTimeOutput + ", End Time: " + endTimeOutput + ", Time Taken: " + timeTakenF + ", Operator Name: " + sourceName + ", Source Name: " + folderName + ", TPS: " + Tps + ", Error: " + error + ", inSet: " + inSet + ", totalCount: " + totalCount + ", duplicate: " + duplicate + ", volume: " + outputOffset + ", tag: " + tag);
                                            insertReportv2("O", fileName, totalCount, error, duplicate, inSet, startTimeOutput.toString(), endTimeOutput.toString(), timeTakenF, Tps, sourceName, folderName, outputOffset, tag, fileCount, headCount);
                                            headCount = 0;
                                            error = 0;
                                            inSet = 0;
                                            totalCount = 0;
                                            duplicate = 0;
                                            outputOffset = 0;
                                            fileCount = 0;
                                            processed = 0;
                                            BookHashMap.clear();
                                            makeErrorCsv();
                                            logger.info("Output Error File Report");
                                            logger.info("FileName: " + fileName + ", Date: " + dtf.format(now) + ", Error: " + errorDuplicate + ", inFile: " + inErrorSet);
                                            errorDuplicate = 0;
                                            inErrorSet = 0;
                                            errorFile.clear();
                                            System.exit(0);
                                        }
                                    } else {
                                        logger.info("No file or Incorrect file format present");
                                        processed++;
                                        //sleep 5sec
                                        //System.exit(0);
                                        //Thread.sleep(sleep);
                                        continue;
                                    }
                                }

                            }
                        }
                    } else {
                        //to generate output file
                        makeCsv();
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                        LocalDateTime now = LocalDateTime.now();
                        Instant endTimeOutput = Instant.now(offsetClock);
                        timeTaken = Duration.between(startTimeOutput, endTimeOutput).toMillis();
                        Float timeTakenF = ((float) timeTaken / 1000);
                        if (timeTakenF == 0.0) {
                            timeTakenF = (float) 0.001;
                        }
                        Tps = totalCount / timeTakenF;
                        logger.info("Output File Report");
                        logger.info("FileName: " + fileName + ", Date: " + dtf.format(now) + ", Start Time: " + startTimeOutput + ", End Time: " + endTimeOutput + ", Time Taken: " + timeTakenF + ", Operator Name: " + sourceName + ", Source Name: " + folderName + ", TPS: " + Tps + ", Error: " + error + ", inSet: " + inSet + ", totalCount: " + totalCount + ", duplicate: " + duplicate + ", volume: " + outputOffset + ", tag: " + tag);
                        insertReportv2("O", fileName, totalCount, error, duplicate, inSet, startTimeOutput.toString(), endTimeOutput.toString(), timeTakenF, Tps, sourceName, folderName, outputOffset, tag, fileCount, headCount);
                        headCount = 0;
                        error = 0;
                        inSet = 0;
                        totalCount = 0;
                        duplicate = 0;
                        outputOffset = 0;
                        fileCount = 0;
                        processed = 0;
                        BookHashMap.clear();
                        makeErrorCsv();
                        logger.info("Output Error File Report");
                        logger.info("FileName: " + fileName + ", Date: " + dtf.format(now) + ", Error: " + errorDuplicate + ", inFile: " + inErrorSet);
                        errorDuplicate = 0;
                        inErrorSet = 0;
                        errorFile.clear();
                        System.exit(0);
                    }
                } else if ("Time".equalsIgnoreCase(type)) {
                    File folder = new File(inputLocation + "/" + sourceName + "/" + folderName);
                    File[] listOfFiles = folder.listFiles();
                    if (listOfFiles.length <= 0) {
                        logger.info("No file present");
                        System.exit(0);
                        //sleep 5sec
                        Thread.sleep(sleep);
                        continue;
                    } else {
                        for (int j = 0; j < listOfFiles.length; j++) {
//                                   File file = listOfFiles[j];
                            file = listOfFiles[j];
                            Instant startTime = Instant.now(offsetClock);
                            if ("".equals(extension)) {
                                if (file.isFile()) {
                                    logger.info("Content: " + file.getName());
                                    fileName = file.getName();
                                    if ("sm_ims".equals(folderName)) {
                                        boolean check = readBooksFromCSV_ims(file.getName());
                                        if (!check) {
                                            processed++;
                                            move(fileName);
                                            continue;
                                        }
                                    } else {
                                        boolean check = readBooksFromCSV(file.getName());
                                        if (!check) {
                                            processed++;
                                            move(fileName);
                                            continue;
                                        }
                                    }
                                    //check source directory
                                    Path pathSource = Paths.get(outputLocation + "/" + sourceName);

                                    if (!Files.exists(pathSource)) {

                                        Files.createDirectory(pathSource);
                                        logger.info("Directory created");
                                    }
                                    //check folder
                                    Path pathFolder = Paths.get(outputLocation + "/" + sourceName + "/" + folderName);

                                    if (!Files.exists(pathFolder)) {

                                        Files.createDirectory(pathFolder);
                                        logger.info("Directory created");
                                    }
                                    //check directory
                                    Path path = Paths.get(outputLocation + "/" + sourceName + "/" + folderName + "/processed/");

                                    if (!Files.exists(path)) {

                                        Files.createDirectory(path);
                                        logger.info("Directory created");
                                    }
                                    //year
                                    Path pathYear = Paths.get(outputLocation + "/" + sourceName + "/" + folderName + "/processed/" + year);

                                    if (!Files.exists(pathYear)) {

                                        Files.createDirectory(pathYear);
                                        logger.info("Directory created");
                                    }
                                    //month
                                    Path pathMonth = Paths.get(outputLocation + "/" + sourceName + "/" + folderName + "/processed/" + year + "/" + month);

                                    if (!Files.exists(pathMonth)) {

                                        Files.createDirectory(pathMonth);
                                        logger.info("Directory created");
                                    }
                                    //day
                                    Path pathDay = Paths.get(outputLocation + "/" + sourceName + "/" + folderName + "/processed/" + year + "/" + month + "/" + day);

                                    if (!Files.exists(pathDay)) {

                                        Files.createDirectory(pathDay);
                                        logger.info("Directory created");
                                    }
                                    //rename file
                                    if (Files.exists(Paths.get(outputLocation + "/" + sourceName + "/" + folderName + "/processed/" + year + "/" + month + "/" + day + "/" + fileName))) {
                                        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                                        File sourceFile = new File(outputLocation + "/" + sourceName + "/" + folderName + "/processed/" + year + "/" + month + "/" + day + "/" + fileName);
                                        String newName = fileName + "-" + sdf.format(timestamp);
                                        File destFile = new File(outputLocation + "/" + sourceName + "/" + folderName + "/processed/" + year + "/" + month + "/" + day + "/" + newName);
                                        if (sourceFile.renameTo(destFile)) {
                                            logger.info("File renamed successfully");
                                        } else {
                                            logger.info("Failed to rename file");
                                        }
                                    }
                                    //move file
                                    Path temp = Files.move(Paths.get(inputLocation + "/" + sourceName + "/" + folderName + "/" + file.getName()), Paths.get(outputLocation + "/" + sourceName + "/" + folderName + "/processed/" + year + "/" + month + "/" + day + "/" + fileName));
                                    if (temp != null) {
                                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                                        LocalDateTime now = LocalDateTime.now();
                                        Instant endTime = Instant.now(offsetClock);
                                        timeTaken = Duration.between(startTime, endTime).toMillis();
                                        Float timeTakenF = ((float) timeTaken / 1000);
                                        if (timeTakenF == 0.0) {
                                            timeTakenF = (float) 0.001;
                                        }
                                        Tps = itotalCount / timeTakenF;
                                        logger.info("Input File Report");
                                        logger.info("FileName: " + fileName + ", Date: " + dtf.format(now) + ", Start Time: " + startTime + ", End Time: " + endTime + ", Time Taken: " + timeTakenF + ", Operator Name: " + sourceName + ", Source Name: " + folderName + ", TPS: " + Tps + ", Error: " + ierror + ", inSet: " + iinSet + ", totalCount: " + itotalCount + ", duplicate: " + iduplicate + ", volume: " + inputOffset + ", tag: " + tag);
                                        fileCount++;
                                        insertReportv2("I", fileName, itotalCount, ierror, iduplicate, iinSet, startTime.toString(), endTime.toString(), timeTakenF, Tps, sourceName, folderName, inputOffset, tag, 1, headCount);
                                        headCount = 0;
                                        ierror = 0;
                                        iinSet = 0;
                                        itotalCount = 0;
                                        iduplicate = 0;
                                        inputOffset = 0;
                                        logger.info("File renamed and moved successfully");
                                    } else {
                                        logger.info("Failed to move the file");
                                    }
                                    Instant end = Instant.now(offsetClock);
                                    Duration timeElapsed = Duration.between(start, end);
                                    logger.info("Difference in seconds: " + timeElapsed.toMillis());
                                    if (timeElapsed.toMillis() >= value) {
                                        makeCsv();
                                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                                        LocalDateTime now = LocalDateTime.now();
                                        Instant endTimeOutput = Instant.now(offsetClock);
                                        timeTaken = Duration.between(startTimeOutput, endTimeOutput).toMillis();
                                        Float timeTakenF = ((float) timeTaken / 1000);
                                        if (timeTakenF == 0.0) {
                                            timeTakenF = (float) 0.001;
                                        }
                                        Tps = totalCount / timeTakenF;
                                        logger.info("Output File Report");
                                        logger.info("FileName: " + fileName + ", Date: " + dtf.format(now) + ", Start Time: " + startTimeOutput + ", End Time: " + endTimeOutput + ", Time Taken: " + timeTakenF + ", Operator Name: " + sourceName + ", Source Name: " + folderName + ", TPS: " + Tps + ", Error: " + error + ", inSet: " + inSet + ", totalCount: " + totalCount + ", duplicate: " + duplicate + ", volume: " + outputOffset + ", tag: " + tag);
                                        insertReportv2("O", fileName, totalCount, error, duplicate, inSet, startTimeOutput.toString(), endTimeOutput.toString(), timeTakenF, Tps, sourceName, folderName, outputOffset, tag, fileCount, headCount);
                                        headCount = 0;
                                        error = 0;
                                        inSet = 0;
                                        totalCount = 0;
                                        duplicate = 0;
                                        outputOffset = 0;
                                        fileCount = 0;
                                        processed = 0;
                                        BookHashMap.clear();
                                        makeErrorCsv();
                                        logger.info("Output Error File Report");
                                        logger.info("FileName: " + fileName + ", Date: " + dtf.format(now) + ", Error: " + errorDuplicate + ", inFile: " + inErrorSet);
                                        errorDuplicate = 0;
                                        inErrorSet = 0;
                                        errorFile.clear();
                                        start = Instant.now(offsetClock);
                                        System.exit(0);
                                    }
                                } else {
                                    logger.info("No file or Incorrect file format present");
                                    processed++;
                                    //sleep 5sec
                                    //System.exit(0);
                                    //Thread.sleep(sleep);
                                    continue;
                                }
                            } else {
                                if (file.isFile() && file.getName().endsWith(extension)) {
                                    logger.info("Content: " + file.getName());
                                    fileName = file.getName();
                                    if ("sm_ims".equals(folderName)) {
                                        boolean check = readBooksFromCSV_ims(file.getName());
                                        if (!check) {
                                            processed++;
                                            move(fileName);
                                            continue;
                                        }
                                    } else {
                                        boolean check = readBooksFromCSV(file.getName());
                                        if (!check) {
                                            processed++;
                                            move(fileName);
                                            continue;
                                        }
                                    }
                                    //check source directory
                                    Path pathSource = Paths.get(outputLocation + "/" + sourceName);

                                    if (!Files.exists(pathSource)) {

                                        Files.createDirectory(pathSource);
                                        logger.info("Directory created");
                                    }
                                    //check folder
                                    Path pathFolder = Paths.get(outputLocation + "/" + sourceName + "/" + folderName);

                                    if (!Files.exists(pathFolder)) {

                                        Files.createDirectory(pathFolder);
                                        logger.info("Directory created");
                                    }
                                    //check directory
                                    Path path = Paths.get(outputLocation + "/" + sourceName + "/" + folderName + "/processed/");

                                    if (!Files.exists(path)) {

                                        Files.createDirectory(path);
                                        logger.info("Directory created");
                                    }
                                    //year
                                    Path pathYear = Paths.get(outputLocation + "/" + sourceName + "/" + folderName + "/processed/" + year);

                                    if (!Files.exists(pathYear)) {

                                        Files.createDirectory(pathYear);
                                        logger.info("Directory created");
                                    }
                                    //month
                                    Path pathMonth = Paths.get(outputLocation + "/" + sourceName + "/" + folderName + "/processed/" + year + "/" + month);

                                    if (!Files.exists(pathMonth)) {

                                        Files.createDirectory(pathMonth);
                                        logger.info("Directory created");
                                    }
                                    //day
                                    Path pathDay = Paths.get(outputLocation + "/" + sourceName + "/" + folderName + "/processed/" + year + "/" + month + "/" + day);

                                    if (!Files.exists(pathDay)) {

                                        Files.createDirectory(pathDay);
                                        logger.info("Directory created");
                                    }
                                    //rename file
                                    if (Files.exists(Paths.get(outputLocation + "/" + sourceName + "/" + folderName + "/processed/" + year + "/" + month + "/" + day + "/" + fileName))) {
                                        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                                        File sourceFile = new File(outputLocation + "/" + sourceName + "/" + folderName + "/processed/" + year + "/" + month + "/" + day + "/" + fileName);
                                        String newName = fileName + "-" + sdf.format(timestamp);
                                        File destFile = new File(outputLocation + "/" + sourceName + "/" + folderName + "/processed/" + year + "/" + month + "/" + day + "/" + newName);
                                        if (sourceFile.renameTo(destFile)) {
                                            logger.info("File renamed successfully");
                                        } else {
                                            logger.info("Failed to rename file");
                                        }
                                    }
                                    //move file
                                    Path temp = Files.move(Paths.get(inputLocation + "/" + sourceName + "/" + folderName + "/" + file.getName()), Paths.get(outputLocation + "/" + sourceName + "/" + folderName + "/processed/" + year + "/" + month + "/" + day + "/" + fileName));
                                    if (temp != null) {
                                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                                        LocalDateTime now = LocalDateTime.now();
                                        Instant endTime = Instant.now(offsetClock);
                                        timeTaken = Duration.between(startTime, endTime).toMillis();
                                        Float timeTakenF = ((float) timeTaken / 1000);
                                        if (timeTakenF == 0.0) {
                                            timeTakenF = (float) 0.001;
                                        }
                                        Tps = itotalCount / timeTakenF;
                                        logger.info("Input File Report");
                                        logger.info("FileName: " + fileName + ", Date: " + dtf.format(now) + ", Start Time: " + startTime + ", End Time: " + endTime + ", Time Taken: " + timeTakenF + ", Operator Name: " + sourceName + ", Source Name: " + folderName + ", TPS: " + Tps + ", Error: " + ierror + ", inSet: " + iinSet + ", totalCount: " + itotalCount + ", duplicate: " + iduplicate + ", volume: " + inputOffset + ", tag: " + tag);
                                        fileCount++;
                                        insertReportv2("I", fileName, itotalCount, ierror, iduplicate, iinSet, startTime.toString(), endTime.toString(), timeTakenF, Tps, sourceName, folderName, inputOffset, tag, 1, headCount);
                                        headCount = 0;
                                        ierror = 0;
                                        iinSet = 0;
                                        itotalCount = 0;
                                        iduplicate = 0;
                                        inputOffset = 0;
                                        logger.info("File renamed and moved successfully");
                                    } else {
                                        logger.info("Failed to move the file");
                                    }
                                    Instant end = Instant.now(offsetClock);
                                    Duration timeElapsed = Duration.between(start, end);
                                    logger.info("Difference in seconds: " + timeElapsed.toMillis());
                                    if (timeElapsed.toMillis() >= value) {
                                        makeCsv();
                                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                                        LocalDateTime now = LocalDateTime.now();
                                        Instant endTimeOutput = Instant.now(offsetClock);
                                        timeTaken = Duration.between(startTimeOutput, endTimeOutput).toMillis();
                                        Float timeTakenF = ((float) timeTaken / 1000);
                                        if (timeTakenF == 0.0) {
                                            timeTakenF = (float) 0.001;
                                        }
                                        Tps = totalCount / timeTakenF;
                                        logger.info("Output File Report");
                                        logger.info("FileName: " + fileName + ", Date: " + dtf.format(now) + ", Start Time: " + startTimeOutput + ", End Time: " + endTimeOutput + ", Time Taken: " + timeTakenF + ", Operator Name: " + sourceName + ", Source Name: " + folderName + ", TPS: " + Tps + ", Error: " + error + ", inSet: " + inSet + ", totalCount: " + totalCount + ", duplicate: " + duplicate + ", volume: " + outputOffset + ", tag: " + tag);
                                        insertReportv2("O", fileName, totalCount, error, duplicate, inSet, startTimeOutput.toString(), endTimeOutput.toString(), timeTakenF, Tps, sourceName, folderName, outputOffset, tag, fileCount, headCount);
                                        headCount = 0;
                                        error = 0;
                                        inSet = 0;
                                        totalCount = 0;
                                        duplicate = 0;
                                        outputOffset = 0;
                                        fileCount = 0;
                                        processed = 0;
                                        BookHashMap.clear();
                                        makeErrorCsv();
                                        logger.info("Output Error File Report");
                                        logger.info("FileName: " + fileName + ", Date: " + dtf.format(now) + ", Error: " + errorDuplicate + ", inFile: " + inErrorSet);
                                        errorDuplicate = 0;
                                        inErrorSet = 0;
                                        errorFile.clear();
                                        start = Instant.now(offsetClock);
                                        System.exit(0);
                                    }
                                } else {
                                    logger.info("No file or Incorrect file format present");
                                    processed++;
                                    //sleep 5sec
                                    //System.exit(0);
                                    //Thread.sleep(sleep);
                                    continue;
                                }
                            }

                        }
                    }
                    Instant end = Instant.now(offsetClock);
                    Duration timeElapsed = Duration.between(start, end);
                    logger.info("Difference in seconds: " + timeElapsed.toMillis());
                    if (timeElapsed.toMillis() >= value) {
                        makeCsv();
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                        LocalDateTime now = LocalDateTime.now();
                        Instant endTimeOutput = Instant.now(offsetClock);
                        timeTaken = Duration.between(startTimeOutput, endTimeOutput).toMillis();
                        Float timeTakenF = ((float) timeTaken / 1000);
                        if (timeTakenF == 0.0) {
                            timeTakenF = (float) 0.001;
                        }
                        Tps = totalCount / timeTakenF;
                        logger.info("Output File Report");
                        logger.info("FileName: " + fileName + ", Date: " + dtf.format(now) + ", Start Time: " + startTimeOutput + ", End Time: " + endTimeOutput + ", Time Taken: " + timeTakenF + ", Operator Name: " + sourceName + ", Source Name: " + folderName + ", TPS: " + Tps + ", Error: " + error + ", inSet: " + inSet + ", totalCount: " + totalCount + ", duplicate: " + duplicate + ", volume: " + outputOffset + ", tag: " + tag);
                        insertReportv2("O", fileName, totalCount, error, duplicate, inSet, startTimeOutput.toString(), endTimeOutput.toString(), timeTakenF, Tps, sourceName, folderName, outputOffset, tag, fileCount, headCount);
                        headCount = 0;
                        error = 0;
                        inSet = 0;
                        totalCount = 0;
                        duplicate = 0;
                        outputOffset = 0;
                        fileCount = 0;
                        processed = 0;
                        BookHashMap.clear();
                        makeErrorCsv();
                        logger.info("Output Error File Report");
                        logger.info("FileName: " + fileName + ", Date: " + dtf.format(now) + ", Error: " + errorDuplicate + ", inFile: " + inErrorSet);
                        errorDuplicate = 0;
                        inErrorSet = 0;
                        errorFile.clear();
                        start = Instant.now(offsetClock);
                        System.exit(0);
                    }
                }
            }
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionDetails = sw.toString();
            logger.info("Alert " + file.getName() + " , Details : " + e.toString() + " || " + exceptionDetails + "");
            Map<String, String> placeholderMapForAlert = new HashMap<String, String>();
            placeholderMapForAlert.put("<e>", e.toString());
            placeholderMapForAlert.put("<process_name>", "CDR_pre_processor");
            raiseAlert(Alerts.ALERT_006, placeholderMapForAlert, 0);
            logger.info("Alert [ALERT_006] is raised. So, doing nothing.");
        }
    }

    private static boolean readBooksFromCSV(String fileName) {
        Path pathToFile = Paths.get(inputLocation + "/" + sourceName + "/" + folderName + "/" + fileName);
        String line = null;
        try {
            logger.info("File With Path  : " + pathToFile);
            eventTime = getEventTime(fileName);
            String folder_name = "";
            String file_name = "";
            String event_time = "";
            BufferedReader br = Files.newBufferedReader(pathToFile, StandardCharsets.US_ASCII);
            if (folderName.equals("all")) {
                br.readLine();
                headCount++;
            }
            line = br.readLine();
            while (line != null) {
//                    logger.info("Record : " + line);
                inputOffset += line.getBytes(StandardCharsets.US_ASCII).length + 1; //1 is for line separator
                String[] attributes = line.split(propertiesReader.commaDelimiter, -1);
                if (folderName.equals("all")) {
                    folder_name = attributes[5];
                    file_name = attributes[6];
                    event_time = attributes[7];
                } else {
                    folder_name = folderName;
                    file_name = fileName;
                    event_time = eventTime;
                }
                if (attributes[0].equals("IMEI")) {
                    headCount++;
                    line = br.readLine();
                    continue;
                } else if (attributes[0].equals("") || attributes[1].equals("") || attributes[2].equals("") || (attributes[0].length() < 14) || !attributes[0].matches("^[0-9]+$")) {
//                         logger.info("inside Error dpt");
                    if ("1".equals(errorFlag)) {
                        Book bookError = createBook(attributes, folder_name, file_name, event_time);
                        if (errorFile.contains(bookError)) {
                            errorDuplicate++;
                        } else {
                            inErrorSet++;
                            errorFile.add(bookError);
                        }
                    }
                    line = br.readLine();
                    error++;
                    totalCount++;
                    ierror++;
                    itotalCount++;
                    continue;
                }
                if (!set.isEmpty()) {
                    if (!set.contains(attributes[3])) {
                        line = br.readLine();
                        error++;
                        totalCount++;
                        ierror++;
                        itotalCount++;
                        continue;
                    }
                }
                Book book = createBook(attributes, folder_name, file_name, event_time);
                if (BookHashMap.containsKey(book.getIMEI().substring(0, 14))) {
//                         logger.info(" inside  main Dpt");
                    if (!BookHashMap.get(book.getIMEI().substring(0, 14)).containsKey(book.getMSISDN())) {
                        BookHashMap.get(book.getIMEI().substring(0, 14)).put(book.getMSISDN(), book);
                        inSet++;
                        iinSet++;
                        outputOffset += line.getBytes(StandardCharsets.US_ASCII).length + 1; //1 is for line separator
                    } else {
                        duplicate++;
                        iduplicate++;
                    }
                } else {
                    HashMap<String, Book> bookMap = new HashMap<>();
                    bookMap.put(book.getMSISDN(), book);
                    BookHashMap.put(book.getIMEI().substring(0, 14), bookMap);
//                         logger.info("If no imei then object: " + book);
                    inSet++;
                    iinSet++;
                    outputOffset += line.getBytes(StandardCharsets.US_ASCII).length + 1; //1 is for line separator
                }
                line = br.readLine();
                totalCount++;
                itotalCount++;
            }
            br.close();
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionDetails = sw.toString();
            logger.info("Alert " + e.toString() + " || " + exceptionDetails);
            logger.info("Alert in  " + line);
            Map<String, String> placeholderMapForAlert = new HashMap<String, String>();
            placeholderMapForAlert.put("<e>", e.toString() + "; Wrong File Format ");
            placeholderMapForAlert.put("<process_name>", "CDR_pre_processor");
            raiseAlert(Alerts.ALERT_006, placeholderMapForAlert, 0);

            return false;
        }
        return true;
    }

    private static boolean readBooksFromCSV_ims(String fileName) {
        eventTime = getEventTime(fileName);
        String imei = "";
        String imsi = "";
        String msisdn = "";
        String systemType = "";
        String recordType = "";
        Path pathToFile = Paths.get(inputLocation + "/" + sourceName + "/" + folderName + "/" + fileName);
        try {
            BufferedReader br = Files.newBufferedReader(pathToFile, StandardCharsets.US_ASCII);
            String line = br.readLine();
            while (line != null) {
                itotalCount++;  // dec
                totalCount++;  //dec
                inputOffset += line.getBytes(StandardCharsets.US_ASCII).length + 1; //1 is for line separator
                String[] attributes = line.split(propertiesReader.commaDelimiter, -1);
                if (attributes[0].equalsIgnoreCase("role-of-Node")) {
                    line = br.readLine();
                    headCount++;
                    continue;
                }
                if (attributes[1].equalsIgnoreCase("IMEI")) {
                    imei = attributes[2].replaceAll("-", "").substring(0, 14);
                    String imsiTemp = attributes[3].toLowerCase();
                    if (imsiTemp.contains("imsi")) {
                        imsi = attributes[4];
                    }
                    if ("6".equals(attributes[9])) {
                        String tempMsisdn = attributes[10].replace("tel:+", "");
                        msisdn = tempMsisdn;
                    } else {
                        if ("0".equals(attributes[0])) {
                            String tempMsisdn = attributes[5].replace("tel:+", "");
                            msisdn = tempMsisdn;
                        } else if ("1".equals(attributes[0])) {
                            String tempMsisdn = attributes[6].replace("tel:+", "");
                            msisdn = tempMsisdn;
                        }
                    }
                    String[] systemTypeTemp = attributes[7].split(propertiesReader.semiColonDelimiter, -1);
                    systemType = systemTypeTemp[0];
                    if ("0".equals(attributes[0]) && ("INVITE".equals(attributes[8]) || "BYE".equals(attributes[8]))) {
                        recordType = "0";
                    } else if ("1".equals(attributes[0]) && ("INVITE".equals(attributes[8]) || "BYE".equals(attributes[8]))) {
                        recordType = "1";
                    } else if ("0".equals(attributes[0]) && "MESSAGE".equals(attributes[8])) {
                        recordType = "6";
                    } else if ("1".equals(attributes[0]) && "MESSAGE".equals(attributes[8])) {
                        recordType = "7";
                    } else {
                        recordType = "100";
                    }
//                         totalCount++;
//                         itotalCount++;
                    Book book = createBookIms(imei, imsi, msisdn, systemType, recordType, folderName, fileName, eventTime);
                    //error log
                    if (imei.equals("") || imsi.equals("") || msisdn.equals("")) {
                        if ("1".equals(errorFlag)) {
                            Book bookError = createBookIms(imei, imsi, msisdn, systemType, recordType, folderName, fileName, eventTime);
                            if (errorFile.contains(bookError)) {
                                errorDuplicate++;
                            } else {
                                inErrorSet++;
                                errorFile.add(bookError);
                            }
                        }
                        line = br.readLine();
                        error++;
//                              totalCount++;
                        ierror++;
//                              itotalCount++;
                        continue;
                    }
                    if (BookHashMap.containsKey(book.getIMEI())) {
                        if (!BookHashMap.get(book.getIMEI()).containsKey(book.getMSISDN())) {
                            BookHashMap.get(book.getIMEI()).put(book.getMSISDN(), book);
                            inSet++;
                            iinSet++;
                            outputOffset += line.getBytes(StandardCharsets.US_ASCII).length + 1; //1 is for line separator
                        } else {
                            duplicate++;
                            iduplicate++;
                        }
                    } else {
                        HashMap<String, Book> bookMap = new HashMap<>();
                        bookMap.put(book.getMSISDN(), book);
                        BookHashMap.put(book.getIMEI(), bookMap);
                        inSet++;
                        iinSet++;
                        outputOffset += line.getBytes(StandardCharsets.US_ASCII).length + 1; //1 is for line separator
                    }
                    line = br.readLine();
                    //continue;
                } else {
                    line = br.readLine();
                    error++;
                    ierror++;
//                         totalCount++;
//                         itotalCount++;
                }
            }
            br.close();
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionDetails = sw.toString();
            logger.info("Alert " + e.toString() + " || " + exceptionDetails);
            Map<String, String> placeholderMapForAlert = new HashMap<String, String>();
            placeholderMapForAlert.put("<e>", e.toString() + "; Wrong File Format");
            placeholderMapForAlert.put("<process_name>", "CDR_pre_processor");
            raiseAlert(Alerts.ALERT_006, placeholderMapForAlert, 0);
            logger.info("Alert [ALERT_006] is raised. So, doing nothing.");
            return false;
        }
        return true;
    }

    public static void makeCsv() {
        FileWriter fileWriter = null;
        Integer i = 1;
        try {
            //check directory
            Path path = Paths.get(outputLocation + "/" + sourceName + "/" + folderName);

            if (!Files.exists(path)) {

                Files.createDirectory(path);
                logger.info("Directory created");
            }
            //check folder
            Path pathFolder = Paths.get(outputLocation + "/" + sourceName + "/" + folderName + "/" + "output/");
            if (!Files.exists(pathFolder)) {
                Files.createDirectory(pathFolder);
                logger.info("Directory created");
            }
            //rename file
            if (Files.exists(Paths.get(outputLocation + "/" + sourceName + "/" + folderName + "/" + "output/" + fileName))) {
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                File sourceFile = new File(outputLocation + "/" + sourceName + "/" + folderName + "/" + "output/" + fileName);
                String newName = fileName + "-" + sdf.format(timestamp);
                File destFile = new File(outputLocation + "/" + sourceName + "/" + folderName + "/" + "output/" + newName);
                if (sourceFile.renameTo(destFile)) {
                    logger.info("File renamed successfully");
                } else {
                    logger.info("Failed to rename file");
                }
            }
            if (returnCount == 0) {
                logger.info("inside non split block");
                fileWriter = new FileWriter(outputLocation + "/" + sourceName + "/" + folderName + "/" + "output/" + fileName);
                fileWriter.append(propertiesReader.fileHeader);
                fileWriter.append(propertiesReader.newLineSeprator);
                for (HashMap.Entry<String, HashMap<String, Book>> csvf : BookHashMap.entrySet()) {
                    //String levelOne = csvf.getKey();
                    for (HashMap.Entry<String, Book> csvf3 : csvf.getValue().entrySet()) {
                        //String levelTwo = csvf2.getKey();
                        fileWriter.append(String.valueOf(csvf3.getValue().getIMEI()));
                        fileWriter.append(propertiesReader.commaDelimiter);
                        fileWriter.append(String.valueOf(csvf3.getValue().getIMSI()));
                        fileWriter.append(propertiesReader.commaDelimiter);
                        fileWriter.append(String.valueOf(csvf3.getValue().getMSISDN()));
                        fileWriter.append(propertiesReader.commaDelimiter);
                        fileWriter.append(String.valueOf(csvf3.getValue().getRecordType()));
                        fileWriter.append(propertiesReader.commaDelimiter);
                        fileWriter.append(String.valueOf(csvf3.getValue().getSystemType()));
                        fileWriter.append(propertiesReader.commaDelimiter);
                        fileWriter.append(String.valueOf(csvf3.getValue().getSourceName()));
                        fileWriter.append(propertiesReader.commaDelimiter);
                        fileWriter.append(String.valueOf(csvf3.getValue().getFileName()));
                        fileWriter.append(propertiesReader.commaDelimiter);
                        fileWriter.append(String.valueOf(csvf3.getValue().getEventTime()));
                        fileWriter.append(propertiesReader.newLineSeprator);
                        fileWriter.flush();
                    }
                }
            } else {
                logger.info("inside split block");
                Integer count = 0;
                fileWriter = new FileWriter(outputLocation + "/" + sourceName + "/" + folderName + "/" + "output/" + fileName);
                fileWriter.append(propertiesReader.fileHeader);
                fileWriter.append(propertiesReader.newLineSeprator);
                for (HashMap.Entry<String, HashMap<String, Book>> csvf : BookHashMap.entrySet()) {
                    //String levelOne = csvf.getKey();
                    for (HashMap.Entry<String, Book> csvf3 : csvf.getValue().entrySet()) {
                        //String levelTwo = csvf2.getKey();
                        if (count < returnCount) {
//                                   logger.info("count less than return count: " + count);
                            fileWriter.append(String.valueOf(csvf3.getValue().getIMEI()));
                            fileWriter.append(propertiesReader.commaDelimiter);
                            fileWriter.append(String.valueOf(csvf3.getValue().getIMSI()));
                            fileWriter.append(propertiesReader.commaDelimiter);
                            fileWriter.append(String.valueOf(csvf3.getValue().getMSISDN()));
                            fileWriter.append(propertiesReader.commaDelimiter);
                            fileWriter.append(String.valueOf(csvf3.getValue().getRecordType()));
                            fileWriter.append(propertiesReader.commaDelimiter);
                            fileWriter.append(String.valueOf(csvf3.getValue().getSystemType()));
                            fileWriter.append(propertiesReader.commaDelimiter);
                            fileWriter.append(String.valueOf(csvf3.getValue().getSourceName()));
                            fileWriter.append(propertiesReader.commaDelimiter);
                            fileWriter.append(String.valueOf(csvf3.getValue().getFileName()));
                            fileWriter.append(propertiesReader.commaDelimiter);
                            fileWriter.append(String.valueOf(csvf3.getValue().getEventTime()));
                            fileWriter.append(propertiesReader.newLineSeprator);
//                                   logger.info("Entry " + count + ": " + fileWriter + ": imei:" + String.valueOf(csvf3.getValue().getIMEI())
//                                           + ", imsi:" + String.valueOf(csvf3.getValue().getIMSI())
//                                           + ", msisdn:" + String.valueOf(csvf3.getValue().getMSISDN())
//                                           + ", recordtype:" + String.valueOf(csvf3.getValue().getRecordType())
//                                           + ", systemtype:" + String.valueOf(csvf3.getValue().getSystemType())
//                                           + ", sourceName:" + String.valueOf(csvf3.getValue().getSourceName())
//                                           + ", sourceName:" + String.valueOf(csvf3.getValue().getFileName())
//                                           + ", sourceName:" + String.valueOf(csvf3.getValue().getEventTime()));

                            count++;
                            fileWriter.flush();
                        } else {
//                                   logger.info("count greater than split count: " + count);
                            if (Files.exists(Paths.get(outputLocation + "/" + sourceName + "/" + folderName + "/" + "output/" + fileName))) {
                                File sourceFile = new File(outputLocation + "/" + sourceName + "/" + folderName + "/" + "output/" + fileName);
                                String newName = fileName + "_00" + i;
                                i++;
                                File destFile = new File(outputLocation + "/" + sourceName + "/" + folderName + "/" + "output/" + newName);
                                if (sourceFile.renameTo(destFile)) {
                                    logger.info("File split successfully: " + newName);
                                } else {
                                    logger.info("Failed to split file");
                                }
                            }
                            count = 0;
                            fileWriter = new FileWriter(outputLocation + "/" + sourceName + "/" + folderName + "/" + "output/" + fileName);
                            fileWriter.append(propertiesReader.fileHeader);
                            fileWriter.append(propertiesReader.newLineSeprator);
                            fileWriter.append(String.valueOf(csvf3.getValue().getIMEI()));
                            fileWriter.append(propertiesReader.commaDelimiter);
                            fileWriter.append(String.valueOf(csvf3.getValue().getIMSI()));
                            fileWriter.append(propertiesReader.commaDelimiter);
                            fileWriter.append(String.valueOf(csvf3.getValue().getMSISDN()));
                            fileWriter.append(propertiesReader.commaDelimiter);
                            fileWriter.append(String.valueOf(csvf3.getValue().getRecordType()));
                            fileWriter.append(propertiesReader.commaDelimiter);
                            fileWriter.append(String.valueOf(csvf3.getValue().getSystemType()));
                            fileWriter.append(propertiesReader.commaDelimiter);
                            fileWriter.append(String.valueOf(csvf3.getValue().getSourceName()));
                            fileWriter.append(propertiesReader.commaDelimiter);
                            fileWriter.append(String.valueOf(csvf3.getValue().getFileName()));
                            fileWriter.append(propertiesReader.commaDelimiter);
                            fileWriter.append(String.valueOf(csvf3.getValue().getEventTime()));
                            fileWriter.append(propertiesReader.newLineSeprator);
//                                   logger.info("Entry " + count + ": " + fileWriter + ": imei:" + String.valueOf(csvf3.getValue().getIMEI())
//                                           + ", imsi:" + String.valueOf(csvf3.getValue().getIMSI())
//                                           + ", msisdn:" + String.valueOf(csvf3.getValue().getMSISDN())
//                                           + ", recordtype:" + String.valueOf(csvf3.getValue().getRecordType())
//                                           + ", systemtype:" + String.valueOf(csvf3.getValue().getSystemType())
//                                           + ", sourceName:" + String.valueOf(csvf3.getValue().getSourceName())
//                                           + ", sourceName:" + String.valueOf(csvf3.getValue().getFileName())
//                                           + ", sourceName:" + String.valueOf(csvf3.getValue().getEventTime()));

                            count++;
                            fileWriter.flush();
                        }
                    }
                }
                if (Files.exists(Paths.get(outputLocation + "/" + sourceName + "/" + folderName + "/" + "output/" + fileName))) {
                    File sourceFile = new File(outputLocation + "/" + sourceName + "/" + folderName + "/" + "output/" + fileName);
                    String newName = fileName + "_00" + i;
                    i++;
                    File destFile = new File(outputLocation + "/" + sourceName + "/" + folderName + "/" + "output/" + newName);
                    if (sourceFile.renameTo(destFile)) {
                        logger.info("File split successfully: " + newName);
                    } else {
                        logger.info("Failed to split file");
                    }
                }
            }
            logger.info("CSV file was created successfully !!!");
        } catch (Exception e) {
            logger.info("Error in CsvFileWriter !!!");
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionDetails = sw.toString();
            logger.info("Alert " + e.toString() + " || " + exceptionDetails);
            Map<String, String> placeholderMapForAlert = new HashMap<String, String>();
            placeholderMapForAlert.put("<e>", e.toString());
            placeholderMapForAlert.put("<process_name>", "CDR_pre_processor");
            raiseAlert(Alerts.ALERT_006, placeholderMapForAlert, 0);
            logger.info("Alert [ALERT_006] is raised. So, doing nothing.");
        } finally {
            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                logger.info("Error while flushing/closing fileWriter !!!");
                Map<String, String> placeholderMapForAlert = new HashMap<String, String>();
                placeholderMapForAlert.put("<e>", e.toString());
                placeholderMapForAlert.put("<process_name>", "CDR_pre_processor");
                raiseAlert(Alerts.ALERT_006, placeholderMapForAlert, 0);
                logger.info("Alert [ALERT_006] is raised. So, doing nothing.");
            }
        }
    }

    public static void makeErrorCsv() {
        FileWriter fileWriter = null;
        try {
            //check directory
            Path path = Paths.get(outputLocation + "/" + sourceName + "/" + folderName);

            if (!Files.exists(path)) {

                Files.createDirectory(path);
                logger.info("Directory created");
            }
            //check folder
            Path pathFolder = Paths.get(outputLocation + "/" + sourceName + "/" + folderName + "/error/");

            if (!Files.exists(pathFolder)) {

                Files.createDirectory(pathFolder);
                logger.info("Directory created");
            }
            //year
            Path pathYear = Paths.get(outputLocation + "/" + sourceName + "/" + folderName + "/error/" + year);

            if (!Files.exists(pathYear)) {

                Files.createDirectory(pathYear);
                logger.info("Directory created");
            }
            //month
            Path pathMonth = Paths.get(outputLocation + "/" + sourceName + "/" + folderName + "/error/" + year + "/" + month);

            if (!Files.exists(pathMonth)) {

                Files.createDirectory(pathMonth);
                logger.info("Directory created");
            }
            //day
            Path pathDay = Paths.get(outputLocation + "/" + sourceName + "/" + folderName + "/error/" + year + "/" + month + "/" + day);

            if (!Files.exists(pathDay)) {

                Files.createDirectory(pathDay);
                logger.info("Directory created");
            }
            //rename file
            if (Files.exists(Paths.get(outputLocation + "/" + sourceName + "/" + folderName + "/error/" + year + "/" + month + "/" + day + "/" + fileName))) {
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                File sourceFile = new File(outputLocation + "/" + sourceName + "/" + folderName + "/error/" + year + "/" + month + "/" + day + "/" + fileName);
                String newName = fileName + "-" + sdf.format(timestamp);
                File destFile = new File(outputLocation + "/" + sourceName + "/" + folderName + "/error/" + year + "/" + month + "/" + day + "/" + newName);
                if (sourceFile.renameTo(destFile)) {
                    logger.info("File renamed successfully");
                } else {
                    logger.info("Failed to rename file");
                }
            }
            fileWriter = new FileWriter(outputLocation + "/" + sourceName + "/" + folderName + "/error/" + year + "/" + month + "/" + day + "/" + fileName);
            fileWriter.append(propertiesReader.fileHeader);
            fileWriter.append(propertiesReader.newLineSeprator);
            for (Book csvf : errorFile) {
                fileWriter.append(String.valueOf(csvf.getIMEI()));
                fileWriter.append(propertiesReader.commaDelimiter);
                fileWriter.append(String.valueOf(csvf.getIMSI()));
                fileWriter.append(propertiesReader.commaDelimiter);
                fileWriter.append(String.valueOf(csvf.getMSISDN()));
                fileWriter.append(propertiesReader.commaDelimiter);
                fileWriter.append(String.valueOf(csvf.getRecordType()));
                fileWriter.append(propertiesReader.commaDelimiter);
                fileWriter.append(String.valueOf(csvf.getSystemType()));
                fileWriter.append(propertiesReader.commaDelimiter);
                fileWriter.append(String.valueOf(csvf.getSourceName()));
                fileWriter.append(propertiesReader.commaDelimiter);
                fileWriter.append(String.valueOf(csvf.getFileName()));
                fileWriter.append(propertiesReader.commaDelimiter);
                fileWriter.append(String.valueOf(csvf.getEventTime()));
                fileWriter.append(propertiesReader.newLineSeprator);
                fileWriter.flush();
            }
            logger.info("CSV file was created successfully for Error File!!!");
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionDetails = sw.toString();
            logger.info("Alert " + e.toString() + " || " + exceptionDetails);
            logger.info("Error in CsvFileWriter for Error File!!!");
            Map<String, String> placeholderMapForAlert = new HashMap<String, String>();
            placeholderMapForAlert.put("<e>", e.toString());
            placeholderMapForAlert.put("<process_name>", "CDR_pre_processor");
            raiseAlert(Alerts.ALERT_006, placeholderMapForAlert, 0);
            logger.info("Alert [ALERT_006] is raised. So, doing nothing.");
        } finally {
            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                logger.info("Error while flushing/closing fileWriter for Error File!!!");
                Map<String, String> placeholderMapForAlert = new HashMap<String, String>();
                placeholderMapForAlert.put("<e>", e.toString());
                placeholderMapForAlert.put("<process_name>", "CDR_pre_processor");
                raiseAlert(Alerts.ALERT_006, placeholderMapForAlert, 0);
                logger.info("Alert [ALERT_006] is raised. So, doing nothing.");
            }
        }
    }

    private static Book createBook(String[] metadata, String source_name, String file_name, String event_time) {
//          String imei = metadata[0].substring(0, 14);
        String imei = metadata[0];
        String imsi = metadata[1];
//          String msisdn = metadata[2]     ;//Oct20
        String msisdn = ((metadata[2].trim().startsWith("19") || metadata[2].trim().startsWith("00")) ? metadata[2].substring(2) : metadata[2]).replace("1AO", "855");
        String recordType = metadata[3];
        String systemType = metadata[4];
        String sourceName = source_name;
        String fileName = file_name;
        String eventTime = event_time;
        return new Book(imei, imsi, msisdn, recordType, systemType, sourceName, fileName, eventTime);
    }

    private static Book createBookIms(String IMEI, String IMSI, String MSISDN, String system_type, String record_type, String source_name, String file_name, String event_time) {
        String imei = IMEI;
        String imsi = IMSI;
        String msisdn = MSISDN;
        String recordType = system_type;
        String systemType = record_type;
        String sourceName = source_name;
        String fileName = file_name;
        String eventTime = event_time;
        return new Book(imei, imsi, msisdn, recordType, systemType, sourceName, fileName, eventTime);
    }

    public static String getEventTime(String fileName) {
        pattern.addAll(propertiesReader.filePattern);
        if (pattern.contains("null")) {
            pattern = new ArrayList<String>();
        }
        for (String filePattern : pattern) {
            String[] attributes = filePattern.split("-", -1);
            if (fileName.contains(attributes[0])) {
                fileName = fileName.substring(fileName.indexOf(attributes[0]) + Integer.parseInt(attributes[1]), fileName.indexOf(attributes[0]) + Integer.parseInt(attributes[1]) + Integer.parseInt(attributes[2]));
                return fileName;
            }
        }
        return fileName;
    }

    public static void insertReportv2(String fileType, String fileName, Long totalRecords, Long totalErrorRecords, Long totalDuplicateRecords, Long totalOutputRecords, String startTime, String endTime, Float timeTaken, Float tps, String operatorName, String sourceName, long volume, String tag, Integer FileCount, Integer headCount) {
        Connection conn = null;
        Statement stmt = null;
        try {
            
            if (fileType.equalsIgnoreCase("O")) {
               headCount =  headCount+1;
            }
            conn = connectionConfiguration.getConnection();
            stmt = conn.createStatement();

            String sql = "Insert into cdr_pre_processing_report(CREATED_ON,MODIFIED_ON,FILE_TYPE,TOTAL_RECORDS,TOTAL_ERROR_RECORDS,TOTAL_DUPLICATE_RECORDS,TOTAL_OUTPUT_RECORDS,FILE_NAME,START_TIME,END_TIME,TIME_TAKEN,TPS,OPERATOR_NAME,SOURCE_NAME,VOLUME,TAG,FILE_COUNT , HEAD_COUNT ,servername )"
                    + "values(" + defaultNowDate() + "," + defaultNowDate() + ",'" + fileType + "'," + totalRecords + "," + totalErrorRecords + "," + totalDuplicateRecords + "," + totalOutputRecords + ",'" + fileName + "','" + startTime + "','" + endTime + "'," + timeTaken + "," + tps + ",'" + operatorName + "','" + sourceName + "'," + volume + ",'" + tag + "'," + FileCount + "  ," + headCount + " , '"+servername+"'    )";

            logger.info("Inserting into table cdr_pre_processing _report:: " + sql);
            stmt.executeQuery(sql);

        } catch (SQLException e) {
            //Handle errors for JDBC
            Map<String, String> placeholderMapForAlert = new HashMap<String, String>();
            placeholderMapForAlert.put("<e>", e.toString());
            placeholderMapForAlert.put("<process_name>", "CDR_pre_processor");
            raiseAlert(Alerts.ALERT_006, placeholderMapForAlert, 0);
            logger.info("Alert [ALERT_006] is raised. So, doing nothing.");
            System.exit(0);
        } catch (Exception e) {
            //Handle errors for Class.forName
            Map<String, String> placeholderMapForAlert = new HashMap<String, String>();
            placeholderMapForAlert.put("<e>", e.toString());
            placeholderMapForAlert.put("<process_name>", "CDR_pre_processor");
            raiseAlert(Alerts.ALERT_006, placeholderMapForAlert, 0);
            logger.info("Alert [ALERT_006] is raised. So, doing nothing.");
            System.exit(0);
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                Map<String, String> placeholderMapForAlert = new HashMap<String, String>();
                placeholderMapForAlert.put("<e>", e.toString());
                placeholderMapForAlert.put("<process_name>", "CDR_pre_processor");
                raiseAlert(Alerts.ALERT_006, placeholderMapForAlert, 0);
                logger.info("Alert [ALERT_006] is raised. So, doing nothing.");
                System.exit(0);
            }// do nothing
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                Map<String, String> placeholderMapForAlert = new HashMap<String, String>();
                placeholderMapForAlert.put("<e>", e.toString());
                placeholderMapForAlert.put("<process_name>", "CDR_pre_processor");
                raiseAlert(Alerts.ALERT_006, placeholderMapForAlert, 0);
                logger.info("Alert [ALERT_006] is raised. So, doing nothing.");
                System.exit(0);
            }//end finally try
        }//end try
    }

    public static String getAlertbyId(String alertId) {
        Connection conn = null;
        Statement stmt = null;
        String description = "";
        try {
            conn = connectionConfiguration.getConnection();
            stmt = conn.createStatement();

            String sql = "select description from alert_db where alert_id='" + alertId + "'";

            logger.info("Fetching alert message by alert id from alertDb");
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                description = rs.getString("description");
            }

        } catch (SQLException e) {
            //Handle errors for JDBC
            Map<String, String> placeholderMapForAlert = new HashMap<String, String>();
            placeholderMapForAlert.put("<e>", e.toString());
            placeholderMapForAlert.put("<process_name>", "CDR_pre_processor");
            raiseAlert(Alerts.ALERT_006, placeholderMapForAlert, 0);
            logger.info("Alert [ALERT_006] is raised. So, doing nothing.");
            System.exit(0);
        } catch (Exception e) {
            //Handle errors for Class.forName
            Map<String, String> placeholderMapForAlert = new HashMap<String, String>();
            placeholderMapForAlert.put("<e>", e.toString());
            placeholderMapForAlert.put("<process_name>", "CDR_pre_processor");
            raiseAlert(Alerts.ALERT_006, placeholderMapForAlert, 0);
            logger.info("Alert [ALERT_006] is raised. So, doing nothing.");
            System.exit(0);
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                Map<String, String> placeholderMapForAlert = new HashMap<String, String>();
                placeholderMapForAlert.put("<e>", e.toString());
                placeholderMapForAlert.put("<process_name>", "CDR_pre_processor");
                raiseAlert(Alerts.ALERT_006, placeholderMapForAlert, 0);
                logger.info("Alert [ALERT_006] is raised. So, doing nothing.");
                System.exit(0);
            }// do nothing
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                Map<String, String> placeholderMapForAlert = new HashMap<String, String>();
                placeholderMapForAlert.put("<e>", e.toString());
                placeholderMapForAlert.put("<process_name>", "CDR_pre_processor");
                raiseAlert(Alerts.ALERT_006, placeholderMapForAlert, 0);
                logger.info("Alert [ALERT_006] is raised. So, doing nothing.");
                System.exit(0);
            }//end finally try
        }//end try
        return description;
    }

    public static void raiseAlert(String alertId, Map<String, String> bodyPlaceHolderMap, Integer userId) {
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = connectionConfiguration.getConnection();
            stmt = conn.createStatement();
            String alertDescription = getAlertbyId(alertId);

            // Replace Placeholders from bodyPlaceHolderMap.
            if (Objects.nonNull(bodyPlaceHolderMap)) {
                for (Map.Entry<String, String> entry : bodyPlaceHolderMap.entrySet()) {
                    logger.info("Placeholder key : " + entry.getKey() + " value : " + entry.getValue());
                    alertDescription = alertDescription.replaceAll(entry.getKey(), entry.getValue());
                }
            }
            logger.info("alert message: " + alertDescription);
            String sql = "Insert into running_alert_db(alert_id,created_on,modified_on,description,status,user_id)"
                    + "values('" + alertId + "'," + defaultNowDate() + "," + defaultNowDate() + ",'" + alertDescription + "',0," + userId + ")";
            logger.info("Inserting alert into running alert db");

            stmt.executeQuery(sql);
        } catch (SQLException e) {
            //Handle errors for JDBC
            Map<String, String> placeholderMapForAlert = new HashMap<String, String>();
            placeholderMapForAlert.put("<e>", e.toString());
            placeholderMapForAlert.put("<process_name>", "CDR_pre_processor");
            raiseAlert(Alerts.ALERT_006, placeholderMapForAlert, 0);
            logger.info("Alert [ALERT_006] is raised. So, doing nothing.");
            System.exit(0);
        } catch (Exception e) {
            //Handle errors for Class.forName
            Map<String, String> placeholderMapForAlert = new HashMap<String, String>();
            placeholderMapForAlert.put("<e>", e.toString());
            placeholderMapForAlert.put("<process_name>", "CDR_pre_processor");
            raiseAlert(Alerts.ALERT_006, placeholderMapForAlert, 0);
            logger.info("Alert [ALERT_006] is raised. So, doing nothing.");
            System.exit(0);
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                Map<String, String> placeholderMapForAlert = new HashMap<String, String>();
                placeholderMapForAlert.put("<e>", e.toString());
                placeholderMapForAlert.put("<process_name>", "CDR_pre_processor");
                raiseAlert(Alerts.ALERT_006, placeholderMapForAlert, 0);
                logger.info("Alert [ALERT_006] is raised. So, doing nothing.");
                System.exit(0);
            }// do nothing
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                Map<String, String> placeholderMapForAlert = new HashMap<String, String>();
                placeholderMapForAlert.put("<e>", e.toString());
                placeholderMapForAlert.put("<process_name>", "CDR_pre_processor");
                raiseAlert(Alerts.ALERT_006, placeholderMapForAlert, 0);
                logger.info("Alert [ALERT_006] is raised. So, doing nothing.");
                System.exit(0);
            }//end finally try
        }//end try
    }

    public static String defaultNowDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssSSSSS");
        String val = sdf.format(new Date());
        String date = "to_timestamp('" + val + "','YYYY-MM-DD HH24:MI:SSFF5')";
        return date;
    }

    public static void move(String fileName) throws IOException {
        //check source directory
        Path pathSource = Paths.get(outputLocation + "/" + sourceName);

        if (!Files.exists(pathSource)) {

            Files.createDirectory(pathSource);
            logger.info("Directory created");
        }
        //check folder
        Path pathFolder = Paths.get(outputLocation + "/" + sourceName + "/" + folderName);

        if (!Files.exists(pathFolder)) {

            Files.createDirectory(pathFolder);
            logger.info("Directory created");
        }
        //check directory
        Path path = Paths.get(outputLocation + "/" + sourceName + "/" + folderName + "/error/");

        if (!Files.exists(path)) {

            Files.createDirectory(path);
            logger.info("Directory created");
        }
        //year
        Path pathYear = Paths.get(outputLocation + "/" + sourceName + "/" + folderName + "/error/" + year);

        if (!Files.exists(pathYear)) {

            Files.createDirectory(pathYear);
            logger.info("Directory created");
        }
        //month
        Path pathMonth = Paths.get(outputLocation + "/" + sourceName + "/" + folderName + "/error/" + year + "/" + month);

        if (!Files.exists(pathMonth)) {

            Files.createDirectory(pathMonth);
            logger.info("Directory created");
        }
        //day
        Path pathDay = Paths.get(outputLocation + "/" + sourceName + "/" + folderName + "/error/" + year + "/" + month + "/" + day);

        if (!Files.exists(pathDay)) {

            Files.createDirectory(pathDay);
            logger.info("Directory created");
        }
        //file
        Path pathFile = Paths.get(outputLocation + "/" + sourceName + "/" + folderName + "/error/" + year + "/" + month + "/" + day + "/errorFile");

        if (!Files.exists(pathFile)) {

            Files.createDirectory(pathFile);
            logger.info("Directory created");
        }
        //rename file
        if (Files.exists(Paths.get(outputLocation + "/" + sourceName + "/" + folderName + "/error/" + year + "/" + month + "/" + day + "/errorFile/" + fileName))) {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            File sourceFile = new File(outputLocation + "/" + sourceName + "/" + folderName + "/error/" + year + "/" + month + "/" + day + "/errorFile/" + fileName);
            String newName = fileName + "-" + sdf.format(timestamp);
            File destFile = new File(outputLocation + "/" + sourceName + "/" + folderName + "/error/" + year + "/" + month + "/" + day + "/errorFile/" + newName);
            if (sourceFile.renameTo(destFile)) {
                logger.info("File renamed successfully");
            } else {
                logger.info("Failed to rename file");
            }
        }
        //move file
        Path temp = Files.move(Paths.get(inputLocation + "/" + sourceName + "/" + folderName + "/" + fileName), Paths.get(outputLocation + "/" + sourceName + "/" + folderName + "/error/" + year + "/" + month + "/" + day + "/errorFile/" + fileName));
        if (temp != null) {
            logger.info("File moved in Error Folder successfully");
        } else {
            logger.info("Failed to move the file in Error Folder");
        }
    }
}
