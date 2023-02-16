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

public class YearIsEmpty {
    MainPage buttons = new MainPage();
    PaymentPage fillfull = new PaymentPage();

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
    @DisplayName("ByDebit")
    void shouldShowErrorMessageWhenYearIsEmpty() {
        buttons.buyDebitCard();
        val cardNumber = Data.getValidCardNumber();
        val month = Data.getValidMonth();
        val owner = Data.getValidNameEn();
        val cvc = Data.getValidCVC();
        fillfull.fillOutFields(cardNumber, month, "", owner, cvc);
        fillfull.errorMessageInvalidFormat();
    }

    @Test
    @DisplayName("ByCredit")
    void shouldShowErrorMessageWhenYearIsEmptyAndPayByCredit() {
        buttons.buyCreditCard();
        val cardNumber = Data.getValidCardNumber();
        val month = Data.getValidMonth();
        val owner = Data.getValidNameEn();
        val cvc = Data.getValidCVC();
        fillfull.fillOutFields(cardNumber, month, "", owner, cvc);
        fillfull.errorMessageInvalidFormat();
    }
}