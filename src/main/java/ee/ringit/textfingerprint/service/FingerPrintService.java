package ee.ringit.textfingerprint.service;

import ee.ringit.textfingerprint.dto.FileFingerprintResponse;
import ee.ringit.textfingerprint.exception.InvalidFileException;
import ee.ringit.textfingerprint.service.encoder.ConfusableEncoderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static ee.ringit.textfingerprint.util.Constants.LINE_SEPARATOR_KEY;
import static ee.ringit.textfingerprint.util.FileReaderUtil.*;


@Service
@RequiredArgsConstructor
public class FingerPrintService {

    private final ConfusableEncoderService confusableEncoderService;

    public byte[] getFingerPrintData(String initialText, InputStream dataStream) {
        BufferedReader bufferedReader = getBufferedReaderFromInputData(dataStream, StandardCharsets.UTF_8);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream bufferedOutputStream = new DataOutputStream(byteArrayOutputStream);

        String encodedText = confusableEncoderService.encode(initialText);

        Optional<String> filePreviousContent = getExistingTextFromFileIfExists(bufferedReader);
        try {
            if (filePreviousContent.isPresent()) {
                bufferedOutputStream.write(filePreviousContent.get().getBytes());
            }

            bufferedOutputStream.write(encodedText.getBytes());
            byteArrayOutputStream.flush();
            byteArrayOutputStream.close();
        } catch (IOException e) {
            throw new InvalidFileException("Cannot write data to file");
        }
        return byteArrayOutputStream.toByteArray();
    }

    private Optional<String> getExistingTextFromFileIfExists(BufferedReader bufferedReader) {
        String lineSeparator = System.getProperty(LINE_SEPARATOR_KEY);
        try {
            return bufferedReader.ready()
                    ? Optional.of(getBufferReaderContentLineBreakSensitive(bufferedReader).concat(lineSeparator))
                    : Optional.empty();
        } catch (IOException e) {
            throw new InvalidFileException("Cannot read input data");
        }
    }

    public FileFingerprintResponse getFingerPrintDataFromFile(InputStream dataStream) {
        BufferedReader bufferedReader = getBufferedReaderFromInputData(dataStream, StandardCharsets.UTF_8);
        try {
            if (bufferedReader.ready()) {
                return new FileFingerprintResponse()
                        .setEncodedText(getBufferReaderContentLineBreakSensitive(bufferedReader));
            }
            throw new InvalidFileException("Input file is empty");
        } catch (IOException e) {
            throw new InvalidFileException("Cannot read input data");
        }
    }

}
