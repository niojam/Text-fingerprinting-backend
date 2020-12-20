package ee.ringit.textfingerprint.service.encoder;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ee.ringit.textfingerprint.exception.EncodingException;
import ee.ringit.textfingerprint.exception.InvalidFileException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConfusableEncoderService implements EncoderService {

    @Value("classpath:confusablesDataBase.json")
    private Resource resourceFile;

    private final ObjectMapper objectMapper;

    private File confusablesDataBase;

    @PostConstruct
    private void postConstruct() {
        try {
            confusablesDataBase = resourceFile.getFile();
        } catch (IOException e) {
            throw new InvalidFileException("Missing confusables database file");
        }
    }

    public String encode(String initialText) throws EncodingException {
        try {
            TypeReference<Map<String, String>> typeRef = new TypeReference<>() {};
            Map<String, String> confusableHomoglyphs = objectMapper.readValue(confusablesDataBase, typeRef);
            return Arrays.stream(initialText.split(""))
                    .map(character -> confusableHomoglyphs.getOrDefault(character, character))
                    .collect(Collectors.joining());
        } catch (Exception e) {
            throw new EncodingException(e);
        }
    }

}
