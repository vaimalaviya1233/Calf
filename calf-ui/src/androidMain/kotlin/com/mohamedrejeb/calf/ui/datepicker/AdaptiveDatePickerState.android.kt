package com.mohamedrejeb.calf.ui.datepicker

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver

/**
 * A state object that can be hoisted to observe the date picker state. See
 * [rememberDatePickerState].
 *
 * The state's [selectedDateMillis] will provide a timestamp that represents the _start_ of the day.
 *
 * @param initialSelectedDateMillis timestamp in _UTC_ milliseconds from the epoch that
 * represents an initial selection of a date. Provide a `null` to indicate no selection. Note
 * that the state's
 * [selectedDateMillis] will provide a timestamp that represents the _start_ of the day, which
 * may be different than the provided initialSelectedDateMillis.
 * @param initialDisplayedMonthMillis timestamp in _UTC_ milliseconds from the epoch that
 * represents an initial selection of a month to be displayed to the user. In case `null` is
 * provided, the displayed month would be the current one.
 * @param yearRange an [IntRange] that holds the year range that the date picker will be limited
 * to
 * @param initialMaterialDisplayMode an initial [DisplayMode] that this state will hold
 * @see rememberDatePickerState
 * @throws [IllegalArgumentException] if the initial selected date or displayed month represent
 * a year that is out of the year range.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Stable
actual class AdaptiveDatePickerState actual constructor(
    private val initialSelectedDateMillis: Long?,
    private val initialDisplayedMonthMillis: Long?,
    private val yearRange: IntRange,
    private val initialMaterialDisplayMode: DisplayMode,
    private val initialUIKitDisplayMode: UIKitDisplayMode
) {

    /**
     * The date picker state that this state holds.
     */
    val datePickerState: DatePickerState = DatePickerState(
        initialSelectedDateMillis = initialSelectedDateMillis,
        initialDisplayedMonthMillis = initialDisplayedMonthMillis,
        yearRange = yearRange,
        initialDisplayMode = initialMaterialDisplayMode
    )

    /**
     * A timestamp that represents the _start_ of the day of the selected date in _UTC_ milliseconds
     * from the epoch.
     *
     * In case no date was selected or provided, the state will hold a `null` value.
     *
     * @see [setSelection]
     */
    actual val selectedDateMillis: Long?
        get() = datePickerState.selectedDateMillis

    /**
     * Sets the selected date.
     *
     * @param dateMillis timestamp in _UTC_ milliseconds from the epoch that represents the date
     * selection, or `null` to indicate no selection.
     *
     * @throws IllegalArgumentException in case the given timestamps do not fall within the year
     * range this state was created with.
     */
    actual fun setSelection(@Suppress("AutoBoxing") dateMillis: Long?) =
        datePickerState.setSelection(dateMillis)

    /**
     * A mutable state of [DisplayMode] that represents the current display mode of the UI
     * (i.e. picker or input).
     */
    actual var displayMode: DisplayMode
        get() = datePickerState.displayMode
        set(value) {
            datePickerState.displayMode = value
        }

    actual companion object {
        /**
         * The default [Saver] implementation for [DatePickerState].
         */
        actual fun Saver(): Saver<AdaptiveDatePickerState, *> = Saver(
            save = {
                listOf(
                    it.selectedDateMillis,
                    it.yearRange.first,
                    it.yearRange.last,
                    it.displayMode.value,
                    it.initialUIKitDisplayMode.value,
                )
            },
            restore = { value ->
                AdaptiveDatePickerState(
                    initialSelectedDateMillis = value[0] as Long?,
                    initialDisplayedMonthMillis = value[0] as Long?,
                    yearRange = IntRange(value[1] as Int, value[2] as Int),
                    initialMaterialDisplayMode = displayModeFromValue(value[3] as Int),
                    initialUIKitDisplayMode = uiKitDisplayModeFromValue(value[4] as Int),
                )
            }
        )
    }
}