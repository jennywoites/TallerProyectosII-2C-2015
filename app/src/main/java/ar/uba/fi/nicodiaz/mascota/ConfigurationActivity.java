package ar.uba.fi.nicodiaz.mascota;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;

import ar.uba.fi.nicodiaz.mascota.model.service.api.DatabasePetState;
import ar.uba.fi.nicodiaz.mascota.model.service.impl.PetServiceMock;
import ar.uba.fi.nicodiaz.mascota.utils.Configuration;
import ar.uba.fi.nicodiaz.mascota.utils.service.PetServiceFactory;

public class ConfigurationActivity extends AppCompatActivity {

    private SwitchCompat emptyAdoptionSwitch;
    private SwitchCompat onlyDogsSwitch;
    private SwitchCompat onlyCatsSwitch;
    private SwitchCompat devModeSwitch;
    private SwitchCompat emptyMissingSwitch;
    private SwitchCompat onlyDogsMissingSwitch;
    private SwitchCompat onlyCatsMissingSwitch;

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Perderá los cambios si vuelve atrás.")
                .setCancelable(false)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        ConfigurationActivity.this.finish();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.configuracion));

        devModeSwitch = (SwitchCompat) findViewById(R.id.switchDevelopment);
        devModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                devModeSwitchChange(isChecked);
            }
        });

        emptyAdoptionSwitch = (SwitchCompat) findViewById(R.id.switchEmptyAdoption);
        emptyAdoptionSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                emptyAdoptionSwitch(isChecked);
            }
        });

        onlyDogsSwitch = (SwitchCompat) findViewById(R.id.switchOnlyDog);
        onlyDogsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onlyDogsSwitch(isChecked);
            }
        });

        onlyCatsSwitch = (SwitchCompat) findViewById(R.id.switchOnlyCats);
        onlyCatsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onlyCatsSwitch(isChecked);
            }
        });

        emptyMissingSwitch = (SwitchCompat) findViewById(R.id.switchEmptyMissing);
        emptyMissingSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                emptyMissingSwitch(isChecked);
            }
        });

        onlyDogsMissingSwitch = (SwitchCompat) findViewById(R.id.switchOnlyDogMissing);
        onlyDogsMissingSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onlyDogsMissingSwitch(isChecked);
            }
        });

        onlyCatsMissingSwitch = (SwitchCompat) findViewById(R.id.switchOnlyCatsMissing);
        onlyCatsMissingSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onlyCatsMissingSwitch(isChecked);
            }
        });

        Button saveButton = (Button) findViewById(R.id.btn_save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });

        init();

    }

    private void init() {
        if (Configuration.isDevelopmentMode()) {
            devModeSwitch.setChecked(Boolean.TRUE);
            developmentModeEnable();

            PetServiceMock instance = (PetServiceMock) PetServiceFactory.getInstance();
            DatabasePetState databaseAdoptionPetState = instance.getDatabaseAdoptionPetState();
            switch (databaseAdoptionPetState) {

                case EMPTY:
                    emptyAdoptionSwitch.setChecked(Boolean.TRUE);
                    break;
                case DOGS:
                    onlyDogsSwitch.setChecked(Boolean.TRUE);
                    break;
                case CATS:
                    onlyCatsSwitch.setChecked(Boolean.TRUE);
                    break;
                case ALL:
                    break;
            }

            DatabasePetState databaseMissingPetState = instance.getDatabaseMissingPetState();
            switch (databaseMissingPetState) {

                case EMPTY:
                    emptyMissingSwitch.setChecked(Boolean.TRUE);
                    break;
                case DOGS:
                    onlyDogsMissingSwitch.setChecked(Boolean.TRUE);
                    break;
                case CATS:
                    onlyCatsMissingSwitch.setChecked(Boolean.TRUE);
                    break;
                case ALL:
                    break;
            }

        }
    }

    private void save() {

        if (devModeSwitch.isChecked()) {
            Configuration.developmentModeEnable();
            PetServiceMock instance = (PetServiceMock) PetServiceFactory.getInstance();
            if (emptyAdoptionSwitch.isChecked()) {
                instance.loadAdoptionPets(DatabasePetState.EMPTY);
            } else if (onlyDogsSwitch.isChecked()) {
                instance.loadAdoptionPets(DatabasePetState.DOGS);
            } else if (onlyCatsSwitch.isChecked()) {
                instance.loadAdoptionPets(DatabasePetState.CATS);
            } else {
                instance.loadAdoptionPets(DatabasePetState.ALL);
            }

            if (emptyMissingSwitch.isChecked()) {
                instance.loadMissingPets(DatabasePetState.EMPTY);
            } else if (onlyDogsMissingSwitch.isChecked()) {
                instance.loadMissingPets(DatabasePetState.DOGS);
            } else if (onlyCatsMissingSwitch.isChecked()) {
                instance.loadMissingPets(DatabasePetState.CATS);
            } else {
                instance.loadMissingPets(DatabasePetState.ALL);
            }

        } else {
            Configuration.developmentModeDisable();
        }

        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    private void devModeSwitchChange(boolean isChecked) {
        if (isChecked) {
            developmentModeEnable();
        } else {
            developmentModeDisable();
        }
    }

    private void emptyAdoptionSwitch(boolean isChecked) {
        if (isChecked) {
            onlyDogsSwitch.setChecked(false);
            onlyCatsSwitch.setChecked(false);
        }
    }

    private void onlyDogsSwitch(boolean isChecked) {
        if (isChecked) {
            emptyAdoptionSwitch.setChecked(false);
            onlyCatsSwitch.setChecked(false);
        }
    }

    private void onlyCatsSwitch(boolean isChecked) {
        if (isChecked) {
            emptyAdoptionSwitch.setChecked(false);
            onlyDogsSwitch.setChecked(false);
        }
    }


    private void emptyMissingSwitch(boolean isChecked) {
        if (isChecked) {
            onlyDogsMissingSwitch.setChecked(false);
            onlyCatsMissingSwitch.setChecked(false);
        }
    }

    private void onlyDogsMissingSwitch(boolean isChecked) {
        if (isChecked) {
            emptyMissingSwitch.setChecked(false);
            onlyCatsMissingSwitch.setChecked(false);
        }
    }

    private void onlyCatsMissingSwitch(boolean isChecked) {
        if (isChecked) {
            emptyMissingSwitch.setChecked(false);
            onlyDogsMissingSwitch.setChecked(false);
        }
    }


    private void developmentModeEnable() {
        emptyAdoptionSwitch.setEnabled(Boolean.TRUE);
        onlyDogsSwitch.setEnabled(Boolean.TRUE);
        onlyCatsSwitch.setEnabled(Boolean.TRUE);
        emptyMissingSwitch.setEnabled(Boolean.TRUE);
        onlyDogsMissingSwitch.setEnabled(Boolean.TRUE);
        onlyCatsMissingSwitch.setEnabled(Boolean.TRUE);
    }

    private void developmentModeDisable() {
        emptyAdoptionSwitch.setEnabled(Boolean.FALSE);
        onlyDogsSwitch.setEnabled(Boolean.FALSE);
        onlyCatsSwitch.setEnabled(Boolean.FALSE);
        emptyMissingSwitch.setEnabled(Boolean.FALSE);
        onlyDogsMissingSwitch.setEnabled(Boolean.FALSE);
        onlyCatsMissingSwitch.setEnabled(Boolean.FALSE);

    }


}