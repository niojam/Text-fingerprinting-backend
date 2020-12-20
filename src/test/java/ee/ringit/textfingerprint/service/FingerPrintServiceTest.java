package ee.ringit.textfingerprint.service;

import ee.ringit.textfingerprint.dto.FileFingerprintResponse;
import ee.ringit.textfingerprint.exception.InvalidFileException;
import ee.ringit.textfingerprint.service.encoder.ConfusableEncoderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.*;
import java.util.Arrays;

import static ee.ringit.textfingerprint.util.FileUtil.getFileByName;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class FingerPrintServiceTest {


    public static final String TEST_STRING = "testString";
    public static final String TEST_ENCODED_VALUE = "tеѕtЅtrіnɡ";

    @InjectMocks
    private FingerPrintService fingerPrintService;

    @Mock
    private ConfusableEncoderService confusableEncoderService;

    @Test
    void testGetFingerPrintDataEmptyInputFile() throws IOException {
        ByteArrayOutputStream expectedResult = new ByteArrayOutputStream();
        DataOutputStream bufferedOutputStream = new DataOutputStream(expectedResult);
        bufferedOutputStream.write(TEST_ENCODED_VALUE.getBytes());
        expectedResult.flush();
        expectedResult.close();

        Mockito.when(confusableEncoderService.encode(TEST_STRING)).thenReturn("tеѕtЅtrіnɡ");
        File testFile = getFileByName("test-empty.txt");
        byte[] result = fingerPrintService.getFingerPrintData(TEST_STRING, new FileInputStream(testFile));
        assertTrue(Arrays.equals(expectedResult.toByteArray(), result));
    }

    @Test
    void testGetFingerPrintDataEmptyAddNewRowToFile() throws IOException {
        ByteArrayOutputStream expectedResult = new ByteArrayOutputStream();
        DataOutputStream bufferedOutputStream = new DataOutputStream(expectedResult);
        bufferedOutputStream.write("Tere\n".getBytes());
        bufferedOutputStream.write("Siin on moned andmed\n".getBytes());
        bufferedOutputStream.write(TEST_ENCODED_VALUE.getBytes());
        expectedResult.flush();
        expectedResult.close();

        Mockito.when(confusableEncoderService.encode(TEST_STRING)).thenReturn("tеѕtЅtrіnɡ");
        File testFile = getFileByName("test.txt");
        byte[] result = fingerPrintService.getFingerPrintData(TEST_STRING, new FileInputStream(testFile));
        assertTrue(Arrays.equals(expectedResult.toByteArray(), result));
    }

    @Test
    void testGetFingerPrintDataFromFileEmtyFile() {
        File testFile = getFileByName("test-empty.txt");

        Exception exception = assertThrows(InvalidFileException.class, () -> {
            fingerPrintService.getFingerPrintDataFromFile(new FileInputStream(testFile));
        });
        assertEquals("Input file is empty", exception.getMessage());
    }

    @Test
    void testGetFingerPrintDataFromFile() throws IOException {
        File testFile = getFileByName("encoded.txt");

        FileFingerprintResponse result = fingerPrintService.getFingerPrintDataFromFile(new FileInputStream(testFile));
        assertEquals(TEST_ENCODED_VALUE, result.getEncodedText());
    }


}