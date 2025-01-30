# Yahoo Finance Selenium Tests

This repository contains a Selenium script in Java to automate the testing of stock information on Yahoo Finance.

## Prerequisites

- Java Development Kit (JDK) 8 or higher
- Google Chrome
- maven
- ChromeDriver
- Selenium Java bindings

## Installation

1. *Clone the Repository*:
    bash
    git clone https://github.com/your-username/your-repository.git
    cd your-repository
    

2. *Download ChromeDriver*:
    - Download the ChromeDriver that matches your version of Google Chrome from here.
    - Extract the downloaded file and place it in a directory included in your system's PATH.

3. *Add Selenium Java Bindings*:
    - Download the Selenium Java bindings from here.
    - Add the Selenium JAR files to your project's build path.

## Running the Tests

1. *Compile the Java Code*:
    bash
    javac -cp .:path/to/selenium-java-<version>.jar HomePage.java
    
    Replace path/to/selenium-java-<version>.jar with the actual path to the Selenium JAR file.

2. *Run the Script*:
    bash
    java -cp .:path/to/selenium-java-<version>.jar HomePage
    

3. *Test Scenarios*:
    - The script will test the following scenarios:
        - Valid stock symbol (e.g., TSLA)
        - Invalid stock symbol (e.g., INVALID)
        - Missing elements

4. *Output*:
    - The script will print the stock price, previous close, volume, and market cap for valid stock symbols.
    - It will handle and report errors for invalid symbols and missing elements.
