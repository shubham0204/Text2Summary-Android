import java.lang.Exception
import java.lang.StringBuilder
import kotlin.math.floor


// A summarizer which uses weighted-frequencies for text summarization. We calculate weighted-frequencies for all tokens
// in a sentence. Then we calculate the sum of all weighted-frequencies for a sentence. The sentences which have the highest
// N sums are included in the summary.
// N is calculated as Math.floor(( num_sentences * rate ))
class WeightedFrequencySummarizer {

    // Returns the indices of sentences of a summary of the given text. Here, rate is the fraction of the text whose length equals the summary length.
    // For instance, a rate of 0.7 means that the summary will consist of 7 sentences given 10 sentences of text.
    fun getSummary( para : String , rate : Float ) : Array<Int> {

        // Check whether the rate lies in the given range.
        if ( !Tokenizer.checkRate( rate ) ){
            throw Exception( "The compression rate must lie in the interval ( 0 , 1 ].")
        }

        // Get sentences from the text.
        val sentences = Tokenizer.paragraphToSentence( Tokenizer.removeLineBreaks( para ) )
        val words = ArrayList<String>()
        val sentenceTokens = ArrayList<Array<String>>()

        // Collect all the words present in the text.
        for( s in sentences ) {
            val tokens = Tokenizer.sentenceToToken( s )
            words.addAll( tokens )
            sentenceTokens.add( tokens )
        }

        // Create a ( word , frequency ) vocab.
        val vocab = Tokenizer.buildVocab( words.toTypedArray() )
        // Create a ( word , weighted_frequency ) vocab.
        val weightedVocab = Tokenizer.getWeightedVocab( vocab )

        // Collect the sum of weighted frequencies for each sentence
        val weightedFreqSums = ArrayList<Float>()
        for( s in sentenceTokens ){
            val weightedFreq = ArrayList<Float>()
            s.forEach {
                weightedFreq.add( weightedVocab[ it ]!! )
            }
            weightedFreqSums.add( weightedFreq.toTypedArray().sum() )
        }

        // Get the indices of top N maximum values from `weightedFreqSums`.
        val N = floor( (sentences.size * rate).toDouble() ).toInt()
        val topNIndices = Tokenizer.getTopNIndices(
                weightedFreqSums.toTypedArray(),
                weightedFreqSums.toTypedArray().apply{
                    sort()
                    reverse()
                },
                N
        )

        return topNIndices
    }

}