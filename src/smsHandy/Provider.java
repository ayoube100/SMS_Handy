package smsHandy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Provider {

	private Map<String, Integer> credits;
	private Map<String, SmsHandy> subscriber;
	public static ArrayList<Provider> providers = new ArrayList<>();
	private StringProperty name;

	public Provider(String name) {
		credits = new HashMap<String, Integer>();
		subscriber = new HashMap<String, SmsHandy>();
		providers.add(this);
		this.name = new SimpleStringProperty(name);
	}
	
	public String getName() {
		return name.get();
	}

	public void setName(String number) {
		this.name.set(number);
	}
	
	public StringProperty nameProperty() {
        return name;
    }

	public boolean send(Message m) {
		// subscriber.containsKey(m.getTo())
		if (canSendTo(m.getTo())) {
			if (subscriber.get(m.getFrom()).getProvider() instanceof Provider) {
				
				if (m.getTo().equals("*101#")) {
					
					subscriber.get(m.getFrom()).receiveSms(m);
				
				} else {
					
					if (subscriber.get(m.getFrom()) instanceof PrepaidSmsHandy) {
						
						int new_credit = getCreditForSmsHandy(m.getFrom()) - 10;
						credits.put(m.getFrom(), new_credit);
						
						findProvider(m.getTo()).subscriber.get(m.getTo()).receiveSms(m);
						
					} else if (subscriber.get(m.getFrom()) instanceof TariffPlanSmsHandy) {
						
						TariffPlanSmsHandy t = (TariffPlanSmsHandy) subscriber.get(m.getFrom());
						t.setRemainingFreeSms(t.getRemainingFreeSms() - 1);
						
						findProvider(m.getTo()).subscriber.get(m.getTo()).receiveSms(m);
					}

				}
				return true;
			} else {
				System.out.println("this mobile phone " + m.getFrom() + " not affected to eny provider.");
				return false;
			}

		} else {
			return false;
		}

	}

	public void register(SmsHandy s) {
		subscriber.put(s.getNumber(), s);
	}

	public void deposit(String number, int amount) {

		if (credits.containsKey(number)) {
			int currentAmount = credits.get(number) + amount;
			credits.put(number, currentAmount);
		} else {
			credits.put(number, amount);
		}
	}

	public int getCreditForSmsHandy(String number) {
		return credits.get(number);
	}

	private boolean canSendTo(String receiver) {
		if ((findProvider(receiver) instanceof Provider) || (receiver.equals("*101#")))
			return true;
		else
			return false;
	}

	public static Provider findProvider(String number) {
		for (Provider p : providers) {
			if (p.subscriber.containsKey(number)) {
				SmsHandy s = p.subscriber.get(number);
				return s.getProvider();
			}
		}
		return null;
	}

}
