package eu.vmladenov.amymoney.state

import eu.vmladenov.amymoney.models.KMyMoneyModel

class ApplicationState {
    var model: KMyMoneyModel? = null

    val isInitialized: Boolean
        get() {
            return model != null
        }
}

