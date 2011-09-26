#
#  Licensed under the Apache License, Version 2.0 (the "License");
#  you may not use this file except in compliance with the License.
#  You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
#  Unless required by applicable law or agreed to in writing, software
#  distributed under the License is distributed on an "AS IS" BASIS,
#  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#  See the License for the specific language governing permissions and
# limitations under the License.
#

#
# MySQL5 database migration script for Apache Wookie (incubating) 0.9.1
#
# This script updates a database created with version 0.9.0 of Wookie
# to work with version 0.9.1. Always use a clone of your database and not the 
# live version when performing migration.
#

# ----------------------------------------------------------------------- 
# ServerFeature has been removed in 0.9.1; instead, features are loaded
# directly from XML into memory.
# ----------------------------------------------------------------------- 

DROP TABLE IF EXISTS ServerFeature;


# ----------------------------------------------------------------------- 
# Author has been moved from part of Widget into a separate class. This
# means we create a new table, copy the data over from Widget, and then
# modify the Widget table to remove the Author columns
# ----------------------------------------------------------------------- 
DROP TABLE IF EXISTS Author;

CREATE TABLE Author
(
    id INTEGER NOT NULL,
    jpa_version INTEGER,
    author VARCHAR(255)  NULL,
    email VARCHAR(255)  NULL,
    href VARCHAR(255)  NULL,
    lang VARCHAR(255) NULL,
    dir VARCHAR(255) NULL,
    widget_id INTEGER,
    PRIMARY KEY (id)
) ;

CREATE INDEX IXAuthor1 ON Author (widget_id);

INSERT INTO Author (id, jpa_version, widget_id, author, email, href) SELECT id, 1, id, widget_author, widget_author_email, widget_author_href FROM Widget;

ALTER TABLE Widget DROP COLUMN widget_author;
ALTER TABLE Widget DROP COLUMN widget_author_email;
ALTER TABLE Widget DROP COLUMN widget_author_href;

# ----------------------------------------------------------------------- 
# Some new properties have been added to Widget, to support localisation,
# widget updates, and connecting a widget with its original installation
# .wgt package
# ----------------------------------------------------------------------- 

ALTER TABLE Widget ADD COLUMN package_path VARCHAR(255) NULL;
ALTER TABLE Widget ADD COLUMN default_locale VARCHAR(255) NULL;
ALTER TABLE Widget ADD COLUMN update_location MEDIUMTEXT NULL;
ALTER TABLE Widget ADD COLUMN dir VARCHAR(255) NULL;
ALTER TABLE Widget ADD COLUMN lang VARCHAR(255) NULL;

# ----------------------------------------------------------------------- 
# Participant no longer connects directly to Widget, but is instead 
# connected by the shared data key.
# ----------------------------------------------------------------------- 

ALTER TABLE Participant DROP INDEX IXParticipant1;
ALTER TABLE Participant DROP FOREIGN KEY FKParticipant1;
ALTER TABLE Participant DROP COLUMN widget_id;