package smsHandy;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TariffPlanSmsHandyTest {
	
	private Provider marocTelecom;
	private TariffPlanSmsHandy tariffPlanSmsHandy, tariffPlanSmsHandy1;
	
	@BeforeEach
	void setUp() throws Exception {
		marocTelecom = new Provider("marocTelecom");
		tariffPlanSmsHandy = new TariffPlanSmsHandy("+212 638 28 06 10", marocTelecom);
		tariffPlanSmsHandy1 = new TariffPlanSmsHandy("+212 667 98 09 67", marocTelecom);
	}

	@DisplayName("testCanSendSms")
	@Test
	void testCanSendSms() {
		boolean check = tariffPlanSmsHandy.canSendSms();
		assertEquals(true, check);
	}

	@DisplayName("testSendSms")
	@Test
	void testSendSms() {
		tariffPlanSmsHandy.sendSms("+212 667 98 09 67", "Sms Content");
		int amount = tariffPlanSmsHandy.getRemainingFreeSms();
		assertEquals(99, amount);
	}

}
