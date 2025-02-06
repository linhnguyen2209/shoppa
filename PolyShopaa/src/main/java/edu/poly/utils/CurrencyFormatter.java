package edu.poly.utils;

import org.springframework.stereotype.Component;
import java.text.NumberFormat;
import java.util.Locale;

@Component("currencyFormatter")
public class CurrencyFormatter {
    public String formatCurrency(double amount) {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
        return currencyFormat.format(amount);
    }
}

