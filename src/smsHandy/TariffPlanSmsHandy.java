package smsHandy;

public class TariffPlanSmsHandy extends SmsHandy {

	private int remainingFreeSms = 00;

	public TariffPlanSmsHandy(String number, Provider provider) {
		super(number, provider);
		remainingFreeSms = 100;
		provider.register(this);
	}

	@Override
	public boolean canSendSms() {
		if (remainingFreeSms > 0)
			return true;
		else
			return false;
	}

	@Override
	public void payForSms() {

	}

	public int getRemainingFreeSms() {
		return remainingFreeSms;
	}

	public void setRemainingFreeSms(int remainingFreeSms) {
		this.remainingFreeSms = remainingFreeSms;
	}

}
