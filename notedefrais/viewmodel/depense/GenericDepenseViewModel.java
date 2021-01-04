package com.example.notedefrais.viewmodel.depense;

import android.content.Context;
import com.example.notedefrais.model.database.tables.PrismaGestionCo_NDF_depense;

public interface GenericDepenseViewModel {

    void onRowClick(Context c, PrismaGestionCo_NDF_depense depense);
}
