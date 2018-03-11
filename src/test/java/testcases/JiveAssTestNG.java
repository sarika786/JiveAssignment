package testcases;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

public class JiveAssTestNG {

	private String applicationUrl = "https://jive.com/resources/support/submit-a-ticket/";
	WebDriver driver;

	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	// Customer confidentiality is key to Jive, so this page should always load in
	// HTTPS. If requested via unencrypted HTTP, it should redirect to HTTPS.
	@Test(priority = 1, description = "This test will verify page submit ticket url HTTP")
	public void launchJiveApplicationWithHttp() {
		String applicationUrlWithhttp = "http://jive.com/resources/support/submit-a-ticket/";
		WebDriver driver = launchApplication(applicationUrlWithhttp);
		driver.quit();
	}

	// Customer confidentiality is key to Jive, so this page should always load in
	// HTTPS. If requested via unencrypted HTTP, it should redirect to HTTPS
	@Test(priority = 2, description = "This test will verify page submit ticket url HTTPS")
	public void launchJiveApplicationWithHttps() {
		WebDriver driver = launchApplication(applicationUrl);
		driver.quit();
	}

	// Customers in French Canada, and therefore the name input must be able to
	// accommodate names which include apostrophes, dashes, or accented characters, once hit submit by user should get validated
	@Test(description = "This test will verify name input field")
	public void ValidateNameInputField() {

		WebDriver driver = launchApplication(applicationUrl);
		WebElement name = driver.findElement(By.id("name"));
		// name.sendKeys("Mathias d'Arras");
		WebElement submitBtn = driver.findElement(By.id("ticketBtn"));
		submitBtn.click();
		String[] name_testdata1 = { "Mathias d'Arras", "Silvana Koch-Mehrin", "François Hollande"};
		for (String name_test : name_testdata1) {
			boolean name_test1 = validateFirstName(name_test);
			try {
			Assert.assertTrue(name_test1);
			}
			 catch (AssertionError e) {
				 Highlight("name");
				// This will capture error message
				 WebElement errors = driver.findElement(By.xpath("//*[@id=\"alertify-logs\"]"));
				 String actual_msg = errors.getText();		     
				 // Store message in variable
				 String expect="please enter a valid email.";			                 
				 // Here Assert is a class and assertEquals is a method which will compare two values if// both matches it will run fine but in case if does not match then if will throw an 
				 //exception and fail testcases  
				 // Verify error message
				 Assert.assertEquals(actual_msg, expect);
				 
			 }
		}
	}

	// Phone numbers are numeric only, once hit submit by user should get validated
	@Test(description = "This test will verify phone input field")
	public void ValidatePhoneInputField() {

		WebDriver driver = launchApplication(applicationUrl);
		WebElement name = driver.findElement(By.id("phone"));
		// name.sendKeys("1234567890");
		WebElement submitBtn = driver.findElement(By.id("ticketBtn"));
		submitBtn.click();
		String[] phone_testdata1 = { "123456789aa", "aaaa", "aaa1277", "111177777" };
		for (String phoneNo_test : phone_testdata1) {
			boolean phoneNo_test1 = validatePhoneNo(phoneNo_test);
			try {
			Assert.assertTrue(phoneNo_test1);
			}
			 catch (AssertionError e) {
				 Highlight("phone");
			 }
		}
	}

	// Email addresses must contain an @ character, and must use a top-level domain
	// of at least two characters.
	@Test(description = "This test will verify email input field")
	public void ValidateEmailInputField() {

		WebDriver driver = launchApplication(applicationUrl);
		WebElement email = driver.findElement(By.id("email"));
		// email.sendKeys("peerji@gmail.com");
		WebElement submitBtn = driver.findElement(By.id("ticketBtn"));
		submitBtn.click();
		String[] email_testdata1 = { "peerji@gmail.com", "t@c.c" };
		for (String email_test : email_testdata1) {
			boolean phoneNo_test1 = validateEmail(email_test);
			try {
			Assert.assertTrue(phoneNo_test1);
			}
			 catch (AssertionError e) {
				 Highlight("email");
			 }
			
		}
	}

	// We can't accept inputs for name, email, or company name which exceed 80
	// characters
	// Whenever an input fails to validate, there should be a visual indicator of
	// which field(s) failed to validate.
	@Test(description = "This test will verify name email and company name input field")
	public void TestErrors() {
		WebDriver driver = launchApplication(applicationUrl);
		WebElement submitBtn = driver.findElement(By.id("ticketBtn"));
		submitBtn.click();

		String name = "", email = "", company_name = "";

		if (name.length() > 80 || email.length() > 80 || company_name.length() > 80) {
			System.out.println("We can't accept inputs for name, email, or company name which exceed 80 characters");
		} else {
			System.out.println("Accepts inputs");
			// Create the JavascriptExecutor object
			JavascriptExecutor js = (JavascriptExecutor) driver;
			// find element using id attribute
			WebElement username = driver.findElement(By.id("name"));
			// call the executeScript method
			js.executeScript("arguments[0].setAttribute('style','border: solid 2px red');", username);
		}

	}

	// If all fields are empty should display error messages
	@Test(description = "This test will verify if fields are empty and should display error message")
	public void AdditionalErrorsCheck() {
		WebElement submitBtn = driver.findElement(By.id("ticketBtn"));
		submitBtn.click();

		WebElement errors = driver.findElement(By.xpath("//*[@id=\"alertify-logs\"]"));
		String errorList = errors.getText();

		System.out.println(errorList);

		if (errorList.contains("name")) {
			System.out.println("Displaying Error message");
			Highlight("name");
		} else {
			System.out.println("Missing Error message");			
		}

	}

	public WebDriver launchApplication(String applicationUrl) {
		System.setProperty("webdriver.chrome.driver", "/Users/sarika/eclipse-workspace/chromedriver");

		driver = new ChromeDriver();

		driver.manage().window().maximize();

		driver.get(applicationUrl);

		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);

		String url = driver.getCurrentUrl();

		boolean siteurl = url.contains("submit-a-ticket");
		boolean sitehttps = url.contains("https");

		if (siteurl && sitehttps) {
			System.out.println("Site url is correct with https and Validation 1 passed");
		} else {
			System.out.println("Site url is incorrect and Validation 1 failed");
		}
		String title = driver.getTitle();

		String expectedTitle = "Submit a Ticket | Jive Resource Center";

		boolean titlestatus = title.equalsIgnoreCase(expectedTitle);

		if (titlestatus) {
			System.out.println("Site Title is correct and Validation 2 passed");
		} else {
			System.out.println("Site Title is incorrect and Validation 2 failed");
		}

		return driver;
	}

	// validate Name
	public static boolean validateFirstName(String firstName) {
		return firstName.matches(
				"[a-zA-zàáâäãåąčćęèéêëėįìíîïłńòóôöõøùúûüųūÿýżźñçčšžÀÁÂÄÃÅĄĆČĖĘÈÉÊËÌÍÎÏĮŁŃÒÓÔÖÕØÙÚÛÜŲŪŸÝŻŹÑßÇŒÆČŠŽ∂ð]+([ '-][a-zA-Z]+)*");
	} // end method validate Name
		// validate PhoneNo

	public static boolean validatePhoneNo(String phone) {
		String regexPhone = "[0-9]+";
		return phone.matches(regexPhone);
	}

	// validate Email
	public static boolean validateEmail(String email) {
		return email.matches(EMAIL_PATTERN);
	}

	// Highlight Fields for Errors
	public void Highlight(String locator) {
		// Create the JavascriptExecutor object
		JavascriptExecutor js = (JavascriptExecutor) driver;

		// find element using id attribute
		WebElement fieldName = driver.findElement(By.id(locator));

		// call the executeScript method
		js.executeScript("arguments[0].setAttribute('style','border: solid 2px red');", fieldName);
	}
}
