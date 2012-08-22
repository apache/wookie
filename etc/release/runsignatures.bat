rem
rem Licensed under the Apache License, Version 2.0 (the "License");
rem you may not use this file except in compliance with the License.
rem You may obtain a copy of the License at
rem
rem      http://www.apache.org/licenses/LICENSE-2.0
rem
rem Unless required by applicable law or agreed to in writing, software
rem distributed under the License is distributed on an "AS IS" BASIS,
rem WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
rem See the License for the specific language governing permissions and
rem limitations under the License.

set WOOKIE-VERSION=0.12.0-incubating
set ROOT-FOLDER=%WOOKIE-VERSION%
set SOURCE-FOLDER=source
set SRC-BUILD-TAR=apache-wookie-%WOOKIE-VERSION%-src.tar.gz
set SRC-BUILD-ZIP=apache-wookie-%WOOKIE-VERSION%-src.zip
set BINARY-FOLDER-STANDALONE=binary/standalone
set BINARY-STANDALONE-TAR=apache-wookie-%WOOKIE-VERSION%-standalone.tar.gz
set BINARY-STANDALONE-ZIP=apache-wookie-%WOOKIE-VERSION%-standalone.zip
set BINARY-FOLDER-WAR=binary/war
set BINARY-WAR-TAR=apache-wookie-%WOOKIE-VERSION%-war.tar.gz
set BINARY-WAR-ZIP=apache-wookie-%WOOKIE-VERSION%-war.zip

cd %ROOT-FOLDER%/%SOURCE-FOLDER%
gpg2 --armor --output %SRC-BUILD-TAR%.asc --detach-sig %SRC-BUILD-TAR%
echo verifying the signature for %SRC-BUILD-TAR%
gpg2 --verify %SRC-BUILD-TAR%.asc %SRC-BUILD-TAR%
echo Creating MD5 value for %SRC-BUILD-TAR%
gpg2 --print-md MD5 %SRC-BUILD-TAR% > %SRC-BUILD-TAR%.md5
echo Creating SHA512 value for %SRC-BUILD-TAR%
gpg2 --print-md SHA512 %SRC-BUILD-TAR% > %SRC-BUILD-TAR%.sha

gpg2 --armor --output %SRC-BUILD-ZIP%.asc --detach-sig %SRC-BUILD-ZIP%
echo verifying the signature for %SRC-BUILD-ZIP%
gpg2 --verify %SRC-BUILD-ZIP%.asc %SRC-BUILD-ZIP%
echo Creating MD5 value for %SRC-BUILD-ZIP%
gpg2 --print-md MD5 %SRC-BUILD-ZIP% > %SRC-BUILD-ZIP%.md5
echo Creating SHA512 value for %SRC-BUILD-ZIP%
gpg2 --print-md SHA512 %SRC-BUILD-ZIP% > %SRC-BUILD-ZIP%.sha

cd ../%BINARY-FOLDER-STANDALONE%

gpg2 --armor --output %BINARY-STANDALONE-TAR%.asc --detach-sig %BINARY-STANDALONE-TAR%
echo verifying the signature for %BINARY-STANDALONE-TAR%
gpg2 --verify %BINARY-STANDALONE-TAR%.asc %BINARY-STANDALONE-TAR%
echo Creating MD5 value for %BINARY-STANDALONE-TAR%
gpg2 --print-md MD5 %BINARY-STANDALONE-TAR% > %BINARY-STANDALONE-TAR%.md5
echo Creating SHA512 value for %BINARY-STANDALONE-TAR%
gpg2 --print-md SHA512 %BINARY-STANDALONE-TAR% > %BINARY-STANDALONE-TAR%.sha

gpg2 --armor --output %BINARY-STANDALONE-ZIP%.asc --detach-sig %BINARY-STANDALONE-ZIP%
echo verifying the signature for %BINARY-STANDALONE-ZIP%
gpg2 --verify %BINARY-STANDALONE-ZIP%.asc %BINARY-STANDALONE-ZIP%
echo Creating MD5 value for %BINARY-STANDALONE-ZIP%
gpg2 --print-md MD5 %BINARY-STANDALONE-ZIP% > %BINARY-STANDALONE-ZIP%.md5
echo Creating SHA512 value for %BINARY-STANDALONE-ZIP%
gpg2 --print-md SHA512 %BINARY-STANDALONE-ZIP% > %BINARY-STANDALONE-ZIP%.sha

cd ../../%BINARY-FOLDER-WAR%

gpg2 --armor --output %BINARY-WAR-TAR%.asc --detach-sig %BINARY-WAR-TAR%
echo verifying the signature for %BINARY-WAR-TAR%
gpg2 --verify %BINARY-WAR-TAR%.asc %BINARY-WAR-TAR%
echo Creating MD5 value for %BINARY-WAR-TAR%
gpg2 --print-md MD5 %BINARY-WAR-TAR% > %BINARY-WAR-TAR%.md5
echo Creating SHA512 value for %BINARY-WAR-TAR%
gpg2 --print-md SHA512 %BINARY-WAR-TAR% > %BINARY-WAR-TAR%.sha

gpg2 --armor --output %BINARY-WAR-ZIP%.asc --detach-sig %BINARY-WAR-ZIP%
echo verifying the signature for %BINARY-WAR-ZIP%
gpg2 --verify %BINARY-WAR-ZIP%.asc %BINARY-WAR-ZIP%
echo Creating MD5 value for %BINARY-WAR-ZIP%
gpg2 --print-md MD5 %BINARY-WAR-ZIP% > %BINARY-WAR-ZIP%.md5
echo Creating SHA512 value for %BINARY-WAR-ZIP%
gpg2 --print-md SHA512 %BINARY-WAR-ZIP% > %BINARY-WAR-ZIP%.sha
pause

