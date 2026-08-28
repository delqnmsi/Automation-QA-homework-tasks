# Automation-QA-homework-tasks
This repo holds ther solutions of the following tasks :
1. "Write a function called FooBar..." and unit tests to check the code logic;
2. UI tests for amazon.co.uk : book search , gift , basket flows - separated in deicated test scenarious;
3. Java concurrency

## Tech stack

- Java 21
- Selenium 4 (`selenium-java` 4.23.1)
- JUnit 5 (Jupiter 5.11)
- WebDriverManager 5.9 (driver binaries resolved automatically)
- Maven + Surefire 3.5

 ###Configuration

Defaults live in src/main/resources/config.properties. 
Every key can be overridden without editing the file — resolution order is: 
system property > environment variable > file > built-in default. 

base.url defaults to https://www.amazon.co.uk/ — target site.

browser defaults to chrome, with allowed options: chrome, firefox, or edge.

headless defaults to false.

element.load.seconds defaults to 5 —  default explicit-wait timeout.

page.load.timeout.seconds defaults to 30.
