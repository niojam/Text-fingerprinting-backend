package ee.ringit.textfingerprint.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class FileFingerprintResponse {

    private String encodedText;

}
