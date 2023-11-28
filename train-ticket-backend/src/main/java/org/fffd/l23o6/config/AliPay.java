package org.fffd.l23o6.config;

import lombok.Data;


@Data
public class AliPay {
    private String traceNo;
    private double totalAmount;
    private String subject;
    private String alipayTraceNo;
}
