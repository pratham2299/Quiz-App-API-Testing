package com.quiz_app.testcases;

import java.util.Random;

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

	@Step("verify By Getting All Subjects With Valid Endpoint")
	@Test(priority = 1)
	public void verifyByGettingAllSubjectsWithValidEndpoint() {
		requestSpec.basePath("/subjects/all");
		response = requestSpec.get();
		String responseBody = response.getBody().asPrettyString();
		log.info("Response Body: " + responseBody);

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

	@Step("verify By Getting All Subjects With InValid Endpoint")
	@Test(priority = 2)
	public void verifyByGettingAllSubjectsWithInValidEndpoint() {
		requestSpec.basePath("/subject/all");
		response = requestSpec.get();
		String responseBody = response.getBody().asPrettyString();
		log.info("Response Body: " + responseBody);

		log.info("Response Code: " + response.getStatusCode());
		int actualStatusCode = response.getStatusCode();
		Assert.assertEquals(actualStatusCode, 404, "Invalid status code");
		log.info("Response Time: " + response.getTime());

		String actualResponse = response.jsonPath().getString("error");
		Assert.assertEquals(actualResponse, "Not Found", "value not present in response body");
	}

	@Step("verify By Getting One Subjects With Valid Subject Name")
	@Test(priority = 3)
	public void verifyByGettingOneSubjectsWithValidSubjectName() {
		String subjectName[] = { "Maths", "Science" };
		Random random = new Random();

		// Generate a random index within the array's bounds
		int randomIndex = random.nextInt(subjectName.length);

		// Retrieve the randomly selected string
		String randomSubjectName = subjectName[randomIndex];
		requestSpec.basePath("/questions/" + randomSubjectName + "/all");
		response = requestSpec.get();
		String responseBody = response.getBody().asPrettyString();
		log.info("Response Body: " + responseBody);

		if (randomSubjectName.equals("Maths")) {
			Assert.assertEquals(response.jsonPath().getInt("[0].questionId"), 2);
			Assert.assertEquals(response.jsonPath().getString("[0].questionType"), "Image");
			Assert.assertEquals(response.jsonPath().getString("[0].question"),
					"https://imageio.forbes.com/specials-images/imageserve/5d35eacaf1176b0008974b54/0x0.jpg?format=jpg&crop=4560,2565,x790,y784,safe&width=1200");
			Assert.assertEquals(response.jsonPath().getString("[0].option1"), "true");
			Assert.assertEquals(response.jsonPath().getString("[0].option2"), "false");
			Assert.assertEquals(response.jsonPath().getString("[0].option3"), null);
			Assert.assertEquals(response.jsonPath().getString("[0].option4"), null);
			Assert.assertEquals(response.jsonPath().getString("[0].option5"), null);
			Assert.assertEquals(response.jsonPath().getString("[0].correctOption"), "false");
			Assert.assertEquals(response.jsonPath().getString("[0].explanation"),
					"Distance is a Scalar quantity because we couldn't consider direction");
			Assert.assertEquals(response.jsonPath().getString("[0].subject.subjectName"), "Maths");

			Assert.assertEquals(response.jsonPath().getInt("[1].questionId"), 5);
			Assert.assertEquals(response.jsonPath().getString("[1].questionType"), "Text");
			Assert.assertEquals(response.jsonPath().getString("[1].question"),
					"Which of the following is an example of application of force?");
			Assert.assertEquals(response.jsonPath().getString("[1].option1"), "when a body at rest starts moving");
			Assert.assertEquals(response.jsonPath().getString("[1].option2"), "when a moving body stops");
			Assert.assertEquals(response.jsonPath().getString("[1].option3"), "when moving body changes its direction");
			Assert.assertEquals(response.jsonPath().getString("[1].option4"), "All of the above");
			Assert.assertEquals(response.jsonPath().getString("[1].option5"), null);
			Assert.assertEquals(response.jsonPath().getString("[1].correctOption"),
					"when moving body changes its direction");
			Assert.assertEquals(response.jsonPath().getString("[1].explanation"),
					"Force can be defined as an external effort in the form of push or pull which: (i) Produces or tries to produce motion in a resting body (ii) Stops or tries to stop a moving body (iii) Changes or tries to change the direction of motion of the body");
			Assert.assertEquals(response.jsonPath().getString("[1].subject.subjectName"), "Maths");
		} else if (randomSubjectName.equals("Science")) {
			Assert.assertEquals(response.jsonPath().getInt("[0].questionId"), 1);
			Assert.assertEquals(response.jsonPath().getString("[0].questionType"), "Text");
			Assert.assertEquals(response.jsonPath().getString("[0].question"),
					"Distance is a ________________ quantity.");
			Assert.assertEquals(response.jsonPath().getString("[0].option1"), "Scalar");
			Assert.assertEquals(response.jsonPath().getString("[0].option2"), "Vector");
			Assert.assertEquals(response.jsonPath().getString("[0].option3"), "Both a and b");
			Assert.assertEquals(response.jsonPath().getString("[0].option4"), "None of Above");
			Assert.assertEquals(response.jsonPath().getString("[0].option5"), null);
			Assert.assertEquals(response.jsonPath().getString("[0].correctOption"), "Scalar");
			Assert.assertEquals(response.jsonPath().getString("[0].explanation"),
					"Distance is a Scalar quantity because we couldn't consider direction");
			Assert.assertEquals(response.jsonPath().getString("[0].subject.subjectName"), "Science");

			Assert.assertEquals(response.jsonPath().getInt("[1].questionId"), 3);
			Assert.assertEquals(response.jsonPath().getString("[1].questionType"), "Text");
			Assert.assertEquals(response.jsonPath().getString("[1].question"),
					"Which of the following is an example of application of force?");
			Assert.assertEquals(response.jsonPath().getString("[1].option1"), "when a body at rest starts moving");
			Assert.assertEquals(response.jsonPath().getString("[1].option2"), "when a moving body stops");
			Assert.assertEquals(response.jsonPath().getString("[1].option3"), "when moving body changes its direction");
			Assert.assertEquals(response.jsonPath().getString("[1].option4"), "All of the above");
			Assert.assertEquals(response.jsonPath().getString("[1].option5"), null);
			Assert.assertEquals(response.jsonPath().getString("[1].correctOption"),
					"when a body at rest starts moving");
			Assert.assertEquals(response.jsonPath().getString("[1].explanation"),
					"Force can be defined as an external effort in the form of push or pull which: (i) Produces or tries to produce motion in a resting body (ii) Stops or tries to stop a moving body (iii) Changes or tries to change the direction of motion of the body");
			Assert.assertEquals(response.jsonPath().getString("[1].subject.subjectName"), "Science");

			Assert.assertEquals(response.jsonPath().getInt("[2].questionId"), 4);
			Assert.assertEquals(response.jsonPath().getString("[2].questionType"), "Text");
			Assert.assertEquals(response.jsonPath().getString("[2].question"),
					"Which of the following is an example of application of force?");
			Assert.assertEquals(response.jsonPath().getString("[2].option1"), "when a body at rest starts moving");
			Assert.assertEquals(response.jsonPath().getString("[2].option2"), "when a moving body stops");
			Assert.assertEquals(response.jsonPath().getString("[2].option3"), "when moving body changes its direction");
			Assert.assertEquals(response.jsonPath().getString("[2].option4"), "All of the above");
			Assert.assertEquals(response.jsonPath().getString("[2].option5"), null);
			Assert.assertEquals(response.jsonPath().getString("[2].correctOption"),
					"when moving body changes its direction");
			Assert.assertEquals(response.jsonPath().getString("[2].explanation"),
					"Force can be defined as an external effort in the form of push or pull which: (i) Produces or tries to produce motion in a resting body (ii) Stops or tries to stop a moving body (iii) Changes or tries to change the direction of motion of the body");
			Assert.assertEquals(response.jsonPath().getString("[2].subject.subjectName"), "Science");
		}

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

	@Step("verify By Getting One Subjects With Invalid Subject Name")
	@Test(priority = 4)
	public void verifyByGettingOneSubjectsWithInvalidSubjectName() {
		String fakeSubjectName = faker.animal().name();
		requestSpec.basePath("/questions/" + fakeSubjectName + "/all");
		response = requestSpec.get();
		String responseBody = response.getBody().asPrettyString();
		log.info("Response Body: " + responseBody);

		Assert.assertEquals(responseBody.contains("Subject Not Found"), true, "Invalid response");

		log.info("Response Code: " + response.getStatusCode());
		int actualStatusCode = response.getStatusCode();
		Assert.assertEquals(actualStatusCode, 404, "Invalid status code");
		log.info("Response Time: " + response.getTime());

		String statusLine = response.getStatusLine();
		Assert.assertEquals(statusLine, "HTTP/1.1 404 ", "Incorrect status line");
	}

}
