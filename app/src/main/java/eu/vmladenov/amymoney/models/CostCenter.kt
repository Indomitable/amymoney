package eu.vmladenov.amymoney.models

data class CostCenters(val costCenters: List<CostCenter>): ArrayList<CostCenter>(costCenters)

data class CostCenter(
    @XmlAttribute("id") val id: String,
    @XmlAttribute("name") val name: String
)
