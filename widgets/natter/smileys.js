function replaceTextSmileys(text)
{
    // ***add textual emoticons to the array below
    var textSmileys = new Array(
        ":)",
        ":(",
        ";)",
        ":D",
        ":P",

        ":-)",
        ":-(",
        ";-)",
        ":-D",
        ":-P",
        
        "(beer)",
        "<3",
        "(moon)",
        "O_o"
        );
    // *** add the url's from the corresponding images below
    var realSmileys = new Array(
        "smileys/smile.png",
        "smileys/frown.png",
        "smileys/wink.png",
        "smileys/grin2.png",
        "smileys/tongue.png",
        
        "smileys/smile.png",
        "smileys/frown.png",
        "smileys/wink.png",
        "smileys/grin2.png",
        "smileys/tongue.png",
        
        "smileys/beer.png",
        "smileys/heart.png",
        "smileys/ass.png",
        "smileys/O_o.png"
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