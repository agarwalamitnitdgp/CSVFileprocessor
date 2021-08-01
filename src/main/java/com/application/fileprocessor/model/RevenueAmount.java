package com.application.fileprocessor.model;

import com.opencsv.bean.CsvBindByName;

public class RevenueAmount {

    @CsvBindByName(column = "Revenue")
    private Long revenueAmount;

    public Long getRevenueAmount() {
        return revenueAmount;
    }

    public void setRevenueAmount(Long revenueAmount) {
        this.revenueAmount = revenueAmount;
    }
}
