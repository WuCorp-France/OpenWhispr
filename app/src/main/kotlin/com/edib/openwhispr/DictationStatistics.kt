package com.edib.openwhispr

import android.content.Context
import org.json.JSONObject
import java.time.LocalDate

/**
 * Aggregate-only, on-device dictation statistics.
 * Never stores recorded audio, transcripts, or API keys.
 */
object DictationStatistics {
    private const val PREFS = "dictation_statistics_v1"
    private const val DAYS = "days"

    @Synchronized
    fun record(context: Context, transcript: String, durationMs: Long, transcriptionMs: Long, engine: String) {
        val words = transcript.trim().split(Regex("\\s+")).count { it.isNotBlank() }
        if (words == 0 || durationMs <= 0) return
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val data = try { JSONObject(prefs.getString(DAYS, "{}") ?: "{}") } catch (_: Exception) { JSONObject() }
        val dayKey = LocalDate.now().toString()
        val day = data.optJSONObject(dayKey) ?: JSONObject()
        fun add(key: String, value: Long) { day.put(key, day.optLong(key, 0L) + value) }
        add("words", words.toLong())
        add("dictations", 1)
        add("speechMs", durationMs)
        add("transcriptionMs", transcriptionMs.coerceAtLeast(0))
        add(if (engine == "local") "localCount" else "cloudCount", 1)
        data.put(dayKey, day)
        prefs.edit().putString(DAYS, data.toString()).apply()
    }

    fun summary(context: Context): String {
        val raw = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getString(DAYS, "{}") ?: "{}"
        val data = try { JSONObject(raw) } catch (_: Exception) { JSONObject() }
        var words = 0L
        var count = 0L
        var speech = 0L
        var processing = 0L
        var local = 0L
        var cloud = 0L
        val today = LocalDate.now()
        var weekWords = 0L
        var monthWords = 0L
        val dates = data.keys()
        while (dates.hasNext()) {
            val key = dates.next()
            val day = data.optJSONObject(key) ?: continue
            val w = day.optLong("words")
            words += w
            count += day.optLong("dictations")
            speech += day.optLong("speechMs")
            processing += day.optLong("transcriptionMs")
            local += day.optLong("localCount")
            cloud += day.optLong("cloudCount")
            val date = try { LocalDate.parse(key) } catch (_: Exception) { null }
            if (date != null) {
                if (!date.isBefore(today.minusDays(6)) && !date.isAfter(today)) weekWords += w
                if (date.year == today.year && date.month == today.month) monthWords += w
            }
        }
        val wpm = if (speech > 0) words * 60000 / speech else 0
        val avgMs = if (count > 0) processing / count else 0
        return "Mots dictés : $words\nCette semaine : $weekWords\nCe mois : $monthWords\n" +
            "Dictées : $count\nTemps de parole : ${speech / 60000} min ${(speech / 1000) % 60} s\n" +
            "Vitesse moyenne : $wpm mots/min\nTranscription moyenne : $avgMs ms\n" +
            "Local : $local • Groq : $cloud\n\nStatistiques conservées uniquement sur cet appareil."
    }

    fun clear(context: Context) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit().clear().apply()
    }
}
