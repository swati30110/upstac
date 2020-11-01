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
import org.upgrad.upstac.testrequests.TestRequest;
import org.upgrad.upstac.testrequests.consultation.Consultation;
import org.upgrad.upstac.testrequests.consultation.ConsultationController;
import org.upgrad.upstac.testrequests.consultation.CreateConsultationRequest;
import org.upgrad.upstac.testrequests.consultation.DoctorSuggestion;
import org.upgrad.upstac.testrequests.lab.CreateLabResult;
import org.upgrad.upstac.testrequests.lab.LabResult;
import org.upgrad.upstac.testrequests.lab.TestStatus;
import org.upgrad.upstac.testrequests.RequestStatus;
import org.upgrad.upstac.testrequests.TestRequestQueryService;
import org.upgrad.upstac.users.User;
import org.upgrad.upstac.users.models.Gender;

import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;


@Slf4j
@ExtendWith(MockitoExtension.class)
class ConsultationControllerTest {


    @InjectMocks
    ConsultationController consultationController;


    @Mock
    TestRequestQueryService testRequestQueryService;

    @Mock
    private TestRequestUpdateService testRequestUpdateService;

    @Mock
    UserLoggedInService userLoggedInService;

    @Test
    public void calling_assignForConsultation_with_valid_test_request_id_should_update_the_request_status(){

        /*TestRequest testRequest = getTestRequestByStatus(RequestStatus.LAB_TEST_COMPLETED);

        //Implement this method

        //Create another object of the TestRequest method and explicitly assign this object for Consultation using assignForConsultation() method
        // from consultationController class. Pass the request id of testRequest object.

        //Use assertThat() methods to perform the following two comparisons
        //  1. the request ids of both the objects created should be same
        //  2. the status of the second object should be equal to 'DIAGNOSIS_IN_PROCESS'
        // make use of assertNotNull() method to make sure that the consultation value of second object is not null
        // use getConsultation() method to get the lab result*/
        User doctor= createUser();
        TestRequest mockedtestRequest = createTestRequest();
        Mockito.when(userLoggedInService.getLoggedInUser()).thenReturn(doctor);
        Mockito.when(testRequestUpdateService.assignForConsultation(21L ,doctor)).thenReturn(mockedtestRequest);

        TestRequest testRequest = consultationController.assignForConsultation(21L);
        assertEquals(mockedtestRequest.requestId,testRequest.requestId);
        assertEquals(RequestStatus.LAB_TEST_COMPLETED,testRequest.getStatus());
    }

    public TestRequest getTestRequestByStatus(RequestStatus status) {
        return testRequestQueryService.findBy(status).stream().findFirst().get();
    }

    @Test
    @WithUserDetails(value = "doctor")
    public void calling_assignForConsultation_with_valid_test_request_id_should_throw_exception(){

        Long InvalidRequestId= -34L;

        /*Implement this method


        // Create an object of ResponseStatusException . Use assertThrows() method and pass assignForConsultation() method
        // of consultationController with InvalidRequestId as Id
        //Use assertThat() method to perform the following comparison
        //  the exception message should be contain the string "Invalid ID"*/
        User doctor = createUser();
        Mockito.when(userLoggedInService.getLoggedInUser()).thenReturn(doctor);
        Mockito.when(testRequestUpdateService.assignForConsultation(InvalidRequestId,doctor)).thenThrow(new AppException("Invalid ID"));


        ResponseStatusException result = assertThrows(ResponseStatusException.class,()->{

            consultationController.assignForConsultation(InvalidRequestId);
        });

        assertNotNull(result);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatus());
        assertEquals("Invalid ID",result.getReason());


    }

    @Test
    public void calling_updateConsultation_with_valid_test_request_id_should_update_the_request_status_and_update_consultation_details(){

        /*TestRequest testRequest = getTestRequestByStatus(RequestStatus.DIAGNOSIS_IN_PROCESS);

        Implement this method
        //Create an object of CreateConsultationRequest and call getCreateConsultationRequest() to create the object. Pass the above created object as the parameter

        //Create another object of the TestRequest method and explicitly update the status of this object
        // to be 'COMPLETED'. Make use of updateConsultation() method from labRequestController class (Pass the previously created two objects as parameters)

        //Use assertThat() methods to perform the following three comparisons
        //  1. the request ids of both the objects created should be same
        //  2. the status of the second object should be equal to 'COMPLETED'
        // 3. the suggestion of both the objects created should be same. Make use of getSuggestion() method to get the results.*/

        User doctor = createUser();
        TestRequest mockedtestRequestResponse = createTestRequest();
        CreateConsultationRequest mockedConsultationRequest = getCreateConsultationRequest(mockedtestRequestResponse);
        Consultation consultation = createConsultaion(mockedConsultationRequest,mockedtestRequestResponse);
        mockedtestRequestResponse.setConsultation(consultation);
        Mockito.when(userLoggedInService.getLoggedInUser()).thenReturn(doctor);
        Mockito.when(testRequestUpdateService.updateConsultation(21L,mockedConsultationRequest,doctor)).thenReturn(mockedtestRequestResponse);

        TestRequest testRequest =consultationController.updateConsultation(21L,mockedConsultationRequest);

        assertEquals(testRequest.requestId,mockedtestRequestResponse.requestId);
        assertEquals(RequestStatus.COMPLETED,testRequest.getStatus());
        assertEquals(DoctorSuggestion.NO_ISSUES,testRequest.getConsultation().getSuggestion());

    }


    @Test
    @WithUserDetails(value = "doctor")
    public void calling_updateConsultation_with_invalid_test_request_id_should_throw_exception(){

        /*TestRequest testRequest = getTestRequestByStatus(RequestStatus.DIAGNOSIS_IN_PROCESS);

        /*Implement this method

        //Create an object of CreateConsultationRequest and call getCreateConsultationRequest() to create the object. Pass the above created object as the parameter

        // Create an object of ResponseStatusException . Use assertThrows() method and pass updateConsultation() method
        // of consultationController with a negative long value as Id and the above created object as second parameter
        //Refer to the TestRequestControllerTest to check how to use assertThrows() method


        //Use assertThat() method to perform the following comparison
        //  the exception message should be contain the string "Invalid ID"*/

        Long InvalidRequestId= -34L;
        User doctor = createUser();
        TestRequest mockedTestRequest = createTestRequest();
        CreateConsultationRequest mockedConcultationRequest = getCreateConsultationRequest(mockedTestRequest);
        Mockito.when(userLoggedInService.getLoggedInUser()).thenReturn(doctor);
        Mockito.when(testRequestUpdateService.updateConsultation(InvalidRequestId,mockedConcultationRequest,doctor)).thenThrow(new AppException("Invalid ID"));


        ResponseStatusException result = assertThrows(ResponseStatusException.class,()->{

            consultationController.updateConsultation(InvalidRequestId,mockedConcultationRequest);
        });

        assertNotNull(result);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatus());
        assertEquals("Invalid ID",result.getReason());


    }

    @Test
    @WithUserDetails(value = "doctor")
    public void calling_updateConsultation_with_invalid_empty_status_should_throw_exception(){

        /*TestRequest testRequest = getTestRequestByStatus(RequestStatus.DIAGNOSIS_IN_PROCESS);

        //Implement this method

        //Create an object of CreateConsultationRequest and call getCreateConsultationRequest() to create the object. Pass the above created object as the parameter
        // Set the suggestion of the above created object to null.

        // Create an object of ResponseStatusException . Use assertThrows() method and pass updateConsultation() method
        // of consultationController with request Id of the testRequest object and the above created object as second parameter
        //Refer to the TestRequestControllerTest to check how to use assertThrows() method*/


        User doctor = createUser();
        TestRequest mockedTestRequest = createTestRequest();
        CreateConsultationRequest mockedConsultationRequest = getCreateConsultationRequest(mockedTestRequest);
        mockedConsultationRequest.setSuggestion(null);
        Mockito.when(userLoggedInService.getLoggedInUser()).thenReturn(doctor);
        Mockito.when(testRequestUpdateService.updateConsultation(21l,mockedConsultationRequest,doctor)).thenThrow(new AppException("ConstraintViolationException"));


        ResponseStatusException result = assertThrows(ResponseStatusException.class,()->{

            consultationController.updateConsultation(21l,mockedConsultationRequest);
        });

        assertNotNull(result);

        assertEquals("ConstraintViolationException",result.getReason());

    }

    public CreateConsultationRequest getCreateConsultationRequest(TestRequest testRequest) {

        /*Create an object of CreateLabResult and set all the values
        // if the lab result test status is Positive, set the doctor suggestion as "HOME_QUARANTINE" and comments accordingly
        // else if the lab result status is Negative, set the doctor suggestion as "NO_ISSUES" and comments as "Ok"
        // Return the object*/
        CreateConsultationRequest createConsultationRequest = new CreateConsultationRequest();
        if(testRequest.getLabResult().getResult() == TestStatus.POSITIVE)
            createConsultationRequest.setSuggestion(DoctorSuggestion.HOME_QUARANTINE);
        else if(testRequest.getLabResult().getResult() == TestStatus.NEGATIVE)
            createConsultationRequest.setSuggestion(DoctorSuggestion.NO_ISSUES);
        testRequest.setStatus(RequestStatus.COMPLETED);
        return createConsultationRequest;

    }

    private User createUser() {
        User user = new User();
        user.setId(1L);
        user.setUserName("doctorx");
        return user;
    }
    public TestRequest createTestRequest() {

        TestRequest testRequest = new TestRequest();
        LabResult labResult = new LabResult();
        labResult.setResult(TestStatus.NEGATIVE);
        testRequest.setName("Swati Maharana");
        testRequest.setRequestId(21L);
        testRequest.setCreated(LocalDate.now());
        testRequest.setStatus(RequestStatus.LAB_TEST_COMPLETED);
        testRequest.setAge(29);
        testRequest.setEmail("swati301091@gmail.com");
        testRequest.setPhoneNumber("8018888142");
        testRequest.setPinCode(76937347);
        testRequest.setAddress("Bhubaneswar");
        testRequest.setGender(Gender.FEMALE);
        testRequest.setCreatedBy(createUser());
        testRequest.setLabResult(labResult);
        return testRequest;
    }
    public Consultation createConsultaion(CreateConsultationRequest createConsultationRequest,TestRequest testRequest){
        Consultation consultation = new Consultation();
       // consultation.setComments(createConsultationRequest.getComments());
        consultation.setSuggestion(createConsultationRequest.getSuggestion());
        consultation.setRequest(testRequest);
        return consultation;
    }
}