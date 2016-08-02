package finalproject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.apache.commons.io.FileUtils;

public class attendance {
	WebDriver driver;
	Actions actions; 
	 String recipient,screenshotname;
	 Logger log = Logger.getLogger("sriramLogger"); 
   ArrayList<String> myList = new ArrayList<String>();
  String SearchText="northwestern polytechnic university fremont ca usa", 
		  LinkText="Accreditation - Northwestern Polytechnic University";
  @Test(dataProvider = "studentdata")
  public void NPUStudentLogin(String  username, String password) throws Exception {
	  captureScreenshot(driver,screenshotname);
		log.info("step1:initializing firefox");
	  // Step 1: open NPU from google search
      //  openNPUFromGoogleSearch(SearchText, LinkText);
    //  log.info("Step 1: open NPU from google search");
		 driver.navigate().to("https://osc.npu.edu/Account/");
		 driver.manage().timeouts().implicitlyWait(40, TimeUnit.SECONDS);
			driver.findElement(By.xpath(".//*[@id='main']/div[2]/div[1]/div[1]/a[1]/div[1]/img")).click();
      // Step 2: click Student Login to open Student Login page
    //  openStudentLogin();
      driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
      log.info("Step 2: click Student Login to open Student Login page");
      
      
      // Step 3: check Student Login page title
  //    checkOpenStudentLogin("Log On");
      captureScreenshot(driver,"step3");
      log.info("Step 3: check Student Login page title");
      
      // Step 4: input Username and Password
      InputUsernamePassword(username, password);
      captureScreenshot(driver,"step4");
      log.info("Step 4: input Username and Password");
      
      // Step 5: click Log On button
      clickLogonbutton();
      captureScreenshot(driver,"step5");
      log.info("Step 5: click Log On button");
      
      //Step 6:attendance report
      attendancereport();
      captureScreenshot(driver,"step6");
      log.info("Step 6:attendance report");
      
    //Step 7:Mail Report
      Mailreport();
       log.info("Step 7:Mail Report");
      
    //   Step 8: Logout
       logout();
      captureScreenshot(driver,"step7");
      log.info("Step 8: Logout");
      
      //step9:closing browser
      Teardown();
      log.info("step9:closing browser");
  }
  



private void logout() throws InterruptedException {
    driver.findElement(By.cssSelector("a[href='/Account/LogOff")).click();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS); // wait maximum up to 30s until page loaded completely
		Thread.sleep(10000); // wait 10s
	
}




public String captureScreenshot(WebDriver driver, String screenshotname) 
	
	{
		
		try {
				TakesScreenshot ts = (TakesScreenshot)driver;
		
				File source = ts.getScreenshotAs(OutputType.FILE);
				
				String dest = "./screenshot/" + screenshotname + ".png";
				
				File destination = new File(dest);
		
				//FileUtils.copyFile(source, new File("./screenshot/" + screenshotname + ".png"));
				
				FileUtils.copyFile(source, destination);
			
				System.out.println("screnshot taken");
				
				return dest;
				
		} 
		catch (Exception e) 
		{
			System.out.println("Exception while taking screenshot." + e.getMessage());
			return e.getMessage();
		}
		 
	}





private void Mailreport() {
	   // Recipient's email ID needs to be mentioned.
	
    String to = recipient;

    // Sender's email ID needs to be mentioned
    String from = "srifans2009@gmail.com";


    // Get system properties
    Properties properties = System.getProperties();

    properties.put("mail.smtp.starttls.enable", "true"); 
    properties.put("mail.smtp.host", "smtp.gmail.com");

    properties.put("mail.smtp.port", "587");
    properties.put("mail.smtp.auth", "true");
    Authenticator authenticator = new Authenticator () {
          public PasswordAuthentication getPasswordAuthentication(){
              return new PasswordAuthentication("srifans2009@gmail.com","ramgadulocal");//userid and password for "from" email address 
          }
      };

      Session session = Session.getDefaultInstance( properties , authenticator);  
    try{
       // Create a default MimeMessage object.
       MimeMessage message = new MimeMessage(session);

       // Set From: header field of the header.
       message.setFrom(new InternetAddress(from));

       // Set To: header field of the header.
       message.addRecipient(Message.RecipientType.TO,
                                new InternetAddress(to));

       // Set Subject: header field
       message.setSubject("NPU Class Report ");
     
       // Now set the actual message
   
       message.setText("Attendance Report:\n"+myList);
       
       // Send message
       Transport.send(message);
       System.out.println("Sent message successfully....");
    }catch (MessagingException mex) {
       mex.printStackTrace();
    }
}




private void attendancereport() throws InterruptedException {
	
	//8.Looking attendance
		driver.findElement(By.cssSelector("a[href='/Student/StuInfo/Attendance?enc=YlL9BQWX9fPV9ei3GyyrOg/GFMfVQZkxnDPRdqDFjBNrxxZZCC01giG8MirITsMM")).click();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS); // wait maximum up to 30s until page loaded completely
		Thread.sleep(3000); // wait 3s
		
		//9.Looking Overview attendance
		driver.findElement(By.cssSelector("a[href='/Student/StuInfo/AttendanceOverview?enc=ALDZaqZ/THGNBi0/LQG++ezutYKM+MK0PQhDuKOJsWwQV7EhpFmSNqO0Nrn/zM0xFj7EhqY/B4FYr4i0Kd1+eiYdLx6r0EE958UQoOEc4bYEtpst9LuGHAygG5YgXKnLaXWqOAejpwKfnWX2A0494TXggBrVUsu2RX3CCasNjM8aq1fHVrP+x3DqkvXjNAV87WsFVWRdtyUWSDe0wkfuYqMvybh/RvCji97oL9hBSD5dtaI8gm4uO2+fKB7QDjlVQ4tKc70V+QIZyq3MQ+iKm2wju12wR797QkDE7kuQHJkb5FVlTNNBlibCSQE3cdaxu7iybjAs5kME/N/X/z+sr45PnhEHqERKkmf0cCVcWM5TU3A6BP4jJsYpXHR+hM4pn3i4g0xJSsaYrgiCtFa69hKpRsCyrtrr6VKofZ3CeWHkICZUEXY6yahlzhfPwzJvXsO+EUW6I6UzPAa/AVgKTEmc+Q+15x9zp3rwIHpgNoOCvtmdn8LoixnuzpntzkNFlAMtntqXu/wzTTaCHadIJQd8B36TF2/peN46+OlNhdA=")).click();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS); // wait maximum up to 30s until page loaded completely
		Thread.sleep(3000); // wait 3s
		
		//Get number of rows In table. 
		//int Row_count = driver.findElements(By.xpath(".//*[@id='main']/fieldset/table/tbody/tr")).size();
		//System.out.println("Number Of Rows = "+Row_count); 
		//Get number of columns In table. 
		int Col_count = driver.findElements(By.xpath(".//*[@id='main']/fieldset/table/tbody/tr[9]/td")).size();
		//System.out.println("Number Of Columns = "+Col_count);
		
		
		String first_part = ".//*[@id='main']/fieldset/table/tbody/tr[";
		String second_part = "]/td["; 
		String third_part = "]";
	
		String mailid = driver.findElement(By.xpath(".//*[@id='main']/fieldset[1]/fieldset[2]/div[6]")).getText();
	    //System.out.println(mailid);
		recipient=mailid;
		//Used for loop for number of rows. 

		for (int i=9; i<=12; i++)
		{
			//Used for loop for number of columns. 
			for(int j=1; j<=Col_count; j++)
			{
				if(j==2 || j==4 )
			{//Prepared final xpath of specific cell as per values of i and j. 
				String final_xpath = first_part+i+second_part+j+third_part;
				//Will retrieve value from located cell and print It. 
				String Table_data = driver.findElement(By.xpath(final_xpath)).getText();
			//	System.out.print(Table_data +" ");
				myList.add(Table_data);
				
		    }
		}	
			myList.add(" ");
			//System.out.println("");
			
		}
				
}



private void clickLogonbutton() {
	  driver.findElement(By.cssSelector("input[value='Log On']")).click();
	  driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	
}
private void InputUsernamePassword(String username, String password) {
	  driver.findElement(By.id("UserName")).clear(); // clear the field, good practice
		driver.findElement(By.id("UserName")).sendKeys(username); // type NPU in the search field

		driver.findElement(By.id("Password")).clear(); // clear the field, good practice
		driver.findElement(By.id("Password")).sendKeys(password); 
		 driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
}
private void checkOpenStudentLogin(String title) {
Assert.assertTrue(driver.getTitle().contains(title),"Log on page failure");
	
}


private void openStudentLogin() {


		driver.findElement(By.xpath(".//*[@id='block-tb-megamenu-menu-header-top-menu']/div/div/div/ul/li[6]/a")).click();
		driver.findElement(By.xpath(".//*[@id='tb-megamenu-column-21']/div/ul/li[3]/a")).click();

	 driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS); 

	  String expectedTitle="https://osc.npu.edu/Account/";
   	 String actualTitle=driver.getCurrentUrl();
	//	System.out.println(actualTitle);
		
			 if (actualTitle.contains(expectedTitle))
	    	 {
	    	        System.out.println("Verification Successful - The correct title is displayed on the web page.");
	    	 } 
			 else
			 {
				 System.out.println("Verification failed ");
				 driver.navigate().to("https://osc.npu.edu/Account/");
			 }
	
}@DataProvider(name = "studentdata")
public Object[][] passData() throws IOException,InvalidFormatException
{
	ExcelDataConfig config=new ExcelDataConfig("C:/Users/sriram/Desktop/credentials.xlsx");
	int rows = config.getRowCount(0);
	Object [][] data=new Object[rows][2];
	for(int i=0;i<rows;i++)
	{
		data[i][0]=config.getData(0,i,0);
		data[i][1]=config.getData(0,i,1);
	    writeDataToExcelSheet(i);
	}
	  return data;
}
public class ExcelDataConfig
{
	XSSFWorkbook wb;
	XSSFSheet sheet1;
	public ExcelDataConfig(String excelPath)
	{
try{
	File src=new File(excelPath);
	FileInputStream fis=new FileInputStream(src);
	wb=new XSSFWorkbook(fis);

	}
catch(Exception e)
{
	System.out.println(e.getMessage());
}
}
	public String getData(int sheetNumber,int row,int column){
	sheet1=wb.getSheetAt(sheetNumber);
	String data=sheet1.getRow(row).getCell(column).getStringCellValue();
	return data;
	}
	public int getRowCount(int sheetIndex){
		int row=wb.getSheetAt(sheetIndex).getLastRowNum();
		row=row+1;
		return row;
	}
}
public void writeDataToExcelSheet(int row) throws IOException,InvalidFormatException{
	try{
		FileInputStream fis=new FileInputStream(new File("C:/Users/sriram/Desktop/credentials.xlsx"));
		XSSFWorkbook workbook=new XSSFWorkbook(fis);
		XSSFSheet sheet=workbook.getSheet("sheet1");
		sheet.getRow(row).createCell(2).setCellValue("Pass");
		FileOutputStream fos=new FileOutputStream(new File("C:/Users/sriram/Desktop/credentials.xlsx"));
		workbook.write(fos);
				fos.close();
		workbook.close();
		}
	catch(FileNotFoundException e){
		e.printStackTrace();
	  }
		catch(IOException ex)
		{
		ex.printStackTrace();	
		}
}
  public void openNPUFromGoogleSearch(String SearchText,String LinkText) throws Exception
  {
	  googleSearch(SearchText);
	  captureScreenshot(driver,"step1");
	  openSearchResultLink(LinkText);
	  captureScreenshot(driver,"step2");
      
  }
public void googleSearch(String search) throws InterruptedException{
	
	driver.get("http://www.google.com");
	driver.manage().window().maximize(); // maximum window size
	driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS); // wait maximum up to 30s until page loaded completely
	Thread.sleep(3000); // wait 3s
	 driver.findElement(By.id("lst-ib")).clear(); // clear the field, good practice
	 driver.findElement(By.id("lst-ib")).sendKeys(search); // type NPU in the search field
	 Actions actions = new Actions(driver);
	 actions.sendKeys(Keys.ENTER).build().perform();
	 driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }
  private void openSearchResultLink(String link) {
	
	  driver.findElement(By.partialLinkText(link)).click();
		 driver.manage().timeouts().implicitlyWait(100, TimeUnit.SECONDS); // wait maximum up to 10s until page loaded completely
		 
	  
	
}

  @BeforeMethod
  public void initiate(){

      driver = new FirefoxDriver();
  }
 
  

  public void Teardown() {

      driver.quit();
  }  

}