package ru.netology.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.val;
import org.junit.jupiter.api.*;
import ru.netology.data.Data;
import ru.netology.data.DbInteraction;
import ru.netology.page.MainPage;
import ru.netology.page.PaymentPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RandomCardNumber {
    MainPage buttons = new MainPage();
    PaymentPage willful = new PaymentPage();

    private static String appUrl = System.getProperty("sut.url");

    @BeforeEach
    public void setUp2() {
        open(appUrl, MainPage.class);
//        Configuration.holdBrowserOpen = true;
        DbInteraction.deleteDataFromDb();
    }

    @BeforeAll
    static void setUpAll() {
        Configuration.headless = true;
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @Test
    @DisplayName("ByDebitDbRequest")
    void shouldDeclineWhenDebitCardBuyFromRandomCardNumber() {
        buttons.buyDebitCard();
        val cardNumber = Data.getRandomCardNumber();
        val month = Data.getValidMonth();
        val year = Data.getValidYear();
        val owner = Data.getValidNameEn();
        val cvc = Data.getValidCVC();
        willful.fillOutFields(cardNumber, month, year, owner, cvc);
        willful.expectApprovalFromBank();
        val expected = "DECLINED";
        val actual = DbInteraction.getStatusBuyDebit();
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("ByCreditDbRequest")
    void shouldDeclineWhenCreditCardBuyFromRandomCardNumber() {
        buttons.buyCreditCard();
        val cardNumber = Data.getRandomCardNumber();
        val month = Data.getValidMonth();
        val year = Data.getValidYear();
        val owner = Data.getValidNameEn();
        val cvc = Data.getValidCVC();
        willful.fillOutFields(cardNumber, month, year, owner, cvc);
        willful.expectApprovalFromBank();
        val expected = "DECLINED";
        val actual = DbInteraction.getStatusBuyCredit();
        assertEquals(expected, actual);
    }
}