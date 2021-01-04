package com.example.notedefrais.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import com.example.notedefrais.App;
import com.example.notedefrais.R;
import com.example.notedefrais.model.database.tables.PrismaGestionCo_NDF;
import com.example.notedefrais.model.utils.Helper;
import com.example.notedefrais.model.utils.ImageSourceManager;
import com.example.notedefrais.model.utils.IonBackPressed;
import com.example.notedefrais.model.utils.NdfLogger;
import com.example.notedefrais.view.about.AboutFragment;
import com.example.notedefrais.view.approval.ApprovalFragment;
import com.example.notedefrais.view.depense.DepenseFragment;
import com.example.notedefrais.view.ndf.NdfFragment;
import com.example.notedefrais.view.project.ProjectFragment;
import com.example.notedefrais.view.selection.ItemSelectionFragment;
import com.example.notedefrais.view.ndf.NdfDetailsActivity;
import com.example.notedefrais.view.selection.DepenseCategorySelectionActivity;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import com.google.android.material.navigation.NavigationView;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;
import io.reactivex.disposables.CompositeDisposable;

public class NdfActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ItemSelectionFragment.SelectOptionListener {

    public static final String TAG = NdfActivity.class.getSimpleName();
    /* request code used inside all app */
    public final static int NEW_NDF_REQUEST_CODE = 1;
    public final static int CAMERA_REQUEST_PERMISSION_CODE = 500;
    public final static int STORAGE_REQUEST_PERMISSION_CODE = 501;
    public final static int IMAGE_PICK_GALLERY_CODE = 502;
    public final static int IMAGE_PICK_CAMERA_CODE = 503;
    public final static int CALL_REQUEST_PERMISSION_CODE = 504;

    private ImageSourceManager mManager;
    private boolean exit = false;
    private CompositeDisposable disposable;
    private NavigationView navigationView;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ndf);
        try
        {
            mManager = new ImageSourceManager(this, this);

            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            navigationView = findViewById(R.id.nav_view);
            Toolbar toolbar = findViewById(R.id.toolbar);

            navigationView.setNavigationItemSelectedListener(this);
            setSupportActionBar(toolbar);
            setHeadProfil(navigationView, App.getUserName());

            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            Helper.replaceMainFragment(getSupportFragmentManager(), new NdfFragment(), NdfFragment.TAG);
        }
        catch (Exception e)
        {
            NdfLogger.e(TAG, e.getMessage());
        }
    }
    private void setHeadProfil(NavigationView nav, String name) {
        View headerLayout = nav.getHeaderView(0);
        TextView userName = headerLayout.findViewById(R.id.userName);
        userName.setText(name);
    }


    /* ItemSelectionFragment SelectOptionListener callback */
    @Override
    public void onNewNdfSelection()
    {
        Intent i = new Intent(this, NdfDetailsActivity.class);
        i.putExtra(NdfDetailsActivity.EXTRA_NDF, new PrismaGestionCo_NDF());
        startActivityForResult(i, NEW_NDF_REQUEST_CODE);
    }


    @Override
    public void onNewDepenseSelection(int idNdf)
    {
        mManager.getImageFromSource(idNdf);
    }


    /* this callback */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        // treatment of the image capture
        if(resultCode == RESULT_OK)
        {
            /* get picture from source start cropping */
            if(requestCode == IMAGE_PICK_CAMERA_CODE)
            {
                CropImage.activity(mManager.getImageUri())
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(this);
            }
            if(requestCode == IMAGE_PICK_GALLERY_CODE)
            {
                CropImage.activity(data.getData())
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(this);
            }
        }

        /* treatment of the cropped image */
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE )
        {
            if(resultCode == RESULT_OK)
            {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);

                if(result != null)
                {
                    Uri resultUri = result.getUri();

                    if(resultUri != null)
                    {
                        Intent i = new Intent(this, DepenseCategorySelectionActivity.class);
                        i.setData(resultUri);
                        startActivity(i);
                    }
                }
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        switch (requestCode)
        {
            case CAMERA_REQUEST_PERMISSION_CODE:
            {
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    try
                    {
                        mManager.onCameraPicking();
                    }
                    catch (Exception e)
                    {
                        NdfLogger.e(TAG, e.getMessage());
                    }
                } else
                    {
                        Toasty.warning(this, getString(R.string.ALERT_AUTORISATION_REQUIS), Toast.LENGTH_LONG).show();
                    }
                break;
            }

            case STORAGE_REQUEST_PERMISSION_CODE:
            {
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    mManager.onGalleryImageSelecting();
                } else
                {
                    Toasty.warning(this, getString(R.string.ALERT_AUTORISATION_REQUIS), Toast.LENGTH_LONG).show();
                }
                break;
            }

            case CALL_REQUEST_PERMISSION_CODE:
            {
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    AboutFragment.launchCall(this, App.get().getDB().prismagestionco_ndf_smartphone_parameter_dao().get().getPrismaPhoneNumer());

                } else
                {
                    Toasty.warning(this, getString(R.string.ALERT_AUTORISATION_REQUIS), Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        int id = item.getItemId();
        Fragment fragment = null;
        String name = null;

        switch (id) {
            case R.id.menu_project:
                fragment = new ProjectFragment();
                name = ProjectFragment.TAG;
                break;
            case R.id.menu_expense_report:
                fragment = new NdfFragment();
                name = NdfFragment.TAG;
                break;
            case R.id.menu_approval:
                fragment = new ApprovalFragment();
                name = ApprovalFragment.TAG;
                break;
            case R.id.menu_about:
                fragment = new AboutFragment();
                name = ApprovalFragment.TAG;
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        if (fragment != null) {
            Helper.replaceMainFragment(getSupportFragmentManager(), fragment, name);
        }

        return true;
    }


    /* Menu and navigation method
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    */

    @SuppressLint("NewApi")
    @Override
    public void onBackPressed()
    {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.host_fragment);

        if (!(fragment instanceof IonBackPressed) || !((IonBackPressed) fragment).onBackPressed())
        {
            if (exit)
            {
                finishAffinity();
            } else
                {
                    Toasty.info(this, getString(R.string.APPUYER_DE_NOUVEAU_POUR_QUITTER), Toast.LENGTH_LONG).show();
                    exit = true;
                    new Handler().postDelayed(() -> exit = false, 3000);
                }
        } else if (fragment.getClass().getSimpleName().equals(ItemSelectionFragment.TAG))
        {
            Helper.replaceMainFragment(getSupportFragmentManager(), new NdfFragment(), NdfFragment.TAG);

        } else if (fragment.getClass().getSimpleName().equals(DepenseFragment.TAG))
        {
            Helper.replaceMainFragment(getSupportFragmentManager(), new ProjectFragment(), NdfFragment.TAG);
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if (disposable != null && !disposable.isDisposed())
        {
            disposable.dispose();
        }
    }

    /** intent manager*/
    public static Intent launchDetail(Context context)
    {
        return new Intent(context, NdfActivity.class);
    }

}
