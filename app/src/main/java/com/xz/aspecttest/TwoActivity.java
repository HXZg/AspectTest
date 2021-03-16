package com.xz.aspecttest;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.xz.aspecttest.navigator.KeepStateNavigator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 * @author xian_zhong  admin
 * @version 1.0
 * @title com.xz.aspecttest  AspectTest
 * @Des TwoActivity
 * @DATE 2020/7/30  14:12 星期四
 */
public class TwoActivity extends AppCompatActivity {

    private NavController navController;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HashMap mp;
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        Fragment navHostFragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController.getNavigatorProvider().addNavigator(new KeepStateNavigator(this,navHostFragment.getChildFragmentManager(),R.id.nav_host_fragment));
        navController.setGraph(R.navigation.nav_graph);

        Log.i("zzzzzzzz","" + navHostFragment.toString() + "   " + navController);

        BottomNavigationView bottomView = findViewById(R.id.bottom_view);
        NavigationUI.setupWithNavController(bottomView,navController);

        /*bottomView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Bundle bundle = new Bundle();
                bundle.putString("name","bundle");
                if (menuItem.getItemId() == R.id.BFragment) {
//                    navController.navigate(Uri.parse("http://www.test.com/MAX"));
                    navController.navigate(R.id.action_to_cfragment,bundle);
                }
                return true;
            }
        });*/
    }
}
