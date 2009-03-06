function replaceTextSmileys(text)
{
    // ***add textual emoticons to the array below
    var textSmileys = new Array(
        ":)",
        ":(",
        ";)",
        ":D",
        
        ":-)",
        ":-(",
        ";-)",
        ":-D"
        );
    // *** add the url's from the corresponding images below
    var realSmileys = new Array(
        "smileys/emoticon-0100-smile.gif",
        "smileys/emoticon-0101-sadsmile.gif",
        "smileys/emoticon-0105-wink.gif",
        "smileys/emoticon-0102-bigsmile.gif",
        
        "smileys/emoticon-0100-smile.gif",
        "smileys/emoticon-0101-sadsmile.gif",
        "smileys/emoticon-0105-wink.gif",
        "smileys/emoticon-0102-bigsmile.gif"
        );
   
    var indx;
    var smiley;
    var replacement;
    
    for ( var n = 0 ; n < textSmileys.length; n++ ){ 
            replacement = '';
            indx = text.indexOf(textSmileys[n]);
            if (indx != -1){
                smiley = '<img src=\"' + realSmileys[n] + '">'
                replacement = text.replace(textSmileys[n],smiley);
                text = replacement;              
            }    
    }
                    
    return text;                 
 
    } 