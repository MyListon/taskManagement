package org.example

import javax.swing.text.html.HTML.Tag.OL

data class Task (
    val id: Long = OL,
    val title: String,
    val description: String,
    val dueDate: Date,
    val priority: Priority,
    val isCompleted: Boolean = false
    )

    enum class Priority {
        LOW, MEDIUM, HIGH
    }