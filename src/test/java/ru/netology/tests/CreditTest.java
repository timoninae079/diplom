package ru.netology.tests;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.page.CreditPage;
import ru.netology.page.MainPage;
import ru.netology.page.PaymentPage;

import java.util.concurrent.TimeUnit;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.data.DataGenerator.getApprovedCard;
import static ru.netology.data.DataGenerator.getDeclinedCard;
import static ru.netology.data.DataHelper.*;

public class CreditTest {
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
    void test(){
        main.cardCredit();
    }

    @Test
    @SneakyThrows
    @DisplayName("Покупка валидной картой")
    public void shouldPaymentValidCard() {
        main.cardPayment();
        var info = getApprovedCard();
        credit.sendingData(info);
        //Время отправки данных в базу данных, в секундах:
        TimeUnit.SECONDS.sleep(10);
        var expected = "APPROVED";
        var paymentInfo = getPaymentInfo();
        var orderInfo = getOrderInfo();
        //Проверка соответствия статуса в базе данных в таблице покупок:
        assertEquals(expected, paymentInfo.getStatus());
        //Проверка соответствия в базе данных id в таблице покупок и в таблице заявок:
        assertEquals(paymentInfo.getTransaction_id(), orderInfo.getPayment_id());
        //Проверка вывода соответствующего уведомления пользователю на странице покупок:
        credit.bankApproved();
    }

    @Test
    @SneakyThrows
    @DisplayName("Получение кредита на покупку по валидной карте")
    public void shouldCreditValidCard() {
        main.cardPayment();
        var info = getApprovedCard();
        credit.sendingData(info);
        //Время отправки данных в базу данных, в секундах:
        TimeUnit.SECONDS.sleep(20);
        var expected = "APPROVED";
        var creditRequestInfo = getCreditRequestInfo();
        var orderInfo = getOrderInfo();
        //Проверка соответствия статуса в базе данных в таблице запросов кредита:
        assertEquals(expected, creditRequestInfo.getStatus());
        //Проверка соответствия в базе данных id в таблице запросов кредита и в таблице заявок:
        assertEquals(creditRequestInfo.getBank_id(), orderInfo.getCredit_id());
        //Проверка вывода соответствующего уведомления пользователю на странице покупок:
        credit.bankApproved();
    }
    @Test
    @SneakyThrows
    @DisplayName("Получение кредита на покупку по не валидной карте")
    public void shouldCreditInvalidCard() {
        var info = getDeclinedCard();
        credit.sendingData(info);
        //Время отправки данных в базу данных, в секундах:
        TimeUnit.SECONDS.sleep(10);
        var expected = "DECLINED";
        var creditRequestInfo = getCreditRequestInfo();
        var orderInfo = getOrderInfo();
        //Проверка соответствия статуса в базе данных в таблице запросов кредита:
        assertEquals(expected, creditRequestInfo.getStatus());
        //Проверка соответствия в базе данных id в таблице запросов кредита и в таблице заявок:
        assertEquals(creditRequestInfo.getBank_id(), orderInfo.getCredit_id());
        //Проверка вывода соответствующего уведомления пользователю на странице покупок:
        credit.bankApproved();
    }
}


