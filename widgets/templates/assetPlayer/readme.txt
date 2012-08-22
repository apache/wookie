* Asset Player Template
The Asset Player Template allows new player widgets to be quickly and
easily built. It can, by default, handle images, video and
audio. However, at present there is no way for the template to
differentiate between these. The appropriate tags, if not <img..> must
be provide in the content_primary or content_secondary files when
building a widget from this template.

By defaul the template expects to find an <img...> tag with the ID
"asset". This can be changed to an audio or video tag in the widgets
that build on this template (see content_primary.html).

** Functions

  - play a collection of assets in a rolling "slideshow"
  - move to the next asset
  - move back tot he previous asset
