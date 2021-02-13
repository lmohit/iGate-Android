package com.application.igate

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MenuItem
import android.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.application.igate.event.AddVisitor
import com.application.igate.event.RxBus
import com.application.igate.visitor.AddVisitorFragment
import com.application.igate.visitorDetails.VisitorDetailsFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class SplashActivity : AppCompatActivity() {

    private var disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initDrawerLayout()
        initDrawerListener()
        observeChanges()
        addFragmentToBackStack(AddVisitorFragment.newInstance())
    }

    private fun initDrawerListener() {
        navigationView.setNavigationItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.add_visitor -> addFragmentToBackStack(AddVisitorFragment.newInstance())
                R.id.visitor_details -> addFragmentToBackStack(VisitorDetailsFragment.newInstance())
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }

    private fun initDrawerLayout() {
        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.drawer_open,
            R.string.drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun observeChanges() {
        Log.d(TAG, "observeChanges")
        disposable.add(RxBus.getObservable()
            .subscribeOn(AndroidSchedulers.mainThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                when (it) {
                    is AddVisitor -> {
                        addFragmentToBackStack(AddVisitorFragment.newInstance())
                    }
                }
            })
    }

    private fun addFragmentToBackStack(fragment: Fragment) {
        Log.d(TAG, "addFragmentToBackStack")
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment)
            .addToBackStack("")
            .commitAllowingStateLoss()
    }

    companion object {
        private val TAG = SplashActivity::class.java.simpleName
    }
}