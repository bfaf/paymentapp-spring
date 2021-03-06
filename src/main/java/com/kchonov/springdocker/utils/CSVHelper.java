package com.kchonov.springdocker.utils;

import com.kchonov.springdocker.entity.Merchant;
import com.kchonov.springdocker.entity.Transaction;
import com.kchonov.springdocker.entity.User;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Krasi
 */
public class CSVHelper {

    public static String TYPE = "text/csv";

    public static boolean hasCSVFormat(MultipartFile file) {
        return TYPE.equals(file.getContentType());
    }

    public static List<Merchant> csvToMerchants(InputStream is) {
        try ( BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));  CSVParser csvParser = new CSVParser(fileReader,
                CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {
            List<Merchant> merchants = new ArrayList<>();
            Iterable<CSVRecord> csvRecords = csvParser.getRecords();
            for (CSVRecord csvRecord : csvRecords) {
                Merchant merchant = new Merchant(
                        null,
                        csvRecord.get("name"),
                        csvRecord.get("description"),
                        csvRecord.get("email"),
                        csvRecord.get("status"),
                        Long.parseLong(csvRecord.get("total_transaction_sum")),
                        null
                );
                merchants.add(merchant);
            }
            return merchants;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
        }
    }
    
    public static List<User> csvToUsers(InputStream is) {
        try ( BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));  CSVParser csvParser = new CSVParser(fileReader,
                CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {
            List<User> users = new ArrayList<>();
            Iterable<CSVRecord> csvRecords = csvParser.getRecords();
            for (CSVRecord csvRecord : csvRecords) {
                User user = new User(
                        csvRecord.get("username"),
                        csvRecord.get("password"),
                        Integer.parseInt(csvRecord.get("enabled")),
                        csvRecord.get("role")
                );
                users.add(user);
            }
            return users;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
        }
    }
}
