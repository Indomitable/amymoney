package eu.vmladenov.amymoney.storage.xml

class XmlParseException(val tagName: XmlTags, message: String) : Exception(message)
