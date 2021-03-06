/*
Faolan Project, a free Age of Conan server emulator made for educational purpose
Copyright (C) 2008 Project Faolan team

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
*/

#include "MysqlDatabase.h"
#include <boost/thread.hpp>
#include <boost/foreach.hpp>
#include "Logger.h"


MysqlDatabase* MysqlDatabase::m_db = NULL;

MysqlDatabase* MysqlDatabase::createInstance(std::size_t poolSize, const std::string &login, const std::string &host, const std::string &password, const std::string &database, uint32 port)
{
	if(m_db==NULL)
	{
		m_db = new MysqlDatabase(poolSize,login,host,password,database,port);
	}
	return m_db;
}

MysqlDatabase* MysqlDatabase::instance()
{
	return m_db;
}

void MysqlDatabase::destroy()
{
	if(m_db)
	{
		m_db->shutdown();
		delete m_db;
	}
}

MysqlDatabase::MysqlDatabase(std::size_t poolSize, const std::string& login,
        const std::string& host, const std::string& password,
        const std::string& database, uint32 port) :
    Database(poolSize), m_login(login), m_host(host), m_password(password),
            m_database(database), m_port(port)
{

}



bool MysqlDatabase::dbInitialize()
{
   
		for (uint32 i=0; i < m_poolConnSize; i++)
		{
			DatabaseConnection* dbc = new MysqlDatabaseConnection(this,m_login,m_host,m_password,m_database,m_port);
			m_dbConnQueue.push(dbc);
			m_dbConn.push_back(dbc);
			
			if(!dbc->dbInitialize())
			{
				Logger::log("%s\n",dbc->error().c_str());
				return false;
			}
			m_group.create_thread(boost::bind(&MysqlDatabaseConnection::run, dbc));
		}

		return true;

}

bool MysqlDatabase::start()
{
	boost::mutex::scoped_lock lock(m_mutex);
    
	m_runThread = new boost::thread(boost::bind(&MysqlDatabase::run,this));
	m_condition.wait(lock);
	
	
	if(m_initialized)
	{
		Logger::log("Connected to Mysql DB on port: %d Host: %s DB name: %s \n",m_port,m_host.c_str(),m_database.c_str());
	}
	else
	{
		Logger::log("Fail to connect to Mysql DB on port: %d Host: %s DB name: %s \n",m_port,m_host.c_str(),m_database.c_str());
	}

	return m_initialized;
}

MysqlDatabase::~MysqlDatabase()
{
    BOOST_FOREACH(DatabaseConnection* dbc, m_dbConn)
    {
        if(dbc)
        {
            delete dbc;
        }
    }

    if (m_runThread)
    {
        delete m_runThread;
    }
}

/////////////////////////////////////////////////////////////:
// MysqlDatabaseConnection def
//////////////////////////////////////////////////////////////

MysqlDatabase::MysqlDatabaseConnection::MysqlDatabaseConnection(Database* db,
        const std::string& login, const std::string& host,
        const std::string& password, const std::string& database, uint32 port) :
    DatabaseConnection(db), m_login(login), m_host(host), m_password(password),
            m_database(database), m_port(port)
{

}

bool MysqlDatabase::MysqlDatabaseConnection::dbInitialize()
{

    mysql_init(&m_mysql);
    //mysql_option(&m_mysql, MYSQL_SET_CHARSET_NAME, "utf8");

    if (!mysql_real_connect(&m_mysql, m_host.c_str(), m_login.c_str(),
            m_password.c_str(), m_database.c_str(), m_port, NULL, 0))
    {

		m_initialized = false;
    }

    m_initialized = connected();

	return m_initialized;

}

bool MysqlDatabase::MysqlDatabaseConnection::connected()
{
    return mysql_ping(&m_mysql) ? false : true;
}

bool MysqlDatabase::MysqlDatabaseConnection::disconnect()
{
    mysql_close(&m_mysql);
    return true;
}

MysqlDatabase::MysqlDatabaseConnection::~MysqlDatabaseConnection()
{

    disconnect();
}

void MysqlDatabase::MysqlDatabaseConnection::shutdown()
{
    m_shutdown=true;
    m_condition.notify_one();
}

std::string MysqlDatabase::MysqlDatabaseConnection::error()
{
	return mysql_error(&m_mysql);

}



