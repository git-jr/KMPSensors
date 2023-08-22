package extensions

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun Long.timestampToDisplayDate(): String {

    // coverting a data like "1629780000000" (alura api pattern) to "23/08/2021 às 16:00"
    val dateTime =
        Instant.fromEpochMilliseconds(this).toLocalDateTime(TimeZone.currentSystemDefault())
    return "${dateTime.dayOfMonth.toString().padStart(2, '0')}/${
        dateTime.monthNumber.toString().padStart(2, '0')
    }/${dateTime.year} às ${dateTime.hour.toString().padStart(2, '0')}:${
        dateTime.minute.toString().padStart(2, '0')
    }"
}