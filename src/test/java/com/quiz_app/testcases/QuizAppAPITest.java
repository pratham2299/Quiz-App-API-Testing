package com.quiz_app.testcases;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class QuizAppAPITest {
	public static Logger log = LogManager.getLogger(QuizAppAPITest.class);

	public RequestSpecification requestSpec;
	public Response response;

	private Faker faker = new Faker();

	@BeforeClass
	public void baseURL() {
		// Define the base URL of the API
		requestSpec = RestAssured.given();

		requestSpec.baseUri("http://192.168.0.173:8085/quiz");
	}

	@Step("verifyByGettingAllSubjectsWithValidEndpoint")
	@Test(priority = 1)
	public void verifyByGettingAllSubjectsWithValidEndpoint() {
		requestSpec.basePath("/subjects/all");
		response = requestSpec.get();
		String responseBody = response.getBody().asPrettyString();
		log.info("Response Body:\n" + responseBody);

		log.info("Response Code: " + response.getStatusCode());
		int actualStatusCode = response.getStatusCode();
		Assert.assertEquals(actualStatusCode, 200, "Invalid status code");
		log.info("Response Time: " + response.getTime());

		String actualFirstSubjectName = response.jsonPath().getString("subjectName[0]");
		Assert.assertEquals(actualFirstSubjectName, "Maths", "value not present in response body");

		String actualSecondSubjectName = response.jsonPath().getString("subjectName[1]");
		Assert.assertEquals(actualSecondSubjectName, "Science", "value not present in response body");

		String statusLine = response.getStatusLine();
		Assert.assertEquals(statusLine, "HTTP/1.1 200 ", "Incorrect status line");

		String contentType = response.getHeader("Content-Type");
		Assert.assertEquals(contentType, "application/json", "invalid content type value");

		String transferEncoding = response.getHeader("Transfer-Encoding");
		Assert.assertEquals(transferEncoding, "chunked", "invalid transfer encoding value");

		String keepAlive = response.getHeader("Keep-Alive");
		Assert.assertEquals(keepAlive, "timeout=60", "invalid keep-alive value");

		String connection = response.getHeader("Connection");
		Assert.assertEquals(connection, "keep-alive", "invalid connection value");

		// read all header key value
		Headers headersList = response.getHeaders();

		for (Header header : headersList) {
			log.info("Key: " + header.getName() + " Value: " + header.getValue());
		}
	}

	@Step("verifyByGettingOneSubjectsWithValidSubjectName")
	@Test(priority = 2)
	public void verifyByGettingOneSubjectsWithValidSubjectName() {
		requestSpec.basePath("/questions/Maths/all");
		response = requestSpec.get();
		String responseBody = response.getBody().asPrettyString();
		log.info("Response Body:\n" + responseBody);

		log.info("Response Code: " + response.getStatusCode());
		int actualStatusCode = response.getStatusCode();
		Assert.assertEquals(actualStatusCode, 200, "Invalid status code");
		log.info("Response Time: " + response.getTime());

		String statusLine = response.getStatusLine();
		Assert.assertEquals(statusLine, "HTTP/1.1 200 ", "Incorrect status line");

		String contentType = response.getHeader("Content-Type");
		Assert.assertEquals(contentType, "application/json", "invalid content type value");

		String transferEncoding = response.getHeader("Transfer-Encoding");
		Assert.assertEquals(transferEncoding, "chunked", "invalid transfer encoding value");

		String keepAlive = response.getHeader("Keep-Alive");
		Assert.assertEquals(keepAlive, "timeout=60", "invalid keep-alive value");

		String connection = response.getHeader("Connection");
		Assert.assertEquals(connection, "keep-alive", "invalid connection value");

		// read all header key value
		Headers headersList = response.getHeaders();

		for (Header header : headersList) {
			log.info("Key: " + header.getName() + " Value: " + header.getValue());
		}
	}

	@Step("verifyByGettingOneSubjectsWithInvalidSubjectName")
	@Test(priority = 3)
	public void verifyByGettingOneSubjectsWithInvalidSubjectName() {
		String fakeSubjectName = faker.animal().name();
		requestSpec.basePath("/questions/" + fakeSubjectName + "/all");
		response = requestSpec.get();
		String responseBody = response.getBody().asPrettyString();
		log.info("Response Body:\n" + responseBody);

		Assert.assertEquals(responseBody.contains("Subject Not Found"), true, "Invalid response");

		log.info("Response Code: " + response.getStatusCode());
		int actualStatusCode = response.getStatusCode();
		Assert.assertEquals(actualStatusCode, 404, "Invalid status code");
		log.info("Response Time: " + response.getTime());

		String statusLine = response.getStatusLine();
		Assert.assertEquals(statusLine, "HTTP/1.1 404 ", "Incorrect status line");
	}

}
