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
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    
    <xsl:output method="html" indent="yes"/>

	<xsl:template match="/">
	  <div id="results" data-role="collapsible-set">
		<xsl:apply-templates select="widgets/widget"/>
	  </div>
	</xsl:template>

	<xsl:template match="widget">
	  <div class='result' data-role='collapsible'>
	    <xsl:attribute name="wid"><xsl:value-of select="@id"/></xsl:attribute>
	    
	    <xsl:apply-templates select="title"/>
	    <div class="detail">
	      <xsl:apply-templates select="icon"/>
	      <xsl:apply-templates select="description"/>
	      <xsl:apply-templates select="author"/>
	    </div>
	  </div>
	</xsl:template>

	<xsl:template match="title">
	  <h3><xsl:value-of select="."/></h3>
	</xsl:template>

	<xsl:template match="icon">
	  <img alt="icon">
	    <xsl:attribute name="src"><xsl:value-of select="."/></xsl:attribute>
	  </img>
	</xsl:template>

	<xsl:template match="description">
	  <p><xsl:value-of select="."/></p>
	</xsl:template>

	<xsl:template match="author">
	  <p>Author: <xsl:value-of select="."/></p>
	</xsl:template>

</xsl:stylesheet>

