package ta;

import app.page.*;
import helper.BaseTest;

import org.testng.annotations.*;

import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.firefox.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

public class MyTest extends BaseTest {
	
	FirefoxDriver firefoxDriver;
	InternetExplorerDriver ieDriver;
	
	private String TESTIVIESTI_AIHE="";
	private String TESTILOMAKKEEN_OTSIKKO = "";

	@BeforeTest(alwaysRun=true)
	public void setup() {
		firefoxDriver = new FirefoxDriver();
		ieDriver = new InternetExplorerDriver();
	}

	@AfterTest(alwaysRun=true)
	public void cleanup() {
		firefoxDriver.close();
		ieDriver.close();
	}
	
	
	@Test(groups = { "smoke-test", "functional-test", "Firefox" } )
	public void tc_Firefox_Tyontekija_Login_Logout() {
		setupTest(firefoxDriver, Thread.currentThread().getStackTrace());
		tc_Tyontekija_Login_Logout();
	}

	@Test(groups = { "smoke-test", "functional-test", "IE" } )
	public void tc_IE_Tyontekija_Login_Logout() {
		setupTest(ieDriver, Thread.currentThread().getStackTrace());
		tc_Tyontekija_Login_Logout();
	}

	public void tc_Tyontekija_Login_Logout() {
		
		Tyontekija tyontekija = new Tyontekija(this);
		
		String username="paivi.paivakoti";
		String password="test";
		
		tyontekija.login(username, password);
		
		//Screen capture added for demo purpose
		captureScreenShot();
		
		tyontekija.logout();
		
		//Screen capture added for demo purpose
		captureScreenShot();
		
	}


	@Test(groups = { "smoke-test", "functional-test", "Firefox" } )
	public void tc_Firefox_Huoltaja_Login_Logout() {
		setupTest(firefoxDriver, Thread.currentThread().getStackTrace());
		tc_Huoltaja_Login_Logout();
	}

	
	@Test(groups = { "smoke-test", "functional-test", "IE" } )
	public void tc_IE_Huoltaja_Login_Logout() {
		setupTest(ieDriver, Thread.currentThread().getStackTrace());
		tc_Huoltaja_Login_Logout();
	}
	

	public void tc_Huoltaja_Login_Logout() {
		
		Huoltaja huoltaja = new Huoltaja(this);
		
		String username="kalle.kuntalainen";
		String password="test";
		
		huoltaja.login(username, password);
		
		huoltaja.logout();
		
	}
	

	@Test(groups = { "smoke-test", "functional-test", "Firefox" } )
	public void tc_Firefox_Tyontekija_Sopimukset_Ja_Suunnitelmat() {
		setupTest(firefoxDriver, Thread.currentThread().getStackTrace());
		tc_Tyontekija_Sopimukset_Ja_Suunnitelmat();
	}
	
	@Test(groups = { "smoke-test", "functional-test", "IE" } )
	public void tc_IE_Tyontekija_Sopimukset_Ja_Suunnitelmat() {
		setupTest(ieDriver, Thread.currentThread().getStackTrace());
		tc_Tyontekija_Sopimukset_Ja_Suunnitelmat();
	}

	public void tc_Tyontekija_Sopimukset_Ja_Suunnitelmat() {
		
		Tyontekija tyontekija = new Tyontekija(this);
		
		String username="paivi.paivakoti";
		String password="test";
		
		tyontekija.login(username, password);
		
		tyontekija.sopimuksetJaSuunnitelmat();
		
		tyontekija.logout();
		
	}


	@Test(groups = { "smoke-test", "functional-test", "Firefox" } )
	public void tc_Firefox_Tyontekija_Lokihallinta_Paakayttaja() {
		setupTest(firefoxDriver, Thread.currentThread().getStackTrace());
		tc_Tyontekija_Lokihallinta_Paakayttaja();
	}
	
	@Test(groups = { "smoke-test", "functional-test", "IE" } )
	public void tc_IE_Tyontekija_Lokihallinta_Paakayttaja() {
		setupTest(ieDriver, Thread.currentThread().getStackTrace());
		tc_Tyontekija_Lokihallinta_Paakayttaja();
	}
	
	public void tc_Tyontekija_Lokihallinta_Paakayttaja() {
		
		Tyontekija tyontekija = new Tyontekija(this,Tyontekija.USER_ADMIN);
		
		String username="pertti.paakayttaja";
		String password="test";
		
		tyontekija.login(username, password);
		
		tyontekija.lokihallinta();
		tyontekija.lokihallinta_hae_lokitietoja();
		tyontekija.lokihallinta_takaisin();
		
		tyontekija.lokihallinta_lokitietojen_arkistointi();
		tyontekija.lokihallinta_takaisin();
		
		tyontekija.logout();
		
	}


	@Test(groups = { "smoke-test", "functional-test", "Firefox" } )
	public void tc_Firefox_Tyontekija_Lokihallinta_Ei_oikeuksia() {
		setupTest(firefoxDriver, Thread.currentThread().getStackTrace());
		tc_Tyontekija_Lokihallinta_Ei_oikeuksia();
	}
	
	@Test(groups = { "smoke-test", "functional-test", "IE" } )
	public void tc_IE_Tyontekija_Lokihallinta_Ei_oikeuksia() {
		setupTest(ieDriver, Thread.currentThread().getStackTrace());
		tc_Tyontekija_Lokihallinta_Ei_oikeuksia();
	}
	
	public void tc_Tyontekija_Lokihallinta_Ei_oikeuksia() {
		
		Tyontekija tyontekija = new Tyontekija(this);
		
		String username="paivi.paivakoti";
		String password="test";
		
		tyontekija.login(username, password);
		tyontekija.lokihallinta();
		
		tyontekija.logout();
		
	}
	
	
	@Test(groups = { "smoke-test", "functional-test", "Firefox" } )
	public void tc_Firefox_Tyontekija_Viestit() {
		setupTest(firefoxDriver, Thread.currentThread().getStackTrace());
		tc_Tyontekija_Viestit();
	}
	
	@Test(groups = { "smoke-test", "functional-test", "IE" } )
	public void tc_IE_Tyontekija_Viestit() {
		setupTest(ieDriver, Thread.currentThread().getStackTrace());
		tc_Tyontekija_Viestit();
	}
	
	public void tc_Tyontekija_Viestit() {
		
		Tyontekija tyontekija = new Tyontekija(this);
		
		String username="paivi.paivakoti";
		String password="test";
		
		tyontekija.login(username, password);
		
		tyontekija.viestit();
		
		tyontekija.logout();
		
	}

	
	// About Firefox implementation - Could not get it working reliable -> SKIP Firefox test for now

 	@Test(groups = { "smoke-test", "functional-test", "IE" } )
 	public void tc_IE_Tyontekija_Viestin_lahetys_lapsen_huoltajille() {
		setupTest(ieDriver, Thread.currentThread().getStackTrace());
		tc_Tyontekija_Viestin_lahetys_lapsen_huoltajille();
	}
	
	public void tc_Tyontekija_Viestin_lahetys_lapsen_huoltajille() {
		
		Tyontekija tyontekija = new Tyontekija(this);
		
		String username="paivi.paivakoti";
		String password="test";
		String lapsenHETU="444444-4444";
		
		tyontekija.login(username, password);
		
		tyontekija.viestit();
		
		Tyontekija_Uusi_viesti uusiViesti = new Tyontekija_Uusi_viesti(this);
		
		uusiViesti.set_HETU(lapsenHETU);
		uusiViesti.hae();
		uusiViesti.set_Lisaa_vanhemmat_vastaanottajiksi();
		
		//Store for later usage
		TESTIVIESTI_AIHE = String.format("Testiviesti - %s",getTimeStamp());
		
		uusiViesti.set_Aihe( TESTIVIESTI_AIHE );
		uusiViesti.set_Viesti("Testiviestin sisältö...");
		uusiViesti.laheta();
		
		uusiViesti.browserBack();

		//Wait before checking outbox (i.e. not visible immediately)
		pauseSeconds(10);
		
		Tyontekija_Lahetetyt lahetetyt= new Tyontekija_Lahetetyt(this);
		
		assert(lahetetyt.subjectFound( TESTIVIESTI_AIHE ));
					
		tyontekija.logout();
		
	}

	
	@Test(groups = { "smoke-test", "functional-test", "Firefox" }, dependsOnMethods={"tc_IE_Tyontekija_Viestin_lahetys_lapsen_huoltajille"} )
	public void tc_Firefox_Huoltaja_Viestin_vastaanotto() {
		setupTest(firefoxDriver, Thread.currentThread().getStackTrace());
		tc_Huoltaja_Viestin_vastaanotto();
	}


	@Test(groups = { "smoke-test", "functional-test", "IE" }, dependsOnMethods={"tc_IE_Tyontekija_Viestin_lahetys_lapsen_huoltajille"} )
	public void tc_IE_Huoltaja_Viestin_vastaanotto() {
		setupTest(ieDriver, Thread.currentThread().getStackTrace());
		tc_Huoltaja_Viestin_vastaanotto();
	}

	
	public void tc_Huoltaja_Viestin_vastaanotto() {
		
		Huoltaja huoltaja = new Huoltaja(this);
		
		String username="kalle.kuntalainen";
		String password="test";

		huoltaja.login(username, password);
		
		Huoltaja_Saapuneet saapuneet = new Huoltaja_Saapuneet(this);
		
		assert(saapuneet.subjectFound( TESTIVIESTI_AIHE ));
		
		huoltaja.logout();
		
	}
	

	@Test(groups = { "smoke-test", "functional-test", "IE" } )
	public void tc_IE_Tyontekija_Pyynnot_Luo_uusi_pohja() {
		setupTest(ieDriver, Thread.currentThread().getStackTrace());
		tc_Tyontekija_Pyynnot_Luo_uusi_pohja();
	}

	public void tc_Tyontekija_Pyynnot_Luo_uusi_pohja() {
		
		Tyontekija tyontekija = new Tyontekija(this);
		
		String username="paivi.paivakoti";
		String password="test";

		tyontekija.login(username, password);
		
		Tyontekija_Pyynnot_Luo_uusi_pohja uusiPohja = new Tyontekija_Pyynnot_Luo_uusi_pohja(this);
		
		TESTILOMAKKEEN_OTSIKKO = String.format("Testilomake - %s",getTimeStamp());
		uusiPohja.set_Otsikko( TESTILOMAKKEEN_OTSIKKO );
		
		uusiPohja.lisaa_Kysymys_Vastaustyyppi_Vapaa_tekstikentta("Mitä kuuluu ?");
		uusiPohja.lisaa_Kysymys_Vastaustyyppi_Kylla_Ei("Onko toivomuksia ?");
		uusiPohja.lisaa_Kysymys_Vastaustyyppi_Numero("Montako kutsutaan ?");
		
		uusiPohja.set_Pohjan_nakyvyys_Nakyy_vain_minulle();
		
		uusiPohja.tallennaKyselypohja();
		
		//TODO: tarkistus
		
		tyontekija.logout();
		
	}

	 
}
