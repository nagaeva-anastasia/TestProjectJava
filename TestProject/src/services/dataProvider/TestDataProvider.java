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

/**
 * Class provides data from TestData.txt file
 * 
 * @author Vyacheslav Milashov
 */

// move functionality into BaseTest
public class TestDataProvider {
	private String[] _texts;

	public TestDataProvider() throws IOException {
		String projectDir = System.getProperty("user.dir");
		// move path to properties file
		File file = new File(String.format("%s/src/services/dataProvider/TestData/TestData.txt", projectDir));
		FileInputStream fis = new FileInputStream(file);
		byte[] data = new byte[(int) file.length()];
		fis.read(data);
		fis.close();

		String str = new String(data, "UTF-8");
		_texts = str.split("----");
	}

	/**
	 * Method returns authorization data
	 * 
	 * @author Vyacheslav Milashov
	 */

	public Map<LoginEnum, String> getAuthData() {
		String text = Stream.of(_texts).filter(t -> t.contains("AuthData")).findFirst().get();
		String[] fields = text.split("\\r?\\n");

		return new HashMap<LoginEnum, String>() {
			{
				put(LoginEnum.Login, getFieldValue(fields, "Login:"));
				put(LoginEnum.Password, getFieldValue(fields, "Password:"));
				put(LoginEnum.Domain, getFieldValue(fields, "Domain:"));
			}
		};
	}

	/**
	 * Method returns drafts data
	 * 
	 * @author Vyacheslav Milashov
	 */

	public List<Map<LetterEnum, String>> getDraftsData() {
		String[] lettersTexts = Stream.of(_texts).filter(t -> t.contains("Letter") && !t.contains("ToSend"))
		        .toArray(String[]::new);

		List<Map<LetterEnum, String>> drafts = new ArrayList<Map<LetterEnum, String>>();

		for (String text : lettersTexts) {
			drafts.add(getLetter(text));
		}
		return drafts;
	}

	/**
	 * Method returns letter for sending
	 * 
	 * @author Vyacheslav Milashov
	 */

	public Map<LetterEnum, String> getLetterToSendData() {
		String letterToSend = Stream.of(_texts).filter(t -> t.contains("Letter") && t.contains("ToSend")).findFirst()
		        .get();
		return getLetter(letterToSend);
	}

	/**
	 * Method returns single letter from provider
	 * 
	 * @param test
	 * @return map of ...
	 * 
	 * @author Vyacheslav Milashov
	 */

	private Map<LetterEnum, String> getLetter(String text) {
		String[] fields = text.split("\\r?\\n");

		return new HashMap<LetterEnum, String>() {
			{
				put(LetterEnum.Email, getFieldValue(fields, "Email:"));
				put(LetterEnum.Subject, getFieldValue(fields, "Subject:"));
				put(LetterEnum.Body, getFieldValue(fields, "Body:"));
			}
		};
	}

	/**
	 * Method returns field value from provider
	 * 
	 * @author Vyacheslav Milashov
	 */

	private String getFieldValue(String[] testDataStrings, String splitter) {
		return Stream.of(testDataStrings).filter(s -> s.contains(splitter)).findAny().get().split(splitter)[1].trim();
	}
}