package org.example

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.text.SimpleDateFormat
import java.util.*

class TaskDatabaseHelper (context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    fun addTask(task: Task): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, task.title)
            put(COLUMN_DESCRIPTION, task.description)
            put(COLUMN_DUE_DATE, SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(task.dueDate))
            put(COLUMN_PRIORITY, task.priority.name)
            put(COLUMN_COMPLETED, task.isCompleted)
        }
        return db.insert(TABLE_NAME, null, values)
    }

    fun getAllTasks(): List<Task> {
        val tasks = mutableListOf<Task>()
        val db = readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)
        with(cursor) {
            while (moveToNext()) {
                val id = getLong(getColumnIndexOrThrow(COLUMN_ID))
                val title = getString(getColumnIndexOrThrow(COLUMN_TITLE))
                val description = getString(getColumnIndexOrThrow(COLUMN_DESCRIPTION))
                val dueDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(getString(getColumnIndexOrThrow(COLUMN_DUE_DATE)))
                val priority = Priority.valueOf(getString(getColumnIndexOrThrow(COLUMN_PRIORITY)))
                val isCompleted = getInt(getColumnIndexOrThrow(COLUMN_COMPLETED)) == 1
                tasks.add(Task(id, title, description, dueDate, priority, isCompleted))
            }
        }
        cursor.close()
        return tasks
    }

    // Outras funções para atualizar e excluir tarefas
}

private const val DATABASE_NAME = "TaskDatabase.db"
private const val DATABASE_VERSION = 1

private const val TABLE_NAME = "tasks"
private const val COLUMN_ID = "_id"
private const val COLUMN_TITLE = "title"
private const val COLUMN_DESCRIPTION = "description"
private const val COLUMN_DUE_DATE = "due_date"
private const val COLUMN_PRIORITY = "priority"
private const val COLUMN_COMPLETED = "completed"

private const val SQL_CREATE_ENTRIES = """
    CREATE TABLE $TABLE_NAME (
        $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
        $COLUMN_TITLE TEXT,
        $COLUMN_DESCRIPTION TEXT,
        $COLUMN_DUE_DATE TEXT,
        $COLUMN_PRIORITY TEXT,
        $COLUMN_COMPLETED INTEGER
    )
"""

private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS $TABLE_NAME"