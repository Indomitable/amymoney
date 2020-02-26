package eu.vmladenov.amymoney.android_test.storage.xml

import eu.vmladenov.amymoney.storage.xml.XmlTagsHandler
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class XmlTagsHandlerTests: BaseXmlHandlerTest() {
    private lateinit var service: XmlTagsHandler

    @Before
    fun setup() {
        service = XmlTagsHandler()
    }

    @Test
    fun shouldBeAbleToDeserialize() {
        val parser = createParser(
            """
 <TAGS count="1">
  <TAG id="G000001" name="Test" closed="1" tagcolor="#FFAABB"/>
 </TAGS>
            """
        )
        val tags = service.read(parser)
        Assert.assertEquals(1, tags.size)
        val tag = tags[0]
        Assert.assertEquals("G000001", tag.id)
        Assert.assertEquals("Test", tag.name)
        Assert.assertTrue(tag.closed)
        Assert.assertEquals("#FFAABB", tag.tagColor)
    }

    @Test
    fun shouldBeAbleToDeserializeMultiple() {
        val parser = createParser(
            """
 <TAGS count="2">
  <TAG id="G000001" name="Test" closed="1" tagcolor="#FFAABB"/>
  <TAG closed="0" name="Cash" id="G000002" tagcolor="#aa55ff" notes="Some tag notes&#xa;More notes"/>
 </TAGS>
            """
        )
        val tags = service.read(parser)
        Assert.assertEquals(2, tags.size)
        val tag0 = tags[0]
        Assert.assertEquals("G000001", tag0.id)
        Assert.assertEquals("Test", tag0.name)
        Assert.assertTrue(tag0.closed)
        Assert.assertEquals("#FFAABB", tag0.tagColor)

        val tag1 = tags[1]
        Assert.assertEquals("G000002", tag1.id)
        Assert.assertEquals("Cash", tag1.name)
        Assert.assertFalse(tag1.closed)
        Assert.assertEquals("#aa55ff", tag1.tagColor)
        Assert.assertEquals("Some tag notes\nMore notes", tag1.notes)
    }

    @Test
    fun shouldBeAbleToDeserializeWhenNone()
    {
        val parser = createParser(
            """
 <TAGS count="0" />
            """
        )
        val tags = service.read(parser)
        Assert.assertEquals(0, tags.size)
    }
}
