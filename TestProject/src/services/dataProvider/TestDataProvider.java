package services.dataProvider;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import services.helpers.LetterEnum;
import services.helpers.LoginEnum;

public class TestDataProvider {
	private String[] _texts;

	public TestDataProvider() throws IOException {
		String projectDir = System.getProperty("user.dir");
		File file = new File(String.format("%s/src/services/dataProvider/TestData/TestData.txt", projectDir));
		FileInputStream fis = new FileInputStream(file);
		byte[] data = new byte[(int) file.length()];
		fis.read(data);
		fis.close();

		String str = new String(data, "UTF-8");
		_texts = str.split("----");
	}

	public Map<LoginEnum, String> GetAuthData() {
		String text = Stream.of(_texts).filter(t -> t.contains("AuthData")).findFirst().get();
		String[] fields = text.split("\\r?\\n");

		return new HashMap<LoginEnum, String>() {
			{
				put(LoginEnum.Login, GetValue(fields, "Login:"));
				put(LoginEnum.Password, GetValue(fields, "Password:"));
				put(LoginEnum.Domain, GetValue(fields, "Domain:"));
			}
		};
	}

	public List<Map<LetterEnum, String>> GetDraftsData() {
		String[] lettersTexts = Stream.of(_texts).filter(t -> t.contains("Letter") && !t.contains("ToSend"))
				.toArray(String[]::new);

		List<Map<LetterEnum, String>> drafts = new ArrayList<Map<LetterEnum, String>>();

		for (String text : lettersTexts) {
			drafts.add(GetLetter(text));
		}
		return drafts;
	}

	public Map<LetterEnum, String> GetLetterToSendData() {
		String letterToSend = Stream.of(_texts).filter(t -> t.contains("Letter") && t.contains("ToSend")).findFirst()
				.get();
		return GetLetter(letterToSend);
	}

	private Map<LetterEnum, String> GetLetter(String text) {
		String[] fields = text.split("\\r?\\n");

		return new HashMap<LetterEnum, String>() {
			{
				put(LetterEnum.Email, GetValue(fields, "Email:"));
				put(LetterEnum.Subject, GetValue(fields, "Subject:"));
				put(LetterEnum.Body, GetValue(fields, "Body:"));
			}
		};
	}

	private String GetValue(String[] testDataStrings, String splitter) {
		return Stream.of(testDataStrings).filter(s -> s.contains(splitter)).findAny().get().split(splitter)[1].trim();
	}
}