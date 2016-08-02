package finalproject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import finalproject.homeworks.ExcelDataConfig;

public class homeworks {
	WebDriver driver;
	Actions actions; 
	 String recipient,screenshotname,mobile;
	 Logger log = Logger.getLogger("sriramLogger"); 
   ArrayList<String> myList = new ArrayList<String>();
   ArrayList<String> textdeadlines = new ArrayList<String>();
	
   //comments/
   @Test(dataProvider = "studentdata")
   public void NPUStudentLogin(String  username, String password) throws Exception {
 	  captureScreenshot(driver,screenshotname);
 		log.info("step1:initializing firefox");
 	  // Step 1: opening college portal
       openNPUportal();
   		  			
       // Step 2: check  Student Login page
       checkingstudentlogin();
  
       // Step 3: input Username and Password
       InputUsernamePassword(username, password);
            
       // Step 4: click Log On button
       clickLogonbutton();
    
       //step 5: Checkhomeworks
       activities();
           
       //Step 6: Logout
        logout();
        
       //Step 7:Mail Report
       Mailreport();
       
       //step8:Text Notification
        Text(username);
      
       
       //step9:closing browser
       Teardown();
       log.info("step9:closing browser");
   }
   







private void activities() throws InterruptedException {
	// TODO Auto-generated method stub
	 
	 
	 int row_count = driver.findElements(By.xpath(".//*[@id='main']/fieldset[2]/table/tbody/tr")).size();
		System.out.println("Number Of Columns = "+row_count);
		 int col_count = driver.findElements(By.xpath(".//*[@id='main']/fieldset[2]/table/tbody/tr[2]/td")).size();
			System.out.println("Number Of Columns = "+col_count);
		
		String first_part = ".//*[@id='main']/fieldset/table/tbody/tr[";
		String second_part = "]/td["; 
		String third_part = "]";
	
		String mailid = driver.findElement(By.xpath(".//*[@id='main']/fieldset[1]/fieldset[2]/div[6]")).getText();
		
	    //System.out.println(mailid);
		recipient=mailid;
		//Used for loop for number of rows. 
         mobile = driver.findElement(By.xpath(".//*[@id='main']/fieldset[1]/fieldset[2]/div[4]/table/tbody/tr/td[1]")).getText();
         mobile=mobile.replace("Cell Phone: 1 - (", ""); 
         mobile=mobile.replace(")", "");  
         mobile=mobile.replace(" ", "");  
	    System.out.println(mobile);
	    

		for (int i=2; i<=row_count; i++)
		{
			//Used for loop for number of columns. 
			for(int j=1; j<=col_count; j++)
			{
				 if(j==8 )
				{ 
					 WebElement element = driver.findElement(By.xpath(".//*[@id='fancybox-close']"));
					 if (element.isDisplayed()) {
			                try {
			                    element.click();
			                  
			           
			                } catch (Exception e) {
			                    e.printStackTrace();
			                }
					 }
					 driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS); // wait maximum up to 30s until page loaded completely
						Thread.sleep(3000);
					driver.findElement(By.xpath(".//*[@id='main']/fieldset[2]/table/tbody/tr["+i+"]/td[8]/span/span/a[2]")).click();
					driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS); // wait maximum up to 30s until page loaded completely
					Thread.sleep(3000);
					String Table_data = driver.findElement(By.xpath(".//*[@id='main']/fieldset[1]/table/tbody/tr/td[1]/div[2]")).getText();
		
					int count=0;
						for(int k=1;k<15;k++)
						{
							
								
								boolean b = driver.getPageSource().contains("W"+k);
								
								//boolean x = isElementPresent(By.partialLinkText("W"+k));
								if (b){
									
									
										
							driver.findElement(By.partialLinkText("W"+k)).click();
							driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS); // wait maximum up to 30s until page loaded completely
							Thread.sleep(3000);
							boolean option = driver.getPageSource().contains("Submit My Answer Online");
							if(option)
							{
								if(count==0)
							{
								System.out.print(Table_data);
								myList.add(Table_data);
								myList.add("\n");
								count=1;
							}
								
								String Table_data1 = driver.findElement(By.xpath(".//*[@id='main']/fieldset/fieldset[1]/table/tbody/tr[3]/td[2]/span")).getText();
						
								// System.out.print(Table_data1);  

														   
								   
						System.out.println("Week"+k+"-"+Table_data1+"");
							
							myList.add("Week"+k+"-"+Table_data1+" ");
							myList.add("\n");
							
							}
							
								}
								
						}
						driver.navigate().to("https://osc.npu.edu/Student");
						driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS); // wait maximum up to 30s until page loaded completely
						Thread.sleep(3000);
										
					
					
				}
		}	
			//myList.add("\n");
			//System.out.println("");
			
		}

	 System.out.print("Successfull");
  	 
   }

private void Text(String username) {


	
    // TODO Auto-generated method stub
    driver.navigate().to("http://www.textport.com/");
 
    driver.findElement(By.id("ctl00_BodyContent1_TextBoxMobileTo")).clear(); 
	driver.findElement(By.id("ctl00_BodyContent1_TextBoxMobileTo")).sendKeys(mobile);
	driver.findElement(By.id("ctl00_BodyContent1_TextBoxFrom")).sendKeys("Capstoneteam16@gmail.com");
	driver.findElement(By.id("ctl00_BodyContent1_TextBoxMessage")).sendKeys("Hi"+username+", \n We have your Homework Updates Kindly Check you Mail for Details");		

	driver.findElement(By.xpath(".//*[@id='ctl00_BodyContent1_ButtonSubmit']")).click();
 	 driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
 	if(driver.getPageSource().contains("Your message was sent. Message ID:"))
 	{
 		myList.clear();
 	}

 	else
 	{
 		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
 		Text(username);
 	}
 	
}




private void openNPUportal() throws InterruptedException {
	 driver.get("https://osc.npu.edu/Account/");
	 driver.manage().window().maximize(); // maximum window size
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS); // wait maximum up to 30s until page loaded completely
		Thread.sleep(3000); // wait 3s
		
		driver.findElement(By.xpath(".//*[@id='main']/div[2]/a")).click();
		driver.findElement(By.xpath(".//*[@id='main']/div[2]/div[1]/div[1]/a[1]/div[1]/img")).click();
}




private void logout() throws InterruptedException {
	
	 WebElement element = driver.findElement(By.xpath(".//*[@id='fancybox-close']"));
	 if (element.isDisplayed()) {
            try {
                element.click();
              
       
            } catch (Exception e) {
                e.printStackTrace();
            }
	 }
	 driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS); // wait maximum up to 30s until page loaded completely
		Thread.sleep(10000);
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
     String from = "Capstoneteam16@gmail.com";


     // Get system properties
     Properties properties = System.getProperties();

     properties.put("mail.smtp.starttls.enable", "true"); 
     properties.put("mail.smtp.host", "smtp.gmail.com");

     properties.put("mail.smtp.port", "587");
     properties.put("mail.smtp.auth", "true");
     Authenticator authenticator = new Authenticator () {
           public PasswordAuthentication getPasswordAuthentication(){
               return new PasswordAuthentication("Capstoneteam16@gmail.com","Capstoneteam2016");//userid and password for "from" email address 
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
        message.setSubject("Home Work Deadlines");
      
        // Now set the actual message
        String formatedString = myList.toString();
        formatedString=formatedString.replace(",", "");  //remove the commas
        formatedString=formatedString.replace("[", "");  //remove the right bracket
        formatedString= formatedString.replace("]", "");  //remove the left bracket
            
        message.setText("Home Work Updates:\n"+formatedString);
        
        // Send message
        Transport.send(message);
        System.out.println("Sent message successfully....");
     }catch (MessagingException mex) {
        mex.printStackTrace();
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



 private void checkingstudentlogin() {


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
 				 
 			 }
 	
 }
 @DataProvider(name = "studentdata")
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