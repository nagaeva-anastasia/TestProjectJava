package businessObjects;

import java.io.IOException;
import java.util.Map;

import services.dataProvider.TestDataProvider;
import services.helpers.LoginEnum;

public class User {
	public String Login;

	public String Password;

	public String Domain;

	public User() throws IOException {
		TestDataProvider provider = new TestDataProvider();
		Map<LoginEnum, String> data = provider.GetAuthData();

		Login = data.get(LoginEnum.Login);
		Password = data.get(LoginEnum.Password);
		Domain = data.get(LoginEnum.Domain);
	}
}
