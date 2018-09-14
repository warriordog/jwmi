package net.acomputerdog.jwmi.test.junit;

import net.acomputerdog.jwmi.JWMI;
import net.acomputerdog.jwmi.nat.ReleasableVariant;
import net.acomputerdog.jwmi.wbem.WbemClassObject;
import net.acomputerdog.jwmi.wbem.WbemLocator;
import net.acomputerdog.jwmi.wbem.WbemServices;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class PropertyTests {

    private static final String SERVICES_QUERY = "SELECT * FROM Win32_Service";
    private static final String WINMGMT_QUERY = "SELECT * FROM Win32_Service WHERE Name='Winmgmt'";

    private static WbemLocator locator;
    private static WbemServices services;
    private static WbemClassObject classObject;

    @BeforeAll
    public static void init() {
        locator = JWMI.getInstance().createWbemLocator();
        services = locator.connectServer("root\\CIMV2");
        classObject = services.execQuerySingle(WINMGMT_QUERY);
    }

    @Test
    public void testStringProperty() {
        try (ReleasableVariant name = classObject.get("Name")) {
            Assertions.assertEquals("Winmgmt", name.stringValue());
        }
    }

    @Test
    public void testStringPropertyQuick() {
        Assertions.assertEquals("Winmgmt", classObject.getString("Name"));
    }

    @Test
    public void testBooleanProperty() {
        // This service handles WMI, so it must be running
        try (ReleasableVariant started = classObject.get("Started")) {
            Assertions.assertTrue(started.booleanValue());
        }
    }

    @Test
    public void testBooleanPropertyQuick() {
        // This service handles WMI, so it must be running
        Assertions.assertTrue(classObject.getBoolean("Started"));
    }

    @Test
    public void testIntProperty() {
        // This services handles WMI, so it must have a process w/ a process ID
        try (ReleasableVariant procId = classObject.get("ProcessId")) {
            Assertions.assertNotEquals(0, procId.intValue());
        }
    }

    @Test
    public void testIntPropertyQuick() {
        // This services handles WMI, so it must have a process w/ a process ID
        Assertions.assertNotEquals(0, classObject.getInt("ProcessId"));
    }

    @AfterAll
    public static void finish() {
        classObject.release();
        services.release();
        locator.release();
    }
}
