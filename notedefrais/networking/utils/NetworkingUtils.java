package com.example.notedefrais.networking.utils;

import com.example.notedefrais.networking.adapter.RetrofitAdapter;
import com.example.notedefrais.networking.api.IPlateformeWebService;

public class NetworkingUtils {

    private static IPlateformeWebService plateformeWebService;

    public static IPlateformeWebService getApiInstanceForJob() {
        plateformeWebService = RetrofitAdapter.getInstanceForJob().create(IPlateformeWebService.class);
        return plateformeWebService;
    }

    public static IPlateformeWebService getApiInstance() {
        plateformeWebService = RetrofitAdapter.getInstance().create(IPlateformeWebService.class);
        return plateformeWebService;
    }

}
