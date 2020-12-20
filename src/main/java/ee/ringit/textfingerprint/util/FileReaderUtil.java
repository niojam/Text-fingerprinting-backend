package ee.ringit.textfingerprint.util;

import java.io.*;
import java.nio.charset.Charset;
import java.util.stream.Collectors;

import static ee.ringit.textfingerprint.util.Constants.LINE_SEPARATOR_KEY;

public class FileReaderUtil {

    public static BufferedReader getBufferedReaderFromInputData(InputStream inputData, Charset charset) {
        InputStreamReader inputStreamReader = new InputStreamReader(inputData, charset);
        return new BufferedReader(inputStreamReader);
    }

    public static String getBufferReaderContentLineBreakSensitive(BufferedReader bufferedReader) {
       return bufferedReader.lines().collect(Collectors.joining(System.getProperty(LINE_SEPARATOR_KEY)));
    }

}
