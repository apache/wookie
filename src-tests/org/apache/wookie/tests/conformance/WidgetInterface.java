/*
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.wookie.tests.conformance;

import org.junit.Test;

/**
 * Test cases for W3C Widgets 1.0: The Widget Interface
 * See test case definitions at http://dev.w3.org/2006/waf/widgets-api/test-suite/
 * 
 * Currently this just outputs HTML that you need to paste into a text file and view
 * in your browser to "eyeball" the results. But at least its a start
 */
public class WidgetInterface extends AbstractFunctionalConformanceTest{
	
	@Test
	public void taza(){
	  doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-za/aa/aa.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-za/author_attrexists/author_attrexists.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-za/authorEmail_attrexists/authorEmail_attrexists.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-za/authorHref_attrexists/authorHref_attrexists.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-za/description_attrexists/description_attrexists.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-za/height_attrexists/height_attrexists.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-za/id_attrexists/id_attrexists.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-za/name_attrexists/name_attrexists.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-za/preferences_attrexists/preferences_attrexists.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-za/shortName_attrexists/shortName_attrexists.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-za/version_attrexists/version_attrexists.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-za/width_attrexists/width_attrexists.wgt");
		
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-za/author_attrreadonly/author_attrreadonly.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-za/authorEmail_attrreadonly/authorEmail_attrreadonly.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-za/authorHref_attrreadonly/authorHref_attrreadonly.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-za/description_attrreadonly/description_attrreadonly.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-za/height_attrreadonly/height_attrreadonly.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-za/id_attrreadonly/id_attrreadonly.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-za/name_attrreadonly/name_attrreadonly.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-za/preferences_attrreadonly/preferences_attrreadonly.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-za/shortName_attrreadonly/shortName_attrreadonly.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-za/version_attrreadonly/version_attrreadonly.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-za/width_attrreadonly/width_attrreadonly.wgt");
		
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-za/author_attrtype/author_attrtype.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-za/authorEmail_attrtype/authorEmail_attrtype.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-za/authorHref_attrtype/authorHref_attrtype.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-za/description_attrtype/description_attrtype.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-za/height_attrtype/height_attrtype.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-za/id_attrtype/id_attrtype.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-za/name_attrtype/name_attrtype.wgt");
    doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-za/shortName_attrtype/shortName_attrtype.wgt");
    doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-za/version_attrtype/version_attrtype.wgt");
    doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-za/width_attrtype/width_attrtype.wgt");
		
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-za/ad/ad.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-za/ae/ae.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-za/af/af.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-za/ag/ag.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-za/ah/ah.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-za/ai/ai.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-za/aj/aj.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-za/ak/ak.wgt");
	}
	
	@Test
	public void taah(){
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-ah/ab/ab.wgt");		
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-ah/ax/ax.wgt");
	}
	
	@Test
	public void taAttr(){
	  doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-attribute-tests/return-emtpy-strings/return-emtpy-strings.wgt");
	  doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-attribute-tests/return-proper-strings/return-proper-strings.wgt");
	}
	
	@Test
	public void taGet18nString(){
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-lro/001/i18nlro01.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-lro/002/i18nlro02.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-lro/003/i18nlro03.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-lro/004/i18nlro04.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-lro/006/i18nlro06.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-lro/007/i18nlro07.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-lro/008/i18nlro08.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-lro/010/i18nlro10.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-lro/011/i18nlro11.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-lro/012/i18nlro12.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-lro/014/i18nlro14.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-lro/015/i18nlro15.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-lro/016/i18nlro16.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-lro/017/i18nlro17.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-lro/019/i18nlro19.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-lro/020/i18nlro20.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-lro/021/i18nlro21.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-lro/022/i18nlro22.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-lro/036/i18nlro36.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-lro/037/i18nlro37.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-lro/041/i18nlro41.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-lro/042/i18nlro42.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-lro/044/i18nlro44.wgt");
		
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-rlo/001/i18nrlo01.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-rlo/002/i18nrlo02.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-rlo/003/i18nrlo03.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-rlo/004/i18nrlo04.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-rlo/010/i18nrlo10.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-rlo/011/i18nrlo11.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-rlo/012/i18nrlo12.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-rlo/014/i18nrlo14.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-rlo/015/i18nrlo15.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-rlo/016/i18nrlo16.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-rlo/017/i18nrlo17.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-rlo/019/i18nrlo19.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-rlo/020/i18nrlo20.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-rlo/021/i18nrlo21.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-rlo/022/i18nrlo22.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-rlo/036/i18nrlo36.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-rlo/037/i18nrlo37.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-rlo/041/i18nrlo41.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-rlo/042/i18nrlo42.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-rlo/044/i18nrlo44.wgt");
		// ltr
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-ltr/001/i18nltr01.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-ltr/002/i18nltr02.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-ltr/003/i18nltr03.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-ltr/004/i18nltr04.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-ltr/006/i18nltr06.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-ltr/007/i18nltr07.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-ltr/008/i18nltr08.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-ltr/010/i18nltr10.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-ltr/011/i18nltr11.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-ltr/012/i18nltr12.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-ltr/014/i18nltr14.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-ltr/015/i18nltr15.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-ltr/016/i18nltr16.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-ltr/017/i18nltr17.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-ltr/020/i18nltr20.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-ltr/021/i18nltr21.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-ltr/022/i18nltr22.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-ltr/036/i18nltr36.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-ltr/037/i18nltr37.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-ltr/041/i18nltr41.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-ltr/042/i18nltr42.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-ltr/044/i18nltr44.wgt");
		// rtl
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-rtl/001/i18nrtl01.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-rtl/002/i18nrtl02.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-rtl/003/i18nrtl03.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-rtl/004/i18nrtl04.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-rtl/006/i18nrtl06.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-rtl/007/i18nrtl07.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-rtl/008/i18nrtl08.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-rtl/010/i18nrtl10.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-rtl/011/i18nrtl11.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-rtl/012/i18nrtl12.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-rtl/014/i18nrtl14.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-rtl/015/i18nrtl15.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-rtl/016/i18nrtl16.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-rtl/017/i18nrtl17.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-rtl/020/i18nrtl20.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-rtl/021/i18nrtl21.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-rtl/036/i18nrtl36.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-rtl/037/i18nrtl37.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-rtl/041/i18nrtl41.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-rtl/042/i18nrtl42.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-rtl/044/i18nrtl44.wgt");
	}

	@Test
	public void tapa(){
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-pa/ao/ao.wgt");
	}
	@Test
	public void tapb(){
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-pb/ap/ap.wgt");
	}
	
	@Test
	public void tastorageevent(){
	  doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-storage-event/setItem-fires-event/setItem-fires-event.wgt");
    doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-storage-event/removeItem-fires-event/removeItem-fires-event.wgt");
    doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-storage-event/clear-fires-event/clear-fires-event.wgt");
	}
	
	@Test
	public void taal(){
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-al/ar/ar.wgt");
    doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-al/as/as.wgt");
	}

	@Test
	public void taae(){
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-ae/at/at.wgt");
	}
	
	@Test
	public void taaa(){
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-aa/au/au.wgt");
	}	

}
