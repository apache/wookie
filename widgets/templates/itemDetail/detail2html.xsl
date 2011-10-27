<!-- Licensed to the Apache Software Foundation (ASF) under one or more contributor 
	license agreements. See the NOTICE file distributed with this work for additional 
	information regarding copyright ownership. The ASF licenses this file to 
	You under the Apache License, Version 2.0 (the "License"); you may not use 
	this file except in compliance with the License. You may obtain a copy of 
	the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required 
	by applicable law or agreed to in writing, software distributed under the 
	License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS 
	OF ANY KIND, either express or implied. See the License for the specific 
	language governing permissions and limitations under the License. -->
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:template match="/">
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="workflow">
		<div class="workflow">
			<div class="overview">
				<xsl:apply-templates select="thumbnail-big" />
				<div class="meta-data">
					<xsl:apply-templates select="title" />
					<xsl:apply-templates select="uploader" />
					<div class="update-rating">
						<xsl:apply-templates select="updated-at" />
						<xsl:apply-templates select="ratings" />
					</div>
					<div class="version-info">
						<xsl:apply-templates select="versions" />
						<xsl:apply-templates select="type" />
						<xsl:apply-templates select="license-type" />
					</div>
				</div>
				<xsl:apply-templates select="description" />
			</div>

			<div class="clear">
				<div class="boxed">
					<xsl:apply-templates select="created-at" />
					<xsl:apply-templates select="statistics" />
				</div>
				<div class="boxed">
					<xsl:apply-templates select="tags" />
					<p>FIXME: Groups should be here</p>
				</div>
			</div>
		</div>
	</xsl:template>

	<xsl:template match="title">
		<h3>
			<xsl:value-of select="." />
		</h3>
	</xsl:template>

	<xsl:template match="ratings">
		<div class="rating">
			Rating:
			<xsl:value-of select="sum(rating) div count(rating)" />
		</div>
	</xsl:template>

	<xsl:template match="type">
		<p>
			Type:
			<xsl:value-of select="." />
		</p>
	</xsl:template>

	<xsl:template match="license-type">
		<p>
			License:
			<xsl:value-of select="." />
		</p>
	</xsl:template>

	<xsl:template match="versions">
		<p>
			Version:
			<xsl:for-each select="workflow[last()]">
				<xsl:number count="*"/>
			</xsl:for-each>
		</p>
	</xsl:template>

	<xsl:template match="uploader">
		<p>
			Uploaded By:
			<xsl:value-of select="." />
		</p>
	</xsl:template>

	<xsl:template match="thumbnail-big">

		<div class="thumbnail">
			<img width="241">
				<xsl:attribute name="src"><xsl:value-of select="." /></xsl:attribute>
				<xsl:attribute name="alt">Preview of the <xsl:value-of
					select="../title" /> workflow.</xsl:attribute>
			</img>
		</div>
	</xsl:template>

	<xsl:template match="updated-at">
		<p>
			Last Updated:
			<xsl:value-of select="." />
		</p>
	</xsl:template>

	<xsl:template match="created-at">
		<p>
			Created:
			<xsl:value-of select="." />
		</p>
	</xsl:template>

	<xsl:template match="description">
		<div class="description">
			<xsl:value-of select="." />
		</div>
	</xsl:template>

	<xsl:template match="statistics">
		<p>
			Viewed:
			<xsl:value-of select="viewings/total" />
		</p>
		<p>
			Downloaded:
			<xsl:value-of select="downloads/total" />
		</p>
	</xsl:template>

	<xsl:template match="tags">
		<p>
			Tags:
            <xsl:for-each select="tag">
                <xsl:value-of select="." />
                <xsl:if test="position() != last()">
                    <xsl:text>, </xsl:text>
                </xsl:if>
            </xsl:for-each>
		</p>
	</xsl:template>

</xsl:stylesheet>
