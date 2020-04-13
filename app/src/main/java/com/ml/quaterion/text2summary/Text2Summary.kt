import java.lang.StringBuilder
import java.util.*

// The Text2Summary API for Android.
class Text2Summary() {

    // Use the weighted frequencies for text summarization.
    val SUMMARIZATION_ALGO_WEIGHTED_FREQ = 0

    // Use the TF-IDF algorithm for text summarization.
    val SUMMARIZATION_ALGO_TFIDF = 1

    // Summarizes the given text.
    fun summarize( text : String , compressionRate : Float , summarizationMode : Int ): String {
        val sentences = Tokenizer.paragraphToSentence( Tokenizer.removeLineBreaks( text ) )
        when ( summarizationMode ){
            SUMMARIZATION_ALGO_TFIDF -> {
                val tfidfSummarizer = TFIDFSummarizer()
                val p2 = tfidfSummarizer.compute( text , compressionRate )
                return buildString( sentences , p2 )
            }
            SUMMARIZATION_ALGO_WEIGHTED_FREQ -> {
                val weightedFrequencySummarizer = WeightedFrequencySummarizer()
                val p1 = weightedFrequencySummarizer.getSummary( text , compressionRate )
                return buildString( sentences , p1 )
            }
            else -> {
                throw Exception( "Invalid summarizationMode found. Use SUMMARIZATION_ALGO_WEIGHTED_FREQ or " +
                        "SUMMARIZATION_ALGO_TFIDF")
            }
        }
    }

    private fun buildString( sentences : Array<String> , topNValues : Array<Int> ) : String {
        val stringBuilder = StringBuilder()
        for( n in topNValues ){
            stringBuilder.append( sentences[ n ] + " ." )
        }
        return stringBuilder.toString()
    }


}