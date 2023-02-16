package ru.netology.data;

import com.github.javafaker.Faker;

import java.util.Locale;


public class Data {
    final static Faker fakerEn = new Faker(new Locale("en"));
    final static Faker fakerRu = new Faker(new Locale("ru"));

    public Data() {
    }

    public static String getValidCardNumber() {
        return "4444 4444 4444 4441";
    }

    public static String getInvalidCardNumber() {
        return "4444 4444 4444 4442";
    }

    public static String getValidMonth() {
        return String.valueOf(fakerEn.number().numberBetween(10, 12));
    }

    public static String getValidCardStatus() {
        return "APPROVED";
    }

    public static String getInvalidCardStatus() {
        return "DECLINED";
    }

    public static String getInValidMonth() {
        return String.valueOf(fakerEn.number().numberBetween(13, 50));
    }

    public static String getValidYear() {
        return String.valueOf(fakerEn.number().numberBetween(22, 25));
    }

    public static String getInvalidYear() {
        return String.valueOf(fakerEn.number().numberBetween(15, 21));
    }

    public static String getValidNameEn() {
        return "Ivan Petrov-Ivanov";
    }

    public static String getRandomNameRu() {
        return fakerRu.name().fullName();
    }

    public static String getRandomNameEn() {
        return fakerEn.name().fullName();
    }

    public static String getRandomCardNumber() {
        return fakerEn.business().creditCardNumber();
    }

    public static String getValidCVC() {
        return String.valueOf(fakerEn.number().numberBetween(100, 999));
    }

    public static String getInvalidCVC2Digit() {
        return String.valueOf(fakerEn.number().numberBetween(1, 99));
    }

    public static String getCVCEqualsNulls() {
        return "000";
    }
}
