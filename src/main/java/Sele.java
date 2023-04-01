import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Sele {
	public static void main(String[] args) throws IOException {

		WebDriverManager.chromedriver().setup();

		ChromeOptions options = new ChromeOptions();
		options.addArguments("--remote-allow-origins=*");
		WebDriver driver = new ChromeDriver(options);
		driver.manage().window().maximize();
		// 시작주소
		driver.get("https://myscience.co.kr/goods/goods_list.php?cateCd=067009");

		delay(2000);

		WebElement title = driver.findElement(By.className("cg-main"));

		String title_name = title.findElement(By.tagName("h2")).getText().replace("/", "");

		try {

			WebElement search = driver.findElement(By.className("lower-category"));
			List<WebElement> links = search.findElements(By.tagName("a"));

				for (int i = 0; i < links.size(); i++) {
					// 카테고리 명 긁어오기
					search = driver.findElement(By.className("lower-category"));
					links = search.findElements(By.tagName("a"));
					
					//if(i==0) { i=2; }
					String mid_name = links.get(i).getText().replace("/", "");
					
						System.out.println(i + "번째");
						System.out.println(mid_name);

						links.get(i).click();
						delay(2000);
						// 여기서 페이지 판단도 해줘야됨..
						driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL, Keys.END);

						WebElement page = driver.findElement(By.className("pagination"));
						List<WebElement> pages = page.findElements(By.tagName("li"));
						System.out.println(pages.size());
						for (int y = 1; y <= pages.size(); y++) {
							page = driver.findElement(By.className("pagination"));
							pages = page.findElements(By.tagName("li"));

							if (y != 1) {

								pages.get(y - 1).findElement(By.tagName("a")).click();
								delay(2000);
								extracted(driver, title_name, i, mid_name);
							} else {
								driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL, Keys.HOME);
								extracted(driver, title_name, i, mid_name);
							}
						}
					 

				}

			

		} catch (Exception e) {
			System.out.println("lower-category 없음");
			driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL, Keys.END);
			WebElement page = driver.findElement(By.className("pagination"));
			List<WebElement> pages = page.findElements(By.tagName("li"));
			for (int y = 1; y <= pages.size(); y++) {
				page = driver.findElement(By.className("pagination"));
				pages = page.findElements(By.tagName("li"));
				if (y != 1) {
					pages.get(y - 1).findElement(By.tagName("a")).click();
					delay(2000);
					extracted(driver, title_name);
				} else {
					driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL, Keys.HOME);
					extracted(driver, title_name);
				}
			}
		}

		driver.quit();
	}// main()

	private static void extracted(WebDriver driver, String title_name, int i, String mid_name) throws IOException {
		WebElement list = driver.findElement(By.className("list"));
		List<WebElement> each = list.findElements(By.className("txt"));
		for (int j = 0; j < each.size(); j++) {
			scroll(driver, 100 * j);
			list = driver.findElement(By.className("list"));
			each = list.findElements(By.className("txt"));

			String p_name = each.get(j).findElement(By.tagName("a")).getText().replace("/", "").replace("*", "x")
					.replace(":", "").replace("?", "").trim();
			;
			System.out.println(p_name);

			// 들어감
			each.get(j).findElement(By.tagName("a")).click();
			String path = title_name + "/" + i + "_" + mid_name + "/" + j + "_" + p_name;
			WebElement thumbzone = driver.findElement(By.className("slick-list"));
			List<WebElement> thumbs = thumbzone.findElements(By.className("middle"));

			// 썸네일 다운로드 반복
			for (int k = 0; k < thumbs.size(); k++) {
				String source = thumbs.get(k).getAttribute("src");
				thumbdownload(source, path, k);
			}
			driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL, Keys.END);

			// 디테일 다운로드 반복
			WebElement detail = driver.findElement(By.className("txt-manual"));
			List<WebElement> details = detail.findElements(By.tagName("img"));
			for (int l = 0; l < details.size(); l++) {
				String source = details.get(l).getAttribute("src");
				detailDownload(source, path, l);
			}

			// 뒤로가기
			driver.navigate().back();
			delay(2000);

		}
	}
	

	private static void extracted(WebDriver driver, String title_name) throws IOException {

		WebElement list = driver.findElement(By.className("list"));
		List<WebElement> each = list.findElements(By.className("txt"));
		for (int j = 0; j < each.size(); j++) {
			scroll(driver, 100 * j);
			list = driver.findElement(By.className("list"));
			each = list.findElements(By.className("txt"));

			String p_name = each.get(j).findElement(By.tagName("a")).getText().replace("/", "").replace("*", "x")
					.replace(":", "").replace("?", "").trim();
			;
			System.out.println(p_name);

			// 들어감
			each.get(j).findElement(By.tagName("a")).click();
			String path = title_name + "/" + j + "_" + p_name;
			WebElement thumbzone = driver.findElement(By.className("slick-list"));
			List<WebElement> thumbs = thumbzone.findElements(By.className("middle"));

			// 썸네일 다운로드 반복
			for (int k = 0; k < thumbs.size(); k++) {
				String source = thumbs.get(k).getAttribute("src");
				thumbdownload(source, path, k);
			}
			driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL, Keys.END);

			// 디테일 다운로드 반복
			WebElement detail = driver.findElement(By.className("txt-manual"));
			List<WebElement> details = detail.findElements(By.tagName("img"));
			for (int l = 0; l < details.size(); l++) {
				String source = details.get(l).getAttribute("src");
				detailDownload(source, path, l);
			}

			// 뒤로가기
			driver.navigate().back();
			delay(2000);

		}
	}

	// thumnail다운로드
	public static void thumbdownload(String imageUrl, String path, int idx) throws IOException {
		URL url = null;
		InputStream inputStream = null;
		FileOutputStream fileOutputStream = null;

		String filepath = "D:/myscience/" + path + "/";
		File folder = new File(filepath);

		if (!folder.exists())
			folder.mkdirs();
		try {
			url = new URL(imageUrl);
			inputStream = url.openStream();
			fileOutputStream = new FileOutputStream(filepath + idx + "_thumb.jpg");
			byte[] buffer = new byte[2048];
			int length;
			while ((length = inputStream.read(buffer)) != -1) {
				fileOutputStream.write(buffer, 0, length);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		inputStream.close();
		fileOutputStream.close();

	}

	// detail 다운로드
	public static void detailDownload(String imageUrl, String path, int idx) throws IOException {
		URL url = null;
		String filepath = "D:/myscience/" + path + "/";
		File folder = new File(filepath);

		if (!folder.exists())
			folder.mkdirs();

		try (InputStream inputStream = new URL(imageUrl).openStream();
				FileOutputStream fileOutputStream = new FileOutputStream(filepath + "detail_" + idx + ".jpg")) {
			byte[] buffer = new byte[2048];
			int length;
			while ((length = inputStream.read(buffer)) != -1) {
				fileOutputStream.write(buffer, 0, length);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
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
}
