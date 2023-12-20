package com.quiz_app.testcases;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;

public class QuizAppAPITest {
	public static Logger log = LogManager.getLogger(QuizAppAPITest.class);

	@BeforeClass
	public void setup() {
		// Define the base URL of the API
		RestAssured.baseURI = "http://192.168.0.173:8085/quiz";
	}

	@Description("positiveTestingOfAllSubjects")
	@Test(priority = 1)
	public void positiveTestingOfAllSubjects() {
		// Send a GET request to the API endpoint
		Response response = RestAssured.get("/subjects/all");
		// Extract the response body as a string
		String responseBody = response.getBody().asString();
		log.info("Response Body:\n" + responseBody);

		Assert.assertEquals(responseBody.contains("Maths"), true, "value not present in response body");
		log.info("Maths value present in response body");

		// Extract and validate the response
		int statusCode = response.getStatusCode();

		// Verify that the response status code is 200 (OK)
		Assert.assertEquals(statusCode, 200, "Incorrect status code");
		log.info("Correct status code received");

		String statusLine = response.getStatusLine();
		Assert.assertEquals(statusLine, "HTTP/1.1 200 ", "Incorrect status line");
		log.info("Correct status line received");

		// Verify that the response body contains the expected data
		String expectedData = "[{\"subjectName\":\"Maths\"},{\"subjectName\":\"Science\"}]";
		Assert.assertEquals(responseBody, expectedData, "Invalid response body");

		// Get the response time in milliseconds
		long responseTimeInMS = response.getTime();
		// Print the response time
		log.info("Response time: " + responseTimeInMS + " milliseconds");

		if (responseTimeInMS < 200) {
			log.info("Response time is below 200ms");
		} else {
			log.info("Response time is more than 200ms");
		}

		String contentType = response.getHeader("Content-Type");
		Assert.assertEquals(contentType, "application/json", "invalid content type value");

		String transferEncoding = response.getHeader("Transfer-Encoding");
		Assert.assertEquals(transferEncoding, "chunked", "invalid transfer encoding value");

		String connection = response.getHeader("Connection");
		Assert.assertEquals(connection, "keep-alive", "invalid connection value");

		// read all header key value
		Headers headersList = response.getHeaders();

		for (Header header : headersList) {
			log.info("Key: " + header.getName() + " Value: " + header.getValue());
		}
	}

	@Description("negativeTestingOfAllSubjects")
	@Test(priority = 2)
	public void negativeTestingOfAllSubjects() {
		// Send a GET request to the API endpoint
		Response response = RestAssured.get("/subject/all");
		// Extract the response body as a string
		String responseBody = response.getBody().asString();
		log.info("\nResponse Body:\n" + responseBody);

		Assert.assertEquals(responseBody.contains("404"), true, "value not present in response body");
		log.info("404 value present in response body");

		// Extract and validate the response
		int statusCode = response.getStatusCode();

		// Verify that the response status code is 404 (Not Found)
		Assert.assertEquals(statusCode, 404, "Incorrect status code");
		log.info("Correct status code received");

		String statusLine = response.getStatusLine();
		Assert.assertEquals(statusLine, "HTTP/1.1 404 ", "Incorrect status line");
		log.info("Correct status line received");

		// Get the response time in milliseconds
		long responseTimeInMS = response.getTime();
		// Print the response time
		log.info("Response time: " + responseTimeInMS + " milliseconds");

		if (responseTimeInMS < 200) {
			log.info("Response time is below 200ms");
		} else {
			log.info("Response time is more than 200ms");
		}

		// read all header key value
		Headers headersList = response.getHeaders();

		for (Header header : headersList) {
			log.info("Key: " + header.getName() + " Value: " + header.getValue());
		}
	}

	@Description("positiveTestingOneSubject")
	@Test(priority = 3)
	public void positiveTestingOneSubject() {
		// Send a GET request to the API endpoint
		Response response = RestAssured.get("/questions/Maths/all");

		// Extract and validate the response
		int statusCode = response.getStatusCode();

		// Verify that the response status code is 200 (OK)
		if (statusCode == 200) {
			log.info("\nGET Request is successful. Status code: " + statusCode);

			// You can further parse and validate the response JSON as needed
		} else {
			log.info("\nGET Request failed. Status code: " + statusCode);
		}

		// Extract the response body as a string
		String responseBody = response.getBody().asString();
		log.info("Response body is: " + responseBody);

		// Get the response time in milliseconds
		long responseTimeInMS = response.getTime();

		// Print the response time
		log.info("Response time: " + responseTimeInMS + " milliseconds");

		if (responseTimeInMS < 200) {
			log.info("Response time is below 200ms");
		} else {
			log.info("Response time is more than 200ms");
		}


		// read all header key value
		Headers headersList = response.getHeaders();

		for (Header header : headersList) {
			log.info("Key: " + header.getName() + " Value: " + header.getValue());
		}
	}

	@Description("negativeTestingOneSubject")
	@Test(priority = 4)
	public void negativeTestingOneSubject() {
		// Send a GET request to the API endpoint
		Response response = RestAssured.get("/questions/English/all");

		// Extract and validate the response
		int statusCode = response.getStatusCode();

		// Verify that the response status code is 404
		Assert.assertEquals(statusCode, 404, "invalid status code");

		// Extract the response body as a string
		String responseBody = response.getBody().asString();
		log.info("Response body is: " + responseBody);

		Assert.assertEquals(responseBody.contains("Subject Not Found"), true, "Subject found and it is bug");
		log.info("Subject Not Found value present in response body");

		// Get the response time in milliseconds
		long responseTimeInMS = response.getTime();

		// Print the response time
		log.info("Response time: " + responseTimeInMS + " milliseconds");

		if (responseTimeInMS < 200) {
			log.info("Response time is below 200ms");
		} else {
			log.info("Response time is more than 200ms");
		}


		// read all header key value
		Headers headersList = response.getHeaders();

		for (Header header : headersList) {
			log.info("Key: " + header.getName() + " Value: " + header.getValue());
		}
	}

}
