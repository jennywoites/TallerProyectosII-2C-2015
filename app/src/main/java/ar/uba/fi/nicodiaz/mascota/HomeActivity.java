package ar.uba.fi.nicodiaz.mascota;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import ar.uba.fi.nicodiaz.mascota.fragment.AdopcionFragment;
import ar.uba.fi.nicodiaz.mascota.model.User;
import ar.uba.fi.nicodiaz.mascota.model.UserService;
import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private String currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_home);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        //Fragment fragment = new HomeFragment();
        Fragment fragment = new AdopcionFragment();
        tx.replace(R.id.frame, fragment);
        tx.commit();
        //getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
        getSupportActionBar().setTitle(getResources().getString(R.string.title_activity_adopcion));

        // Fill user information in header.
        User user = UserService.getInstance().getUser();
        CircleImageView imageProfileView = (CircleImageView) findViewById(R.id.profile_image);
        if (user.isMale()) {
            imageProfileView.setImageResource(R.drawable.user_male);
        } else if (user.isFemale()) {
            imageProfileView.setImageResource(R.drawable.user_female);
        }

        TextView userName = (TextView) findViewById(R.id.username);
        userName.setText(user.getName());

        TextView email = (TextView) findViewById(R.id.email);
        if (user.getEmail() != null) {
            email.setText(user.getEmail());
        }

        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setCheckedItem(R.id.drawer_home);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                drawerLayout.closeDrawers();
                return selectFragment(menuItem);
            }
        });

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };


        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    private boolean selectFragment(MenuItem menuItem) {
        Fragment fragment = null;
        FragmentTransaction fragmentTransaction;

        switch (menuItem.getItemId()) {
            /*case R.id.drawer_home:
                // Toast.makeText(getApplicationContext(), "Home selected", Toast.LENGTH_SHORT).show();
                fragment = new HomeFragment();
                currentFragment = getResources().getString(R.string.app_name);
                break;*/
            case R.id.drawer_adoptions:
                // Toast.makeText(getApplicationContext(), "Adoptions selected", Toast.LENGTH_SHORT).show();
                fragment = new AdopcionFragment();
                currentFragment = getResources().getString(R.string.title_activity_adopcion);
                break;
            /*
            case R.id.drawer_profile:
                Toast.makeText(getApplicationContext(), "Profile selected", Toast.LENGTH_SHORT).show();
                fragment = new ProfileFragment();
                currentFragment = getResources().getString(R.string.title_activity_profile);
                break;
            case R.id.drawer_missing:
                Toast.makeText(getApplicationContext(), "Missing selected", Toast.LENGTH_SHORT).show();
                fragment = new MissingFragment();
                currentFragment = getResources().getString(R.string.title_activity_missing);
                break;
            case R.id.drawer_about:
                Toast.makeText(getApplicationContext(), "About selected", Toast.LENGTH_SHORT).show();
                fragment = new AboutFragment();
                currentFragment = getResources().getString(R.string.title_activity_about);
                break;
            */
            default:
                break;
        }

        if (fragment != null) {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame, fragment);
            fragmentTransaction.commit();
            getSupportActionBar().setTitle(currentFragment);
            return true;
        }
        return false;
    }
}
