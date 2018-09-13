package net.acomputerdog.jwmi.test.junit;

import com.sun.jna.Pointer;
import net.acomputerdog.jwmi.JWMI;
import net.acomputerdog.jwmi.wbem.EnumWbemClassObject;
import net.acomputerdog.jwmi.wbem.WbemClassObject;
import net.acomputerdog.jwmi.wbem.WbemLocator;
import net.acomputerdog.jwmi.wbem.WbemServices;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class NamespaceTests {

    private static WbemLocator locator;

    @BeforeAll
    public static void init() {
        locator = JWMI.getInstance().createWbemLocator();
    }

    @Test
    public void testCIMV2() {
        try (WbemServices services = locator.connectServer("root\\CIMV2")) {
            Assertions.assertNotEquals(Pointer.NULL, services.getPointer());
            try (EnumWbemClassObject results = services.execQuery("SELECT * FROM Win32_Service")) {
                Assertions.assertNotEquals(Pointer.NULL, results.getPointer());
            }
        }
    }

    @Test
    public void testHardware() {
        try (WbemServices services = locator.connectServer("root\\Hardware")) {
            Assertions.assertNotEquals(Pointer.NULL, services.getPointer());
            try (EnumWbemClassObject results = services.execQuery("SELECT * FROM RegisteredProfile")) {
                Assertions.assertNotEquals(Pointer.NULL, results.getPointer());
            }
        }
    }

    @Test
    public void testMultiNS() {
        try (WbemServices nsCIMV2 = locator.connectServer("root\\CIMV2")) {
            Assertions.assertNotEquals(Pointer.NULL, nsCIMV2.getPointer());
            try (WbemServices nsHardware = locator.connectServer("root\\Hardware")) {
                Assertions.assertNotEquals(Pointer.NULL, nsHardware.getPointer());

                try (EnumWbemClassObject results = nsCIMV2.execQuery("SELECT * FROM Win32_Service")) {
                    Assertions.assertNotEquals(Pointer.NULL, results.getPointer());
                }
                try (EnumWbemClassObject results = nsHardware.execQuery("SELECT * FROM RegisteredProfile")) {
                    Assertions.assertNotEquals(Pointer.NULL, results.getPointer());
                }
            }
        }
    }

    @Test
    public void testDupeNS() {
        try (WbemServices ns1 = locator.connectServer("root\\CIMV2")) {
            Assertions.assertNotEquals(Pointer.NULL, ns1.getPointer());
            try (WbemServices ns2 = locator.connectServer("root\\CIMV2")) {
                Assertions.assertNotEquals(Pointer.NULL, ns2.getPointer());

                try (EnumWbemClassObject results = ns1.execQuery("SELECT * FROM Win32_Service")) {
                    Assertions.assertNotEquals(Pointer.NULL, results.getPointer());
                }
                try (EnumWbemClassObject results = ns2.execQuery("SELECT * FROM Win32_Printer")) {
                    Assertions.assertNotEquals(Pointer.NULL, results.getPointer());
                }
            }
        }
    }

    @Test
    public void testDupeQuery() {
        try (WbemServices ns1 = locator.connectServer("root\\CIMV2")) {
            Assertions.assertNotEquals(Pointer.NULL, ns1.getPointer());

            try (EnumWbemClassObject r1 = ns1.execQuery("SELECT * FROM Win32_Service")) {
                Assertions.assertNotEquals(Pointer.NULL, r1.getPointer());
                try (EnumWbemClassObject r2 = ns1.execQuery("SELECT * FROM Win32_Service")) {
                    Assertions.assertNotEquals(Pointer.NULL, r2.getPointer());

                    while (r1.hasNext()) {
                        Assertions.assertTrue(r2.hasNext());
                        try (WbemClassObject cls1 = r1.next()) {
                            try (WbemClassObject cls2 = r2.next()) {
                                String name1 = cls1.getString("Name");
                                String name2 = cls2.getString("Name");

                                Assertions.assertEquals(name1, name2);
                            }
                        }
                    }

                    // make sure that r2 doesn't have more
                    Assertions.assertFalse(r2.hasNext());
                }
            }
        }
    }

    @AfterAll
    public static void finish() {
        locator.release();
    }

}
