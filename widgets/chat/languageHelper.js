/*
 * Language helper object
 */
var LanguageHelper = {

	getLocalizedString : function(key) {
		try {
			var evalString = "localizedStrings['"+key+"'];";		
			var ret = eval(evalString);
			if (ret === undefined)
				ret = key;
			return ret;
		}
		catch (ex) {
		}
		return key;
	}
}