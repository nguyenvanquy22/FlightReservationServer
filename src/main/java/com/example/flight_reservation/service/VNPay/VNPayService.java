package com.example.flight_reservation.service.VNPay;


import com.example.flight_reservation.config.VNPayConfig;
import com.example.flight_reservation.util.VNPayUtil;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class VNPayService {

  private final VNPayConfig vnPayConfig;

  public VNPayService(VNPayConfig vnPayConfig) {
    this.vnPayConfig = vnPayConfig;
  }

  public String createPaymentUrl(Long bookingId, Long amount, String orderInfo) {
    String vnp_TxnRef = String.valueOf(bookingId);
    String vnp_TmnCode = vnPayConfig.getVnp_TmnCode();
    String bankCode = "NCB";

    Map<String, String> vnpParamsMap = vnPayConfig.getVNPayConfig();
    vnpParamsMap.put("vnp_TxnRef",vnp_TxnRef);
    vnpParamsMap.put("vnp_TmnCode",vnp_TmnCode);
    vnpParamsMap.put("vnp_OrderInfo","Thanh toan don hang "+ bookingId);
    vnpParamsMap.put("vnp_Amount", String.valueOf(amount*100L));
    vnpParamsMap.put("vnp_BankCode", bankCode);
    vnpParamsMap.put("vnp_IpAddr", VNPayUtil.getClientIpAddress());
    //build query url
    String queryUrl = VNPayUtil.getPaymentURL(vnpParamsMap, true);
    String hashData = VNPayUtil.getPaymentURL(vnpParamsMap, false);
    String vnpSecureHash = VNPayUtil.hmacSHA512(vnPayConfig.getSecretKey(), hashData);
    queryUrl += "&vnp_SecureHash=" + vnpSecureHash;
    return vnPayConfig.getVnp_PayUrl() + "?" + queryUrl;
  }
}
