package com.example.notedefrais.model.database;

import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import com.example.notedefrais.model.database.converter.Converters;
import com.example.notedefrais.model.database.dao.PrismaGestionCo_NDF_Dao;
import com.example.notedefrais.model.database.dao.PrismaGestionCo_NDF_synchronisationDao;
import com.example.notedefrais.model.database.dao.PrismaGestionCo_NDF_synchronisation_lineDao;
import com.example.notedefrais.model.database.dao.PrismaGestionCo_NDF_depense_Dao;
import com.example.notedefrais.model.database.dao.PrismaGestionCo_NDF_depense_avionDao;
import com.example.notedefrais.model.database.dao.PrismaGestionCo_NDF_depense_carburantDao;
import com.example.notedefrais.model.database.dao.PrismaGestionCo_NDF_depense_categorieDao;
import com.example.notedefrais.model.database.dao.PrismaGestionCo_NDF_depense_hotelDao;
import com.example.notedefrais.model.database.dao.PrismaGestionCo_NDF_depense_interventionDao;
import com.example.notedefrais.model.database.dao.PrismaGestionCo_NDF_depense_kilometrageDao;
import com.example.notedefrais.model.database.dao.PrismaGestionCo_NDF_depense_locationVehiculeDao;
import com.example.notedefrais.model.database.dao.PrismaGestionCo_NDF_depense_multitauxDao;
import com.example.notedefrais.model.database.dao.PrismaGestionCo_NDF_depense_restaurationDao;
import com.example.notedefrais.model.database.dao.PrismaGestionCo_NDF_depense_trainDao;
import com.example.notedefrais.model.database.dao.PrismaGestionCo_NDF_smartphone_parameterDao;
import com.example.notedefrais.model.database.dao.PrismaGestionCo_NDF_vehiculeDao;
import com.example.notedefrais.model.database.dao.PrismaGestionCo_personneDao;
import com.example.notedefrais.model.database.dao.PrismaGestionCo_projetDao;
import com.example.notedefrais.model.database.dao.PrismaGestionCo_reglement_modeDao;
import com.example.notedefrais.model.database.dao.PrismaGestionCo_tiersDao;
import com.example.notedefrais.model.database.tables.PrismaGestionCo_NDF;
import com.example.notedefrais.model.database.tables.PrismaGestionCo_NDF_synchronisation;
import com.example.notedefrais.model.database.tables.PrismaGestionCo_NDF_synchronisation_line;
import com.example.notedefrais.model.database.tables.PrismaGestionCo_NDF_depense;
import com.example.notedefrais.model.database.tables.PrismaGestionCo_NDF_depense_avion;
import com.example.notedefrais.model.database.tables.PrismaGestionCo_NDF_depense_carburant;
import com.example.notedefrais.model.database.tables.PrismaGestionCo_NDF_depense_categorie;
import com.example.notedefrais.model.database.tables.PrismaGestionCo_NDF_depense_hotel;
import com.example.notedefrais.model.database.tables.PrismaGestionCo_NDF_depense_intervention;
import com.example.notedefrais.model.database.tables.PrismaGestionCo_NDF_depense_kilometrage;
import com.example.notedefrais.model.database.tables.PrismaGestionCo_NDF_depense_locationVehicule;
import com.example.notedefrais.model.database.tables.PrismaGestionCo_NDF_depense_multitaux;
import com.example.notedefrais.model.database.tables.PrismaGestionCo_NDF_depense_restauration;
import com.example.notedefrais.model.database.tables.PrismaGestionCo_NDF_depense_train;
import com.example.notedefrais.model.database.tables.PrismaGestionCo_NDF_smartphone_parameter;
import com.example.notedefrais.model.database.tables.PrismaGestionCo_NDF_vehicule;
import com.example.notedefrais.model.database.tables.PrismaGestionCo_personne;
import com.example.notedefrais.model.database.tables.PrismaGestionCo_projet;
import com.example.notedefrais.model.database.tables.PrismaGestionCo_reglement_mode;
import com.example.notedefrais.model.database.tables.PrismaGestionCo_tiers;

@androidx.room.Database(entities =
        {PrismaGestionCo_projet.class
                , PrismaGestionCo_personne.class
                , PrismaGestionCo_tiers.class
                , PrismaGestionCo_reglement_mode.class
                , PrismaGestionCo_NDF.class
                , PrismaGestionCo_NDF_vehicule.class
                , PrismaGestionCo_NDF_depense.class
                , PrismaGestionCo_NDF_depense_categorie.class
                , PrismaGestionCo_NDF_depense_restauration.class
                , PrismaGestionCo_NDF_depense_intervention.class
                , PrismaGestionCo_NDF_depense_locationVehicule.class
                , PrismaGestionCo_NDF_depense_carburant.class
                , PrismaGestionCo_NDF_depense_kilometrage.class
                , PrismaGestionCo_NDF_depense_avion.class
                , PrismaGestionCo_NDF_depense_hotel.class
                , PrismaGestionCo_NDF_depense_train.class
                , PrismaGestionCo_NDF_depense_multitaux.class
                , PrismaGestionCo_NDF_smartphone_parameter.class
                , PrismaGestionCo_NDF_synchronisation.class
                , PrismaGestionCo_NDF_synchronisation_line.class
        }
        , version = 1
)
@TypeConverters({Converters.class})
public abstract class Database extends RoomDatabase
{
    public abstract PrismaGestionCo_projetDao prismagestionco_projet_dao();
    public abstract PrismaGestionCo_personneDao prismagestionco_personne_dao();
    public abstract PrismaGestionCo_tiersDao prismagestionco_tiers_dao();
    public abstract PrismaGestionCo_reglement_modeDao prismaGestionCo_reglement_mode_dao();
    public abstract PrismaGestionCo_NDF_Dao prismagestionco_ndf_dao();
    public abstract PrismaGestionCo_NDF_vehiculeDao prismagestionco_ndf_vehicule_dao();
    public abstract PrismaGestionCo_NDF_depense_Dao prismagestionco_ndf_depense_dao();
    public abstract PrismaGestionCo_NDF_depense_categorieDao prismagestionco_ndf_depense_categorie_dao();
    public abstract PrismaGestionCo_NDF_depense_restaurationDao prismagestionco_ndf_depense_restauration_dao();
    public abstract PrismaGestionCo_NDF_depense_interventionDao prismagestionco_ndf_depense_intervention_dao();
    public abstract PrismaGestionCo_NDF_depense_locationVehiculeDao prismagestionco_ndf_depense_locationvehicule_dao();
    public abstract PrismaGestionCo_NDF_depense_carburantDao prismagestionco_ndf_depense_carburant_dao();
    public abstract PrismaGestionCo_NDF_depense_kilometrageDao prismagestionco_ndf_depense_kilometrage_dao();
    public abstract PrismaGestionCo_NDF_depense_avionDao prismagestionco_ndf_depense_avion_dao();
    public abstract PrismaGestionCo_NDF_depense_hotelDao prismagestionco_ndf_depense_hotel_dao();
    public abstract PrismaGestionCo_NDF_depense_trainDao prismagestionco_ndf_depense_train_dao();
    public abstract PrismaGestionCo_NDF_depense_multitauxDao prismagestionco_ndf_depense_multitaux_dao();
    public abstract PrismaGestionCo_NDF_smartphone_parameterDao prismagestionco_ndf_smartphone_parameter_dao();
    public abstract PrismaGestionCo_NDF_synchronisationDao prismagestionco_ndf_synchronisation_dao();
    public abstract PrismaGestionCo_NDF_synchronisation_lineDao prismagestionco_ndf_synchronisation_line_dao();
}
