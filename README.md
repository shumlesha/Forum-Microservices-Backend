# Forum

Проект представляет собой API для пользовательского взаимодействия в рамках системы форума.


# Architecture
Основные микросервисы:
1. **FORUM-APP:** сервис ядра форума (обеспечивает работу с категориями, темами, сообщениями)
2. **AUTH-APP:** сервис, отвечающий за аутентификацию пользователей
3. **GATEWAY:** шлюз API
4. **USERS-APP:** сервис, отвечающий за управление пользователями со стороны Администратора
5. **FILES-APP:** сервис, отвечающий за загрузку и скачивание файлов, связан с MinIO Bucket
6. **NOTIFICATION-APP:** сервис, отвечающий за прием уведомлений от других сервисов и рассылку их по указанным каналам
7. **EUREKA-REGISTRY-APP:** сервер, регистрирующий остальные микросервисы в Eureka


# Role-specific

Доступны роли **пользователя**, **модератора**, **администратора**.
Модератор обладает привелегиями в рамках категории и её дочерних - подкатегорий; 
соответственно - обладает привелегиями над топиками в категориях нижнего уровня

# Database-architecture
На данный момент используются различные Базы Данных под сервисы:

## Files Database
[<img src="docs/Files_DB.png" width="250" />](docs/Files_DB.png)

## Forum Database
[<img src="docs/Forum_DB.png" width="250" />](docs/Forum_DB.png)

## Users Database
[<img src="docs/Users_DB.png" width="250" />](docs/Users_DB.png)


### Migrations
В .env файле используется переменная MIGRATION_TYPE=validate + Flyway


# Configuration/Installation

Необходимо запустить сервисы в следующем порядке:
1. Eureka Registry
2. ForumServer
3. NotificationsServer
4. AuthServer
5. UsersServer
6. FileServer
7. Cloud Gateway

Также для FILES-APP необходимо:
1) Поднять MinIO:
minio server [Путь хранения MinIO-файлов] --console-address ":9001"
2) Запустить Kafka (приведено для Windows):
.\bin\windows\zookeeper-server-start.bat .\config\zookeeper.properties
.\bin\windows\kafka-server-start.bat .\config\server.properties


# Environments

Необходимо в корне проекта создать .env файл, где будут содержаться переменные окружения.

И указать следующие переменные:
- **HOST:** хост приложения
- **POSTGRES_USERNAME:** имя пользователя для подключения к БД Postgres
- **POSTGRES_PASSWORD:** пароль для подключения к БД Postgres
- **POSTGRES_DATABASE:** имя базы данных, используемой в приложении
- **JWT_SECRET:** секретный ключ для подписи JWT (можно сгенерировать на https://www.base64encode.net/)
- **JWT_EMAIL_SECRET:** секретный ключ для подписи JWT-токенов для email-ссылок (можно сгенерировать на https://www.base64encode.net/)
- **JWT_ACCESS:** время жизни access-токена в миллисекундах
- **JWT_REFRESH:** время жизни refresh-токена в миллисекундах
- **API_SECRET:** межсервисный API-ключ (также можно сгенерировать на https://www.base64encode.net/)
- **MAIL_HOST:** хост для отправки gmail по SMTP
- **MAIL_PORT:** spring-mail порт
- **MAIL_USERNAME:** gmail-адрес отправляющего от имени форума
- **MAIL_PASSWORD:** пароль для MAIL_USERNAME
- **MIGRATION_TYPE:** флаг ddl-auto
- **KAFKA_BOOTSTRAP_SERVERS:** URL для Kafka
- **KAFKA_SUBSCRIBED_TOPICS:** Топики, на которые могут подписываться консюмеры


# API Documentation

Для проекта настроен Swagger:
http://localhost:8989/webjars/swagger-ui/index.html

Для переключения между сервисами необходимо выбрать опцию в поле "Select a definition"

(Эндпоинты с пагинацией имеют поле sort, которое может принимать поле и вид сортировки в таком виде):
sort: [
"createTime,desc"
]