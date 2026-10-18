package com.example.payment.controller;

import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
public class RazorpayController {

    @Value("${razorpay.key.id}") private String keyId;
    @Value("${razorpay.key.secret}") private String keySecret;
    @Autowired
    private RazorpayClient client;

    @PostMapping("/pay/create-order")
    @ResponseBody
    public Map<String, Object> createOrder() throws RazorpayException {
        JSONObject req = new JSONObject();
        req.put("amount", 50 * 100);          // paise
        req.put("currency", "INR");
        req.put("receipt", "rcpt_" + System.currentTimeMillis());

        com.razorpay.Order order = client.orders.create(req);
        return Map.of("orderId", order.get("id"), "amount", order.get("amount"), "key", keyId);
    }

    @PostMapping("/pay/verify")
    @ResponseBody
    public ResponseEntity<String> verify(@RequestBody Map<String, String> data) {
        try {
            JSONObject o = new JSONObject();
            o.put("razorpay_order_id", data.get("razorpay_order_id"));
            o.put("razorpay_payment_id", data.get("razorpay_payment_id"));
            o.put("razorpay_signature", data.get("razorpay_signature"));

            boolean ok = Utils.verifyPaymentSignature(o, keySecret);
            return ok ? ResponseEntity.ok("success")
                    : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("invalid signature");
        } catch (RazorpayException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/success")
    public String success() { return "success"; }
}