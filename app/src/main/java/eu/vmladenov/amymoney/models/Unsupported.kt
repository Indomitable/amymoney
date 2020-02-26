package eu.vmladenov.amymoney.models

data class UnsupportedTag (
    val tagName: String,
    val attributes: Map<String, String>,
    val children: List<UnsupportedTag>
)

