package smsHandy;

public class PrepaidSmsHandy extends SmsHandy {

	private static final int COST_PER_SMS = 10;

	public PrepaidSmsHandy(String number, Provider provider) {
		super(number, provider);
		provider.deposit(number, 100);
		provider.register(this);
	}

	@Override
	public boolean canSendSms() {
		if (getProvider().getCreditForSmsHandy(getNumber()) >= COST_PER_SMS)
			return true;
		else
			return false;
	}

	@Override
	public void payForSms() {
	}

	public void deposit(int amount) {
		getProvider().deposit(getNumber(), amount);
	}

}
