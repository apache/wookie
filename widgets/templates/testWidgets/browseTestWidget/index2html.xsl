<!--
  Licensed to the Apache Software Foundation (ASF) under one or more
  contributor license agreements.  See the NOTICE file distributed with
  this work for additional information regarding copyright ownership.
  The ASF licenses this file to You under the Apache License, Version 2.0
  (the "License"); you may not use this file except in compliance with
  the License.  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
-->
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:atom="http://www.w3.org/2005/Atom">

	<!-- Index requests return a custom XML format -->
	<xsl:template match="statuses">
		<div id="results" data-role="collapsible-set">
			<xsl:apply-templates/>
		</div>
	</xsl:template>

	<!-- Search requests return an ATOM feed -->
	<xsl:template match="atom:feed">
	  <div id="results" data-role="collapsible-set">
	  	<xsl:apply-templates/>
	  </div>
	</xsl:template>

	<xsl:template match="status">
      <div class='result' data-role='collapsible'>
        <xsl:attribute name="wid">
          <xsl:value-of select="./id"/>
        </xsl:attribute>
	  
	    <h3><xsl:value-of select="./text"/></h3>
	    <div class="detail">
	      <p>Loading...</p>
        </div>
      </div>
	</xsl:template>

	<xsl:template match="atom:entry">
	  <div class='result' data-role='collapsible'>
        <xsl:attribute name="wid">
        	<xsl:value-of select="substring-after(substring-after(atom:id, ':'), ':')"/>
        </xsl:attribute>
	  
	    <h3><xsl:value-of select="./atom:title"/></h3>
	    <div class="detail">
	      <p>Loading...</p>
        </div>
      </div>
	</xsl:template>

	<xsl:template match="text( )|@*"/>

</xsl:stylesheet>

