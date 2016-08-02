package finalproject;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;


public class Pricesearch {
	WebDriver driver;
	Actions actions; 
	 String recipient,screenshotname;
	 Logger log = Logger.getLogger("sriramLogger"); 
   ArrayList<String> myList = new ArrayList<String>();
	
   private static Pattern patternDomainName;
   private Matcher matcher;
   private static final String DOMAIN_NAME_PATTERN 
 	= "([a-zA-Z0-9]([a-zA-Z0-9\\-]{0,61}[a-zA-Z0-9])?\\.)+[a-zA-Z]{2,6}";
   static {
 	patternDomainName = Pattern.compile(DOMAIN_NAME_PATTERN);
   }
   
	  @Test(dataProvider = "searchdata")
	  public void priceprocess(String  itemsearch, String emailid,String mobile) throws Exception {
		  openbrowser();
		  googleSearch(itemsearch);
		  Set<String>  result=getDataFromGoogle(itemsearch);
		  for(String temp : result){
				System.out.println(temp);
			}
			System.out.println(result.size());
	  }	  
	 
	  
	  
	  
	  public String getDomainName(String url){
			
			String domainName = "";
			matcher = patternDomainName.matcher(url);
			if (matcher.find()) {
				domainName = matcher.group(0).toLowerCase().trim();
			}
			return domainName;
				
		  }
			
		  private Set<String> getDataFromGoogle(String query) {
				
			Set<String> result = new HashSet<String>();	
			String request = "https://www.google.com/search?q=" + query + "&num=20";
			System.out.println("Sending request..." + request);
				
			try {

				// need http protocol, set this as a Google bot agent :)
				Document doc = Jsoup
					.connect(request)
					.userAgent(
					  "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)")
					.timeout(5000).get();

				// get all links
				Elements links = doc.select("a[href]");
				Elements titles = doc.select("h3.r > a");
			
				
				for(Element e: titles){
					  System.out.println("titles: " +e.attr("href"));
				}
				for (Element link : links) {

					String temp = link.attr("href");	
					
					if(temp.startsWith("/url?q=")){
		                                //use regex to get domain name
						result.add(getDomainName(temp));
					}

				}

			} catch (IOException e) {
				e.printStackTrace();
			}
				
			return result;
		  }

	  
	  
	  
	  
	  
	  
	  
	private void openbrowser() throws InterruptedException {
		driver.get("http://www.google.com");
		 driver.manage().window().maximize(); // maximum window size
			driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS); // wait maximum up to 30s until page loaded completely
			Thread.sleep(3000); // wait 3s
	}

		
		public void googleSearch(String itemsearch) throws InterruptedException{
			
			 driver.findElement(By.id("lst-ib")).clear(); // clear the field, good practice
			 driver.findElement(By.id("lst-ib")).sendKeys(itemsearch); // type NPU in the search field
			 Actions actions = new Actions(driver);
			 actions.sendKeys(Keys.ENTER).build().perform();
			 driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			 
			 
			 
		  }

		 private void openSearchResultLink(String itemsearch) {
				
			  driver.findElement(By.partialLinkText(itemsearch)).click();
				 driver.manage().timeouts().implicitlyWait(100, TimeUnit.SECONDS); // wait maximum up to 10s until page loaded completely
				 
			  
			
		}
	@DataProvider(name = "searchdata")
		public Object[][] passData() throws IOException,InvalidFormatException
	{
		ExcelDataConfig config=new ExcelDataConfig("C:/Users/sriram/Desktop/itemsearch.xlsx");
		int rows = config.getRowCount(0);
		Object [][] data=new Object[rows][3];
		for(int i=0;i<rows;i++)
		{
			data[i][0]=config.getData(0,i,0);
			data[i][1]=config.getData(0,i,1);
			data[i][2]=config.getData(0,i,2);
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
			FileInputStream fis=new FileInputStream(new File("C:/Users/sriram/Desktop/itemsearch.xlsx"));
			XSSFWorkbook workbook=new XSSFWorkbook(fis);
			XSSFSheet sheet=workbook.getSheet("sheet1");
			sheet.getRow(row).createCell(3).setCellValue("Read");
			FileOutputStream fos=new FileOutputStream(new File("C:/Users/sriram/Desktop/itemsearch.xlsx"));
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

	@BeforeMethod
	  public void initiate(){

	      driver = new FirefoxDriver();
	  }	

	  public void Teardown() {

	      driver.quit();
	  }  
}