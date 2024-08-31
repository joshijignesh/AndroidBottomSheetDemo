import android.content.Context

// Extension function to convert dp to pixels
fun Int.dpToPx(context: Context): Int {
    return (this * context.resources.displayMetrics.density).toInt()
}