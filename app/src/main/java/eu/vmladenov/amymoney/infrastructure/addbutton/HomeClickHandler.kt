package eu.vmladenov.amymoney.infrastructure.addbutton

import android.app.Activity
import android.content.Intent
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.PopupWindow
import androidx.core.view.marginEnd
import eu.vmladenov.amymoney.R
import eu.vmladenov.amymoney.infrastructure.IAMyMoneyRepository

class HomeClickHandler {

    fun handle(repository: IAMyMoneyRepository, activity: Activity) {
        if (repository.currentAccounts.values.isEmpty()) {
            val intent = Intent().setType("*/*").setAction(Intent.ACTION_OPEN_DOCUMENT)
            activity.startActivityForResult(Intent.createChooser(intent, "Select file"), FILE_SELECT_REQUEST)
        }
    }

    companion object {
        const val FILE_SELECT_REQUEST = 1001
    }
}
