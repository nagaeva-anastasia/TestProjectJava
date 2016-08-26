package businessObjects;

import java.util.Map;
import services.helpers.LetterEnum;

public class Letter {
	public String EmailAddress;

	public String Subject;

	public String Body;

	public Letter(Map<LetterEnum, String> data) {
		EmailAddress = data.get(LetterEnum.Email);
		Subject = data.get(LetterEnum.Subject);
		Body = data.get(LetterEnum.Body);
	}
}
