package net.acomputerdog.jwmi.test.junit;

import net.acomputerdog.jwmi.JWMI;
import net.acomputerdog.jwmi.nat.ReleasableVariant;
import net.acomputerdog.jwmi.nat.WMIWrapper;
import net.acomputerdog.jwmi.wbem.EnumWbemClassObject;
import net.acomputerdog.jwmi.wbem.WbemClassObject;
import net.acomputerdog.jwmi.wbem.WbemServices;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class BasicTest {

    private static final String SERVICES_QUERY = "SELECT * FROM Win32_Service";

    private static WbemServices services;

    @BeforeAll
    public static void init() {
        services = JWMI.getInstance().createWbemLocator().connectServer("root\\CIMV2");
    }

    @Test
    public void testSimpleQuery() {
        int found = 0;

        try (EnumWbemClassObject enumClsObj = services.execQuery(SERVICES_QUERY)) {
            while (enumClsObj.hasNext()) {
                try (WbemClassObject clsObj = enumClsObj.next()) {
                    try (ReleasableVariant name = clsObj.get("Name")) {
                        if (name.stringValue().equals("TrustedInstaller")) {
                            found++;
                        }
                        if (name.stringValue().equals("wuauserv")) {
                            found++;
                        }
                        if (name.stringValue().equals("Winmgmt")) {
                            found++;
                        }
                    }
                }
            }
        }

        Assertions.assertEquals(3, found);
    }

    @Test
    public void testSimpleQueryBuffered() {
        int found = 0;

        for (WbemClassObject clsObj : services.execQueryBuffered(SERVICES_QUERY)) {
            try (ReleasableVariant name = clsObj.get("Name")) {
                if (name.stringValue().equals("TrustedInstaller")) {
                    found++;
                }
                if (name.stringValue().equals("wuauserv")) {
                    found++;
                }
                if (name.stringValue().equals("Winmgmt")) {
                    found++;
                }
            } finally {
                clsObj.release();
            }
        }

        Assertions.assertEquals(3, found);
    }

    @Test
    public void testSimpleQuerySingle() {
        try (WbemClassObject clsObj = services.execQuerySingle(SERVICES_QUERY)) {
            try (ReleasableVariant name = clsObj.get("Name")) {
                Assertions.assertEquals("AJRouter", name.stringValue());
            }
        }
    }

    @AfterAll
    public static void finish() {
        WMIWrapper.INSTANCE.closeCOM();
    }
}
