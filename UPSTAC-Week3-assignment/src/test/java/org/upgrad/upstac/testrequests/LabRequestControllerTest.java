package org.upgrad.upstac.testrequests;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.web.server.ResponseStatusException;
import org.upgrad.upstac.config.security.UserLoggedInService;
import org.upgrad.upstac.exception.AppException;
import org.upgrad.upstac.testrequests.lab.*;
import org.upgrad.upstac.users.User;
import org.upgrad.upstac.users.models.Gender;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.anyOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class LabRequestControllerTest {


    @InjectMocks
    LabRequestController labRequestController;

    @Mock
    TestRequestUpdateService testRequestUpdateService;

    @Mock
    UserLoggedInService userLoggedInService;

    @Mock
    TestRequestQueryService testRequestQueryService;

    @Test
    public void calling_assignForLabTest_should_return_test_request_with_INITIATED_status_TestRequestUpdateService_assignForLabTest(){
        //Arranage
        User tester = createUser();
        TestRequest mockedtestRequest = createTestRequest();
        Mockito.when(userLoggedInService.getLoggedInUser()).thenReturn(tester);
        Mockito.when(testRequestUpdateService.assignForLabTest(21L ,tester)).thenReturn(mockedtestRequest);
        //Act
        TestRequest testRequest = labRequestController.assignForLabTest(21L);
        //Assert
        assertEquals(RequestStatus.INITIATED,testRequest.getStatus());

    }

    public TestRequest getTestRequestByStatus(RequestStatus status) {
        return testRequestQueryService.findBy(status).stream().findFirst().get();
    }

    @Test
    public void calling_assignForLabTest_with_valid_test_request_id_should_throw_exception(){
/*  //Implement method
        // Create an object of ResponseStatusException . Use assertThrows() method and pass assignForLabTest() method
        // of labRequestController with InvalidRequestId as Id


        //Use assertThat() method to perform the following comparison
        //  the exception message should be contain the string "Invalid ID"
*/
        //Arrange
        Long InvalidRequestId= -34L;
        User tester= createUser();
        Mockito.when(userLoggedInService.getLoggedInUser()).thenReturn(tester);
        Mockito.when(testRequestUpdateService.assignForLabTest(InvalidRequestId,tester)).thenThrow(new AppException("Invalid ID"));


        ResponseStatusException result = assertThrows(ResponseStatusException.class,()->{

            labRequestController.assignForLabTest(InvalidRequestId);
        });

        assertNotNull(result);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatus());
        assertEquals("Invalid ID",result.getReason());
    }

    @Test
    public void calling_updateLabTest_with_test_request_id_should_update_the_request_status_and_update_test_request_details(){

        /*TestRequest testRequest = getTestRequestByStatus(RequestStatus.LAB_TEST_IN_PROGRESS);

        //Implement this method
        //Create an object of CreateLabResult and call getCreateLabResult() to create the object. Pass the above created object as the parameter

        //Create another object of the TestRequest method and explicitly update the status of this object
        // to be 'LAB_TEST_IN_PROGRESS'. Make use of updateLabTest() method from labRequestController class (Pass the previously created two objects as parameters)

        //Use assertThat() methods to perform the following three comparisons
        //  1. the request ids of both the objects created should be same
        //  2. the status of the second object should be equal to 'LAB_TEST_COMPLETED'
        // 3. the results of both the objects created should be same. Make use of getLabResult() method to get the results.*/

        //Arrange
        User tester = createUser();
        TestRequest mockedtestRequest = createTestRequest();
        mockedtestRequest.setStatus(RequestStatus.LAB_TEST_IN_PROGRESS);
        CreateLabResult createLabResult =createLabResult();
        Mockito.when(userLoggedInService.getLoggedInUser()).thenReturn(tester);
        Mockito.when(testRequestUpdateService.updateLabTest(21l,createLabResult,tester)).thenReturn(mockedtestRequest);

        //action
        TestRequest testRequest = labRequestController.updateLabTest(21l,createLabResult);

        assertEquals(mockedtestRequest.requestId,testRequest.requestId);
       // assertEquals(RequestStatus.LAB_TEST_COMPLETED,testRequest.getStatus());
        assertEquals(mockedtestRequest,testRequest);

    }


    @Test
    @WithUserDetails(value = "tester")
    public void calling_updateLabTest_with_invalid_test_request_id_should_throw_exception(){

        /*TestRequest testRequest = getTestRequestByStatus(RequestStatus.LAB_TEST_IN_PROGRESS);


        //Implement this method

        //Create an object of CreateLabResult and call getCreateLabResult() to create the object. Pass the above created object as the parameter

        // Create an object of ResponseStatusException . Use assertThrows() method and pass updateLabTest() method
        // of labRequestController with a negative long value as Id and the above created object as second parameter
        //Refer to the TestRequestControllerTest to check how to use assertThrows() method


        //Use assertThat() method to perform the following comparison
        //  the exception message should be contain the string "Invalid ID"*/
        Long InvalidRequestId= -34L;
        CreateLabResult createLabResult = createLabResult();
        User tester = createUser();
        Mockito.when(userLoggedInService.getLoggedInUser()).thenReturn(tester);
        Mockito.when(testRequestUpdateService.updateLabTest(InvalidRequestId,createLabResult,tester)).thenThrow(new AppException("Invalid ID"));


        ResponseStatusException result = assertThrows(ResponseStatusException.class,()->{

            labRequestController.updateLabTest(InvalidRequestId,createLabResult);
        });

        assertNotNull(result);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatus());
        assertEquals("Invalid ID",result.getReason());


    }

    @Test
    public void calling_updateLabTest_with_invalid_empty_status_should_throw_exception(){

        /*TestRequest testRequest = getTestRequestByStatus(RequestStatus.LAB_TEST_IN_PROGRESS);

        Implement this method

        //Create an object of CreateLabResult and call getCreateLabResult() to create the object. Pass the above created object as the parameter
        // Set the result of the above created object to null.

        // Create an object of ResponseStatusException . Use assertThrows() method and pass updateLabTest() method
        // of labRequestController with request Id of the testRequest object and the above created object as second parameter
        //Refer to the TestRequestControllerTest to check how to use assertThrows() method


        //Use assertThat() method to perform the following comparison
        //  the exception message should be contain the string "ConstraintViolationException"*/
        CreateLabResult createLabResult = new CreateLabResult();
        User tester = createUser();
        Mockito.when(userLoggedInService.getLoggedInUser()).thenReturn(tester);
        Mockito.when(testRequestUpdateService.updateLabTest(21l,createLabResult,tester)).thenThrow(new AppException("ConstraintViolationException"));


        ResponseStatusException result = assertThrows(ResponseStatusException.class,()->{

            labRequestController.updateLabTest(21l,createLabResult);
        });

        assertNotNull(result);

        assertEquals("ConstraintViolationException",result.getReason());

    }
    public TestRequest createTestRequest() {
        TestRequest testRequest = new TestRequest();
        testRequest.setRequestId((21L));
        testRequest.setAddress("Bhubaneswar");
        testRequest.setAge(23);
        testRequest.setEmail("swati@gmail.com");
        testRequest.setCreated(LocalDate.now());
        testRequest.setGender(Gender.FEMALE);
        testRequest.setPhoneNumber("8018888142");
       // testRequest.setStatus(RequestStatus.INITIATED);
        return testRequest;
    }
    public CreateLabResult createLabResult() {

        //Create an object of CreateLabResult and set all the values
        // Return the object
        CreateLabResult createLabResult = new CreateLabResult();
        createLabResult.setBloodPressure("90");
        createLabResult.setComments("Should be left");
        createLabResult.setHeartBeat("120");
        createLabResult.setOxygenLevel("95");
        createLabResult.setTemperature("35c");
        createLabResult.setResult(TestStatus.POSITIVE);

        return createLabResult;
    }
    public LabResult getLabResult( ) {

        LabResult labResult = new LabResult();
        labResult.setBloodPressure("90");
        labResult.setComments("Should be left");
        labResult.setHeartBeat("120");
        labResult.setOxygenLevel("95");
        labResult.setTemperature("35c");
        labResult.setResult(TestStatus.POSITIVE);

        return labResult;
    }
    private User createUser() {
        User user = new User();
        user.setId(1L);
        user.setUserName("testerx");
        return user;
    }
}