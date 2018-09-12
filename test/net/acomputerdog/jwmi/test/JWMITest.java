package net.acomputerdog.jwmi.test;

import net.acomputerdog.jwmi.JWMI;
import net.acomputerdog.jwmi.nat.ReleasableVariant;
import net.acomputerdog.jwmi.wbem.EnumWbemClassObject;
import net.acomputerdog.jwmi.wbem.WbemClassObject;
import net.acomputerdog.jwmi.wbem.WbemLocator;
import net.acomputerdog.jwmi.wbem.WbemServices;


public class JWMITest {
    public static final String TEST_NAMESPACE = "root\\CIMV2";
    public static final String TEST_QUERY = "SELECT * FROM Win32_Service";
    public static final String TEST_PROPERTY = "Name";

    public static void main(String[] args) {
        System.out.printf("Starting test of namespace '%s'.\n", TEST_NAMESPACE);
        System.out.printf("Query is '%s'.\n", TEST_QUERY);
        System.out.printf("Retrieving property '%s'.\n", TEST_PROPERTY);

        try (WbemLocator locator = JWMI.getInstance().createWbemLocator()) {
            try (WbemServices services = locator.connectServer(TEST_NAMESPACE)) {
                try (EnumWbemClassObject enumClsObj = services.execQuery(TEST_QUERY)) {
                    while (enumClsObj.hasNext()) {
                        try (WbemClassObject clsObj = enumClsObj.next()) {
                            try (ReleasableVariant variant = clsObj.get(TEST_PROPERTY)) {
                                System.out.println(variant.stringValue());
                            }
                        }
                    }
                }
            }
        }
    }
}
