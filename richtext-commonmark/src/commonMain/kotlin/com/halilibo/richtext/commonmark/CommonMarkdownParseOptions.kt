package com.halilibo.richtext.commonmark

import org.commonmark.Extension
import org.commonmark.ext.autolink.AutolinkExtension
import org.commonmark.ext.gfm.strikethrough.StrikethroughExtension
import org.commonmark.ext.gfm.tables.TablesExtension

/**
 * Allows configuration of the Markdown parser
 *
 * @param autolink Detect plain text links and turn them into Markdown links.
 */
public class CommonMarkdownParseOptions(
  public val extensions: List<Extension>
) {

  override fun toString(): String {
    return "CommonMarkdownParseOptions(extensions=$extensions)"
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is CommonMarkdownParseOptions) return false

    return extensions == other.extensions
  }

  override fun hashCode(): Int {
    return extensions.hashCode()
  }

  public fun copy(
    extensions: List<Extension> = this.extensions
  ): CommonMarkdownParseOptions = CommonMarkdownParseOptions(
    extensions = extensions
  )

  public companion object {
    public val MarkdownWithLinks: CommonMarkdownParseOptions = CommonMarkdownParseOptions(
      listOfNotNull(
        TablesExtension.create(),
        StrikethroughExtension.create(),
        AutolinkExtension.create(),
        NostrUriExtension.create(),
        HashtagExtension.create()
      )
    )

    public val MarkdownOnly: CommonMarkdownParseOptions = CommonMarkdownParseOptions(
      listOfNotNull(
        TablesExtension.create(),
        StrikethroughExtension.create()
      )
    )

    public val Default: CommonMarkdownParseOptions = MarkdownWithLinks
  }
}
