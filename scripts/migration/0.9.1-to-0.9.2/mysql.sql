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
# MySQL5 database migration script for Apache Wookie (incubating) 0.9.2
#
# This script updates a database created with version 0.9.1 of Wookie
# to work with version 0.9.2. Always use a clone of your database and not the 
# live version when performing migration.
#

# ----------------------------------------------------------------------- 
# New oAuth Token table
# 
# ----------------------------------------------------------------------- 
DROP TABLE IF EXISTS OAuthToken;
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
 ) ;
 
CREATE INDEX IXOAuthToken1 ON OAuthToken (widget_instance_id);
 
ALTER TABLE OAuthToken ADD CONSTRAINT FKOAuthToken1 FOREIGN KEY (widget_instance_id) REFERENCES WidgetInstance (id);




# ----------------------------------------------------------------------- 
# Token replaced by oAuth Token table above
# 
# ----------------------------------------------------------------------- 
DROP TABLE IF EXISTS Token;

# ----------------------------------------------------------------------- 
# Access Request and WhiteList are now managed using a properties file
# 
# ----------------------------------------------------------------------- 
DROP TABLE IF EXISTS AccessRequest;
DROP TABLE IF EXISTS whitelist;

