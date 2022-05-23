package com.kchonov.springdocker.service;

import com.kchonov.springdocker.entity.Merchant;
import com.kchonov.springdocker.entity.constants.Messages;
import com.kchonov.springdocker.messages.ResponseMessage;
import com.kchonov.springdocker.repository.MerchantRepository;
import com.kchonov.springdocker.utils.CSVHelper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Krasi
 */
public class MerchantServiceTest {

    private MerchantRepository merchantRepository = Mockito.mock(MerchantRepository.class);

    private MerchantService merchantService;

    private List<Merchant> merchants;

    public MerchantServiceTest() {
    }

    @BeforeEach
    public void setUp() {
        merchantService = new MerchantService(merchantRepository);

        merchants = new ArrayList<>();
        merchants.add(new Merchant(1L, "John", "", "test1@test.com", "active", 100l, new Date(1653111490)));
        merchants.add(new Merchant(2L, "Alex", "", "test2@test.com", "active", 100l, new Date(1653111490)));
        merchants.add(new Merchant(3L, "Sandra", "", "test3@test.com", "active", 100l, new Date(1653111490)));
    }

    /**
     * Test of findAll method, of class MerchantService.
     */
    @Test
    public void testFindAll() {
        when(merchantRepository.findAll()).thenReturn(merchants);

        List<Merchant> result = merchantService.findAll();
        assertEquals(merchants, result);

    }

    /**
     * Test of insertOne method, of class MerchantService.
     */
    @Test
    public void testInsertOne() {
        Merchant newMerchant = merchants.get(0);

        when(merchantRepository.save(any(Merchant.class))).thenReturn(null);

        merchantService.insertOne(newMerchant);
        verify(merchantRepository, times(1)).save(Mockito.eq(newMerchant));
    }

    /**
     * Test of insertAll method, of class MerchantService.
     */
    @Test
    public void testInsertAll() {
        when(merchantRepository.saveAll(any(List.class))).thenReturn(null);

        merchantService.insertAll(merchants);
        verify(merchantRepository, times(1)).saveAll(Mockito.eq(merchants));
    }

    /**
     * Test of ImportMerchants method, of class MerchantService in successful scenario.
     */
    @Test
    public void testImportMerchantsSuccessfulImport() {
        MultipartFile file = Mockito.mock(MultipartFile.class);
        try ( MockedStatic<CSVHelper> csvHelper = Mockito.mockStatic(CSVHelper.class)) {
            csvHelper.when(() -> CSVHelper.hasCSVFormat(file)).thenReturn(true);
            csvHelper.when(() -> CSVHelper.csvToMerchants(file.getInputStream())).thenReturn(merchants);
            when(merchantRepository.saveAll(any(List.class))).thenReturn(null);
            
            assertThat(CSVHelper.hasCSVFormat(file)).isEqualTo(true);
            assertThat(CSVHelper.csvToMerchants(file.getInputStream())).isEqualTo(merchants);
            
            ResponseEntity<ResponseMessage> response = merchantService.importMerchants(file);
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
            assertThat(response.getBody().getMessage()).isEqualTo(Messages.FILE_UPLOAD_SUCCESSFUL);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    /**
     * Test of ImportMerchants method, of class MerchantService in failed scenario.
     */
    @Test
    public void testImportMerchantsFailedImport() {
        MultipartFile file = Mockito.mock(MultipartFile.class);
        final String filename = "test.csv";
        try ( MockedStatic<CSVHelper> csvHelper = Mockito.mockStatic(CSVHelper.class)) {
            csvHelper.when(() -> CSVHelper.hasCSVFormat(file)).thenReturn(true);
            csvHelper.when(() -> CSVHelper.csvToMerchants(file.getInputStream())).thenThrow(new IOException());
            when(file.getOriginalFilename()).thenReturn(filename);
            
            assertThat(CSVHelper.hasCSVFormat(file)).isEqualTo(true);
            
            ResponseEntity<ResponseMessage> response = merchantService.importMerchants(file);
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.EXPECTATION_FAILED);
            assertThat(response.getBody().getMessage()).isEqualTo(Messages.FILE_UPLOAD_ERROR(filename));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    /**
     * Test of ImportMerchants method, of class MerchantService in not a CSV file scenario.
     */
    @Test
    public void testImportMerchantsNotACsvFile() {
        MultipartFile file = Mockito.mock(MultipartFile.class);
        try ( MockedStatic<CSVHelper> csvHelper = Mockito.mockStatic(CSVHelper.class)) {
            csvHelper.when(() -> CSVHelper.hasCSVFormat(file)).thenReturn(false);
            
            assertThat(CSVHelper.hasCSVFormat(file)).isEqualTo(false);
            
            ResponseEntity<ResponseMessage> response = merchantService.importMerchants(file);
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(response.getBody().getMessage()).isEqualTo(Messages.FILE_NOT_A_CSV);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

}
