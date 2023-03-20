package ru.netology.tests;

import com.codeborne.selenide.logevents.SelenideLogger;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import ru.netology.page.CreditPage;
import ru.netology.page.MainPage;
import ru.netology.page.PaymentPage;

import java.util.concurrent.TimeUnit;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.data.DataGenerator.getApprovedCard;
import static ru.netology.data.DataGenerator.getDeclinedCard;
import static ru.netology.data.DataHelper.*;

public class TourPurchaseTest {
    private CreditPage creditPage = new CreditPage();
    private MainPage mainPage = new MainPage();
    private PaymentPage paymentPage = new PaymentPage();


    @BeforeEach
    public void openPage() {

        open("http://localhost:8080");
    }

    @AfterAll
    public static void tearDownAll() {
        SelenideLogger.removeListener("allure");
        databaseCleanUp();
    }

    @Nested
    //Тесты на оплату и получения кредита по валидной карте:
    public class ValidCard {

        @Test
        @SneakyThrows
        @DisplayName("Покупка валидной картой")
        public void shouldPaymentValidCard() {
            var mainPage = new MainPage();
            mainPage.cardPayment();
            var info = getApprovedCard();
            creditPage.sendingData(info);
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
            creditPage.bankApproved();
        }


    }

    @Nested
    //Тесты на оплату и получения кредита по не валидной карте:
    public class InvalidCard {

        @Test
        @SneakyThrows
        @DisplayName("Покупка не валидной картой")
        public void shouldPaymentInvalidCard() {

            mainPage.cardPayment();
            var info = getDeclinedCard();
            creditPage.sendingData(info);
            //Время отправки данных в базу данных, в секундах:
            TimeUnit.SECONDS.sleep(10);
            var expected = "DECLINED";
            var paymentInfo = getPaymentInfo();
            var orderInfo = getOrderInfo();
            //Проверка соответствия статуса в базе данных в таблице покупок:
            assertEquals(expected, paymentInfo.getStatus());
            //Проверка соответствия в базе данных id в таблице покупок и в таблице заявок:
            assertEquals(paymentInfo.getTransaction_id(), orderInfo.getPayment_id());
            //Проверка вывода соответствующего уведомления пользователю на странице покупок:
        }


        @Nested
        //Тесты на валидацию полей платежной формы:
        public class PaymentFormFieldValidation {

            @BeforeEach
            public void setPayment() {
                mainPage.cardPayment();
            }

            @Test
            @DisplayName("Отправка пустой формы")
            public void shouldEmpty() {

            }

            @Test
            @DisplayName("Поле 'Номер карты', пустое поле")
            public void shouldEmptyCardNumberField() {
            }

            @Test
            @DisplayName("Поле 'Номер карты', не полный номер карты")
            public void shouldCardWithIncompleteCardNumber() {

            }

            @Test
            @DisplayName("Поле 'Месяц', пустое поле")
            public void shouldEmptyMonthField() {
            }

            @Test
            @DisplayName("Поле 'Месяц', просроченный месяц")
            public void shouldCardWithOverdueMonth() {
            }

            @Test
            @DisplayName("Поле 'Месяц', нижнее негативное значение '00'")
            public void shouldCardWithLowerMonthValue() {

            }

            @Test
            @DisplayName("Поле 'Месяц', верхнее негативное значение '13'")
            public void shouldCardWithGreaterMonthValue() {

            }

            @Test
            @DisplayName("Поле 'Год', пустое поле")
            public void shouldEmptyYearField() {

            }

            @Test
            @DisplayName("Поле 'Год', просроченный год")
            public void shouldCardWithOverdueYear() {

            }

            @Test
            @DisplayName("Поле 'Год', год из отдаленного будущего")
            public void shouldCardWithYearFromFuture() {

            }

            @Test
            @DisplayName("Поле 'Владелец', пустое поле")
            public void shouldEmptyOwnerField() {

            }

            @Test
            @DisplayName("Поле 'Владелец', с пробелом или дефисом")
            public void shouldCardWithSpaceOrHyphenOwner() {

            }

            @Test
            @DisplayName("Поле 'Владелец', с несколькими спец символами")
            public void shouldCardWithSpecialSymbolsOwner() {

            }

            @Test
            @DisplayName("Поле 'Владелец', с цифрами")
            public void shouldCardWithNumbersOwner() {

            }

            @Test
            @DisplayName("Поле 'CVC/CVV', пустое поле")
            public void shouldEmptyCVCField() {

            }

            @Test
            @DisplayName("Поле 'CVC/CVV', не полный номер")
            public void shouldCardWithIncompleteCVC() {

            }
        }

        @Nested
        //Тесты на валидацию полей кредитной формы:
        public class CreditFormFieldValidation {

            @BeforeEach
            public void setPayment() {

            }

            @Test
            @DisplayName("Отправка пустой формы")
            public void shouldEmpty() {

            }

            @Test
            @DisplayName("Поле 'Номер карты', пустое поле")
            public void shouldEmptyCardNumberField() {

            }

            @Test
            @DisplayName("Поле 'Номер карты', не полный номер карты")
            public void shouldCardWithIncompleteCardNumber() {

            }

            @Test
            @DisplayName("Поле 'Месяц', пустое поле")
            public void shouldEmptyMonthField() {

            }

            @Test
            @DisplayName("Поле 'Месяц', просроченный месяц")
            public void shouldCardWithOverdueMonth() {

            }

            @Test
            @DisplayName("Поле 'Месяц', нижнее негативное значение '00'")
            public void shouldCardWithLowerMonthValue() {

            }

            @Test
            @DisplayName("Поле 'Месяц', верхнее негативное значение '13'")
            public void shouldCardWithGreaterMonthValue() {

            }

            @Test
            @DisplayName("Поле 'Год', пустое поле")
            public void shouldEmptyYearField() {

            }

            @Test
            @DisplayName("Поле 'Год', просроченный год")
            public void shouldCardWithOverdueYear() {

            }

            @Test
            @DisplayName("Поле 'Год', год из отдаленного будущего")
            public void shouldCardWithYearFromFuture() {

            }

            @Test
            @DisplayName("Поле 'Владелец', пустое поле")
            public void shouldEmptyOwnerField() {

            }

            @Test
            @DisplayName("Поле 'Владелец', с пробелом или дефисом")
            public void shouldCardWithSpaceOrHyphenOwner() {

            }

            @Test
            @DisplayName("Поле 'Владелец', с несколькими спец символами")
            public void shouldCardWithSpecialSymbolsOwner() {

            }

            @Test
            @DisplayName("Поле 'Владелец', с цифрами")
            public void shouldCardWithNumbersOwner() {

            }

            @Test
            @DisplayName("Поле 'CVC/CVV', пустое поле")
            public void shouldEmptyCVCField() {

            }

            @Test
            @DisplayName("Поле 'CVC/CVV', не полный номер")
            public void shouldCardWithIncompleteCVC() {

            }
        }
    }
}

