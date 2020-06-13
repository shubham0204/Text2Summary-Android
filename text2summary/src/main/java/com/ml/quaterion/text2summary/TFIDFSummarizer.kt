package com.ml.quaterion.text2summary

import java.lang.Exception
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

// Uses Term Frequency-Inverse Document Frequency ( TF-IDF ) to summarize texts. First, we vectorize the sentences,
// such that each token has its own TF-IDF score. We take the sum of all TF-IDF scores in a sentence. The sentences with
// top N highest sums are included in the summary.
class TFIDFSummarizer {

    // Returns the indices of sentences of a summary of the given text. Here, rate is the fraction of the text whose length equals the summary length.
    // For instance, a rate of 0.7 means that the summary will consist of 7 sentences given 10 sentences of text.
    fun compute( text : String , rate : Float ) : Array<Int> {

        // Check whether the rate lies in the given range.
        if ( !Tokenizer.checkRate( rate ) ){
            throw Exception( "The compression rate must lie in the interval ( 0 , 1 ].")
        }

        // Get sentences from the text.
        val sentences = Tokenizer.paragraphToSentence( Tokenizer.removeLineBreaks( text ) )
        val sentenceTokens = sentences.map { Tokenizer.sentenceToToken( it ) }
        val tokensList = ArrayList<String>()

        // Collect all the words present in the text.
        sentenceTokens.forEach {
            tokensList.addAll( it )
        }

        // Create a ( word , frequency ) vocab.
        val vocab = Tokenizer.buildVocab( tokensList.toTypedArray() )
        val tfidfSumList = ArrayList<Float>()

        // For each sentence, calculate TF, IDF and then TFIDF. Take the sum of all TF-IDF scores for all words in the
        // sentence and append it to a list.
        for ( tokenizedSentence in sentenceTokens ){
            val termFrequency = computeTermFrequency( tokenizedSentence , vocab )
            val inverseDocumentFrequency =
                    computeInverseDocumentFrequency( termFrequency , sentenceTokens.toTypedArray() )
            val tfIDF = computeTFIDF( termFrequency , inverseDocumentFrequency )
            tfidfSumList.add( tfIDF.values.sum() )
        }

        // Get the indices of top N maximum values from `weightedFreqSums`.
        val N = Math.floor((sentences.size * rate).toDouble()).toInt()
        val topNIndices = Tokenizer.getTopNIndices(
                tfidfSumList.toTypedArray(),
                tfidfSumList.toTypedArray().apply{
                    sort()
                    reverse()
                },
                N
        )
        return topNIndices
    }

    // Computes the Term Frequency for a given sentence. For a token in a sentence, term frequency equals,
    // term_frequency = frequency_in_sentence / num_tokens_in_sentence
    private fun computeTermFrequency(s: Array<String>, vocab: HashMap<String,Int> ): HashMap<String, Float> {
        val freqMatrix = HashMap<String, Float>()
        for (word in vocab.keys) {
            freqMatrix[word] = 0f
        }
        for (word in s) {
            freqMatrix[word] = freqMatrix[word]!! + 1f
        }
        val tfFreqMatrix = HashMap<String, Float>()
        for ((word, count) in freqMatrix) {
            tfFreqMatrix[word] = count / s.size.toFloat() // Calculation of TF
        }
        return tfFreqMatrix
    }

    // Computes Inverse Document Frequency ( IDF ). It calculates the frequency of a tokens in all documents ( sentences ).
    // The IDF value is given by,
    // IDF = log10( num_docs / frequency_in_docs )
    private fun computeInverseDocumentFrequency(freqMatrix : HashMap<String,Float> , docs : Array<Array<String> >)
            : HashMap<String,Float> {
        val freqInDocMap = HashMap<String,Float>()
        for( word in freqMatrix.keys ){
            freqInDocMap[word] = 0f
        }
        for ( doc in docs ){
            for( word in freqMatrix.keys ){
                if ( doc.contains( word )){
                    freqInDocMap[ word ] = freqInDocMap[ word ]!! + 1f
                }
            }
        }
        val numDocs = docs.size.toFloat()
        val idfMap = HashMap<String,Float>()
        for ( ( word , freqInDoc ) in freqInDocMap ){
            idfMap[ word ] = Math.log10( numDocs.toDouble() / freqInDoc.toDouble() ).toFloat()
        }
        return idfMap
    }

    // Calculate the final product of the TF and IDF scores.
    private fun computeTFIDF( tfMatrix : HashMap<String,Float> , idfMatrix : HashMap<String,Float> )
            : HashMap<String,Float>{
        val tfidfMatrix = HashMap<String,Float>()
        for( word in tfMatrix.keys ){
            tfidfMatrix[word] = tfMatrix[ word ]!! * idfMatrix[ word ]!!
        }
        return tfidfMatrix
    }

}