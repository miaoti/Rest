package es.us.isa.restest.util;

import org.junit.Test;

import static org.junit.Assert.*;

import static es.us.isa.restest.util.FileManager.*;

public class FileManagerTest {

    @Test
    public void deleteCreateAndDeleteDir() {
        String parentPath = "src/test/resources/file-manager";
        String path = "src/test/resources/file-manager/test/directory";
        deleteDir(parentPath);

        assertFalse(checkIfExists(parentPath));

        createDir(path);

        assertTrue(checkIfExists(path));

        deleteFile(path);

        assertFalse(checkIfExists(path));
    }

    @Test
    public void deleteDirToleratesNullAndEmpty() {
        // Regression for the NPE in createMstAllureReportManager when the
        // .properties file omits allure.results.dir / allure.report.dir.
        // deleteDir must no-op rather than NPE in new File(null).
        deleteDir(null);
        deleteDir("");
    }

    @Test
    public void writeReadFileTest() {
        String path = "src/test/resources/restest-test-resources/test.txt";
        String text = "prueba";

        createFileIfNotExists(path);
        assertTrue(checkIfExists(path));

        writeFile(path, text);

        String content = readFile(path).trim();
        assertEquals(text, content);

        deleteFile(path);
    }
}
