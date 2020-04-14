
# Text2Summary API

Text2Summary API uses on-device methods to perform text summarization on Android applications. It uses extractive text summarization 
methods namely the Weighted Frequencies Algorithm and TF-IDF algorithm.

## Usage

The text which needs to be summarized has to be a `String` object. Then,
use `Text2Summary.summarize()` method to generate the summary.

```kotlin

// Use TF-IDF for summarization
val summary = Text2Summary.summarize( someLongText , compressionRate = 0.7 ,
Text2Summary.SUMMARIZATION_ALGO_TFIDF )

```

You may extract text from a file and then pass it to Text2Summary,


