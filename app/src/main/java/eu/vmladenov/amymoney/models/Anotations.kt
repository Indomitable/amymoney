package eu.vmladenov.amymoney.models

@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class XmlAttribute(val value: String)