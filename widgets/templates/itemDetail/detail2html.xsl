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

    <xsl:output method="html" indent="yes" />

    <xsl:template match="/">
        <xsl:apply-templates />
    </xsl:template>

    <xsl:template match="status">
        <div class="tweet">
            <div class="overview">
                <div class="quote">
                    <xsl:value-of select="text" />
                </div>
            </div>
            <div class="meta-data">
                <div class="clear">
                    <xsl:apply-templates select="user"
                        mode="summary" />
                    <xsl:apply-templates select="in_reply_to_screen_name" />
                    <xsl:apply-templates select="created_at" />
                    <xsl:apply-templates select="source" />
                </div>
                <div class="clear">
                    <xsl:apply-templates select="retweet_count" />
                </div>
            </div>
            <xsl:apply-templates select="user" mode="detail" />
        </div>
    </xsl:template>

    <xsl:template match="hashtags">
        <span class="type">
            <xslfor-each select="hashtag">
                <xsl:value-of select="hashtag" />
            </xslfor-each>
        </span>
    </xsl:template>

    <xsl:template match="user" mode="summary">
        <xsl:value-of select="name" />
        (
        <xsl:value-of select="screen_name" />
        ) from
        <xsl:value-of select="location" />
    </xsl:template>

    <xsl:template match="user" mode="detail">
        <div class="user clear">
            <h3>
                About
                <xsl:value-of select="name" />
            </h3>
            <xsl:apply-templates select="profile_image_url" />
            <p>
                <xsl:value-of select="name" />
                uses the screen name
                <xsl:value-of select="screen_name" />
                <xsl:text>, and is based in</xsl:text>
                <xsl:value-of select="location" />
            </p>
            <p>
                <xsl:text>They describe themselves as '</xsl:text>
                <xsl:value-of select="description" />
                <xsl:text>' They provide more information about themselves at</xsl:text>
                <a>
                    <xsl:atrribute name="href">
                        <xsl:value-of select="url" />
                    </xsl:atrribute>
                    <xsl:value-of select="url" />
                </a>
            </p>
            <p>
                They have
                <xsl:value-of select="followers_count" />
                followers, are listed in
                <xsl:value-of select="listed_count" />
                lists and follow
                <xsl:value-of select="friends_count" />
                <xsl:text> people. They have tweeted </xsl:text>
                <xsl:value-of select="statuses_count" />
                times.
            </p>
        </div>
    </xsl:template>

    <xsl:template match="in_reply_to_screen_name">
        <xsl:if test="text()">
            in reply to
            <xsl:value-of select="name" />
            (
            <xsl:value-of select="screen_name" />
            )
        </xsl:if>
    </xsl:template>

    <xsl:template match="retweet_count">
        <xsl:choose>
            <xsl:when test="text() = 0">
                Never retweeted.
            </xsl:when>
            <xsl:otherwise>
                Retweeted
                <xsl:value-of select="." />
                times.
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="profile_image_url">
        <div class="thumbnail">
            <img alt="Avatar for tweet author">
                <xsl:attribute name="src"><xsl:value-of
                    select="." /></xsl:attribute>
            </img>
        </div>
    </xsl:template>

    <xsl:template match="created_at">
        at
        <xsl:value-of select="." />
    </xsl:template>

    <xsl:template match="source">
        (using
        <xsl:value-of select="." disable-output-escaping="yes" />
        )
    </xsl:template>
</xsl:stylesheet>
