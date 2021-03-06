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

#include "Query.h"
#include <stdarg.h>

Query::Query(boost::function<void ()> f, CallbackType type, QueryType t) :
    m_callback(f), m_callbackType(type), m_queryType(t), m_dbc(NULL),m_idx(0)
{

}

Query::Query(QueryType t) :
    m_callbackType(Query::NO_CALLBACK), m_queryType(t), m_dbc(NULL),m_idx(0)
{

}

Query::Query(CallbackType type, QueryType t) :
    m_callbackType(type), m_queryType(t), m_dbc(NULL),m_idx(0)
{

}

void Query::setDatabaseConnection(Database::DatabaseConnection* dbc)
{
    m_dbc=dbc;
}

void Query::setQueryText(const char* queryTxt, ...)
{
    va_list Params;
    va_start(Params, queryTxt);
    vsprintf(m_queryTxt, queryTxt, Params);
    va_end(Params);

}

void Query::releaseDBConnection()
{
    if (m_dbc)
    {
        Database* db = m_dbc->m_db;
        db->releaseDBConnection(m_dbc);
    }
}

void Query::setCallbackFunction(boost::function<void ()> f)
{
    m_callback=f;
}

void Query::setCallbackType(CallbackType type)
{
    m_callbackType=type;
}


