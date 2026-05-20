package es.us.isa.restest.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class PropertyManagerTest {

    @Before
    public void resetSingleton() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field properties = PropertyManager.class.getDeclaredField("globalProperties");
        properties.setAccessible(true);
        properties.set(null, null);

        Field experimentProperties = PropertyManager.class.getDeclaredField("userProperties");
        experimentProperties.setAccessible(true);
        experimentProperties.set(null, null);
    }

    @Test
    public void shouldReadPropertyFromMainPropertiesFile() {
        String generator = PropertyManager.readProperty("generator");
        assertEquals("RT", generator);
    }

    @Test
    public void shouldReturnNullForUnknownPropertyName() {
        // Replaces the old "rename config.properties on disk" negative test.
        // PropertyManager now loads config.properties from the classpath (so it
        // works from any CWD, including IDE project-root runs), and the file
        // is always packaged into target/classes; the on-disk file is no longer
        // the sole source. The meaningful null-return contract is "unknown key
        // returns null", which this test pins.
        String value = PropertyManager.readProperty("totally.unknown.property.name");
        assertNull(value);
    }

    @Test
    public void shouldReadPropertyFromExperimentPropertiesFile() {
        String generator = PropertyManager.readProperty("src/test/resources/Bikewise/bikewise_test.properties", "generator");
        assertEquals("CBT", generator);
    }

    @Test
    public void shouldNotReadPropertyFromExperimentPropertiesFile() {
        String generator = PropertyManager.readProperty("unknown.properties", "generator");
        assertNull(generator);
    }

    @After
    public void renamePropertiesFile() {
        File f = new File("src/main/resources/config.properties1");
        if(f.exists()) {
            f.renameTo(new File("src/main/resources/config.properties"));
        }
    }
}
