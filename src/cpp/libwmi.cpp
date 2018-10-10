#include "libwmi.h"

#include <iostream>

#pragma comment(lib, "wbemuuid.lib")

using namespace std;

namespace libwmi {
    HRESULT openCOM() {
        // open COM
        HRESULT hr = CoInitializeEx(0, COINIT_MULTITHREADED); 
        if (FAILED(hr)) {
            #ifdef LIBWMI_DEBUG
            cout << "Failed to initialize COM library. Error code = 0x" << hex << hr << endl; 
            #endif
            return hr;
        }
        
        // S_FALSE is returned if COM is already initialized.  Setting security again will fail.
        if (hr != S_FALSE) {
            // setup security
            hr = CoInitializeSecurity(
                NULL,                        // Security descriptor    
                -1,                          // COM negotiates authentication service
                NULL,                        // Authentication services
                NULL,                        // Reserved
                RPC_C_AUTHN_LEVEL_DEFAULT,   // Default authentication level for proxies
                RPC_C_IMP_LEVEL_IMPERSONATE, // Default Impersonation level for proxies
                NULL,                        // Authentication info
                EOAC_NONE,                   // Additional capabilities of the client or server
                NULL                         // Reserved
            );

            // RPC_E_TOO_LATE is returned if the process already set security somewhere else.
            if (FAILED(hr) && hr != RPC_E_TOO_LATE) {
                #ifdef LIBWMI_DEBUG
                cout << "Failed to initialize security. Error code = 0x" << hex << hr << endl;
                #endif
                CoUninitialize();
                return hr;
            }
        }
        
        return S_OK;
    }
    
    HRESULT createLocator(IWbemLocator** locator) {
        if (locator == nullptr) {
            #ifdef LIBWMI_DEBUG
            cout << "locator is null" << endl;
            #endif
            return E_INVALIDARG;
        }
        
        // connect to WMI
        HRESULT hr = CoCreateInstance(CLSID_WbemLocator, 0, CLSCTX_INPROC_SERVER, IID_IWbemLocator, (LPVOID *) locator);

        if (FAILED(hr)) {
            #ifdef LIBWMI_DEBUG
            cout << "Failed to create IWbemLocator object. Error code = 0x" << hex << hr << endl;
            #endif
            return hr;
        }
        
        return S_OK;
    }
    
    HRESULT setSecurity(IWbemServices* locator) {
        if (locator == nullptr) {
            #ifdef LIBWMI_DEBUG
            cout << "services is null" << endl;
            #endif
            return E_INVALIDARG;
        }
        
        // Set blanket security
        HRESULT hr = CoSetProxyBlanket(
            locator,                     // Indicates the proxy to set
            RPC_C_AUTHN_WINNT,           // RPC_C_AUTHN_xxx
            RPC_C_AUTHZ_NONE,            // RPC_C_AUTHZ_xxx
            NULL,                        // Server principal name 
            RPC_C_AUTHN_LEVEL_CALL,      // RPC_C_AUTHN_LEVEL_xxx 
            RPC_C_IMP_LEVEL_IMPERSONATE, // RPC_C_IMP_LEVEL_xxx
            NULL,                        // client identity
            EOAC_NONE                    // proxy capabilities 
        );

        if (FAILED(hr)) {
            #ifdef LIBWMI_DEBUG
            cout << "Could not set proxy blanket. Error code = 0x" << hex << hr << endl;
            #endif
            return hr;
        }
        
        return S_OK;
    }
    
    HRESULT closeCOM() {
        CoUninitialize();
        return S_OK;
    }
    
    HRESULT IWbemLocator_ConnectServer(
        IWbemLocator*   locator,
        const BSTR      ns,
        IWbemServices** services
    ) {
        if (locator == nullptr || ns == nullptr || services == nullptr) {
            #ifdef LIBWMI_DEBUG
            cout << "Unexpected null argument" << endl;
            #endif
            return E_INVALIDARG;
        }
        
        // Connect to namespace
        HRESULT hr = locator->ConnectServer(
            ns,         //namespace
            NULL,       // User name 
            NULL,       // User password
            NULL,       // Locale 
            0,          // Security flags
            NULL,       // Authority 
            nullptr,    // Context object 
            services    // IWbemServices proxy
        );

        if (FAILED(hr)) {
            #ifdef LIBWMI_DEBUG
            cout << "Could not connect to namespace. Error code = 0x" << hex << hr << endl;
            #endif
            return hr;
        }
        
        return S_OK;
    }
    
    HRESULT IWbemServices_ExecQuery(
        IWbemServices*          services,
        const BSTR              query,
        IEnumWbemClassObject    **clsObjEnum
    ) {
        if (services == nullptr || query == nullptr || clsObjEnum == nullptr) {
            #ifdef LIBWMI_DEBUG
            cout << "Unexpected null argument" << endl;
            #endif
            return E_INVALIDARG;
        }
        
        HRESULT hr = services->ExecQuery(
            bstr_t("WQL"), 
            query,
            WBEM_FLAG_FORWARD_ONLY | WBEM_FLAG_RETURN_IMMEDIATELY, 
            NULL,
            clsObjEnum
        );

        if (FAILED(hr)) {
            #ifdef LIBWMI_DEBUG
            cout << "Query for failed. Error code = 0x" << hex << hr << endl;
            #endif
            return hr;
        }
        
        return S_OK;
    }
    
    HRESULT IEnumWbemClassObject_Next(
        IEnumWbemClassObject*   clsObjEnum,
        IWbemClassObject        **clsObj,
        ULONG                   *count
    ) {
        if (clsObjEnum == nullptr || clsObj == nullptr || count == nullptr) {
            #ifdef LIBWMI_DEBUG
            cout << "Unexpected null argument" << endl;
            #endif
            return E_INVALIDARG;
        }
        
        // read next result
        HRESULT hr = clsObjEnum->Next(WBEM_INFINITE, 1, clsObj, count);
        
        // end of enumeration
        if (hr == WBEM_S_FALSE) {
            
            // if we didn't return anything, then return null
            if (*count == 0) {
                *clsObj = nullptr;
            }
        }
        
        return hr;
    }
    
    HRESULT IUnknown_Release(
        IUnknown*   obj
    ) {
        if (obj == nullptr) {
            #ifdef LIBWMI_DEBUG
            cout << "obj is null" << endl;
            #endif
            return E_INVALIDARG;
        }
        
        obj->Release();
        
        return S_OK;
    }
    
    HRESULT IWbemClassObject_Release(
        IWbemClassObject*   clsObj
    ) {
        if (clsObj == nullptr) {
            #ifdef LIBWMI_DEBUG
            cout << "clsObj is null" << endl;
            #endif
            return E_INVALIDARG;
        }
        
        clsObj->Release();
        
        return S_OK;
    }
    
    HRESULT IWbemClassObject_Get(
        IWbemClassObject*   clsObj,
        LPCWSTR             name,
        VARIANT             *value
    ) {
        if (clsObj == nullptr || name == nullptr || value == nullptr) {
            #ifdef LIBWMI_DEBUG
            cout << "Unexpected null argument" << endl;
            #endif
            return E_INVALIDARG;
        }
        
        // Get the value of the Name property
        HRESULT hr = clsObj->Get(name, 0, value, 0, 0);
        
        if (FAILED(hr)) {
            #ifdef LIBWMI_DEBUG
            cout << "Getting query result failed. Error code = 0x" << hex << hr << endl;
            #endif
            return hr;
        }
        
        return S_OK;
    }
    
    HRESULT ClearVariant(
        VARIANT*    variant
    ) {
        if (variant == nullptr) {
            #ifdef LIBWMI_DEBUG
            cout << "Variant is null" << endl;
            #endif
            return E_INVALIDARG;
        }
        
        return VariantClear(variant);
    }
}
