package ru.netology.tests;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.page.CreditPage;
import ru.netology.page.MainPage;
import ru.netology.page.PaymentPage;

import static com.codeborne.selenide.Selenide.open;

public class PaymentTest {
    private final MainPage main = new MainPage();
    private final CreditPage credit = new CreditPage();
    private final PaymentPage payment = new PaymentPage();

    @BeforeAll
    public static void setUpAll() {

        SelenideLogger.addListener("allure", new AllureSelenide());
    }


    @BeforeEach
    public void openPage() {

        open("http://localhost:8080");
    }

    @Test
    void test() {
        main.cardPayment();
    }


}
