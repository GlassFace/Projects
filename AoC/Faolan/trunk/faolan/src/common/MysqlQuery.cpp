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

#include "MysqlQuery.h"
#include "MysqlDatabase.h"
#include <mysql.h>

MysqlQuery::MysqlQuery(QueryType t) :
    Query(t), m_res(NULL)
{

}

MysqlQuery::MysqlQuery(boost::function<void ()> f, CallbackType type, QueryType t) :
    Query(f, type, t), m_res(NULL)
{

}

MysqlQuery::MysqlQuery(CallbackType type, QueryType t) :
    Query(type, t), m_res(NULL)
{

}

bool MysqlQuery::execute()
{
    if (m_dbc)
    {
        MysqlDatabase::MysqlDatabaseConnection* mydbc =
                (MysqlDatabase::MysqlDatabaseConnection*)m_dbc;

        if (!mysql_query(&(mydbc->m_mysql), m_queryTxt))
        {
            return true;
        }
    }

    return false;
}

bool MysqlQuery::storeResult()
{
    if (execute() && !m_res)
    {
        MysqlDatabase::MysqlDatabaseConnection* mydbc =
                (MysqlDatabase::MysqlDatabaseConnection*)m_dbc;
        m_res = mysql_store_result(&(mydbc->m_mysql));
        if (m_res)
        {
            m_column = mysql_num_fields(m_res);
            return true;
        }
    }

    return false;
}

bool MysqlQuery::fetchRow()
{
    if (m_res)
    {
        m_row = mysql_fetch_row(m_res);
        if (m_row)
        {
            return true;
        }

    }

    return false;
}

std::string MysqlQuery::error()
{
    if (m_dbc)
    {
        MysqlDatabase::MysqlDatabaseConnection* mydbc =
                (MysqlDatabase::MysqlDatabaseConnection*)m_dbc;
        return mysql_error(&(mydbc->m_mysql));
    }

    return "";

}



//////////////////////////////////////////////////////
// get returned values from query
//
////////////////////////////////////////////////////////


uint32 MysqlQuery::getUint32(uint32 idx)
{
    if (m_res && m_row && (idx < m_column))
    {
        m_idx++;
        return atoi(m_row[idx]);
    }

    return NULL;
}

uint64 MysqlQuery::getUint64(uint32 idx)
{
    if (m_res && m_row && (idx < m_column))
    {
        m_idx++;
        return boost::lexical_cast<uint64>(m_row[idx]);
    }

    return NULL;
}

const char* MysqlQuery::getString(uint32 idx)
{
    if (m_res && m_row && (idx < m_column))
    {
        m_idx++;
        return m_row[idx];
    }

    return NULL;
}

uint32 MysqlQuery::getUint32()
{
    return getUint32(m_idx);
}

uint64 MysqlQuery::getUint64()
{
    return getUint64(m_idx);
}

const char* MysqlQuery::getString()
{
    return getString(m_idx);
}

