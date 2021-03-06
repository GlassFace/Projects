#include "SQLiteQuery.h"
#include "SQLiteDatabase.h"
#include <sqlite3.h>

SQLiteQuery::SQLiteQuery(QueryType t) :
    Query(t), m_res(NULL), m_row(false)
{

}

SQLiteQuery::SQLiteQuery(boost::function<void ()> f, CallbackType type, QueryType t) :
    Query(f, type, t), m_res(NULL), m_row(false)
{

}

SQLiteQuery::SQLiteQuery(CallbackType type, QueryType t) :
    Query(type, t), m_res(NULL), m_row(false)
{

}

bool SQLiteQuery::execute()
{
    if (m_dbc)
    {
        SQLiteDatabase::SQLiteDatabaseConnection* mydbc =
                (SQLiteDatabase::SQLiteDatabaseConnection*)m_dbc;

        const char *s= NULL;
        std::string sql = m_queryTxt;
        int rc = sqlite3_prepare(mydbc->m_sqlite, sql.c_str(), sql.size(),
                &m_res, &s);
        if (rc != SQLITE_OK)
        {
            return false;
        }
        if (!m_res)
        {
            return false;
        }

        rc = sqlite3_step(m_res); // execute
        sqlite3_finalize(m_res); // deallocate statement
        m_res = NULL;
        if (rc==SQLITE_DONE)
        {
            return true;
        }
    }

    return false;
}

bool SQLiteQuery::storeResult()
{
    if (m_dbc)
    {
        SQLiteDatabase::SQLiteDatabaseConnection* mydbc =
                (SQLiteDatabase::SQLiteDatabaseConnection*)m_dbc;

        const char *s= NULL;
        std::string sql = m_queryTxt;

        int rc = sqlite3_prepare(mydbc->m_sqlite, sql.c_str(), sql.size(),
                &m_res, &s);
        if (rc != SQLITE_OK)
        {
            return false;
        }
        if (!m_res)
        {
            return false;
        }
        m_column = sqlite3_column_count(m_res);

        return true;

    }
    return false;
}

bool SQLiteQuery::fetchRow()
{
    if (m_res)
    {
        int rc = sqlite3_step(m_res); // execute
        if (rc==SQLITE_ROW)
        {
            m_row=true;
            return true;
        }

    }
    m_row=false;
    return false;
}

std::string SQLiteQuery::error()
{
    if (m_dbc)
    {
        SQLiteDatabase::SQLiteDatabaseConnection* mydbc =
                (SQLiteDatabase::SQLiteDatabaseConnection*)m_dbc;
        return sqlite3_errmsg(mydbc->m_sqlite);
    }

    return "";

}

//////////////////////////////////////////////////////
// get returned values from query
//
////////////////////////////////////////////////////////


uint32 SQLiteQuery::getUint32(uint32 idx)
{
    if (m_res && m_row && (idx < m_column))
    {
        m_idx++;
        return sqlite3_column_int(m_res, idx);
    }

    return NULL;
}

uint64 SQLiteQuery::getUint64(uint32 idx)
{
    if (m_res && m_row && (idx < m_column))
    {
        m_idx++;
        return sqlite3_column_int64(m_res, idx);
    }

    return NULL;
}

const char* SQLiteQuery::getString(uint32 idx)
{
    if (m_res && m_row && (idx < m_column))
    {
        m_idx++;
        const unsigned char *tmp = sqlite3_column_text(m_res, idx);
        return tmp ? (const char *)tmp : "";
    }

    return NULL;
}

uint32 SQLiteQuery::getUint32()
{
    return getUint32(m_idx);
}

uint64 SQLiteQuery::getUint64()
{
    return getUint64(m_idx);
}

const char* SQLiteQuery::getString()
{
    return getString(m_idx);
}

