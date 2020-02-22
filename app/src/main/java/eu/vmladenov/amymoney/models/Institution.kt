package eu.vmladenov.amymoney.models

data class Institutions(val institutions: List<Institution>): ArrayList<Institution>(institutions)

data class Institution(
    val id: String = "",
    val name: String = "",
    val sortCode: String = "",
    val manager: String = "",
    val address: Address = Address(),
    val accountIds: List<String>,
    val extra: List<Pair<String, String>>
)