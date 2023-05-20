package ru.netology.tests;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.page.MainPage;
import ru.netology.page.PaymentPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.data.DataGenerator.*;
import static ru.netology.data.DataHelper.getOrderInfo;
import static ru.netology.data.DataHelper.getPaymentInfo;

public class PaymentTest {

    private MainPage mainPage;
    private PaymentPage paymentPage;

    private final CardInfo validCard = getApprovedCard();


    @BeforeAll
    public static void setUpAll() {

        SelenideLogger.addListener("allure", new AllureSelenide());
    }


    @BeforeEach
    public void openPage() {
        mainPage = open("http://localhost:8080", MainPage.class);
        mainPage.cardPayment();
        paymentPage = new PaymentPage();
    }

    @Test
    @DisplayName("Покупка валидной картой")
    public void shouldPaymentValidCard() {
        var info = getApprovedCard();
        paymentPage.sendingData(info);
        paymentPage.expectApprovalFromBank();
        var expected = "APPROVED";
        var paymentInfo = getPaymentInfo();
        var orderInfo = getOrderInfo();
        assertEquals(expected, paymentInfo.getStatus());
        assertEquals(paymentInfo.getTransaction_id(), orderInfo.getPayment_id());
    }

    @Test
    @DisplayName("Покупка не валидной картой")
    public void shouldPaymentInvalidCard() {
        var info = getDeclinedCard();
        paymentPage.sendingData(info);
        paymentPage.errorBankRefusal();
        var expected = "DECLINED";
        var paymentInfo = getPaymentInfo();
        var orderInfo = getOrderInfo();
        assertEquals(expected, paymentInfo.getStatus());
        assertEquals(paymentInfo.getTransaction_id(), orderInfo.getPayment_id());
    }

    @Test
    @DisplayName("Поле 'Номер карты', пустое поле")
    public void shouldEmptyCardNumberField() {
        paymentPage.fillOutFields("", validCard.getMonth(), validCard.getYear(), validCard.getOwner(), validCard.getCvc());
        paymentPage.errorMessageInvalidFormat();
    }

    @Test
    @DisplayName("Отправка пустой формы")
    public void shouldEmpty() {
        paymentPage.clickOnContinue();
        paymentPage.errorMessageInvalidFormat();
    }

    @Test
    @DisplayName("Поле 'Номер карты', не полный номер карты")
    public void shouldCardWithIncompleteCardNumber() {
        paymentPage.sendingData(getCardWithIncompleteCardNumber());
        paymentPage.clickOnContinue();
        paymentPage.errorMessageInvalidFormat();
    }

    @Test
    @DisplayName("Поле 'Месяц', пустое поле")
    public void shouldEmptyMonthField() {
        paymentPage.sendingData(getCardWithLowerMonthValue());
        paymentPage.clickOnContinue();
        paymentPage.errorMessageInvalidFormat();
    }

    @Test
    @DisplayName("Поле 'Месяц', просроченный месяц")
    public void shouldCardWithOverdueMonth() {
        paymentPage.sendingData(getCardWithOverdueMonth());
        paymentPage.clickOnContinue();
        paymentPage.errorMessageInvalidDuration();
    }

    @Test
    @DisplayName("Поле 'Месяц', нижнее негативное значение '00'")
    public void shouldCardWithLowerMonthValue() {
        paymentPage.sendingData(getCardWithLowerMonthValue());
        paymentPage.errorMessageInvalidDuration();
    }

    @Test
    @DisplayName("Поле 'Месяц', верхнее негативное значение '13'")
    public void shouldCardWithGreaterMonthValue() {
        paymentPage.sendingData(getCardWithGreaterMonthValue());
        paymentPage.errorMessageInvalidDuration();
    }

    @Test
    @DisplayName("Поле 'Год', пустое поле")
    public void shouldEmptyYearField() {
        paymentPage.fillOutFields(validCard.getNumberCard(), validCard.getMonth(), "", validCard.getOwner(), validCard.getCvc());
        paymentPage.errorMessageInvalidFormat();
    }

    @Test
    @DisplayName("Поле 'Год', просроченный год")
    public void shouldCardWithOverdueYear() {
        paymentPage.sendingData(getCardWithOverdueYear());
        paymentPage.errorMessageInvalidYear();
    }

    @Test
    @DisplayName("Поле 'Год', год из будущего")
    public void shouldCardWithYearFromFuture() {
        paymentPage.sendingData(getCardWithYearFromFuture());
        paymentPage.errorMessageInvalidDuration();
    }

    @Test
    @DisplayName("Поле 'Владелец', пустое поле")
    public void shouldEmptyOwnerField() {
        paymentPage.fillOutFields(validCard.getNumberCard(), validCard.getMonth(), validCard.getYear(), "", validCard.getCvc());
        paymentPage.errorMessageWhenOwnerFieldIsEmpty();
    }

    @Test
    @DisplayName("Поле 'Владелец', с пробелом или дефисом")
    public void shouldCardWithSpaceOrHyphenOwner() {
        paymentPage.sendingData(getCardWithSpaceOrHyphenOwner());
        paymentPage.clickOnContinue();
        paymentPage.errorMessageInvalidFormat();
    }

    @Test
    @DisplayName("Поле 'Владелец', с несколькими спец символами")
    public void shouldCardWithSpecialSymbolsOwner() {
        paymentPage.sendingData(getCardWithSpecialSymbolsOwner());
        paymentPage.clickOnContinue();
        paymentPage.errorMessageInvalidFormat();
    }

    @Test
    @DisplayName("Поле 'Владелец', с цифрами")
    public void shouldCardWithNumbersOwner() {
        paymentPage.sendingData(getCardWithNumbersOwner());
        paymentPage.clickOnContinue();
        paymentPage.errorMessageInvalidFormat();
    }

    @Test
    @DisplayName("Поле 'CVC/CVV', пустое поле")
    public void shouldEmptyCVCField() {
        paymentPage.fillOutFields(validCard.getNumberCard(), validCard.getMonth(), validCard.getYear(), validCard.getOwner(), "");
        paymentPage.errorMessageInvalidFormat();
    }

    @Test
    @DisplayName("Поле 'CVC/CVV', не полный номер")
    public void shouldCardWithIncompleteCVC() {
        paymentPage.sendingData(getCardWithIncompleteCVC());
        paymentPage.errorMessageInvalidFormat();
    }

}


