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

	public Map<LoginEnum, String> getAuthData() {
		String text = Stream.of(_texts).filter(t -> t.contains("AuthData")).findFirst().get();
		String[] fields = text.split("\\r?\\n");

		return new HashMap<LoginEnum, String>() {
			{
				put(LoginEnum.Login, getValue(fields, "Login:"));
				put(LoginEnum.Password, getValue(fields, "Password:"));
				put(LoginEnum.Domain, getValue(fields, "Domain:"));
			}
		};
	}

	public List<Map<LetterEnum, String>> getDraftsData() {
		String[] lettersTexts = Stream.of(_texts).filter(t -> t.contains("Letter") && !t.contains("ToSend"))
		        .toArray(String[]::new);

		List<Map<LetterEnum, String>> drafts = new ArrayList<Map<LetterEnum, String>>();

		for (String text : lettersTexts) {
			drafts.add(getLetter(text));
		}
		return drafts;
	}

	public Map<LetterEnum, String> getLetterToSendData() {
		String letterToSend = Stream.of(_texts).filter(t -> t.contains("Letter") && t.contains("ToSend")).findFirst()
		        .get();
		return getLetter(letterToSend);
	}

	private Map<LetterEnum, String> getLetter(String text) {
		String[] fields = text.split("\\r?\\n");

		return new HashMap<LetterEnum, String>() {
			{
				put(LetterEnum.Email, getValue(fields, "Email:"));
				put(LetterEnum.Subject, getValue(fields, "Subject:"));
				put(LetterEnum.Body, getValue(fields, "Body:"));
			}
		};
	}

	private String getValue(String[] testDataStrings, String splitter) {
		return Stream.of(testDataStrings).filter(s -> s.contains(splitter)).findAny().get().split(splitter)[1].trim();
	}
}