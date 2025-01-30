package yahoofinance.home;

import org.openqa.selenium.support.ui.WebDriverWait;
import java.net.MalformedURLException;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;

public class HomePage {

	public static WebDriver initializeDriver() throws MalformedURLException {
		System.setProperty("webdriver.chrome.driver", "C:/Software/test/chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get("https://finance.yahoo.com/");

		// Create an instance of WebDriverWait with a timeout of 10 seconds
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		// Wait until a specific element is visible on the page
		WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("ybar-sbq")));
		return driver;
	}

	public static WebElement waitForElement(WebDriver driver, By by, Duration i) {
		WebDriverWait wait = new WebDriverWait(driver, i);
		try {
			return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
		} catch (TimeoutException e) {
			System.out.println("Element not found within " + i + " seconds: " + by.toString());
			return null;
		}
	}

	public static boolean searchStock(WebDriver driver, String stockSymbol) {
		try {
			WebElement searchBox = driver.findElement(By.id("ybar-sbq"));
			searchBox.clear();
			searchBox.sendKeys(stockSymbol);
			WebElement firstSuggestion = waitForElement(driver, By.xpath("//li[contains(@title,'Tesla, Inc.')]"),
					Duration.ofSeconds(8));
			WebElement textVerify = waitForElement(driver, By.xpath("//li[contains(@title,'Tesla, Inc.')]//div"),
					Duration.ofSeconds(8));
			if (firstSuggestion != null && textVerify.getText().contains(stockSymbol.toUpperCase())) {
				firstSuggestion.click();
				return true;
			} else {
				System.out.println("Valid autosuggest entry not found.");
				return false;
			}
		} catch (NoSuchElementException e) {
			System.out.println("Search box not found.");
			return false;
		}
	}

	public static boolean verifyStockPrice(WebDriver driver, double minPrice) {
		WebElement stockPriceElement = waitForElement(driver, By.xpath("//span[@data-testid='qsp-price']"),
				Duration.ofSeconds(30));
		if (stockPriceElement != null) {
			try {
				double stockPrice = Double.parseDouble(stockPriceElement.getText().replace(",", ""));
				if (stockPrice > minPrice) {
					return true;
				} else {
					System.out.println("Stock price is not greater than $" + minPrice + ": " + stockPrice);
					return false;
				}
			} catch (NumberFormatException e) {
				System.out.println("Failed to convert stock price to double.");
				return false;
			}
		} else {
			System.out.println("Stock price element not found.");
			return false;
		}
	}

	public static boolean captureAdditionalData(WebDriver driver) {
		try {
			WebElement previousClose = driver
					.findElement(By.xpath("//fin-streamer[@data-field='regularMarketPreviousClose']"));
			WebElement volume = driver.findElement(By.xpath("//fin-streamer[@data-field='regularMarketVolume']"));
			WebElement marketCap = driver.findElement(By.xpath("//fin-streamer[@data-field='marketCap']"));
			System.out.println("Previous Close: " + previousClose.getText());
			System.out.println("Volume: " + volume.getText());
			System.out.println("Market Cap: " + marketCap.getText());
			return true;
		} catch (NoSuchElementException e) {
			System.out.println("Failed to capture additional data.");
			return false;
		}
	}

	public static void testStock(WebDriver driver, String stockSymbol, double minPrice) {
		System.out.println("Testing with symbol '" + stockSymbol + "'...");
		if (searchStock(driver, stockSymbol)) {
			// Validate price details
			if (verifyStockPrice(driver, minPrice)) {
				// Capture other details of same stock symbol
				captureAdditionalData(driver);
			}
		}
	}

	public static void main(String[] args) throws MalformedURLException {
		WebDriver driver = initializeDriver();
		try {
			testStock(driver, "TSLA", 200);
			// Validate not expected symbol
			testStock(driver, "AAPL", 150);
			// Validate invalid symbol search
			testStock(driver, "INVALID", 200);
		} finally {
			driver.quit();
		}
	}

}
