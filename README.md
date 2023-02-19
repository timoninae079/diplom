# Дипломный проект по профессии «Тестировщик»
Дипломный проект представляет собой автоматизацию тестирования комплексного сервиса, взаимодействующего с СУБД и API Банка.

Приложение представляет собой веб-сервис, который предоставляет возможность купить тур по определённой цене с помощью двух способов:

Обычная оплата по дебетовой карте
Уникальная технология: выдача кредита по данным банковской карты

# Запуск тестового приложения:
1. Запустить MySQL, PostgreSQL, NodeJS через терминал командой:
    
   docker-compose up
2. В новой вкладке терминала запустить тестируемое приложение:

   Для MySQL:

java -Dspring.datasource.url=jdbc:mysql://localhost:3306/app -jar artifacts/aqa-shop.jar

   Для PostgreSQL:

java -Dspring.datasource.url=jdbc:postgresql://localhost:5432/app -jar artifacts/aqa-shop.jar

# Запуск тестов:
В новой вкладке терминала запустить тесты:

Для MySQL:

gradlew clean test -Ddb.url=jdbc:mysql://localhost:3306/app

Для PostgreSQL:

gradlew clean test -Ddb.url=jdbc:postgresql://localhost:5432/app
