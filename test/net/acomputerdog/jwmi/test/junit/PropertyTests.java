package net.acomputerdog.jwmi.test.junit;

import net.acomputerdog.jwmi.JWMI;
import net.acomputerdog.jwmi.nat.ReleasableVariant;
import net.acomputerdog.jwmi.wbem.WbemClassObject;
import net.acomputerdog.jwmi.wbem.WbemServices;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class PropertyTests {

    private static final String SERVICES_QUERY = "SELECT * FROM Win32_Service";
    private static final String WINMGMT_QUERY = "SELECT * FROM Win32_Service WHERE Name='Winmgmt'";

    private static WbemServices services;
    private static WbemClassObject classObject;

    @BeforeAll
    public static void init() {
        services = JWMI.getInstance().createWbemLocator().connectServer("root\\CIMV2");
        classObject = services.execQuerySingle(WINMGMT_QUERY);
    }

    @Test
    public void testStringProperty() {
        try (ReleasableVariant name = classObject.get("Name")) {
            Assertions.assertEquals("Winmgmt", name.stringValue());
        }
    }

    @Test
    public void testBooleanProperty() {
        // This service handles WMI, so it must be running
        try (ReleasableVariant started = classObject.get("Started")) {
            Assertions.assertTrue(started.booleanValue());
        }
    }

    @Test
    public void testIntProperties() {
        // This services handles WMI, so it must have a process w/ a process ID
        try (ReleasableVariant procId = classObject.get("ProcessId")) {
            Assertions.assertNotEquals(0, procId.intValue());
        }
    }

    @AfterAll
    public static void finish() {
        classObject.release();
        services.release();
    }
}
