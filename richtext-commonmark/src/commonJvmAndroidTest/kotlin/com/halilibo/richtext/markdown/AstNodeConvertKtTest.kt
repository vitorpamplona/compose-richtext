package com.halilibo.richtext.markdown

import com.halilibo.richtext.markdown.node.AstImage
import com.halilibo.richtext.markdown.node.AstLink
import com.halilibo.richtext.markdown.node.AstNode
import com.halilibo.richtext.markdown.node.AstNodeLinks
import org.commonmark.node.Image
import org.commonmark.node.Link
import org.junit.Test
import kotlin.test.assertEquals

internal class AstNodeConvertKtTest {

  @Test
  fun `when image without title is converted, then the content description is empty`() {
    val destination = "/url"
    val image = Image(destination, null)

    val result = convert(image, { false })

    assertEquals(
      expected = AstNode(
        type = AstImage(title = "", destination = destination),
        links = AstNodeLinks()
      ),
      actual = result
    )
  }

  @Test
  fun `when link without title is converted, then the content description is empty`() {
    val destination = "/url"
    val link = Link(destination, null)

    val resultLink = convert(link, { false })

    assertEquals(
      expected = AstNode(
        type = AstLink(title = "", destination = destination),
        links = AstNodeLinks()
      ),
      actual = resultLink
    )

    val resultImage = convert(link, { true })

    assertEquals(
      expected = AstNode(
        type = AstImage(title = "", destination = destination),
        links = AstNodeLinks()
      ),
      actual = resultImage
    )
  }
}
