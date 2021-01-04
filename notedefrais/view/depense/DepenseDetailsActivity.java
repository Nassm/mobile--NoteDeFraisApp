package com.example.notedefrais.view.depense;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import com.example.notedefrais.App;
import com.example.notedefrais.R;
import com.example.notedefrais.model.database.tables.GenericEntity;
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
import com.example.notedefrais.model.utils.Helper;
import com.example.notedefrais.model.utils.NdfLogger;
import com.example.notedefrais.networking.job.RemoteDataStore;
import com.example.notedefrais.view.ActivityBase;
import com.example.notedefrais.view.NdfActivity;
import com.example.notedefrais.view.selection.DepenseCategorySelectionActivity;
import com.example.notedefrais.view.selection.PictureSelectionActivity;
import com.example.notedefrais.viewmodel.depense.DepenseDetailsViewModel;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class DepenseDetailsActivity extends ActivityBase implements View.OnClickListener, DepenseDetailsViewModel.IAction {

    private DepenseDetailsViewModel mViewModel;
    public final static int ADD_PICTURE_CODE = 205;
    public static final String EXTRA_DEPENSE = "DEPENSE";
    private final Calendar calendar = Calendar.getInstance();
    private ArrayList<String> depImagesString = new ArrayList<>();
    private CompositeDisposable disposable = new CompositeDisposable();
    public static final String TAG = DepenseDetailsActivity.class.getSimpleName();

    private TextView mPhotoCount;
    private Button mSubmit, mCancel;
    private EditText depHotelNbNuits;
    private ImageView mPhoto, mPhotoAdd;
    private EditText depKilometrageDistance;
    private EditText mDepRestaurationInvite;
    private EditText mDate, mDescription, mTTC, mHT, mTVA;
    private EditText depInterventionDuree, depInterventionPrix;
    private EditText depCarburantLitrage, depCarburantCompteur;
    private String _date, _description, _projet, _reg, _ttc, _ht, _tva;
    private EditText depMultitauxTTC, depMultitauxHT, depMultitauxTVA, depMultitauxTaxe;
    private Spinner mProjet, mReglementMode, depCarburantVehicule, depKilometrageVehicule;
    private EditText depAvionNumVol, depAvionDateDepart, depAvionDateArrivee, depAvionCompagnie, depAvionTrajet;
    private EditText depTrainNumTrain, depTrainDateDepart, depTrainDateArrivee, depTrainCompagnie, depTrainTrajet;
    private EditText depLocationMarque, depLocationModele, depLocationLoueur, depLocationDateDebut, depLocationDateFin;
    private CardView depRestauration, depIntervention, depCarburant, depKilometrage, depAvion, depHotel, depTrain, depMultitaux, depLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        try
        {
            mViewModel = new DepenseDetailsViewModel(this, disposable, ((PrismaGestionCo_NDF_depense)getIntent().getSerializableExtra(EXTRA_DEPENSE)), getIntent().getData());

        } catch (Exception e)
        {
            NdfLogger.e(TAG, e.getMessage());
        }
    }
    @Override
    public void vmLoadingFinished(boolean isFinish)
    {
        if(isFinish)
        {
            try
            {
                displayMainView(mViewModel);
                displayDetailsView(mViewModel.getDepense().getType());
                displayContentView(mViewModel);
                setClickListener(this);

                String errorAnalysisTaskMaybe = getIntent().getStringExtra(DepenseCategorySelectionActivity.PictureAnalysisTask_WARMING_EXTRA);

                if(errorAnalysisTaskMaybe != null)
                {
                    Helper.generateDialogOneAction(this, errorAnalysisTaskMaybe, (dialog, which) ->
                    {
                        if (which == DialogInterface.BUTTON_POSITIVE)
                        {
                            dialog.dismiss();
                        }
                    });
                }
            } catch (Exception e)
            {
                NdfLogger.e(TAG, e.getMessage());
            }
        }
    }
    private void displayMainView(DepenseDetailsViewModel viewModel)
    {
        try
        {
            setContentView(R.layout.activity_depense_details);
            setTitle(getString(R.string.DEPENSE_DE_TYPE) + viewModel.getDepense().getType());
            mHT = findViewById(R.id.depHT);
            mTTC = findViewById(R.id.depTTC);
            mTVA = findViewById(R.id.depTVA);
            mDate = findViewById(R.id.depDate);
            mPhoto = findViewById(R.id.depPhoto);
            mPhotoAdd = findViewById(R.id.depPhotoAdd);
            mPhotoCount = findViewById(R.id.depPhotoCount);
            mCancel = findViewById(R.id.depCancel);
            mSubmit = findViewById(R.id.depValidate);
            mProjet = findViewById(R.id.depProjetSelect);
            mDescription = findViewById(R.id.depDescription);
            mReglementMode = findViewById(R.id.depReglementMode);

        } catch (Exception e)
        {
            NdfLogger.e(TAG, e.getMessage());
        }

    }
    private void displayDetailsView(String view)
    {
        try
        {
            switch (view)
            {
                case PrismaGestionCo_NDF_depense_categorie.RESTAURATION:
                    depRestauration = findViewById(R.id.depRestauration);
                    mDepRestaurationInvite = findViewById(R.id.depRestaurationInvites);
                    depRestauration.setVisibility(View.VISIBLE);
                    if(mViewModel.getDepense().getId() != 0)
                    {
                        mDepRestaurationInvite.setText(App.get().getDB().prismagestionco_ndf_depense_restauration_dao()
                                .getByIdDepense(mViewModel.getDepense().getId()).getInvites());
                        mDepRestaurationInvite.setEnabled(false);
                    }
                    break;

                case PrismaGestionCo_NDF_depense_categorie.INTERVENTION:
                    depIntervention = findViewById(R.id.depIntervention);
                    depInterventionDuree = findViewById(R.id.depInterventionDuree);
                    depInterventionPrix = findViewById(R.id.depInterventionPrixHoraire);
                    depIntervention.setVisibility(View.VISIBLE);
                    if(mViewModel.getDepense().getId() != 0)
                    {
                        depInterventionDuree.setText(String.valueOf(App.get().getDB().prismagestionco_ndf_depense_intervention_dao()
                                .getByIdDepense(mViewModel.getDepense().getId()).getDureeIntervention()));
                        depInterventionPrix.setText(String.valueOf(App.get().getDB().prismagestionco_ndf_depense_intervention_dao()
                                .getByIdDepense(mViewModel.getDepense().getId()).getPrixHoraire()));
                        depInterventionDuree.setEnabled(false);
                        depInterventionPrix.setEnabled(false);
                    }
                    break;

                case PrismaGestionCo_NDF_depense_categorie.CARBURANT:
                    depCarburant = findViewById(R.id.depCarburant);
                    depCarburantCompteur = findViewById(R.id.depCarburantCompteur);
                    depCarburantLitrage = findViewById(R.id.depCarburantLitrage);
                    depCarburantVehicule = findViewById(R.id.depCarburantVehiculeSelect);
                    depCarburant.setVisibility(View.VISIBLE);
                    if(mViewModel.getDepense().getId() != 0)
                    {
                        depCarburantCompteur.setText(String.valueOf(App.get().getDB().prismagestionco_ndf_depense_carburant_dao()
                                .getByIdDepense(mViewModel.getDepense().getId()).getCompteur()));
                        depCarburantLitrage.setText(String.valueOf(App.get().getDB().prismagestionco_ndf_depense_carburant_dao()
                                .getByIdDepense(mViewModel.getDepense().getId()).getLitrage()));
                        depCarburantVehicule.setAdapter(new ArrayAdapter<>(this, R.layout.item_ndf_spinner, App.get().getDB().prismagestionco_ndf_vehicule_dao().getNameById(App.get().getDB().prismagestionco_ndf_depense_carburant_dao().getByIdDepense(mViewModel.getDepense().getId()).getId())));
                        depCarburantCompteur.setEnabled(false);
                        depCarburantLitrage.setEnabled(false);
                        depCarburantVehicule.setEnabled(false);
                    }
                    else
                    {
                        depCarburantVehicule.setAdapter(new ArrayAdapter<>(this, R.layout.item_ndf_spinner, mViewModel.getmVehiculeStringList()));
                    }
                    break;

                case PrismaGestionCo_NDF_depense_categorie.KILOMETRAGE:
                    depKilometrage = findViewById(R.id.depKilometrage);
                    depKilometrageDistance = findViewById(R.id.depKilometrageDistance);
                    depKilometrageVehicule = findViewById(R.id.depKilometrageVehiculeSelect);
                    depKilometrage.setVisibility(View.VISIBLE);
                    if(mViewModel.getDepense().getId() != 0)
                    {
                        depKilometrageDistance.setText(String.valueOf(App.get().getDB().prismagestionco_ndf_depense_kilometrage_dao()
                                .getByIdDepense(mViewModel.getDepense().getId()).getDistance()));
                        depKilometrageVehicule.setAdapter(new ArrayAdapter<>(this, R.layout.item_ndf_spinner, App.get().getDB().prismagestionco_ndf_vehicule_dao().getNameById(App.get().getDB().prismagestionco_ndf_depense_kilometrage_dao().getByIdDepense(mViewModel.getDepense().getId()).getId())));
                        depKilometrageDistance.setEnabled(false);
                        depKilometrageVehicule.setEnabled(false);
                    }
                    else
                    {
                        depKilometrageVehicule.setAdapter(new ArrayAdapter<>(this, R.layout.item_ndf_spinner, mViewModel.getmVehiculeStringList()));
                    }
                    break;

                case PrismaGestionCo_NDF_depense_categorie.AVION:
                    depAvion = findViewById(R.id.depAvion);
                    depAvionCompagnie = findViewById(R.id.depAvionCompagnie);
                    depAvionNumVol = findViewById(R.id.depAvionNumeroVol);
                    depAvionTrajet = findViewById(R.id.depAvionTrajet);
                    depAvionDateDepart = findViewById(R.id.depAvionDateDepart);
                    depAvionDateArrivee = findViewById(R.id.depAvionDateArrivee);
                    depAvion.setVisibility(View.VISIBLE);
                    if(mViewModel.getDepense().getId() != 0)
                    {
                        depAvionCompagnie.setText(App.get().getDB().prismagestionco_ndf_depense_avion_dao()
                                .getByIdDepense(mViewModel.getDepense().getId()).getCompagnie());
                        depAvionNumVol.setText(App.get().getDB().prismagestionco_ndf_depense_avion_dao()
                                .getByIdDepense(mViewModel.getDepense().getId()).getNumVol());
                        depAvionTrajet.setText(App.get().getDB().prismagestionco_ndf_depense_avion_dao()
                                .getByIdDepense(mViewModel.getDepense().getId()).getTrajet());
                        depAvionDateDepart.setText(Helper.formatDate(App.get().getDB().prismagestionco_ndf_depense_avion_dao()
                                .getByIdDepense(mViewModel.getDepense().getId()).getDateDepart()));
                        depAvionDateArrivee.setText(Helper.formatDate(App.get().getDB().prismagestionco_ndf_depense_avion_dao()
                                .getByIdDepense(mViewModel.getDepense().getId()).getDateArrivee()));
                        depAvionCompagnie.setEnabled(false);
                        depAvionNumVol.setEnabled(false);
                        depAvionTrajet.setEnabled(false);
                        depAvionDateDepart.setEnabled(false);
                        depAvionDateArrivee.setEnabled(false);
                    }
                    break;

                case PrismaGestionCo_NDF_depense_categorie.HOTEL:
                    depHotel = findViewById(R.id.depHotel);
                    depHotelNbNuits = findViewById(R.id.depHotelNombreNuit);
                    depHotel.setVisibility(View.VISIBLE);
                    if(mViewModel.getDepense().getId() != 0)
                    {
                        depHotelNbNuits.setText(String.valueOf(App.get().getDB().prismagestionco_ndf_depense_hotel_dao()
                                .getByIdDepense(mViewModel.getDepense().getId()).getNbNuits()));
                        depHotelNbNuits.setEnabled(false);
                    }
                    break;

                case PrismaGestionCo_NDF_depense_categorie.TRAIN:
                    depTrain = findViewById(R.id.depTrain);
                    depTrainCompagnie = findViewById(R.id.depTrainCompagnie);
                    depTrainNumTrain = findViewById(R.id.depTrainNumeroTrain);
                    depTrainTrajet = findViewById(R.id.depTrainTrajet);
                    depTrainDateDepart = findViewById(R.id.depTrainDateDepart);
                    depTrainDateArrivee = findViewById(R.id.depTrainDateArrivee);
                    depTrain.setVisibility(View.VISIBLE);
                    if(mViewModel.getDepense().getId() != 0)
                    {
                        depTrainCompagnie.setText(App.get().getDB().prismagestionco_ndf_depense_train_dao()
                                .getByIdDepense(mViewModel.getDepense().getId()).getCompagnie());
                        depTrainNumTrain.setText(App.get().getDB().prismagestionco_ndf_depense_train_dao()
                                .getByIdDepense(mViewModel.getDepense().getId()).getNumTrain());
                        depTrainTrajet.setText(App.get().getDB().prismagestionco_ndf_depense_train_dao()
                                .getByIdDepense(mViewModel.getDepense().getId()).getTrajet());
                        depTrainDateDepart.setText(Helper.formatDate(App.get().getDB().prismagestionco_ndf_depense_train_dao()
                                .getByIdDepense(mViewModel.getDepense().getId()).getDateDepart()));
                        depTrainDateArrivee.setText(Helper.formatDate(App.get().getDB().prismagestionco_ndf_depense_train_dao()
                                .getByIdDepense(mViewModel.getDepense().getId()).getDateArrivee()));
                        depTrainCompagnie.setEnabled(false);
                        depTrainNumTrain.setEnabled(false);
                        depTrainTrajet.setEnabled(false);
                        depTrainDateDepart.setEnabled(false);
                        depTrainDateArrivee.setEnabled(false);
                    }
                    break;

                case PrismaGestionCo_NDF_depense_categorie.MULTITAUX:
                    depMultitaux = findViewById(R.id.depMultitaux);
                    depMultitauxTTC = findViewById(R.id.depMultitauxTTC);
                    depMultitauxHT = findViewById(R.id.depMultitauxHT);
                    depMultitauxTVA = findViewById(R.id.depMultitauxTVA);
                    depMultitauxTaxe = findViewById(R.id.depMultitauxTaxe);
                    depMultitaux.setVisibility(View.VISIBLE);
                    if(mViewModel.getDepense().getId() != 0)
                    {
                        depMultitauxTTC.setText(String.valueOf(App.get().getDB().prismagestionco_ndf_depense_multitaux_dao()
                                .getByIdDepense(mViewModel.getDepense().getId()).getMontantTTC()));
                        depMultitauxHT.setText(String.valueOf(App.get().getDB().prismagestionco_ndf_depense_multitaux_dao()
                                .getByIdDepense(mViewModel.getDepense().getId()).getMontantHT()));
                        depMultitauxTVA.setText(String.valueOf(App.get().getDB().prismagestionco_ndf_depense_multitaux_dao()
                                .getByIdDepense(mViewModel.getDepense().getId()).getMontantTVA()));
                        depMultitauxTaxe.setText(String.valueOf(App.get().getDB().prismagestionco_ndf_depense_multitaux_dao()
                                .getByIdDepense(mViewModel.getDepense().getId()).getMontantTaxe()));
                        depMultitauxTTC.setEnabled(false);
                        depMultitauxHT.setEnabled(false);
                        depMultitauxTVA.setEnabled(false);
                        depMultitauxTaxe.setEnabled(false);
                    }
                    break;

                case PrismaGestionCo_NDF_depense_categorie.LOCATIONVEHICULE:
                    depLocation = findViewById(R.id.depLocationVehicule);
                    depLocationMarque = findViewById(R.id.depLocationVehiculeMarque);
                    depLocationModele = findViewById(R.id.depLocationVehiculeModele);
                    depLocationLoueur = findViewById(R.id.depLocationVehiculeLoueur);
                    depLocationDateDebut = findViewById(R.id.depLocationVehiculeDebut);
                    depLocationDateFin = findViewById(R.id.depLocationVehiculeFin);
                    depLocation.setVisibility(View.VISIBLE);
                    if(mViewModel.getDepense().getId() != 0)
                    {
                        depLocationMarque.setText(App.get().getDB().prismagestionco_ndf_depense_locationvehicule_dao()
                                .getByIdDepense(mViewModel.getDepense().getId()).getMarqueVehicule());
                        depLocationModele.setText(App.get().getDB().prismagestionco_ndf_depense_locationvehicule_dao()
                                .getByIdDepense(mViewModel.getDepense().getId()).getModeleVehicule());
                        depLocationLoueur.setText(App.get().getDB().prismagestionco_ndf_depense_locationvehicule_dao()
                                .getByIdDepense(mViewModel.getDepense().getId()).getLoueur());
                        depLocationDateDebut.setText(Helper.formatDate(App.get().getDB().prismagestionco_ndf_depense_locationvehicule_dao()
                                .getByIdDepense(mViewModel.getDepense().getId()).getDateDebut()));
                        depLocationDateFin.setText(Helper.formatDate(App.get().getDB().prismagestionco_ndf_depense_locationvehicule_dao()
                                .getByIdDepense(mViewModel.getDepense().getId()).getDateFin()));
                        depLocationMarque.setEnabled(false);
                        depLocationModele.setEnabled(false);
                        depLocationLoueur.setEnabled(false);
                        depLocationDateDebut.setEnabled(false);
                        depLocationDateFin.setEnabled(false);
                    }
                    break;
            }

        } catch (Exception e)
        {
            NdfLogger.e(TAG, e.getMessage());
        }
    }
    private void displayContentView(DepenseDetailsViewModel viewModel)
    {
        dataMappingTask(viewModel);
    }
    private void dataMappingTask(DepenseDetailsViewModel viewModel)
    {
        try
        {
            if(viewModel.getUri() != null)
            {
                mPhoto.setImageURI(viewModel.getUri());
                mViewModel.getDepense().setImage1(mViewModel.getUri().toString());
                depImagesString.add(mViewModel.getUri().toString());
            } else
                {
                    if(viewModel.getDepense().getImage1() != null)
                    {
                        mPhoto.setImageURI(Uri.parse(viewModel.getDepense().getImage1()));
                        depImagesString.add(viewModel.getDepense().getImage1());
                    }
                    if(viewModel.getDepense().getImage2() != null)
                    {
                        depImagesString.add(viewModel.getDepense().getImage2());
                    }
                    if(viewModel.getDepense().getImage3() != null)
                    {
                        depImagesString.add(viewModel.getDepense().getImage3());
                    }
                }

            if(depImagesString != null)
            {
                mPhotoCount.setText(String.valueOf(depImagesString.size()));
            }

            if(viewModel.getDepense().getDate() != null)
            {
                mDate.setText(Helper.formatDate(viewModel.getDepense().getDate()));

            }
            if(viewModel.getDepense().getDescription() != null)
            {
                mDescription.setText(viewModel.getDepense().getDescription());
            }
            if(viewModel.getDepense().getMontantTTC() != null)
            {
                mTTC.setText(Helper.formatDecimalTwoDigits(viewModel.getDepense().getMontantTTC()));

            }
            if(viewModel.getDepense().getMontantHT() != null)
            {
                mHT.setText(Helper.formatDecimalTwoDigits(viewModel.getDepense().getMontantHT()));
            }
            if(viewModel.getDepense().getMontantTVA() != null)
            {
                mTVA.setText(Helper.formatDecimalTwoDigits(viewModel.getDepense().getMontantTVA()));
            }

            if(viewModel.getDepense().getId() != 0)
            {
                /* if exist object, just view*/
                mDate.setEnabled(false);
                mDescription.setEnabled(false);
                mTTC.setEnabled(false);
                mHT.setEnabled(false);
                mTVA.setEnabled(false);
                mPhotoAdd.setVisibility(View.GONE);
                mSubmit.setVisibility(View.GONE);
                mCancel.setVisibility(View.GONE);

                if(viewModel.getDepense().getIdProjet() != 0)
                {
                    mProjet.setAdapter(new ArrayAdapter<>(this, R.layout.item_ndf_spinner, App.get().getDB().prismagestionco_projet_dao().getNameById(viewModel.getDepense().getIdProjet())));
                    mProjet.setEnabled(false);
                }
                if(viewModel.getDepense().getIdReglementMode() != 0)
                {
                    mReglementMode.setAdapter(new ArrayAdapter<>(this, R.layout.item_ndf_spinner, App.get().getDB().prismaGestionCo_reglement_mode_dao().getLibelleById(viewModel.getDepense().getIdReglementMode())));
                    mReglementMode.setEnabled(false);
                }
            }else
                {
                    mProjet.setAdapter(new ArrayAdapter<>(this, R.layout.item_ndf_spinner, viewModel.getmProjetStringList()));
                    mReglementMode.setAdapter(new ArrayAdapter<>(this, R.layout.item_ndf_spinner, viewModel.getmReglementModeStringList()));
                }

        } catch (Exception e)
        {
            NdfLogger.e(TAG, e.getMessage());
        }
    }
    private void setClickListener(View.OnClickListener v)
    {
        try
        {
            mPhotoAdd.setOnClickListener(v); //picture
            mSubmit.setOnClickListener(v);
            mCancel.setOnClickListener(v);
            mDate.setOnClickListener(v);

            if(mViewModel.getDepense().getType().equals(PrismaGestionCo_NDF_depense_categorie.AVION))
            {
                depAvionDateDepart.setOnClickListener(v);
                depAvionDateArrivee.setOnClickListener(v);

            } else if(mViewModel.getDepense().getType().equals(PrismaGestionCo_NDF_depense_categorie.TRAIN))
            {
                depTrainDateDepart.setOnClickListener(v);
                depTrainDateArrivee.setOnClickListener(v);
            } else if(mViewModel.getDepense().getType().equals(PrismaGestionCo_NDF_depense_categorie.LOCATIONVEHICULE))
            {
                depLocationDateDebut.setOnClickListener(v);
                depLocationDateFin.setOnClickListener(v);
            }

        } catch (Exception e)
        {
            NdfLogger.e(TAG, e.getMessage());
        }
    }
    private boolean isInputLinked()
    {
        _date = mDate.getText().toString();
        _description = mDescription.getText().toString();
        _projet = mProjet.getSelectedItem().toString();
        _reg = mReglementMode.getSelectedItem().toString();
        _ttc = mTTC.getText().toString();
        _ht = mHT.getText().toString();
        _tva = mTVA.getText().toString();

        if (_date.trim().isEmpty()
                || _description.trim().isEmpty()
                || _projet.trim().isEmpty()
                || _reg.trim().isEmpty()
                || _ttc.trim().isEmpty()
                || _ht.trim().isEmpty()
                || _tva.trim().isEmpty())
        {
            return false;
        }
        return  true;
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.depPhotoAdd:
                startActivityForResult(PictureSelectionActivity.launchDetail(this, this.depImagesString), DepenseDetailsActivity.ADD_PICTURE_CODE); // open activity, show current picture and allow add new picture
                break;

            case R.id.depValidate:
                if(!isInputLinked())
                {
                    mViewModel.getAction().message(getString(R.string.INPUT_MISSING));
                    return;
                }
                buildDepenseObject();
                break;

            case R.id.depCancel:
                Helper.generateDialogTwoAction(this, getString(R.string.ALERT_MODIFICATION_RISQUE_PERDU), (dialog, which) ->
                {
                    switch (which)
                    {
                        case DialogInterface.BUTTON_POSITIVE:
                            finish();
                            startActivity(new Intent(this, NdfActivity.class));
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            dialog.cancel();
                            break;
                    }
                });
                break;

            case R.id.depDate:
                updateDateField(mDate);
                break;

            case R.id.depAvionDateDepart:
                updateDateField(depAvionDateDepart);
                break;

            case R.id.depAvionDateArrivee:
                updateDateField(depAvionDateArrivee);
                break;

            case R.id.depTrainDateDepart:
                updateDateField(depTrainDateDepart);
                break;

            case R.id.depTrainDateArrivee:
                updateDateField(depTrainDateArrivee);
                break;

            case R.id.depLocationVehiculeDebut:
                updateDateField(depLocationDateDebut);
                break;

            case R.id.depLocationVehiculeFin:
                updateDateField(depLocationDateFin);
                break;
        }
    }
    @Override
    public void buildDepenseObject()
    {
        disposable.add(Single.fromCallable(() ->
        {
            String error;
            try
            {
                mViewModel.getDepense().setDate(Helper.formatStringToDate(_date));
                mViewModel.getDepense().setDescription(_description);

                if(!(new BigDecimal(Helper.formatStringToFloatString(_tva))).equals((new BigDecimal(Helper.formatStringToFloatString(_ttc))).subtract((new BigDecimal(Helper.formatStringToFloatString(_ht))))))
                {
                    error = App.get().getString(R.string.AMOUNT_ERROR);
                    return error;

                } else if(Helper.formatStringToFloat(_ttc) <= 0 )
                {
                    error = App.get().getString(R.string.ALERT_TOTAL_INCORRECT);
                    return error;
                }
                else {
                        mViewModel.getDepense().setMontantTTC(Helper.formatStringToFloat(_ttc));
                        mViewModel.getDepense().setMontantHT(Helper.formatStringToFloat(_ht));
                        mViewModel.getDepense().setMontantTVA(Helper.formatStringToFloat(_tva));
                    }

                int idProject = App.get().getDB().prismagestionco_projet_dao().getIdProjectByName(_projet);
                mViewModel.getDepense().setIdProjet(idProject);

                int idTiersClient =  App.get().getDB().prismagestionco_projet_dao().getIdTiersClientByIdProject(idProject);
                mViewModel.getDepense().setIdTiersClient(idTiersClient);

                int idReglement = App.get().getDB().prismaGestionCo_reglement_mode_dao().getIdReglementByName(_reg);
                mViewModel.getDepense().setIdReglementMode(idReglement);

                mViewModel.getDepense().setIdSmartphone(UUID.randomUUID().toString());
                mViewModel.getDepense().setIdSmartphoneNDF(App.get().getDB().prismagestionco_ndf_dao().getById(App.getNdfId()).getIdSmartphone());

                error = "";

                return error;
            }
            catch (Exception e)
            {
                NdfLogger.e(TAG, e.getMessage());
                return null;
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((result) ->
                {
                    if (result.equals(""))
                    {
                        validateDepenseType(mViewModel.getDepense().getType());
                    }
                    else if(result.equals(App.get().getString(R.string.AMOUNT_ERROR)))
                    {
                        mViewModel.getAction().error(result);

                    }else if(result.equals(App.get().getString(R.string.ALERT_TOTAL_INCORRECT)))
                    {
                        mViewModel.getAction().error(result);
                    }
                    else
                    {
                        mViewModel.getAction().error(App.get().getString(R.string.ERROR_SAVE_DEPENSE));
                    }
                }));
    }
    @Override
    public void validateDepenseType(String depenseType)
    {
        switch (depenseType)
        {
                case PrismaGestionCo_NDF_depense_categorie.RESTAURATION:
                    saveRestaurationDepense();
                    break;

                case PrismaGestionCo_NDF_depense_categorie.INTERVENTION:
                    saveInterventionDepense();
                    break;

                case PrismaGestionCo_NDF_depense_categorie.CARBURANT:
                    saveCarburantDepense();
                    break;

                case PrismaGestionCo_NDF_depense_categorie.KILOMETRAGE:
                    saveKilometrageDepense();
                    break;

                case PrismaGestionCo_NDF_depense_categorie.AVION:
                    saveAvionDepense();
                    break;

                case PrismaGestionCo_NDF_depense_categorie.HOTEL:
                    saveHotelDepense();
                    break;

                case PrismaGestionCo_NDF_depense_categorie.TRAIN:
                    saveTrainDepense();
                    break;

                case PrismaGestionCo_NDF_depense_categorie.MULTITAUX:
                    saveMultitauxDepense();
                    break;

                case PrismaGestionCo_NDF_depense_categorie.LOCATIONVEHICULE:
                    saveLocationDepense();
                    break;
            default: /* ---- no match type to save  ---- */
                saveBaseDepense();
                break;

        }
    }

    // depense sans formulaire detype de categorie
    private void saveBaseDepense()
    {
        try
        {
            App.get().getDB().prismagestionco_ndf_depense_dao().insert(mViewModel.getDepense());
            App.get().getDB().prismagestionco_ndf_dao().updateSubmitStateById(mViewModel.getDepense().getIdSmartphoneNDF());
            mViewModel.getAction().success(App.get().getString(R.string.ENREGISTREMENT));
            finish();
            startActivity(new Intent(this, NdfActivity.class));
        }
        catch (Exception e)
        {
            NdfLogger.e(TAG, "saveBaseDepenseError: "+e.getMessage());
        }
    }

    private void saveRestaurationDepense()
    {
        if(mDepRestaurationInvite.getText().toString().trim().isEmpty())
        {
            mViewModel.getAction().message(getString(R.string.INPUT_CATEGORY_MISSING));
            return;
        }

        try
        {
            long depenseId = App.get().getDB().prismagestionco_ndf_depense_dao().insert(mViewModel.getDepense());
            App.get().getDB().prismagestionco_ndf_dao().updateSubmitStateById(mViewModel.getDepense().getIdSmartphoneNDF());

            PrismaGestionCo_NDF_depense_restauration rest = new PrismaGestionCo_NDF_depense_restauration();
            rest.setIdDepense((int)depenseId);
            rest.setInvites(mDepRestaurationInvite.getText().toString());
            rest.setIdSmartphone(UUID.randomUUID().toString());
            rest.setIdSmartphoneDepense(App.get().getDB().prismagestionco_ndf_depense_dao().getById((int)depenseId).getIdSmartphone());

            long id = App.get().getDB().prismagestionco_ndf_depense_restauration_dao().insert(rest);
            if(id != 0)
            {
                new RemoteDataStore().sync(PrismaGestionCo_NDF_depense.TABLENAME, rest.getIdDepense());
                mViewModel.getAction().success(App.get().getString(R.string.ENREGISTREMENT));
                finish();
                startActivity(new Intent(this, NdfActivity.class));
            }
        }
        catch (Exception e)
        {
            NdfLogger.e(TAG, "saveRestaurationDepenseError: "+e.getMessage());
        }
    }
    private void saveInterventionDepense()
    {
        if(depInterventionDuree.getText().toString().trim().isEmpty()
                || depInterventionPrix.getText().toString().trim().isEmpty())
        {
            mViewModel.getAction().message(getString(R.string.INPUT_CATEGORY_MISSING));
            return;
        }

        try
        {
            long depenseId = App.get().getDB().prismagestionco_ndf_depense_dao().insert(mViewModel.getDepense());
            App.get().getDB().prismagestionco_ndf_dao().updateSubmitStateById(mViewModel.getDepense().getIdSmartphoneNDF());

            PrismaGestionCo_NDF_depense_intervention inter = new PrismaGestionCo_NDF_depense_intervention();
            inter.setIdDepense((int)depenseId);
            inter.setDureeIntervention(Integer.valueOf(depInterventionDuree.getText().toString()));
            inter.setPrixHoraire(Float.valueOf(depInterventionPrix.getText().toString()));
            inter.setIdSmartphone(UUID.randomUUID().toString());
            inter.setIdSmartphoneDepense(App.get().getDB().prismagestionco_ndf_depense_dao().getById((int)depenseId).getIdSmartphone());

            long id = App.get().getDB().prismagestionco_ndf_depense_intervention_dao().insert(inter);
            if(id != 0)
            {
                new RemoteDataStore().sync(PrismaGestionCo_NDF_depense.TABLENAME, inter.getIdDepense());
                mViewModel.getAction().success(App.get().getString(R.string.ENREGISTREMENT));
                finish();
                startActivity(new Intent(this, NdfActivity.class));
            }
        }
        catch (Exception e)
        {
            NdfLogger.e(TAG, "saveInterventionDepenseError: "+e.getMessage());
        }
    }
    private void saveCarburantDepense()
    {
        if(depCarburantVehicule.getSelectedItem().toString().trim().isEmpty()
                || depCarburantCompteur.getText().toString().trim().isEmpty()
                || depCarburantLitrage.getText().toString().trim().isEmpty())
        {
            mViewModel.getAction().message(getString(R.string.INPUT_CATEGORY_MISSING));
            return;
        }

        try
        {
            long depenseId = App.get().getDB().prismagestionco_ndf_depense_dao().insert(mViewModel.getDepense());
            App.get().getDB().prismagestionco_ndf_dao().updateSubmitStateById(mViewModel.getDepense().getIdSmartphoneNDF());

            PrismaGestionCo_NDF_depense_carburant carb = new PrismaGestionCo_NDF_depense_carburant();
            carb.setIdDepense((int)depenseId);
            carb.setIdNDFVehicule(App.get().getDB().prismagestionco_ndf_vehicule_dao().getIdByName(depCarburantVehicule.getSelectedItem().toString()));
            carb.setCompteur(Integer.valueOf(depCarburantCompteur.getText().toString()));
            carb.setLitrage(Float.valueOf(depCarburantLitrage.getText().toString()));
            carb.setIdSmartphone(UUID.randomUUID().toString());
            carb.setIdSmartphoneDepense(App.get().getDB().prismagestionco_ndf_depense_dao().getById((int)depenseId).getIdSmartphone());

            long id = App.get().getDB().prismagestionco_ndf_depense_carburant_dao().insert(carb);
            if(id != 0)
            {
                new RemoteDataStore().sync(PrismaGestionCo_NDF_depense.TABLENAME, carb.getIdDepense());
                mViewModel.getAction().success(App.get().getString(R.string.ENREGISTREMENT));
                finish();
                startActivity(new Intent(this, NdfActivity.class));
            }
        }
        catch (Exception e)
        {
            NdfLogger.e(TAG, "saveCarburantDepenseError: "+e.getMessage());
        }
    }
    private void saveKilometrageDepense()
    {
        if(depKilometrageVehicule.getSelectedItem().toString().trim().isEmpty()
                || depKilometrageDistance.getText().toString().trim().isEmpty())
        {
            mViewModel.getAction().message(getString(R.string.INPUT_CATEGORY_MISSING));
            return;
        }

        try
        {
            long depenseId = App.get().getDB().prismagestionco_ndf_depense_dao().insert(mViewModel.getDepense());
            App.get().getDB().prismagestionco_ndf_dao().updateSubmitStateById(mViewModel.getDepense().getIdSmartphoneNDF());

            PrismaGestionCo_NDF_depense_kilometrage km = new PrismaGestionCo_NDF_depense_kilometrage();
            km.setIdDepense((int)depenseId);
            km.setIdNDFVehicule(App.get().getDB().prismagestionco_ndf_vehicule_dao().getIdByName(depKilometrageVehicule.getSelectedItem().toString()));
            km.setDistance(Integer.valueOf(depKilometrageDistance.getText().toString()));
            km.setIdSmartphone(UUID.randomUUID().toString());
            km.setIdSmartphoneDepense(App.get().getDB().prismagestionco_ndf_depense_dao().getById((int)depenseId).getIdSmartphone());

            long id = App.get().getDB().prismagestionco_ndf_depense_kilometrage_dao().insert(km);
            if(id != 0)
            {
                new RemoteDataStore().sync(PrismaGestionCo_NDF_depense.TABLENAME, km.getIdDepense());
                mViewModel.getAction().success(App.get().getString(R.string.ENREGISTREMENT));
                finish();
                startActivity(new Intent(this, NdfActivity.class));
            }
        }
        catch (Exception e)
        {
            NdfLogger.e(TAG, "saveKilometrageDepenseError: "+e.getMessage());
        }
    }
    private void saveAvionDepense()
    {
        if(depAvionNumVol.getText().toString().trim().isEmpty()
                || depAvionDateDepart.getText().toString().trim().isEmpty()
                || depAvionDateArrivee.getText().toString().trim().isEmpty()
                || depAvionCompagnie.getText().toString().trim().isEmpty()
                || depAvionTrajet.getText().toString().trim().isEmpty())
        {
            mViewModel.getAction().message(getString(R.string.INPUT_CATEGORY_MISSING));
            return;
        }

        try
        {
            long depenseId = App.get().getDB().prismagestionco_ndf_depense_dao().insert(mViewModel.getDepense());
            App.get().getDB().prismagestionco_ndf_dao().updateSubmitStateById(mViewModel.getDepense().getIdSmartphoneNDF());

            PrismaGestionCo_NDF_depense_avion av = new PrismaGestionCo_NDF_depense_avion();
            av.setIdDepense((int)depenseId);
            av.setNumVol(depAvionNumVol.getText().toString());
            av.setDateDepart(Helper.formatStringToDate(depAvionDateDepart.getText().toString()));
            av.setDateArrivee(Helper.formatStringToDate(depAvionDateArrivee.getText().toString()));
            av.setCompagnie(depAvionCompagnie.getText().toString());
            av.setTrajet(depAvionTrajet.getText().toString());
            av.setIdSmartphone(UUID.randomUUID().toString());
            av.setIdSmartphoneDepense(App.get().getDB().prismagestionco_ndf_depense_dao().getById((int)depenseId).getIdSmartphone());

            long id = App.get().getDB().prismagestionco_ndf_depense_avion_dao().insert(av);
            if(id != 0)
            {
                new RemoteDataStore().sync(PrismaGestionCo_NDF_depense.TABLENAME, av.getIdDepense());
                mViewModel.getAction().success(App.get().getString(R.string.ENREGISTREMENT));
                finish();
                startActivity(new Intent(this, NdfActivity.class));
            }
        }
        catch (Exception e)
        {
            NdfLogger.e(TAG, "saveAvionDepenseError: "+e.getMessage());
        }
    }
    private void saveHotelDepense()
    {
        if(depHotelNbNuits.getText().toString().trim().isEmpty())
        {
            mViewModel.getAction().message(getString(R.string.INPUT_CATEGORY_MISSING));
            return;
        }

        try
        {
            long depenseId = App.get().getDB().prismagestionco_ndf_depense_dao().insert(mViewModel.getDepense());
            App.get().getDB().prismagestionco_ndf_dao().updateSubmitStateById(mViewModel.getDepense().getIdSmartphoneNDF());

            PrismaGestionCo_NDF_depense_hotel hotel = new PrismaGestionCo_NDF_depense_hotel();
            hotel.setIdDepense((int)depenseId);
            hotel.setNbNuits(Integer.valueOf(depHotelNbNuits.getText().toString()));
            hotel.setIdSmartphone(UUID.randomUUID().toString());
            hotel.setIdSmartphoneDepense(App.get().getDB().prismagestionco_ndf_depense_dao().getById((int)depenseId).getIdSmartphone());

            long id = App.get().getDB().prismagestionco_ndf_depense_hotel_dao().insert(hotel);
            if(id != 0)
            {
                new RemoteDataStore().sync(PrismaGestionCo_NDF_depense.TABLENAME, hotel.getIdDepense());
                mViewModel.getAction().success(App.get().getString(R.string.ENREGISTREMENT));
                finish();
                startActivity(new Intent(this, NdfActivity.class));
            }
        }
        catch (Exception e)
        {
            NdfLogger.e(TAG, "saveHotelDepenseError: "+e.getMessage());
        }
    }
    private void saveTrainDepense()
    {
        if(depTrainNumTrain.getText().toString().trim().isEmpty()
                || depTrainDateDepart.getText().toString().trim().isEmpty()
                || depTrainDateArrivee.getText().toString().trim().isEmpty()
                || depTrainCompagnie.getText().toString().trim().isEmpty()
                || depTrainTrajet.getText().toString().trim().isEmpty())
        {
            mViewModel.getAction().message(getString(R.string.INPUT_CATEGORY_MISSING));
            return;
        }

        try
        {
            long depenseId = App.get().getDB().prismagestionco_ndf_depense_dao().insert(mViewModel.getDepense());
            App.get().getDB().prismagestionco_ndf_dao().updateSubmitStateById(mViewModel.getDepense().getIdSmartphoneNDF());

            PrismaGestionCo_NDF_depense_train train = new PrismaGestionCo_NDF_depense_train();
            train.setIdDepense((int)depenseId);
            train.setNumTrain(depTrainNumTrain.getText().toString());
            train.setDateDepart(Helper.formatStringToDate(depTrainDateDepart.getText().toString()));
            train.setDateArrivee(Helper.formatStringToDate(depTrainDateArrivee.getText().toString()));
            train.setCompagnie(depTrainCompagnie.getText().toString());
            train.setTrajet(depTrainTrajet.getText().toString());
            train.setIdSmartphone(UUID.randomUUID().toString());
            train.setIdSmartphoneDepense(App.get().getDB().prismagestionco_ndf_depense_dao().getById((int)depenseId).getIdSmartphone());

            long id = App.get().getDB().prismagestionco_ndf_depense_train_dao().insert(train);
            if(id != 0)
            {
                new RemoteDataStore().sync(PrismaGestionCo_NDF_depense.TABLENAME, train.getIdDepense());
                mViewModel.getAction().success(App.get().getString(R.string.ENREGISTREMENT));
                finish();
                startActivity(new Intent(this, NdfActivity.class));
            }
        }
        catch (Exception e)
        {
            NdfLogger.e(TAG, "saveTrainDepenseError: "+e.getMessage());
        }
    }
    private void saveMultitauxDepense()
    {
        if(depMultitauxTTC.getText().toString().trim().isEmpty()
                || depMultitauxHT.getText().toString().trim().isEmpty()
                || depMultitauxTVA.getText().toString().trim().isEmpty()
                || depMultitauxTaxe.getText().toString().trim().isEmpty())
        {
            mViewModel.getAction().message(getString(R.string.INPUT_CATEGORY_MISSING));
            return;
        }

        try
        {
            long depenseId = App.get().getDB().prismagestionco_ndf_depense_dao().insert(mViewModel.getDepense());
            App.get().getDB().prismagestionco_ndf_dao().updateSubmitStateById(mViewModel.getDepense().getIdSmartphoneNDF());

            PrismaGestionCo_NDF_depense_multitaux multitaux = new PrismaGestionCo_NDF_depense_multitaux();
            multitaux.setIdDepense((int)depenseId);
            multitaux.setMontantTTC(Float.valueOf(depMultitauxTTC.getText().toString()));
            multitaux.setMontantHT(Float.valueOf(depMultitauxHT.getText().toString()));
            multitaux.setMontantTVA(Float.valueOf(depMultitauxTVA.getText().toString()));
            multitaux.setMontantTaxe(Float.valueOf(depMultitauxTaxe.getText().toString()));
            multitaux.setIdSmartphone(UUID.randomUUID().toString());
            multitaux.setIdSmartphoneDepense(App.get().getDB().prismagestionco_ndf_depense_dao().getById((int)depenseId).getIdSmartphone());

            long id = App.get().getDB().prismagestionco_ndf_depense_multitaux_dao().insert(multitaux);
            if(id != 0)
            {
                new RemoteDataStore().sync(PrismaGestionCo_NDF_depense.TABLENAME, multitaux.getIdDepense());
                mViewModel.getAction().success(App.get().getString(R.string.ENREGISTREMENT));
                finish();
                startActivity(new Intent(this, NdfActivity.class));
            }
        }
        catch (Exception e)
        {
            NdfLogger.e(TAG, "saveMultitauxDepenseError: "+e.getMessage());
        }
    }
    private void saveLocationDepense()
    {
        if(depLocationMarque.getText().toString().trim().isEmpty()
                || depLocationModele.getText().toString().trim().isEmpty()
                || depLocationLoueur.getText().toString().trim().isEmpty()
                || depLocationDateDebut.getText().toString().trim().isEmpty()
                || depLocationDateFin.getText().toString().trim().isEmpty())
        {
            mViewModel.getAction().message(getString(R.string.INPUT_CATEGORY_MISSING));
            return;
        }

        try
        {
            long depenseId = App.get().getDB().prismagestionco_ndf_depense_dao().insert(mViewModel.getDepense());
            App.get().getDB().prismagestionco_ndf_dao().updateSubmitStateById(mViewModel.getDepense().getIdSmartphoneNDF());

            PrismaGestionCo_NDF_depense_locationVehicule loc = new PrismaGestionCo_NDF_depense_locationVehicule();
            loc.setIdDepense((int)depenseId);
            loc.setMarqueVehicule(depLocationMarque.getText().toString());
            loc.setModeleVehicule(depLocationModele.getText().toString());
            loc.setLoueur(depLocationLoueur.getText().toString());
            loc.setDateDebut(Helper.formatStringToDate(depLocationDateDebut.getText().toString()));
            loc.setDateFin(Helper.formatStringToDate(depLocationDateFin.getText().toString()));
            loc.setIdSmartphone(UUID.randomUUID().toString());
            loc.setIdSmartphoneDepense(App.get().getDB().prismagestionco_ndf_depense_dao().getById((int)depenseId).getIdSmartphone());

            long id = App.get().getDB().prismagestionco_ndf_depense_locationvehicule_dao().insert(loc);
            if(id != 0)
            {
                new RemoteDataStore().sync(PrismaGestionCo_NDF_depense.TABLENAME,loc.getIdDepense());
                mViewModel.getAction().success(App.get().getString(R.string.ENREGISTREMENT));
                finish();
                startActivity(new Intent(this, NdfActivity.class));
            }
        }
        catch (Exception e)
        {
            NdfLogger.e(TAG, "saveLocationDepenseError: "+e.getMessage());
        }
    }
    private void updateDateField(EditText editText)
    {
        DatePickerDialog.OnDateSetListener listener = (view, year, month, dayOfMonth) ->
        {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            editText.setText(Helper.formatDate(calendar.getTime()));
        };

        new DatePickerDialog(this, listener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    /* intent manger*/
    public static Intent launchDetail(Context context, GenericEntity entity)
    {
        Intent intent = new Intent(context, DepenseDetailsActivity.class);
        intent.putExtra(DepenseDetailsActivity.EXTRA_DEPENSE, entity);
        return intent;
    }


    /* this callback */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        // treatment of the image capture
        if(resultCode == RESULT_OK)
        {
            /* get picture from source start cropping */
            if(requestCode == DepenseDetailsActivity.ADD_PICTURE_CODE)
            {
                String newUriString = null;
                if(data != null)
                {
                    newUriString = data.getData().toString() != null ? data.getData().toString() : "";
                }

                if(mViewModel.getDepense().getImage1() == null)
                {
                    depImagesString.add(newUriString);
                    mViewModel.getDepense().setImage1(newUriString);
                    success(getString(R.string.PHOTO_AJOUTEE));

                } else if (mViewModel.getDepense().getImage2() == null)
                {
                    depImagesString.add(newUriString);
                    mViewModel.getDepense().setImage2(newUriString);
                    success(getString(R.string.PHOTO_AJOUTEE));


                } else if(mViewModel.getDepense().getImage3() == null)
                {
                    depImagesString.add(newUriString);
                    mViewModel.getDepense().setImage3(newUriString);
                    success(getString(R.string.PHOTO_AJOUTEE));

                } else
                    {
                        error(getString(R.string.ALERT_MAX_PICTURE));
                    }
            }
        }
    }
}
