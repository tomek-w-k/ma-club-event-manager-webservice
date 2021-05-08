# ma-club-event-manager-webservice

![Custom badge](https://img.shields.io/static/v1.svg?label=Java&message=11&color=cadetblue)
![Custom badge](https://img.shields.io/static/v1.svg?label=Spring+Boot&message=2.4.0&color=apple)
![Custom badge](https://img.shields.io/static/v1.svg?label=Hibernate&message=5.4.23&color=5E6D74)
![Custom badge](https://img.shields.io/static/v1.svg?label=Liquibase&message=3.10.3&color=E4360F)

A back-end part of MAClubEventManager project.

## API documentation
REST API documentation is available [here](api-doc.md)

## Compilation & Launch
An application was tested on **Ubuntu 19.10** and with **MySQL 10.4.11** database system. The following manual has been prepared using the above-mentioned systems. Please fill places marked with [] braces by replace them with a specified data.

1. Create a database in your database system. For **MySQL** you may use the command:
    ```
    CREATE DATABASE [your-database-name] DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_general_ci;
    ```    
2. From the main project directory go to `src/main/resources` directory and create a `db-access.properties` file, then copy all the content of `db-access-template.properties` file into the created file. In the `db-access.properties` file enter your database system access data:
    ```
    spring.datasource.username=[your-database-username]
    spring.datasource.password=[your-database-password]
    ```
3. Create `liquibase.properties` file. Copy all the content of `liquibase-template.properties` file into the created file. Enter your database access data:
    ```
    username=[your-database-username]
    password=[your-database-password]
    url=jdbc:mysql://localhost:3306/[your-database-name]?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC
    ```
   Make sure that you have entered your database name in `url` parameter.
4. Configure your mailbox SMTP settings. For **Gmail** you can find instructions <a href="https://www.androidauthority.com/gmail-smtp-settings-801100/">here</a>.
5. Create `mail-sender.properties` file. As above, copy all the content of its template file and fill it with your SMTP mail server data:
    ```
    mail-sender.sender-email-address=[your-email-address]
    mail-sender.sender-email-password=[your-email-password]
    mail-sender.mail-smtp-host=[your-mail-smtp-host]
    mail-sender.mail-smtp-port=[your-mail-smtp-port]
    mail-sender.frontend-reset-password-component-path=password_reset
    mail-sender.frontend-server-domain=localhost:3001
    ```
6. From `resources` directory go to `liquibase` directory. Create file `changelog.xml` and copy all the content of its template file.
7. In console, go to the main project directory. Run `mvn liquibase:diff` command. The `changelog.xml` file should be now filled up with an entire entity structure of the project.
8. Run `mvn compile` command to build the project.
9. Run `mvn spring-boot:run` to start the application.
10. Instead of above two points, you may also run `mvn package` command to build a project package and then run `java -jar target/ma-club-event-manager-webservice-0.0.1-SNAPSHOT.jar` to start the application.
