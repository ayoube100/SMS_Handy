package smsHandy;

import java.util.Date;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Message {

	private StringProperty content;
	private ObjectProperty<Date> date;
	private StringProperty from;
	private StringProperty to;

	public Message() {
	}

	public Message(String from, String to, String content, Date date) {
		this.from = new SimpleStringProperty(from);
		this.to = new SimpleStringProperty(to);
		this.content = new SimpleStringProperty(content);
		this.date = new SimpleObjectProperty<Date>(date);
	}

	public String getContent() {
		return content.get();
	}

	public void setContent(String content) {
		this.content.set(content);
	}

	public StringProperty contentProperty() {
		return content;
	}

	public Date getDate() {
		return date.get();
	}

	public void setDate(Date date) {
		this.date.set(date);
	}

	public ObjectProperty<Date> dateProperty() {
		return date;
	}

	public String getFrom() {
		return from.get();
	}

	public void setFrom(String from) {
		this.from.set(from);
	}

	public StringProperty fromProperty() {
		return from;
	}

	public String getTo() {
		return to.get();
	}

	public void setTo(String to) {
		this.to.set(to);
	}

	public StringProperty toProperty() {
		return to;
	}

	@Override
	public String toString() {
		return "Message [content=" + content + ", date=" + date + ", from=" + from + ", to=" + to + "]";
	}

}
