package eu.vmladenov.amymoney.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import eu.vmladenov.amymoney.AMyMoneyApplication

class LaunchActivity: Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val state = (applicationContext as AMyMoneyApplication).state
        if (!state.isInitialized) {
            startActivity(EmptyAppStateActivity::class.java)
        } else {
            startActivity(MainActivity::class.java)
        }
    }

    private fun startActivity(cls: Class<*>) {
        val intent = Intent(this, cls)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }
}
