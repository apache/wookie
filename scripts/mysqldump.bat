@ECHO OFF
REM -u root -p admin 

SET MYSQL="D:\Program Files\MySQL\MySQL Server 5.0\bin\mysql"
SET MYSQLDUMP="D:\Program Files\MySQL\MySQL Server 5.0\bin\mysqldump"
SET MYSQL_PORT=3306
SET MYSQL_USER=root
SET MYSQL_PWD=isbn0471
SET DB_NAME=widgetDB

REM *** DUMP DATABASE ***
%MYSQLDUMP% --port=%MYSQL_PORT% --user=%MYSQL_USER% --password=%MYSQL_PWD% %DB_NAME% > "./db_dump.sql"
pause
#%MYSQL% --port=%MYSQL_PORT% --user=%MYSQL_USER% --password=%MYSQL_PWD% %DB_NAME% < "./db_dump.sql"
pause