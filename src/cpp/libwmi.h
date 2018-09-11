#ifndef WMI_WRAPPER_H
#define WMI_WRAPPER_H

#define LIBWMI_DEBUG

#include "libwmi_dllspec.h"
#include <comdef.h>
#include <wbemidl.h>

namespace libwmi {
    
    #ifdef __cplusplus
    extern "C" {
    #endif
    
    /**
     * Initializes COM system
     */
    HRESULT LIBWMI openCOM();
    
    /**
     * Create an instance of IWbemLocator
     */
    HRESULT LIBWMI createLocator(
        IWbemLocator** locator  // Where to write the pointer to the new instance
    );
    
    /**
     * Sets default security for IWbemServices
     */
    HRESULT LIBWMI setSecurity(
        IWbemServices* locator  // The instance to set security on
    );
    
    /**
     * Shuts down the COM system
     */
    HRESULT LIBWMI closeCOM();
    
    /**
     * Calls IWbemLocator::ConnectServer()
     */
    HRESULT LIBWMI IWbemLocator_ConnectServer(
        IWbemLocator*   locator,    // Instance to call method on
        const BSTR      ns,         // WMI namespace to connect to
        IWbemServices** services    // Where to write the pointer to the new IWbemServices instance
    );
    
    /**
     * Calls IWbemServices::ExecQuery()
     */
    HRESULT LIBWMI IWbemServices_ExecQuery(
        IWbemServices*          services,   // Instance to call method on
        const BSTR              query,      // WMI Query to execute
        IEnumWbemClassObject**  clsObjEnum  // Where to write pointer to returned enumerator
    );
    
    /**
     * Calls IEnumWbemClassObject::Next()
     * 
     * If there are more objects in the enumeration, then it will write back an object and the count will be 1.
     * If there are NOT more objects, then it will write back NULL and count is 0.
     * 
     * WBEM_S_NO_ERROR is returned on success.
     * WBEM_S_FALSE is returned on the last object.
     */
    HRESULT LIBWMI IEnumWbemClassObject_Next(
        IEnumWbemClassObject*   clsObjEnum, // Instance to call on
        IWbemClassObject**      clsObj,     // Where to write returned class object
        ULONG*                  count       // Pointer to ULONG that will store number of returned objects
    );
    
    /**
     * Calls IUnknown::Release()
     * 
     * Releases any object returned by WMI
     */
    HRESULT LIBWMI IUnknown_Release(
        IUnknown*   clsObjEnum  // Instance to release
    );
    
    /**
     * Calls IWbemClassObject::Get()
     */
    HRESULT LIBWMI IWbemClassObject_Get(
        IWbemClassObject*   clsObj, // Instance to get from
        LPCWSTR             name,   // Name of the property to get
        VARIANT*            value   // Pointer to variant where value will be written
    );
    
    /**
     * Clears a VARIANT (frees its memory in the remote process)
     */
    HRESULT LIBWMI ClearVariant(
        VARIANT*    variant // The variant to clear
    );
    
    #ifdef __cplusplus
    }
    #endif
}

#endif
