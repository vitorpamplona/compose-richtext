package com.halilibo.richtext.commonmark

/**
 * Allows configuration of the Markdown parser.
 *
 * The concrete parser extensions backing these options are platform-specific and are
 * therefore not exposed here. Obtain an instance through one of the presets on the
 * companion object ([Default], [MarkdownWithLinks], or [MarkdownOnly]).
 */
public expect class CommonMarkdownParseOptions {

  public companion object {
    /**
     * Parses Markdown with tables and strikethrough, and additionally autolinks plain text
     * links as well as platform specific URIs (e.g. nostr) and hashtags.
     */
    public val MarkdownWithLinks: CommonMarkdownParseOptions

    /**
     * Parses Markdown with tables and strikethrough, without any autolinking.
     */
    public val MarkdownOnly: CommonMarkdownParseOptions

    /**
     * The default parse options. Equivalent to [MarkdownWithLinks].
     */
    public val Default: CommonMarkdownParseOptions
  }
}
