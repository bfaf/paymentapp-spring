package com.kchonov.springdocker.entity.constants;

/**
 *
 * @author Krasi
 */
public class Constants {
    public enum TransactionType {
        AUTHORIZE("authorize"),
        CHARGE("charge"),
        REFUND("refund"),
        REVERSAL("reversal"),
        ERROR("error");
        
        private String value;    

        private TransactionType(String value) {
          this.value = value;
        }

        public String getValue() {
          return value;
        }
    }
    
    public enum TransactionStatusType {
        APPROVED("approved"),
        REVERSED("reversed"),
        REFUNDED("refunded"),
        ERROR("error");
        
        private String value;    

        private TransactionStatusType(String value) {
          this.value = value;
        }

        public String getValue() {
          return value;
        }
    }
}
