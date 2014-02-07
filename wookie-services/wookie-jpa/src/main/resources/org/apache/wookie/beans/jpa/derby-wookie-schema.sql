ALTER TABLE OAuthToken
    DROP CONSTRAINT FKOAuthToken1;

ALTER TABLE Param
    DROP CONSTRAINT FKParam1;

ALTER TABLE Feature
    DROP CONSTRAINT FKFeature1;

ALTER TABLE SharedData
    DROP CONSTRAINT FKSharedData1;

ALTER TABLE Preference
    DROP CONSTRAINT FKPreference1;

ALTER TABLE PreferenceDefault
    DROP CONSTRAINT FKPreferenceDefault1;

ALTER TABLE WidgetInstance
    DROP CONSTRAINT FKWidgetInstance1;

ALTER TABLE StartFile
    DROP CONSTRAINT FKStartFile1;

ALTER TABLE Description
    DROP CONSTRAINT FKDescription1;

ALTER TABLE Author
    DROP CONSTRAINT FKAuthor1;

ALTER TABLE Name
    DROP CONSTRAINT FKName1;

ALTER TABLE License
    DROP CONSTRAINT FKLicense1;

ALTER TABLE WidgetIcon
    DROP CONSTRAINT FKWidgetIcon1;

-- ----------------------------------------------------------------------- 
-- OAuthToken 
-- ----------------------------------------------------------------------- 

DROP TABLE OAuthToken;

-- ----------------------------------------------------------------------- 
-- Param 
-- ----------------------------------------------------------------------- 

DROP TABLE Param;

-- ----------------------------------------------------------------------- 
-- Feature 
-- ----------------------------------------------------------------------- 

DROP TABLE Feature;

-- ----------------------------------------------------------------------- 
-- SharedData 
-- ----------------------------------------------------------------------- 

DROP TABLE SharedData;

-- ----------------------------------------------------------------------- 
-- Preference 
-- ----------------------------------------------------------------------- 

DROP TABLE Preference;

-- ----------------------------------------------------------------------- 
-- Participant 
-- ----------------------------------------------------------------------- 

DROP TABLE Participant;

-- ----------------------------------------------------------------------- 
-- PreferenceDefault 
-- ----------------------------------------------------------------------- 

DROP TABLE PreferenceDefault;

-- ----------------------------------------------------------------------- 
-- WidgetInstance 
-- ----------------------------------------------------------------------- 

DROP TABLE WidgetInstance;

-- ----------------------------------------------------------------------- 
-- StartFile 
-- ----------------------------------------------------------------------- 

DROP TABLE StartFile;

-- ----------------------------------------------------------------------- 
-- Description 
-- ----------------------------------------------------------------------- 

DROP TABLE Description;

-- ----------------------------------------------------------------------- 
-- Author 
-- ----------------------------------------------------------------------- 

DROP TABLE Author;

-- ----------------------------------------------------------------------- 
-- Name 
-- ----------------------------------------------------------------------- 

DROP TABLE Name;

-- ----------------------------------------------------------------------- 
-- License 
-- ----------------------------------------------------------------------- 

DROP TABLE License;

-- ----------------------------------------------------------------------- 
-- WidgetIcon 
-- ----------------------------------------------------------------------- 

DROP TABLE WidgetIcon;

-- ----------------------------------------------------------------------- 
-- Widget 
-- ----------------------------------------------------------------------- 

DROP TABLE Widget;

-- ----------------------------------------------------------------------- 
-- OPENJPA_SEQUENCE_TABLE 
-- ----------------------------------------------------------------------- 

DROP TABLE OPENJPA_SEQUENCE_TABLE;

-- ----------------------------------------------------------------------- 
-- OPENJPA_SEQUENCE_TABLE 
-- ----------------------------------------------------------------------- 

CREATE TABLE OPENJPA_SEQUENCE_TABLE
(
    ID SMALLINT NOT NULL,
    SEQUENCE_VALUE BIGINT,
    PRIMARY KEY (ID)
);

-- ----------------------------------------------------------------------- 
-- Widget 
-- ----------------------------------------------------------------------- 

CREATE TABLE Widget
(
    id INTEGER NOT NULL,
    jpa_version INTEGER,
    package_path VARCHAR(255),
    default_locale VARCHAR(255),
    height INTEGER,
    width INTEGER,
    guid VARCHAR(255) NOT NULL,
    update_location LONG VARCHAR,
    widget_version VARCHAR(255),
    dir VARCHAR(255),
    lang VARCHAR(255),
    PRIMARY KEY (id)
);

CREATE UNIQUE INDEX UNWidget1 ON Widget (guid);

-- ----------------------------------------------------------------------- 
-- WidgetIcon 
-- ----------------------------------------------------------------------- 

CREATE TABLE WidgetIcon
(
    id INTEGER NOT NULL,
    jpa_version INTEGER,
    src LONG VARCHAR,
    height INTEGER,
    width INTEGER,
    lang VARCHAR(255),
    widget_id INTEGER,
    PRIMARY KEY (id)
);

CREATE INDEX IXWidgetIcon1 ON WidgetIcon (widget_id);

-- ----------------------------------------------------------------------- 
-- License 
-- ----------------------------------------------------------------------- 

CREATE TABLE License
(
    id INTEGER NOT NULL,
    jpa_version INTEGER,
    href LONG VARCHAR,
    text LONG VARCHAR,
    dir VARCHAR(255),
    lang VARCHAR(255),
    widget_id INTEGER,
    PRIMARY KEY (id)
);

CREATE INDEX IXLicense1 ON License (widget_id);

-- ----------------------------------------------------------------------- 
-- Name 
-- ----------------------------------------------------------------------- 

CREATE TABLE Name
(
    id INTEGER NOT NULL,
    jpa_version INTEGER,
    name LONG VARCHAR,
    shortName VARCHAR(255),
    dir VARCHAR(255),
    lang VARCHAR(255),
    widget_id INTEGER,
    PRIMARY KEY (id)
);

CREATE INDEX IXName1 ON Name (widget_id);

-- ----------------------------------------------------------------------- 
-- Author 
-- ----------------------------------------------------------------------- 

CREATE TABLE Author
(
    id INTEGER NOT NULL,
    jpa_version INTEGER,
    author LONG VARCHAR,
    email LONG VARCHAR,
    href LONG VARCHAR,
    lang VARCHAR(255),
    dir VARCHAR(255),
    widget_id INTEGER,
    PRIMARY KEY (id)
);

CREATE INDEX IXAuthor1 ON Author (widget_id);

-- ----------------------------------------------------------------------- 
-- Description 
-- ----------------------------------------------------------------------- 

CREATE TABLE Description
(
    id INTEGER NOT NULL,
    jpa_version INTEGER,
    content LONG VARCHAR,
    dir VARCHAR(255),
    lang VARCHAR(255),
    widget_id INTEGER,
    PRIMARY KEY (id)
);

CREATE INDEX IXDescription1 ON Description (widget_id);

-- ----------------------------------------------------------------------- 
-- StartFile 
-- ----------------------------------------------------------------------- 

CREATE TABLE StartFile
(
    id INTEGER NOT NULL,
    jpa_version INTEGER,
    url LONG VARCHAR,
    charset VARCHAR(255),
    lang VARCHAR(255),
    widget_id INTEGER,
    PRIMARY KEY (id)
);

CREATE INDEX IXStartFile1 ON StartFile (widget_id);

-- ----------------------------------------------------------------------- 
-- WidgetInstance 
-- ----------------------------------------------------------------------- 

CREATE TABLE WidgetInstance
(
    id INTEGER NOT NULL,
    jpa_version INTEGER,
    apikey VARCHAR(255) NOT NULL,
    userId VARCHAR(255) NOT NULL,
    sharedDataKey VARCHAR(255),
    nonce VARCHAR(255),
    idKey VARCHAR(255) NOT NULL,
    opensocialToken LONG VARCHAR NOT NULL,
    widget_id VARCHAR(255) NOT NULL,
    updated CHAR(1),
    shown CHAR(1),
    hidden CHAR(1),
    locked CHAR(1),
    lang VARCHAR(255),
    PRIMARY KEY (id)
);

CREATE INDEX IXWidgetInstance1 ON WidgetInstance (widget_id);

CREATE UNIQUE INDEX UNWidgetInstance1 ON WidgetInstance (idKey);

-- ----------------------------------------------------------------------- 
-- PreferenceDefault 
-- ----------------------------------------------------------------------- 

CREATE TABLE PreferenceDefault
(
    id INTEGER NOT NULL,
    jpa_version INTEGER,
    widget_id INTEGER,
    preference VARCHAR(255) NOT NULL,
    value VARCHAR(1024) NOT NULL,
    readOnly CHAR(1),
    PRIMARY KEY (id)
);

CREATE INDEX IXPreferenceDefault1 ON PreferenceDefault (widget_id);

-- ----------------------------------------------------------------------- 
-- Participant 
-- ----------------------------------------------------------------------- 

CREATE TABLE Participant
(
    id INTEGER NOT NULL,
    jpa_version INTEGER,
    participant_id VARCHAR(255) NOT NULL,
    participant_display_name VARCHAR(255) NOT NULL,
    participant_thumbnail_url VARCHAR(1024),
    sharedDataKey VARCHAR(255) NOT NULL,
    role VARCHAR(255),
    PRIMARY KEY (id)
);

-- ----------------------------------------------------------------------- 
-- Preference 
-- ----------------------------------------------------------------------- 

CREATE TABLE Preference
(
    id INTEGER NOT NULL,
    jpa_version INTEGER,
    widget_instance_id INTEGER,
    dkey VARCHAR(255),
    dvalue LONG VARCHAR,
    readOnly CHAR(1),
    PRIMARY KEY (id)
);

CREATE INDEX IXPreference1 ON Preference (widget_instance_id);

CREATE UNIQUE INDEX UNPreference1 ON Preference (widget_instance_id, dkey);

-- ----------------------------------------------------------------------- 
-- SharedData 
-- ----------------------------------------------------------------------- 

CREATE TABLE SharedData
(
    id INTEGER NOT NULL,
    jpa_version INTEGER,
    sharedDataKey VARCHAR(255),
    dkey VARCHAR(255),
    dvalue LONG VARCHAR,
    widget_id INTEGER,
    PRIMARY KEY (id)
);

CREATE INDEX IXSharedData1 ON SharedData (widget_id);

-- ----------------------------------------------------------------------- 
-- Feature 
-- ----------------------------------------------------------------------- 

CREATE TABLE Feature
(
    id INTEGER NOT NULL,
    jpa_version INTEGER,
    featureName VARCHAR(255) NOT NULL,
    required CHAR(1),
    widget_id INTEGER,
    PRIMARY KEY (id)
);

CREATE INDEX IXFeature1 ON Feature (widget_id);

-- ----------------------------------------------------------------------- 
-- Param 
-- ----------------------------------------------------------------------- 

CREATE TABLE Param
(
    id INTEGER NOT NULL,
    jpa_version INTEGER,
    feature_id INTEGER,
    parameterName VARCHAR(255) NOT NULL,
    parameterValue VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE INDEX IXParam1 ON Param (feature_id);

-- ----------------------------------------------------------------------- 
-- OAuthToken 
-- ----------------------------------------------------------------------- 

CREATE TABLE OAuthToken
(
    id INTEGER NOT NULL,
    jpa_version INTEGER,
    authzUrl VARCHAR(255) NOT NULL,
    accessToken VARCHAR(255) NOT NULL,
    clientId VARCHAR(255) NOT NULL,
    expires BIGINT NOT NULL,
    widget_instance_id INTEGER,
    PRIMARY KEY (id)
);

CREATE INDEX IXOAuthToken1 ON OAuthToken (widget_instance_id);

ALTER TABLE WidgetIcon
    ADD CONSTRAINT FKWidgetIcon1 FOREIGN KEY (widget_id) REFERENCES Widget (id);

ALTER TABLE License
    ADD CONSTRAINT FKLicense1 FOREIGN KEY (widget_id) REFERENCES Widget (id);

ALTER TABLE Name
    ADD CONSTRAINT FKName1 FOREIGN KEY (widget_id) REFERENCES Widget (id);

ALTER TABLE Author
    ADD CONSTRAINT FKAuthor1 FOREIGN KEY (widget_id) REFERENCES Widget (id);

ALTER TABLE Description
    ADD CONSTRAINT FKDescription1 FOREIGN KEY (widget_id) REFERENCES Widget (id);

ALTER TABLE StartFile
    ADD CONSTRAINT FKStartFile1 FOREIGN KEY (widget_id) REFERENCES Widget (id);

ALTER TABLE PreferenceDefault
    ADD CONSTRAINT FKPreferenceDefault1 FOREIGN KEY (widget_id) REFERENCES Widget (id);

ALTER TABLE Preference
    ADD CONSTRAINT FKPreference1 FOREIGN KEY (widget_instance_id) REFERENCES WidgetInstance (id);

ALTER TABLE SharedData
    ADD CONSTRAINT FKSharedData1 FOREIGN KEY (widget_id) REFERENCES Widget (id);

ALTER TABLE Feature
    ADD CONSTRAINT FKFeature1 FOREIGN KEY (widget_id) REFERENCES Widget (id);

ALTER TABLE Param
    ADD CONSTRAINT FKParam1 FOREIGN KEY (feature_id) REFERENCES Feature (id);

ALTER TABLE OAuthToken
    ADD CONSTRAINT FKOAuthToken1 FOREIGN KEY (widget_instance_id) REFERENCES WidgetInstance (id);

