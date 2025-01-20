package com.ambev.util;

public class Utils {

    public static boolean isValidCPF(String cpf) {
        cpf = cpf.replaceAll("[^0-9]", "");

        if(cpf.length() != 11) {
            return false;
        }

        if (cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        int firstDigit = calculateCPFDigit(cpf, 10);
        int secondDigit = calculateCPFDigit(cpf, 11);

        return cpf.charAt(9) == (firstDigit + '0') && cpf.charAt(10) == (secondDigit + '0');
    }

    private static int calculateCPFDigit(String cpf, int length) {
        int sum = 0;
        int weight = length;

        for (int i = 0; i < length - 1; i++) {
            sum += (cpf.charAt(i) - '0') * weight--;
        }

        int remainder = sum % 11;
        return (remainder < 2) ? 0 : 11 - remainder;
    }
}
