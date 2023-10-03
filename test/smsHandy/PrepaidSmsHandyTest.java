package smsHandy;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PrepaidSmsHandyTest {
	
	private Provider inwi, marocTelecom;
	private PrepaidSmsHandy prepaidSmsHandy, prepaidSmsHandy1;
	
	@BeforeEach
	void setUp() throws Exception {
		inwi = new Provider("inwi");
		marocTelecom = new Provider("marocTelecom");
		prepaidSmsHandy = new PrepaidSmsHandy("+212 638 28 06 10", inwi);
		prepaidSmsHandy1 = new PrepaidSmsHandy("+212 667 98 09 67", marocTelecom);
	}

	@DisplayName("testCanSendSms")
	@Test
	void testCanSendSms() {
		boolean check = prepaidSmsHandy.canSendSms();
		assertEquals(true, check);
	}
	
	@DisplayName("testDeposit")
	@Test
	void testDeposit() {
	    prepaidSmsHandy.deposit(20);
	    int amount = prepaidSmsHandy.getProvider().getCreditForSmsHandy("+212 638 28 06 10");
		assertEquals(120, amount);
	}
	
	@DisplayName("testSendSms")
	@Test
	void testSendSms() {
		prepaidSmsHandy.sendSms("+212 667 98 09 67", "Sms Content");
		int amount = prepaidSmsHandy.getProvider().getCreditForSmsHandy("+212 638 28 06 10");
		assertEquals(90, amount);
	}

}
