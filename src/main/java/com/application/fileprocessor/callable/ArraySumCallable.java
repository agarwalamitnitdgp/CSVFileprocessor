package com.application.fileprocessor.callable;

import com.application.fileprocessor.model.RevenueAmount;

import java.util.List;
import java.util.concurrent.Callable;

public class ArraySumCallable implements Callable<Long> {

    List<RevenueAmount> revenueAmounts;

    public ArraySumCallable(List<RevenueAmount> revenueAmounts) {
        this.revenueAmounts = revenueAmounts;
    }

    @Override
    public Long call() {
        Long sum = Long.valueOf(0);
        for (RevenueAmount i : revenueAmounts) {
            sum += i.getRevenueAmount();
        }
        return sum;
    }
}
