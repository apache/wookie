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
# This script updates a database created with version 0.9.2 of Wookie
# to work with version 0.10.0. Always use a clone of your database and not the 
# live version when performing migration.
#

#
# ----------------------------------------------------------------------- 
# Services have been removed (see WOOKIE-263)
# 
# ----------------------------------------------------------------------- 
DROP TABLE IF EXISTS WidgetDefault;
DROP TABLE IF EXISTS Widgetservice;
DROP TABLE IF EXISTS WidgetType;
