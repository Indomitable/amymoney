package eu.vmladenov.amymoney.models

class CostCenters : BaseList<CostCenter>()

data class CostCenter(
    @XmlAttribute("id") val id: String,
    @XmlAttribute("name") val name: String
)
