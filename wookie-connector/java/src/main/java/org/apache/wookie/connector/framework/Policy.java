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
package org.apache.wookie.connector.framework;

public class Policy {
	  private String scope;
	  private String origin;
	  private String directive;
	  
	  public Policy ( String inScope, String inOrigin, String inDirective ) {
		  this.scope = inScope;
		  this.origin = inOrigin;
		  this.directive = inDirective;
	  }

	/**
	 * @return the scope
	 */
	public String getScope() {
		return scope;
	}

	/**
	 * @param scope the scope to set
	 */
	public void setScope(String scope) {
		this.scope = scope;
	}

	/**
	 * @return the origin
	 */
	public String getOrigin() {
		return origin;
	}

	/**
	 * @param origin the origin to set
	 */
	public void setOrigin(String origin) {
		this.origin = origin;
	}

	/**
	 * @return the directive
	 */
	public String getDirective() {
		return directive;
	}

	/**
	 * @param directive the directive to set
	 */
	public void setDirective(String directive) {
		this.directive = directive;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return scope + " " + origin + " " + directive;
	}
	
	
	  
	  
}
