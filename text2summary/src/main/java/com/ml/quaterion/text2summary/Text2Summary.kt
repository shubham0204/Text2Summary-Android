import android.os.AsyncTask
import java.lang.StringBuilder

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

        // Summarizes the given text. Note, this method should be used whe you're dealing with long texts.
        // It performs the summarization on the background thread. Once the process is complete the summary is
        // passed to the SummaryCallback.onSummaryProduced callback.
        fun summarizeAsync( text : String , compressionRate : Float , callback : SummaryCallback ) {
            SummaryTask( text , compressionRate , callback ).execute()
        }

        // The AsyncTask which will be used for processing texts on the background thread.
        private class SummaryTask( var text : String , var rate : Float , var callback: SummaryCallback )
            : AsyncTask<Void , Void , String>() {

            override fun doInBackground(vararg params: Void?): String {
                val sentences = Tokenizer.paragraphToSentence( Tokenizer.removeLineBreaks( text ) )
                val tfidfSummarizer = TFIDFSummarizer()
                val p1 = tfidfSummarizer.compute( text , rate )
                val stringBuilder = StringBuilder()
                for( n in p1 ){
                    stringBuilder.append( sentences[ n ] + " ." )
                }
                return stringBuilder.toString()
            }

            override fun onPostExecute(result: String?) {
                super.onPostExecute(result)
                callback.onSummaryProduced( result!! )
            }
        }

        // Fetchs the sentences from topNValues and concatenates them to produce a complete. String.
        private fun buildString( sentences : Array<String> , topNValues : Array<Int> ) : String {
            val stringBuilder = StringBuilder()
            for( n in topNValues ){
                stringBuilder.append( sentences[ n ] + " ." )
            }
            return stringBuilder.toString()
        }
    }

    // Use this callback only with Text2Summary.summarizeAsync
    interface SummaryCallback {
        fun onSummaryProduced( summary : String )
    }

}