#!/bin/bash
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
echo "Run Signatures"
export WOOKIE_VERSION="0.13.1"
export ROOT_FOLDER=$WOOKIE_VERSION
export SOURCE_FOLDER="$WOOKIE_VERSION/source/"
export SRC_BUILD_TAR="apache-wookie-$WOOKIE_VERSION-src.tar.gz"
export SRC_BUILD_ZIP="apache-wookie-$WOOKIE_VERSION-src.zip"
export BINARY_FOLDER_STANDALONE="binary/standalone"
export BINARY_STANDALONE_TAR="apache-wookie-$WOOKIE_VERSION-standalone.tar.gz"
export BINARY_STANDALONE_ZIP="apache-wookie-$WOOKIE_VERSION-standalone.zip"
export BINARY_FOLDER_WAR="binary/war"
export BINARY_WAR_TAR="apache-wookie-$WOOKIE_VERSION-war.tar.gz"
export BINARY_WAR_ZIP="apache-wookie-$WOOKIE_VERSION-war.zip"
#
cd $SOURCE_FOLDER
gpg --armor --output $SRC_BUILD_TAR.asc --detach-sig $SRC_BUILD_TAR
echo verifying the signature for $SRC_BUILD_TAR
gpg --verify $SRC_BUILD_TAR.asc $SRC_BUILD_TAR
echo Creating MD5 value for $SRC_BUILD_TAR
gpg --print-md MD5 $SRC_BUILD_TAR > $SRC_BUILD_TAR.md5
echo Creating SHA512 value for $SRC_BUILD_TAR
gpg --print-md SHA512 $SRC_BUILD_TAR > $SRC_BUILD_TAR.sha
#
gpg --armor --output $SRC_BUILD_ZIP.asc --detach-sig $SRC_BUILD_ZIP
echo verifying the signature for $SRC_BUILD_ZIP
gpg --verify $SRC_BUILD_ZIP.asc $SRC_BUILD_ZIP
echo Creating MD5 value for $SRC_BUILD_ZIP
gpg --print-md MD5 $SRC_BUILD_ZIP > $SRC_BUILD_ZIP.md5
echo Creating SHA512 value for $SRC_BUILD_ZIP
gpg --print-md SHA512 $SRC_BUILD_ZIP > $SRC_BUILD_ZIP.sha
#
cd ../$BINARY_FOLDER_STANDALONE
#
gpg --armor --output $BINARY_STANDALONE_TAR.asc --detach-sig $BINARY_STANDALONE_TAR
echo verifying the signature for $BINARY_STANDALONE_TAR
gpg --verify $BINARY_STANDALONE_TAR.asc $BINARY_STANDALONE_TAR
echo Creating MD5 value for $BINARY_STANDALONE_TAR
gpg --print-md MD5 $BINARY_STANDALONE_TAR > $BINARY_STANDALONE_TAR.md5
echo Creating SHA512 value for $BINARY_STANDALONE_TAR
gpg --print-md SHA512 $BINARY_STANDALONE_TAR > $BINARY_STANDALONE_TAR.sha
#
gpg --armor --output $BINARY_STANDALONE_ZIP.asc --detach-sig $BINARY_STANDALONE_ZIP
echo verifying the signature for $BINARY_STANDALONE_ZIP
gpg --verify $BINARY_STANDALONE_ZIP.asc $BINARY_STANDALONE_ZIP
echo Creating MD5 value for $BINARY_STANDALONE_ZIP
gpg --print-md MD5 $BINARY_STANDALONE_ZIP > $BINARY_STANDALONE_ZIP.md5
echo Creating SHA512 value for $BINARY_STANDALONE_ZIP
gpg --print-md SHA512 $BINARY_STANDALONE_ZIP > $BINARY_STANDALONE_ZIP.sha
#
cd ../../$BINARY_FOLDER_WAR
#
gpg --armor --output $BINARY_WAR_TAR.asc --detach-sig $BINARY_WAR_TAR
echo verifying the signature for $BINARY_WAR_TAR
gpg --verify $BINARY_WAR_TAR.asc $BINARY_WAR_TAR
echo Creating MD5 value for $BINARY_WAR_TAR
gpg --print-md MD5 $BINARY_WAR_TAR > $BINARY_WAR_TAR.md5
echo Creating SHA512 value for $BINARY_WAR_TAR
gpg --print-md SHA512 $BINARY_WAR_TAR > $BINARY_WAR_TAR.sha
#
gpg --armor --output $BINARY_WAR_ZIP.asc --detach-sig $BINARY_WAR_ZIP
echo verifying the signature for $BINARY_WAR_ZIP
gpg --verify $BINARY_WAR_ZIP.asc $BINARY_WAR_ZIP
echo Creating MD5 value for $BINARY_WAR_ZIP
gpg --print-md MD5 $BINARY_WAR_ZIP > $BINARY_WAR_ZIP.md5
echo Creating SHA512 value for $BINARY_WAR_ZIP
gpg --print-md SHA512 $BINARY_WAR_ZIP > $BINARY_WAR_ZIP.sha