package net.acomputerdog.jwmi.test.junit;

import net.acomputerdog.jwmi.JWMI;
import net.acomputerdog.jwmi.nat.ReleasableVariant;
import net.acomputerdog.jwmi.wbem.EnumWbemClassObject;
import net.acomputerdog.jwmi.wbem.WbemClassObject;
import net.acomputerdog.jwmi.wbem.WbemLocator;
import net.acomputerdog.jwmi.wbem.WbemServices;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class VariantTests {
    private static WbemClassObject winmgmt;
    private static WbemClassObject cpu;

    @BeforeAll
    public static void init() {
        try (WbemLocator locator = JWMI.getInstance().createWbemLocator()) {
            Assertions.assertNotNull(locator);
            try (WbemServices services = locator.connectServer("root\\CIMV2")) {
                Assertions.assertNotNull(services);
                try (EnumWbemClassObject results = services.execQuery("SELECT * FROM Win32_Service WHERE Name='Winmgmt'")) {
                    Assertions.assertNotNull(results);
                    Assertions.assertTrue(results.hasNext());
                    winmgmt = results.next();
                    Assertions.assertNotNull(winmgmt);
                }

                try (EnumWbemClassObject results = services.execQuery("SELECT * FROM Win32_Processor")) {
                    Assertions.assertNotNull(results);
                    Assertions.assertTrue(results.hasNext());
                    cpu = results.next();
                    Assertions.assertNotNull(cpu);
                }
            }
        }
    }

    @AfterAll
    public static void cleanup() {
        winmgmt.close();
    }

    @Test
    public void testBoolean() {
        try (ReleasableVariant var = winmgmt.get("DesktopInteract")) {
            Assertions.assertNotNull(var);

            Object obj = var.getJavaType();
            Assertions.assertTrue(obj instanceof Boolean);
            Assertions.assertFalse((Boolean) obj);
        }
    }

    @Test
    public void testInt() {
        try (ReleasableVariant var = winmgmt.get("TagId")) {
            Assertions.assertNotNull(var);

            Object obj = var.getJavaType();
            Assertions.assertTrue(obj instanceof Integer);
            Assertions.assertEquals(0, obj);
        }
    }

    @Test
    public void testString() {
        try (ReleasableVariant var = winmgmt.get("Name")) {
            Assertions.assertNotNull(var);

            Object obj = var.getJavaType();
            Assertions.assertTrue(obj instanceof String);
            Assertions.assertEquals("Winmgmt", obj);
        }
    }
}
