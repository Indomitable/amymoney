package eu.vmladenov.amymoney.models

import eu.vmladenov.amymoney.storage.xml.XmlTags
import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class XmlTag(val value: XmlTags)

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class XmlCollection(val childClass: KClass<*>)


@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class XmlAttribute(val value: String)