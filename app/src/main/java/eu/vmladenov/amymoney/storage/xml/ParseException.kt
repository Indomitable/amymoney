package eu.vmladenov.amymoney.storage.xml

class ParseException(val tagName: XmlTags, message: String) : Exception(message)