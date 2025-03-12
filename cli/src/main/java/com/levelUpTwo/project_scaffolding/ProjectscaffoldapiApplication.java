package com.levelUpTwo.project_scaffolding;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Scanner;

@SpringBootApplication
public class ProjectscaffoldapiApplication {
	public static String AccessToken = null;
	public static String RefreshToken = null;
	public static void main(String[] args) {
		SpringApplication.run(ProjectscaffoldapiApplication.class, args);
		
		if(AccessToken == null){
			authenticateUser();
		}
		Scanner input = new Scanner(System.in);
		System.out.println("Press Enter to fetch user info...");
		input.nextLine();
		input.close();
		
		while(AccessToken == null){}

		// Call the /userinfo endpoint on the client (port 8001)
		String apiUserUrl = "http://projectscaff-env.eba-phzwex9m.us-east-1.elasticbeanstalk.com/api/userinfo";
		RestTemplate restTemplate = new RestTemplate();

		try {
			String userDetails = restTemplate.getForObject(apiUserUrl + "?access_token=" + AccessToken, String.class);
			System.out.println("User Details from Server: " + userDetails);
		} catch (Exception e) {
			System.err.println("Error fetching user details: " + e.getMessage());
		}
	}

	@SuppressWarnings("deprecation")
	public static Boolean authenticateUser(){
		Boolean result = false;
		try {
			Runtime.getRuntime().exec("cmd /c start http://localhost:8001");
			Integer tryCount = 12;
			while (AccessToken == null && tryCount > 0) {
				Thread.sleep(5000);			
				--tryCount;	
			}
			Runtime.getRuntime().exec("cmd /c start http://localhost:8001/login/success");
			result = !(AccessToken == null);
		} catch (IOException | InterruptedException e) {
			System.err.println("Please authenticate your google account");
			System.exit(1);
		}
		return result;
	}
}
