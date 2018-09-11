#ifndef LIBWMI_DLLSPEC_H
#define LIBWMI_DLLSPEC_H

#ifdef BUILDING_LIBWMI_DLL
#define LIBWMI __declspec(dllexport)
#else
#define LIBWMI __declspec(dllimport)   
#endif

#endif