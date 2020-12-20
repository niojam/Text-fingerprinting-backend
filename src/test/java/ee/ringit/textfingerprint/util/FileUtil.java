package ee.ringit.textfingerprint.util;

import java.io.File;
import java.util.Objects;

public class FileUtil {

    public static File getFileByName(String fileName) {
        ClassLoader classLoader = FileReaderUtil.class.getClassLoader();
        return new File(Objects.requireNonNull(classLoader.getResource(fileName)).getFile());
    }

}
