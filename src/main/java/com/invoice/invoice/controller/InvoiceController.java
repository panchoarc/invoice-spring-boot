package com.invoice.invoice.controller;


import com.invoice.invoice.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;


    @GetMapping(value = "/invoice")
    public ResponseEntity<InputStreamResource> generateInvoice() throws IOException {

        HttpHeaders headers = new HttpHeaders();

        DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD");

        String filetype = "attachment; filename=invoice_details_" +dateFormat.format(new Date()).concat(".pdf");
        headers.add("Content-Disposition", filetype);
        headers.add("Content-Type", MediaType.APPLICATION_PDF_VALUE);
        ByteArrayInputStream bis = invoiceService.createInvoice();

        return ResponseEntity.ok().headers(headers).body(new InputStreamResource(bis));


    }
}
