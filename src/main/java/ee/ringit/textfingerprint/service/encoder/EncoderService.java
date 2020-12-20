package ee.ringit.textfingerprint.service.encoder;

import ee.ringit.textfingerprint.exception.EncodingException;

public interface EncoderService {

    String encode(String initialText) throws EncodingException;

}
