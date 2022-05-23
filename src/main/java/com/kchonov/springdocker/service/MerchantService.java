package com.kchonov.springdocker.service;

import com.kchonov.springdocker.cron.ICleanup;
import com.kchonov.springdocker.entity.Merchant;
import com.kchonov.springdocker.entity.constants.Messages;
import com.kchonov.springdocker.messages.ResponseMessage;
import com.kchonov.springdocker.repository.MerchantRepository;
import com.kchonov.springdocker.utils.CSVHelper;
import com.kchonov.springdocker.utils.Utilities;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class MerchantService implements ICleanup {

    Logger logger = LoggerFactory.getLogger(MerchantService.class);

    private final MerchantRepository merchantRepository;

    public List<Merchant> findAll() {
        return merchantRepository.findAll();
    }

    public void insertOne(Merchant newMerchant) {
        merchantRepository.save(newMerchant);
    }

    @Transactional
    public void insertAll(List<Merchant> transactions) {
        merchantRepository.saveAll(transactions);
    }

    @Modifying
    @Transactional
    public ResponseEntity<ResponseMessage> importMerchants(MultipartFile file) {
        logger.info("Begin file upload: " + file.getOriginalFilename());
        String message;
        if (CSVHelper.hasCSVFormat(file)) {
            try {
                List<Merchant> merchants = CSVHelper.csvToMerchants(file.getInputStream());
                merchantRepository.saveAll(merchants);
                message = Messages.FILE_UPLOAD_SUCCESSFUL;
                logger.info(message);
                return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseMessage(message));
            } catch (Exception e) {
                logger.error("Error occured during file upload");
                message = Messages.FILE_UPLOAD_ERROR(file.getOriginalFilename()); // "Could not upload the file: " + file.getOriginalFilename() + "!";
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
            }
        }
        message = Messages.FILE_NOT_A_CSV;
        logger.error(message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
    }

    @Override
    @Modifying
    @Transactional
    public void clean(long timestamp) {
        merchantRepository.deleteByTimestampCreatedLessThan(Utilities.fixTimezone(timestamp));
    }
}
