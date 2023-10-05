import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class Sele {
    public static String WEB_DRIVER_ID = "webdriver.chrome.driver"; // Properties 설정
    public static String WEB_DRIVER_PATH = "C:/chromedriver.exe"; // WebDriver 경로
    
	public static void main(String[] args) throws IOException {
		System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);
		
		//WebDriverManager.chromedriver().setup();
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--remote-allow-origins=*");
		WebDriver driver = new ChromeDriver(options);
		driver.manage().window().maximize();
		
		
		// 시작주소
		String startPage="https://toyscience.co.kr/product/list.html?cate_no=29";
		
		//끝나는 페이지 숫자
		int endPageNum=1;

		for ( int startPageNum = 1  ; startPageNum <= endPageNum ; startPageNum++) {
		
			//페이지 별로 돌아가게 첫페이지 시작
			driver.get(startPage+"&page="+startPageNum);
		
			
			//일반div찾기
			WebElement normal = driver.findElement(By.className("xans-product-normalpackage"));
			//리스트 탐색
			WebElement list = normal.findElement(By.className("prdList"));
			//전체목록에서 썸네일 클래스 리스트로 찾기
			List<WebElement> each = list.findElements(By.className("thumb"));
			
			
			//하나씩반복
			//System.out.println(each.size());
			for(int j = 0 ; j < each.size() ; j++) {
				normal = driver.findElement(By.className("xans-product-normalpackage"));
				list = normal.findElement(By.className("prdList"));
				each = list.findElements(By.className("thumb"));
				
				
				//태그찾아서 클릭
				each.get(j).click();

				//이름따기
				String pName = driver.findElement(By.xpath("/html/body/div[3]/div[3]/div[2]/div[2]/div[2]/div[2]/div[1]/table/tbody/tr[1]/td/span")).getText();
				pName = startPageNum +"_"+ j +"_"+ pName.replace("/", "").replace("*", "x").replace(":", "").replace("?", "").replace("!", "").trim();
				System.out.println(pName); 
				
				//썸네일 하나씩
				
				try {
				WebElement listImg = driver.findElement(By.className("listImg")); 
				List<WebElement> eachImg = listImg.findElements(By.className("ThumbImage"));
				//System.out.println(eachImg.size());
					for(int k = 0 ; k < eachImg.size() ; k++) {
						listImg = driver.findElement(By.className("listImg")); 
						eachImg = listImg.findElements(By.className("ThumbImage"));

						//작은거라서 클릭후 재 검색해서 큰 이미지 저장
						eachImg.get(k).click();
						String imgPath=driver.findElement(By.className("BigImage")).getAttribute("src");
						
						//String imgPath = eachImg.get(k).getAttribute("src");
						//System.out.println(imgPath); 현재 작음.
						thumbdownload(k, imgPath,  pName);
					}
				}
					catch (IOException e) {
						System.out.println("ㄴ썸네일다운로드실패");
					}
					
				try {	
				// 디테일 다운로드
				WebElement detailDiv = driver.findElement(By.className("cont"));
				//WebElement detailP = detailDiv.findElement(By.tagName("p"));
				List<WebElement> eachDetailImg = detailDiv.findElements(By.tagName("img"));
					for(int l = 0 ; l < eachDetailImg.size() ; l++) {
						detailDiv = driver.findElement(By.className("cont"));
						//detailP = detailDiv.findElement(By.tagName("p"));
						eachDetailImg = detailDiv.findElements(By.tagName("img"));
						
						String imgPath = eachDetailImg.get(l).getAttribute("src");
						
						//System.out.println(imgPath);
						
						//data형태인경우 안보여서 그런거라 스크롤 내려갔다가 올라온다.
						while(imgPath.startsWith("data:image")) {
							imgPath = eachDetailImg.get(l).getAttribute("src");
							scroll(driver,500);
							delay(100);
							scroll(driver,1000);
							System.out.println("디테일 안보여서 재시도");
						}
				        
						detailDownload(imgPath, pName, l);
						
					}
				}catch (IOException e) {
					System.out.println("ㄴ디테일다운로드실패");
				}	
				//뒤로가기	
				driver.navigate().back();	
			}
			
			
		
		
		}
		driver.quit();
		
		
		

		//로그인부
//		WebElement login= driver.findElement(By.xpath("/html/body/header/div[1]/div/ul[3]/li[1]/a"));
//		login.click();
//		
//		login = driver.findElement(By.xpath("/html/body/div[1]/div/div/form/div/div/fieldset/label[1]/input"));
//		login.click();
//		login.sendKeys("maroo2510");
//		
//		login = driver.findElement(By.xpath("/html/body/div[1]/div/div/form/div/div/fieldset/label[2]/input"));
//		login.click();
//		login.sendKeys("dlwnduf12!");
//	
//		
//		login.findElement(By.xpath("/html/body/div[1]/div/div/form/div/div/fieldset/a"));
//		login.click();
//		driver.get(siteURL);
		
		
		
//			
//			//디테일 다운로드
//			WebElement detaildiv = driver.findElement(By.className("detail_con"));
//			List<WebElement> eachdetail = detaildiv.findElements(By.tagName("img"));
//			for(int k = 0; k < eachdetail.size() ; k++) {
//				String url = eachdetail.get(k).getAttribute("src");
//				detailDownload(url, title_name + "/" + p_name , k);
//				
//			}
//			
//			driver.get(siteURL);
//		}
//		
//		driver.quit();
	}// main()

	
	// thumnail다운로드
		public static void thumbdownload(int i , String imageUrl, String path) throws IOException {
			String extension = getExtension(imageUrl);
			URL url = null;
			InputStream inputStream = null;
			FileOutputStream fileOutputStream = null;

			String filepath = "D:/toysci/" + path + "/";
			File folder = new File(filepath);

			if (!folder.exists())
				folder.mkdirs();
			try {
				url = new URL(imageUrl);
				inputStream = url.openStream();
				fileOutputStream = new FileOutputStream(filepath + i + "_thumb."+extension);
				byte[] buffer = new byte[2048];
				int length;
				while ((length = inputStream.read(buffer)) != -1) {
					fileOutputStream.write(buffer, 0, length);
				}
				inputStream.close();
				fileOutputStream.close();
			} catch (Exception e) {
				System.out.println("ㄴ썸네일다운로드실패");
			}


		}
	
	// detail 다운로드
	public static void detailDownload(String imageUrl, String path, int idx) throws IOException {
		//첫번째 필요없는 이미지 때문에 패스
		//if(idx!=0) {
		String extension = getExtension(imageUrl);
		URL url = null;
		String filepath = "D:/toysci/" + path + "/";
		File folder = new File(filepath);

		if (!folder.exists())
			folder.mkdirs();
		

			try (InputStream inputStream = new URL(imageUrl).openStream();
					FileOutputStream fileOutputStream = new FileOutputStream(filepath + "detail_" + idx + "." + extension)) {
				byte[] buffer = new byte[2048];
				int length;
				while ((length = inputStream.read(buffer)) != -1) {
					fileOutputStream.write(buffer, 0, length);
				}
				inputStream.close();
				fileOutputStream.close();
				
			} catch (IOException e) {
				System.out.println("ㄴ디테일다운로드실패");
			}

		//}
	}

	public static void delay(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	// 스크롤 관리
	public static void scroll(WebDriver driver, int length) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollBy(0," + length + ")", "");
	}
	
	public static String getExtension(String path) {
	    String extension = path.substring(path.lastIndexOf(".") + 1);
	    return extension;
	}
	


        
        

	
	
}
