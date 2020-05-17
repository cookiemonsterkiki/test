package dateparse;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

@Test

public class DateParse {

	WebDriver driver = new ChromeDriver();

	private static String dateInput = null;

	private static String actualResp = "";

	@BeforeTest
	public void setup() throws InterruptedException {

		// initializing Chrome Driver
		System.setProperty("webdriver.chrome.driver", "src/chromedriver.exe");
		// launching URL on the browser
		driver.get("https://vast-dawn-73245.herokuapp.com");
		// maximized the browser window
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
	}

	@Test
	public void test() {

		System.out.println("Test block started");
		recursiveTest();
		// Expected parsed date validation
		try {
			@SuppressWarnings("deprecation")
			Date date = new Date(dateInput);
			SimpleDateFormat sdf = new SimpleDateFormat("E MMM dd yyyy HH:mm:ss ", Locale.ENGLISH);
			String exptd = sdf.format(date).toString();
			System.out.println("input provided after formatting: " + exptd);
			JOptionPane.showMessageDialog(null,
					actualResp.contentEquals(exptd.concat("GMT+0000")) ? "Success.!!!" : "Sorry No Match - Try again");
			System.out.println("Actual resp: " + actualResp);
			if (!actualResp.contentEquals(exptd.concat("GMT+0000"))) {
				recursiveTest();
			}

		} catch (IllegalArgumentException e) {
			System.out.println("IllegalArgumentException");
			JOptionPane.showMessageDialog(null,
					"Invalid input - Kindly try with the Date Format - Please try dd MMM yyyy");
			test();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Invalid Date Format - Please try dd MMM yyyy");
			System.out.println("Exception thrown");
			e.printStackTrace();
		}

	}

	private void recursiveTest() {
		// TODO Auto-generated method stub
		WebDriverWait wait = new WebDriverWait(driver, 5);
		wait.until(ExpectedConditions.presenceOfElementLocated((By.xpath("//*[@name='date']"))));

		// Actual Input by User
		driver.findElement(By.xpath("//*[@name='date']")).click();
		dateInput = JOptionPane.showInputDialog(null, "Please Enter Date", "Please Enter Date",
				JOptionPane.QUESTION_MESSAGE);

		driver.findElement(By.xpath("//*[@name='date']")).sendKeys(dateInput);
		driver.findElement(By.xpath("//*[@type='submit']")).click();

		actualResp = driver.findElement(By.xpath("html/body/div[2]/div/div[2]/div[contains(text(),'')]")).getText();

	}

	@AfterTest
	public void teardown() throws InterruptedException {
		driver.close();
		driver.quit();

	}
}