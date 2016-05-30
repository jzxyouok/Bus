package com.scrat.app.bus.module.search;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.scrat.app.bus.R;
import com.scrat.app.bus.common.BaseActivity;
import com.scrat.app.core.utils.ActivityUtils;

/**
 * Created by yixuanxuan on 16/5/20.
 */
public class SearchActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    private SearchFragment mFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        if (drawer != null) {
            drawer.addDrawerListener(toggle);
            drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
                @Override
                public void onDrawerSlide(View drawerView, float slideOffset) {
                }

                @Override
                public void onDrawerOpened(View drawerView) {
                    ActivityUtils.hideKeyboard(SearchActivity.this);
                }

                @Override
                public void onDrawerClosed(View drawerView) {
                }

                @Override
                public void onDrawerStateChanged(int newState) {
                }
            });
        }

        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
        }

        if (mFragment == null) {
            mFragment = SearchFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), mFragment, R.id.contentFrame);
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_bus) {

        } else if (id == R.id.nav_send) {
            sendFeedback();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null) {
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null && drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void sendFeedback() {
        Intent data = new Intent(Intent.ACTION_SENDTO);
        data.setData(Uri.parse("mailto:huzhenjie.dev@gmail.com"));
        data.putExtra(Intent.EXTRA_SUBJECT, "[广州公交][" + System.currentTimeMillis() + "]");
        data.putExtra(Intent.EXTRA_TEXT, "请输入您的反馈内容");
        startActivity(data);
    }

}
