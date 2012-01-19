This template creates a widget capable of displaying the detail of an item retrieved from a REST API.

In its default configuration an item is retrieved from the location defined by the property itemDetail.get.url.

This is expected to return an XML or JSON document which is used to create HTML using item_template.html -
any tokens defined in the template using the format ${TOKEN} will be replaced by matching elements defined in 
browse.item.elements or attributes in browse.item.attributes returned by the API. 

Note that the token is always in uppercase, and that item_template.html must only contain single (') and
not double (") quotes.

The initial item displayed is identified by the itemDetail.default.itemId property. However, this can be changed
by calling Widget.preferences.setItem("itemId", value).

TODO
====

Later iterations of this template will use a form of Inter-Widget Communication to allow other widgets to set
the itemId. For example, it will be possible to use the browse template to force this widget to update to 
display an item defined in a browse widget.

We also plan to add a configurable "actions" section to the widget that will allow a set of user enabled actions
to be performed on the item, e.g. marking as a favourite. 