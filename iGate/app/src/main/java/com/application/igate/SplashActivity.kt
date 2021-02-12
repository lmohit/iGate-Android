package com.application.igate

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.application.igate.event.AddVisitor
import com.application.igate.event.RxBus
import com.application.igate.visitor.AddVisitorFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class SplashActivity : AppCompatActivity() {

    private var disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        observeChanges()
        addFragmentToBackStack(AddVisitorFragment.newInstance())
    }

    private fun observeChanges() {
        Log.d(TAG, "observeChanges")
        disposable.add(RxBus.getObservable()
            .subscribeOn(Schedulers.io())
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