package com.ml.quaterion.text2summary

import java.util.regex.Pattern
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

// Helper class which transforms texts to tokens, builds ( word , frequency )  HashMaps.
class Tokenizer {

    companion object {

        // English Stop words taken from here -> https://gist.github.com/sebleier/554280
        private val englishStopWords = arrayOf(
                "i", "me", "my", "myself", "we", "our", "ours", "ourselves", "you", "your", "yours", "yourself", "yourselves",
                "he", "him", "his", "himself", "she", "her", "hers", "herself", "it", "its", "itself", "they", "them", "their",
                "theirs", "themselves", "what", "which", "who", "whom", "this", "that", "these", "those", "am", "is", "are", "was",
                "were", "be", "been", "being", "have", "has", "had", "having", "do", "does", "did", "doing", "a", "an", "the",
                "and", "but", "if", "or", "because", "as", "until", "while", "of", "at", "by", "for", "with", "about", "against",
                "between", "into", "through", "during", "before", "after", "above", "below", "to", "from", "up", "down", "in",
                "out", "on", "off", "over", "under", "again", "further", "then", "once", "here", "there", "when", "where", "why",
                "how", "all", "any", "both", "each", "few", "more", "most", "other", "some", "such", "no",
                "nor", "not", "only", "own", "same", "so", "than", "too", "very", "s", "t", "can", "will", "just", "don", "should",
                "now"
        )

        // Returns a list of sentences from the given paragraph.
        fun paragraphToSentence( para : String ) : Array<String> {
            val text = para.trim()
            val pattern = Pattern.compile("[^.!?\\s][^.!?]*(?:[.!?](?!['\"]?\\s|$)[^.!?]*)*[.!?]?['\"]?(?=\\s|$)",
                    Pattern.MULTILINE or Pattern.COMMENTS)
            val matcher = pattern.matcher( text )
            val sentences = ArrayList<String>()
            while( matcher.find() ) {
                sentences.add( matcher.group() )
            }
            return sentences.toTypedArray()
        }

        // Returns a list of tokens ( words ) from a given sentence ( seq of words ).
        fun sentenceToToken(  s : String ) : Array<String> {
            val sentence = s.trim().toLowerCase()
            var tokens = sentence.split( " " )
            tokens = tokens.map { Regex("[^A-Za-z0-9 ]").replace( it , "") }
            tokens = tokens.filter { !englishStopWords.contains( it.trim() ) }
            tokens = tokens.filter { it.trim().isNotEmpty() and it.trim().isNotBlank() }
            return tokens.toTypedArray()
        }

        // Builds a ( word , frequency ) HashMap.
        fun buildVocab( words : Array<String> ) : HashMap<String,Int> {
            val sortedWords = words.toSet()
            val vocab = HashMap<String,Int>()
            for ( word in sortedWords ){
                vocab[word] = words.count { it.equals( word ) }
            }
            return vocab
        }

        // Builds a ( word , weighted_frequency ) HashMap.
        fun getWeightedVocab( vocab : HashMap<String,Int> ) : HashMap<String,Float> {
            val maxFreq = vocab.values.max()?.toFloat()!!
            val weightedFreqHashMap = HashMap<String,Float>()
            vocab.entries.forEach {
                weightedFreqHashMap.put( it.key , it.value.toFloat() / maxFreq )
            }
            return weightedFreqHashMap
        }

        // Removes \n and \r from the given String.
        fun removeLineBreaks( para : String ) : String {
            return para
                    .replace("\n", " " )
                    .replace("\r", " " )
        }

        // Checks if the compression rate lie in ( 0 , 1 ].
        fun checkRate( rate : Float ) : Boolean {
            return rate > 0.0 && rate <= 1.0
        }

        // Get the indices of top N maximum values in X.
        fun getTopNIndices( x : Array<Float> , xSorted : Array<Float> , N : Int ) : Array<Int> {
            val topN = xSorted.take( N )
            val topNIndices = ArrayList<Int>()
            for ( i in topN ) {
                topNIndices.add( x.indexOf( i ) )
            }
            topNIndices.sort()
            return topNIndices.distinct().toTypedArray()
        }

    }

}