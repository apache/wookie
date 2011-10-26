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

	<xsl:template match="/">
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="workflow">
		<div class="workflow">
			<div class="overview">
				<div class="thumbnail">
					<xsl:apply-templates select="thumbnail" />
				</div>
				<xsl:apply-templates select="description" />
			</div>
			<div class="meta-data">
				<div class="clear">
					<xsl:apply-templates select="uploader" />
					<xsl:apply-templates select="updated-at" />
				</div>
				<div class="clear">
					<xsl:apply-templates select="type" />
					<xsl:apply-templates select="license-type" />
				</div>
			</div>
		</div>
	</xsl:template>

	<xsl:template match="type">
		<span class="type">
			Type:
			<xsl:value-of select="." />
		</span>
	</xsl:template>

	<xsl:template match="license-type">
		<span class="license">
			License:
			<xsl:value-of select="." />
		</span>
	</xsl:template>

	<xsl:template match="uploader">
		<span class="uploader">
			Uploaded By:
			<xsl:value-of select="." />
		</span>
	</xsl:template>

	<xsl:template match="thumbnail">
		<img alt="Thumbnail of the workflow" width="98" height="100">
			<xsl:attribute name="src"><xsl:value-of select="." /></xsl:attribute>
		</img>
	</xsl:template>

	<xsl:template match="updated-at">
		<span class="update">
			Last Updated:
			<xsl:value-of select="." />
		</span>
	</xsl:template>

	<xsl:template match="description">
		<div class="description">
			<xsl:value-of select="substring(.,0,255)" />
			<xsl:if test="string-length(.) > 255">
				...
			</xsl:if>
		</div>
	</xsl:template>

</xsl:stylesheet>
