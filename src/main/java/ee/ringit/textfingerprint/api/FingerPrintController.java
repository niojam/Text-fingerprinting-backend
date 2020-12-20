package ee.ringit.textfingerprint.api;

import ee.ringit.textfingerprint.dto.FileFingerprintResponse;
import ee.ringit.textfingerprint.service.FingerPrintService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.io.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Validated
public class FingerPrintController {

    private final FingerPrintService fingerPrintService;

    @PostMapping("fingerprint")
    public byte[] getTextFingerPrintData(@RequestParam @NotBlank String text, @NotNull InputStream dataStream) {
        return fingerPrintService.getFingerPrintData(text, dataStream);
    }


    @PostMapping("identify")
    public FileFingerprintResponse getFingerPrintDataFromFile(InputStream dataStream) {
        return fingerPrintService.getFingerPrintDataFromFile(dataStream);
    }

}
