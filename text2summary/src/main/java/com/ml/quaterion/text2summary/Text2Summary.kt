import java.lang.StringBuilder
import java.util.*

// The Text2Summary API for Android.
class Text2Summary() {

    companion object {

        // Summarizes the given text.
        fun summarize( text : String , compressionRate : Float ): String {
            val sentences = Tokenizer.paragraphToSentence( Tokenizer.removeLineBreaks( text ) )
            val tfidfSummarizer = TFIDFSummarizer()
            val p1 = tfidfSummarizer.compute( text , compressionRate )
            return buildString( sentences , p1 )
        }

        private fun buildString( sentences : Array<String> , topNValues : Array<Int> ) : String {
            val stringBuilder = StringBuilder()
            for( n in topNValues ){
                stringBuilder.append( sentences[ n ] + " ." )
            }
            return stringBuilder.toString()
        }
    }

}