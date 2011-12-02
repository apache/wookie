Walkthrough

Walk through is a HTML app / widget that presents a series of linked questions.
Adiitional links in the question text are used to add items to a list of 
actions to be carried out. Some of the actions contain links to external web 
pages and some to internal notes (both open in a new tab). The Actions list
can be download as text and internal notes are included.

The questions are provided in a XML document and so can be easily changed. 
Questions are in HTML (using suitable escaping in the XML doc). Notes are
formated using the Creole wiki markup.

Walkthrough is a W3C widget and designed to be used with the Wookie widget 
server. Apart from the Save Actions feature (which required PHP) it is 
completely client side and so requires a modern browser with Javascript 
enabled.

Notes:

* Questions can be referenced by id in a URI such as index.html#q6
* Notes can be referenced by id in a URI such as noteviewer.html#n1
* WAI ARIA roles have been used but not HTML5 landmark elements


