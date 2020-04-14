
# Text2Summary API

[![](https://jitpack.io/v/shubham0204/Text2Summary.svg)](https://jitpack.io/#shubham0204/Text2Summary)

Text2Summary API uses on-device methods to perform text summarization on Android applications. It uses extractive text summarization 
methods namely the Weighted Frequencies Algorithm and TF-IDF algorithm.

## Installation

First, we need to add the JitPack Maven repository to the root-level `build.gradle` file,

```
allprojects {
    repositories {
	    // Other dependencies
		maven { url 'https://jitpack.io' }
	}
}
```

Then in the module-level `build.gradle` file, add the Text2Summary dependency,

```
dependencies {
    // Other dependencies
    implementation 'com.github.shubham0204::Tag'
}
```



## Usage

The text which needs to be summarized has to be a `String` object. Then,
use `Text2Summary.summarize()` method to generate the summary.

```kotlin

// Use TF-IDF for summarization
var summary = Text2Summary.summarize( someLongText , compressionRate = 0.7 , Text2Summary.SUMMARIZATION_ALGO_TFIDF )
// Or use weighted frequencies
var summary = Text2Summary.summarize( someLongText , compressionRate = 0.7 , Text2Summary.SUMMARIZATION_ALGO_WEIGHTED_FREQ )

```
You may extract text from a file and then pass it to Text2Summary,

```kotlin

val bufferedReader: BufferedReader = File( "poems.txt" ).bufferedReader()
val text = bufferedReader.use{ it.readText() }
val summary = Text2Summary.summarize( text , 0.7 , Text2Summary.SUMMARIZATION_ALGO_WEIGHTED_FREQ )

```





