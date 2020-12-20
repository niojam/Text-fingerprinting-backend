package ee.ringit.textfingerprint.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import ee.ringit.textfingerprint.api.error.BusinessLogicErrorResponse;
import ee.ringit.textfingerprint.api.error.ErrorResponse;
import ee.ringit.textfingerprint.dto.FileFingerprintResponse;
import ee.ringit.textfingerprint.exception.InvalidFileException;
import ee.ringit.textfingerprint.service.FingerPrintService;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static ee.ringit.textfingerprint.util.FileUtil.getFileByName;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(FingerPrintController.class)
class FingerPrintControllerTest {

    @Autowired
    private MockMvc mvc;

    @InjectMocks
    private ObjectMapper objectMapper;

    @MockBean
    private FingerPrintService fingerPrintService;

    @Test
    void testGetTextFingerPrintDataResponseOk() throws Exception {
        String testText = "testString";
        String testEncodedValue = "tеѕtЅtrіnɡ";

        ByteArrayOutputStream expectedResult = new ByteArrayOutputStream();
        DataOutputStream bufferedOutputStream = new DataOutputStream(expectedResult);
        bufferedOutputStream.write("Tere\n".getBytes());
        bufferedOutputStream.write("Siin on moned andmed\n".getBytes());
        bufferedOutputStream.write(testEncodedValue.getBytes());
        expectedResult.flush();
        expectedResult.close();

        Mockito.when(fingerPrintService.getFingerPrintData(any(), any()))
                .thenReturn(expectedResult.toByteArray());

        byte[] response = mvc.perform(post("/fingerprint")
                .param("text", testText))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsByteArray();

        assertTrue(Arrays.equals(expectedResult.toByteArray(), response));
    }

    @Test
    void testGetFingerPrintDataFromFile() throws Exception {
        String testEncodedValue = "tеѕtЅtrіnɡ";
        FileFingerprintResponse testFileFingerprintResponse = new FileFingerprintResponse().setEncodedText(testEncodedValue);

        File testFile = getFileByName("encoded.txt");

        Mockito.when(fingerPrintService.getFingerPrintDataFromFile(any()))
                .thenReturn(testFileFingerprintResponse);

        String response = mvc.perform(post("/identify")
                .content(new FileInputStream(testFile).readAllBytes()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        assertEquals(testFileFingerprintResponse.getEncodedText(), objectMapper.readValue(response, FileFingerprintResponse.class).getEncodedText());

    }

    @Test
    void testGetFingerPrintDataFromFileEmptyFile() throws Exception {
        File testFile = getFileByName("test-empty.txt");

        String exceptionMessage = "Input file is empty";

        Mockito.when(fingerPrintService.getFingerPrintDataFromFile(any()))
                .thenThrow(new InvalidFileException(exceptionMessage));

        String response = mvc.perform(post("/identify")
                .content(new FileInputStream(testFile).readAllBytes()))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        BusinessLogicErrorResponse expectedBusinessLogicErrorResponse = new BusinessLogicErrorResponse(HttpStatus.BAD_REQUEST);
        expectedBusinessLogicErrorResponse.setMessage(exceptionMessage);

        JSONObject responseJson = new JSONObject(response);

        assertEquals(expectedBusinessLogicErrorResponse.getMessage(), responseJson.getString("message"));
    }
}