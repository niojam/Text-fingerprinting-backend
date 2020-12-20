package ee.ringit.textfingerprint.service.encoder;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ConfusableEncoderServiceTest {

    @Autowired
    private ConfusableEncoderService confusableEncoderService;

    @Test
    void testEncodeWord() {
        String testWord = "testString";
        String expectedOutput = "tеѕtЅtrіnɡ";
        String result = confusableEncoderService.encode(testWord);
        assertNotEquals(testWord, result);
        assertEquals(expectedOutput.getBytes().length, result.getBytes().length);
        assertEquals(expectedOutput, result);
    }

    @Test
    void testEncodeSentence() {
        String testString = "Input for unit test";
        String expectedOutput = "Ιnрut for unіt tеѕt";
        String result = confusableEncoderService.encode(testString);
        assertNotEquals(testString, result);
        assertEquals(expectedOutput.getBytes().length, result.getBytes().length);
        assertEquals(expectedOutput, result);
    }

}