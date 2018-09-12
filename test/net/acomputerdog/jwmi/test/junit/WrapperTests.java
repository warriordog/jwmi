package net.acomputerdog.jwmi.test.junit;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Variant;
import com.sun.jna.platform.win32.WTypes;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.PointerByReference;
import net.acomputerdog.jwmi.nat.WMIWrapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class WrapperTests {
    @BeforeAll
    public static void init() {
        WMIWrapper.INSTANCE.openCOM();
    }

    @Test
    public void testCreateLocator() {
        WinNT.HRESULT hresult;

        // create instance
        PointerByReference locatorRef = new PointerByReference();
        hresult = WMIWrapper.INSTANCE.createLocator(locatorRef);
        Assertions.assertEquals(WMIWrapper.S_OK, hresult.longValue());
        Assertions.assertNotEquals(Pointer.NULL, locatorRef.getValue());

        hresult = WMIWrapper.INSTANCE.IUnknown_Release(locatorRef.getValue());
        Assertions.assertEquals(WMIWrapper.S_OK, hresult.longValue());
    }

    @Test
    public void testIWbemLocator_ConnectServer() {
        WinNT.HRESULT hresult;

        // create locator
        PointerByReference locatorRef = new PointerByReference();
        hresult = WMIWrapper.INSTANCE.createLocator(locatorRef);
        Assertions.assertEquals(WMIWrapper.S_OK, hresult.longValue());
        Assertions.assertNotEquals(Pointer.NULL, locatorRef.getValue());

        // connect to namespace
        PointerByReference servicesRef = new PointerByReference();
        hresult = WMIWrapper.INSTANCE.IWbemLocator_ConnectServer(locatorRef.getValue(),  new WTypes.BSTR("root\\CIMV2"), servicesRef);
        Assertions.assertEquals(WMIWrapper.S_OK, hresult.longValue());
        Assertions.assertNotEquals(Pointer.NULL, servicesRef.getValue());

        // Release
        hresult = WMIWrapper.INSTANCE.IUnknown_Release(servicesRef.getValue());
        Assertions.assertEquals(WMIWrapper.S_OK, hresult.longValue());
        hresult = WMIWrapper.INSTANCE.IUnknown_Release(locatorRef.getValue());
        Assertions.assertEquals(WMIWrapper.S_OK, hresult.longValue());
    }

    @Test
    public void testSetSecurity() {
        WinNT.HRESULT hresult;

        // create locator
        PointerByReference locatorRef = new PointerByReference();
        hresult = WMIWrapper.INSTANCE.createLocator(locatorRef);
        Assertions.assertEquals(WMIWrapper.S_OK, hresult.longValue());
        Assertions.assertNotEquals(Pointer.NULL, locatorRef.getValue());

        // connect to namespace
        PointerByReference servicesRef = new PointerByReference();
        hresult = WMIWrapper.INSTANCE.IWbemLocator_ConnectServer(locatorRef.getValue(),  new WTypes.BSTR("root\\CIMV2"), servicesRef);
        Assertions.assertEquals(WMIWrapper.S_OK, hresult.longValue());
        Assertions.assertNotEquals(Pointer.NULL, servicesRef.getValue());

        // set security
        hresult = WMIWrapper.INSTANCE.setSecurity(servicesRef.getValue());
        Assertions.assertEquals(WMIWrapper.S_OK, hresult.longValue());

        // Release
        hresult = WMIWrapper.INSTANCE.IUnknown_Release(servicesRef.getValue());
        Assertions.assertEquals(WMIWrapper.S_OK, hresult.longValue());
        hresult = WMIWrapper.INSTANCE.IUnknown_Release(locatorRef.getValue());
        Assertions.assertEquals(WMIWrapper.S_OK, hresult.longValue());
    }

    @Test
    public void testIWbemServices_ExecQuery() {
        WinNT.HRESULT hresult;

        // create locator
        PointerByReference locatorRef = new PointerByReference();
        hresult = WMIWrapper.INSTANCE.createLocator(locatorRef);
        Assertions.assertEquals(WMIWrapper.S_OK, hresult.longValue());
        Assertions.assertNotEquals(Pointer.NULL, locatorRef.getValue());

        // connect to namespace
        PointerByReference servicesRef = new PointerByReference();
        hresult = WMIWrapper.INSTANCE.IWbemLocator_ConnectServer(locatorRef.getValue(),  new WTypes.BSTR("root\\CIMV2"), servicesRef);
        Assertions.assertEquals(WMIWrapper.S_OK, hresult.longValue());
        Assertions.assertNotEquals(Pointer.NULL, servicesRef.getValue());

        // set security
        hresult = WMIWrapper.INSTANCE.setSecurity(servicesRef.getValue());
        Assertions.assertEquals(WMIWrapper.S_OK, hresult.longValue());

        // exec query
        PointerByReference clsObjEnum = new PointerByReference(); // will hold results
        hresult = WMIWrapper.INSTANCE.IWbemServices_ExecQuery(servicesRef.getValue(), new WTypes.BSTR("SELECT * FROM Win32_Service"), clsObjEnum);
        Assertions.assertEquals(WMIWrapper.S_OK, hresult.longValue());
        Assertions.assertNotEquals(Pointer.NULL, clsObjEnum.getValue());

        // Release
        hresult = WMIWrapper.INSTANCE.IUnknown_Release(clsObjEnum.getValue());
        Assertions.assertEquals(WMIWrapper.S_OK, hresult.longValue());
        hresult = WMIWrapper.INSTANCE.IUnknown_Release(servicesRef.getValue());
        Assertions.assertEquals(WMIWrapper.S_OK, hresult.longValue());
        hresult = WMIWrapper.INSTANCE.IUnknown_Release(locatorRef.getValue());
        Assertions.assertEquals(WMIWrapper.S_OK, hresult.longValue());
    }

    @Test
    public void testIEnumWbemClassObject_Next() {
        WinNT.HRESULT hresult;

        // create locator
        PointerByReference locatorRef = new PointerByReference();
        hresult = WMIWrapper.INSTANCE.createLocator(locatorRef);
        Assertions.assertEquals(WMIWrapper.S_OK, hresult.longValue());
        Assertions.assertNotEquals(Pointer.NULL, locatorRef.getValue());

        // connect to namespace
        PointerByReference servicesRef = new PointerByReference();
        hresult = WMIWrapper.INSTANCE.IWbemLocator_ConnectServer(locatorRef.getValue(),  new WTypes.BSTR("root\\CIMV2"), servicesRef);
        Assertions.assertEquals(WMIWrapper.S_OK, hresult.longValue());
        Assertions.assertNotEquals(Pointer.NULL, servicesRef.getValue());

        // set security
        hresult = WMIWrapper.INSTANCE.setSecurity(servicesRef.getValue());
        Assertions.assertEquals(WMIWrapper.S_OK, hresult.longValue());

        // exec query
        PointerByReference clsObjEnum = new PointerByReference(); // will hold results
        hresult = WMIWrapper.INSTANCE.IWbemServices_ExecQuery(servicesRef.getValue(), new WTypes.BSTR("SELECT * FROM Win32_Service"), clsObjEnum);
        Assertions.assertEquals(WMIWrapper.S_OK, hresult.longValue());
        Assertions.assertNotEquals(Pointer.NULL, clsObjEnum.getValue());

        // Get result
        PointerByReference clsObj = new PointerByReference();  // will store the result
        WinDef.ULONGByReference count = new WinDef.ULONGByReference(); // will store how many results were returned
        hresult = WMIWrapper.INSTANCE.IEnumWbemClassObject_Next(clsObjEnum.getValue(), clsObj, count);
        Assertions.assertEquals(1, count.getValue().longValue());
        Assertions.assertTrue(hresult.longValue() == WMIWrapper.WBEM_S_NO_ERROR || hresult.longValue() == WMIWrapper.WBEM_S_FALSE);
        Assertions.assertNotEquals(Pointer.NULL, clsObj.getValue());

        // Release
        hresult = WMIWrapper.INSTANCE.IUnknown_Release(clsObjEnum.getValue());
        Assertions.assertEquals(WMIWrapper.S_OK, hresult.longValue());
        hresult = WMIWrapper.INSTANCE.IUnknown_Release(servicesRef.getValue());
        Assertions.assertEquals(WMIWrapper.S_OK, hresult.longValue());
        hresult = WMIWrapper.INSTANCE.IUnknown_Release(locatorRef.getValue());
        Assertions.assertEquals(WMIWrapper.S_OK, hresult.longValue());
    }

    @Test
    public void testIWbemClassObject_Get() {
        WinNT.HRESULT hresult;

        // create locator
        PointerByReference locatorRef = new PointerByReference();
        hresult = WMIWrapper.INSTANCE.createLocator(locatorRef);
        Assertions.assertEquals(WMIWrapper.S_OK, hresult.longValue());
        Assertions.assertNotEquals(Pointer.NULL, locatorRef.getValue());

        // connect to namespace
        PointerByReference servicesRef = new PointerByReference();
        hresult = WMIWrapper.INSTANCE.IWbemLocator_ConnectServer(locatorRef.getValue(),  new WTypes.BSTR("root\\CIMV2"), servicesRef);
        Assertions.assertEquals(WMIWrapper.S_OK, hresult.longValue());
        Assertions.assertNotEquals(Pointer.NULL, servicesRef.getValue());

        // set security
        hresult = WMIWrapper.INSTANCE.setSecurity(servicesRef.getValue());
        Assertions.assertEquals(WMIWrapper.S_OK, hresult.longValue());

        // exec query
        PointerByReference clsObjEnum = new PointerByReference(); // will hold results
        hresult = WMIWrapper.INSTANCE.IWbemServices_ExecQuery(servicesRef.getValue(), new WTypes.BSTR("SELECT * FROM Win32_Service"), clsObjEnum);
        Assertions.assertEquals(WMIWrapper.S_OK, hresult.longValue());
        Assertions.assertNotEquals(Pointer.NULL, clsObjEnum.getValue());

        // Get result
        PointerByReference clsObj = new PointerByReference();  // will store the result
        WinDef.ULONGByReference count = new WinDef.ULONGByReference(); // will store how many results were returned
        hresult = WMIWrapper.INSTANCE.IEnumWbemClassObject_Next(clsObjEnum.getValue(), clsObj, count);
        Assertions.assertTrue(hresult.longValue() == WMIWrapper.WBEM_S_NO_ERROR || hresult.longValue() == WMIWrapper.WBEM_S_FALSE);
        Assertions.assertEquals(1, count.getValue().longValue());
        Assertions.assertNotEquals(Pointer.NULL, clsObj.getValue());

        // get property
        Variant.VARIANT variant = new Variant.VARIANT(); // will hold result
        hresult = WMIWrapper.INSTANCE.IWbemClassObject_Get(clsObj.getValue(),  new WTypes.BSTR("Name"), variant.getPointer());
        Assertions.assertEquals(WMIWrapper.S_OK, hresult.longValue());
        Assertions.assertFalse(variant.stringValue().isEmpty());

        // Release
        hresult = WMIWrapper.INSTANCE.ClearVariant(variant.getPointer());
        Assertions.assertEquals(WMIWrapper.S_OK, hresult.longValue());
        hresult = WMIWrapper.INSTANCE.IUnknown_Release(clsObjEnum.getValue());
        Assertions.assertEquals(WMIWrapper.S_OK, hresult.longValue());
        hresult = WMIWrapper.INSTANCE.IUnknown_Release(servicesRef.getValue());
        Assertions.assertEquals(WMIWrapper.S_OK, hresult.longValue());
        hresult = WMIWrapper.INSTANCE.IUnknown_Release(locatorRef.getValue());
        Assertions.assertEquals(WMIWrapper.S_OK, hresult.longValue());
    }

    @BeforeAll
    public static void finish() {
        WMIWrapper.INSTANCE.closeCOM();
    }
}
