package eu.vmladenov.amymoney.models

data class KMyMoneyState(
    val fileInfo: FileInfo,
    val user: User,
    val institutions: Institutions
)
