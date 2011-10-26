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

	<xsl:template match="status">
		<div class="tweet">
			<div class="overview">
				<div class="thumbnail">
					<xsl:apply-templates select="user/profile_image_url" />
				</div>
				<div class="description"><xsl:value-of select="text"/></div>
			</div>
			<div class="meta-data">
				<div class="clear">
					<xsl:apply-templates select="user" />
					<xsl:apply-templates select="created-at" />
				</div>
				<div class="clear">
					<xsl:apply-templates select="hashtags" />
				</div>
			</div>
		</div>
	</xsl:template>

	<xsl:template match="hashtags">
		<span class="type">
			<xslfor-each select="hashtag">
			  <xsl:value-of select="hashtag" />
			</xslfor-each>
		</span>
	</xsl:template>

	<xsl:template match="user">
		<span class="uploader">
			<xsl:value-of select="name"/> (<xsl:value-of select="screen_name" />)
		</span>
	</xsl:template>

	<xsl:template match="profile_image_url">
		<img alt="Avatar for tweet author">
			<xsl:attribute name="src"><xsl:value-of select="." /></xsl:attribute>
		</img>
	</xsl:template>

	<xsl:template match="created-at">
		<span class="update">
			<xsl:value-of select="." />
		</span>
	</xsl:template>

</xsl:stylesheet>
