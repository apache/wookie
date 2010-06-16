/*
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.apache.wookie.beans.jpa;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * SQLScriptReader - SQL script file reader.
 * 
 * @author <a href="mailto:rwatler@apache.org">Randy Watler</a>
 * @version $Id$
 */
public class SQLScriptReader
{
    private static final String EOL = System.getProperty("line.separator");
    
    private BufferedReader reader;
    
    /**
     * Construct reader for SQL script stream.
     * 
     * @param scriptStream script stream
     * @throws FileNotFoundException
     */
    public SQLScriptReader(InputStream scriptStream) throws FileNotFoundException
    {
        this.reader = new BufferedReader(new InputStreamReader(scriptStream));
    }
    
    /**
     * Construct reader for SQL script file.
     * 
     * @param scriptFile script file
     * @throws FileNotFoundException
     */
    public SQLScriptReader(File scriptFile) throws FileNotFoundException
    {
        this.reader = new BufferedReader(new FileReader(scriptFile));
    }
    
    /**
     * Read next SQL statement from script file.
     * 
     * @return read SQL statement
     * @throws IOException
     */
    public String readSQLStatement() throws IOException
    {
        StringBuilder sqlStatement = new StringBuilder();
        boolean comment = false;
        for (;;)
        {
            String line = reader.readLine();
            if (line != null)
            {
                line = line.trim();
                if (comment)
                {
                    comment = !line.endsWith("*/");
                }
                else
                {
                    comment = line.startsWith("/*");
                    if (!comment && !line.startsWith("--") && !line.startsWith("//") && !line.startsWith("#") && (line.length() > 0))
                    {
                        if (sqlStatement.length() > 0)
                        {
                            sqlStatement.append(EOL);
                        }
                        if (line.endsWith(";"))
                        {
                            sqlStatement.append(line.substring(0, line.length()-1));
                            break;
                        }
                        else
                        {
                            sqlStatement.append(line);
                        }
                    }
                }
            }
            else
            {
                sqlStatement.setLength(0);
                break;
            }
        }
        return ((sqlStatement.length() > 0) ? sqlStatement.toString() : null);
    }

    /**
     * Close reader.
     * 
     * @throws IOException
     */
    public void close() throws IOException
    {
        reader.close();
    }
}
