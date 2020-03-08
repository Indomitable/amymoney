package eu.vmladenov.amymoney.models

class CostCenters : BaseList<CostCenter>()

data class CostCenter(
    @XmlAttribute("id") override val id: String,
    @XmlAttribute("name") val name: String
): IModel
