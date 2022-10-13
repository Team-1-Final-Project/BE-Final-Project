package com.innovation.backend.global.util;

import com.innovation.backend.domain.Recommends.domain.Recommends;
import com.innovation.backend.domain.Recommends.repository.RecommendsRepository;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SeleniumUtil {

    private final RecommendsRepository recommendsRepository;

    @PostConstruct
    public void saveOfflineList() {
        // 데이터가 없을경우 스크래핑
        if (recommendsRepository.count() == 0) {
        Path path = Paths.get("C:\\chromedriver.exe");

        // WebDriver 경로 설정
        System.setProperty("webdriver.chrome.driver", path.toString());

        // WebDriver 옵션 설정
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");            // 전체화면으로 실행
        options.addArguments("--disable-popup-blocking");    // 팝업 무시
        options.addArguments("--disable-default-apps");
        ChromeDriver driver = new ChromeDriver(options);
        driver.get("https://map.seoul.go.kr/smgis2/seoulStory?tid=11103395&gucd=");
        List<Recommends> recommendsList = new ArrayList<>();
        List<WebElement> elements2 = driver.findElements(By.cssSelector("#divPaging span > a"));
        for (int i = 3; i <= elements2.size() - 1; i++) {
            elements2 = driver.findElements(By.cssSelector("#divPaging span > a"));

            if (i >= elements2.size()){
                driver.quit();
                return;
            }

            WebElement element2 = driver.findElement(By.cssSelector("#divPaging span:nth-child(" + i + ") > a"));

            try {
                element2.click();
                Thread.sleep(200);
                List<WebElement> elements = driver.findElements(By.cssSelector("#ulContentsList li > a"));
                for (WebElement element : elements) {
                    try {
                         elements = driver.findElements(By.cssSelector("#ulContentsList li > a"));
                        System.out.println(elements+"es");
                         if (elements.size() == 0) {
                             System.out.println("멈챠!");
                             driver.quit();
                             return;
                         }
                        element.click();
                        Thread.sleep(100);
                        String title = element.getText().trim();
                        WebElement el = driver.findElement(By.cssSelector("#ulThemeBaseInfo li:nth-child(2)"));
                        String address = el.getText().substring(4).trim();
                        Recommends recommends = new Recommends(title, address, "offLine");
                        recommendsRepository.save(recommends);
                        recommendsList.add(recommends);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Thread.sleep(100);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (i == 8) {
                i = 3;
            }
        }
        driver.quit();
    }
}
}
