# Forum

Проект представляет собой API для пользовательского взаимодействия в рамках системы форума.


# Architecture
Основные микросервисы:
1. FORUM-APP: сервис ядра форума (обеспечивает работу с категориями, темами, сообщениями)
2. AUTH-APP: сервис, отвечающий за аутентификацию пользователей
3. GATEWAY: шлюз API

# Role-specific

На данный момент система не реализует разграничение привелегий.

Доступна роль пользователя

# Database-architecture
На данный момент используется общая БД:
## Class Diagram
![Class Diagram](docs/class-diagram.png)


# Configuration/Installation

Необходимо запустить сервисы в следующем порядке:
1. Eureka Registry
2. ForumServer
3. AuthServer
4. UsersServer
5. CloudGateway (1)

![](docs/boot-instr.png)

# Environments

Необходимо в корне проекта создать .env файл, где будут содержаться переменные окружения.

И указать следующие переменные:
- HOST: хост приложения
- POSTGRES_USERNAME: имя пользователя для подключения к БД Postgres
- POSTGRES_PASSWORD: пароль для подключения к БД Postgres
- POSTGRES_DATABASE: имя базы данных, используемой в приложении
- JWT_SECRET: секретный ключ для подписи JWT (можно сгнерировать на https://www.base64encode.net/)
- JWT_ACCESS: время жизни access-токена в миллисекундах
- JWT_ACCESS: время жизни refresh-токена в миллисекундах
- API_SECRET: межсервисный API-ключ (также можно сгнерировать на https://www.base64encode.net/)

# API Documentation

Для проекта настроен Swagger:
http://localhost:8989/webjars/swagger-ui/index.html

Для переключения между сервисами необходимо выбрать опцию в поле "Select a definition"

(Эндпоинты с пагинацией имеют поле sort, которое может принимать поле и вид сортировки в таком виде):
sort: [
"createTime,desc"
]