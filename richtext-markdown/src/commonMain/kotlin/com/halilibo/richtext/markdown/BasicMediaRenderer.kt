package com.halilibo.richtext.markdown

import androidx.compose.foundation.layout.fillMaxWidth
import com.halilibo.richtext.ui.MediaRenderer
import com.halilibo.richtext.ui.string.UriComposableRenderer
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale

public object DefaultMediaRenderer: BasicMediaRenderer() {

}

public open class BasicMediaRenderer: MediaRenderer {
  override fun renderImage(title: String?, uri: String, helper: UriComposableRenderer) {
    helper.renderInline {
      RemoteImage(
        url = uri,
        contentDescription = title,
        modifier = Modifier.fillMaxWidth(),
        contentScale = ContentScale.Inside
      )
    }
  }

  override fun renderLinkPreview(title: String?, uri: String, helper: UriComposableRenderer) {
    helper.renderAsCompleteLink(uri, uri)
  }

  override fun renderNostrUri(uri: String, helper: UriComposableRenderer) {
    helper.renderAsCompleteLink(uri, uri)
  }

  override fun renderHashtag(tag: String, helper: UriComposableRenderer) {
    helper.renderAsCompleteLink(tag, "hashtag:${tag}")
  }

  override fun shouldRenderLinkPreview(uri: String): Boolean { return false }
}