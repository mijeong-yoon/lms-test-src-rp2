package jp.co.sss.lms.ct.f02_faq;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.Assert.*;

import java.time.Duration;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * 結合テスト よくある質問機能
 * ケース04
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース04 よくある質問画面への遷移")
public class Case04 {

	/** 前処理 */
	@BeforeAll
	static void before() {
		createDriver();
	}

	/** 後処理 */
	@AfterAll
	static void after() {
		closeDriver();
	}

	@Test
	@Order(1)
	@DisplayName("テスト01 トップページURLでアクセス")
	void test01() {
		//ページへの接続
		goTo("http://localhost:8080/lms/");
		//ページタイトル取得
		String title = webDriver.getTitle();

		//トップページURLでアクセスするテストコード検証
		assertEquals("ログイン | LMS", title);

		//evidenceフォルダに保存
		getEvidence(new Object() {
		}, "loginPage");

	}

	@Test
	@Order(2)
	@DisplayName("テスト02 初回ログイン済みの受講生ユーザーでログイン")
	void test02() {
		final WebElement loginId = webDriver.findElement(By.name("loginId"));
		final WebElement password = webDriver.findElement(By.name("password"));
		final WebElement loginBtn = webDriver.findElement(By.className("btn-primary"));

		loginId.clear();
		loginId.sendKeys("StudentAA02");

		password.clear();
		password.sendKeys("StudentAA022");

		//ログイン前の証跡確保
		getEvidence(new Object() {
		}, "Case03beforeLogin");

		loginBtn.click();

		pageLoadTimeout(5);

		assertEquals("http://localhost:8080/lms/course/detail", webDriver.getCurrentUrl());

		//ログイン後の証跡確保
		getEvidence(new Object() {
		}, "Case04afterLogin");

	}

	@Test
	@Order(3)
	@DisplayName("テスト03 上部メニューの「ヘルプ」リンクからヘルプ画面に遷移")
	void test03() {
		webDriver.findElement(By.xpath("//*[@id=\"nav-content\"]/ul[1]/li[4]")).click();

		final WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(60));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"nav-content\"]/ul[1]/li[4]")));
		webDriver.findElement(By.linkText("ヘルプ")).click();

		//ヘルプページがあってるかを検証
		assertEquals("http://localhost:8080/lms/help", webDriver.getCurrentUrl());

		//ヘルプページの証跡確保
		getEvidence(new Object() {
		}, "Case03helpPage");
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「よくある質問」リンクからよくある質問画面を別タブに開く")
	void test04() {
		final WebElement goFaq = webDriver.findElement(By.linkText("よくある質問"));
		goFaq.click();

		WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));
		//ヘルプ画面を（遷移する前に）検証する
		String originalWindow = webDriver.getWindowHandle();
		//新しいタブが開けるように待機
		wait.until(ExpectedConditions.numberOfWindowsToBe(2));
		//遷移前＞＜遷移後の画面が内容違うか確認
		for (String windowHandle : webDriver.getWindowHandles()) {
			if (!originalWindow.contentEquals(windowHandle)) {
				webDriver.switchTo().window(windowHandle);
				break;
			}
		}

		String expectedUrl = "http://localhost:8080/lms/faq";
		String actualUrl = webDriver.getCurrentUrl();
		assertEquals(expectedUrl, actualUrl);

		//よくある質問画面ページの証跡確保
		getEvidence(new Object() {
		}, "Case04faqPage");
	}

}
