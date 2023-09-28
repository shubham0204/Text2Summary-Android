use crate::tokenizer::Tokenizer;
pub struct Summarizer {}

impl Summarizer {

    pub fn compute( text: &str ) {
        let sentences: Vec<&str> = Tokenizer::text_to_sentences( text ) ; 
        let mut tokens: Vec<Vec<&str>> = Vec::new() ; 
        for sentence in sentences {
            tokens.push( Tokenizer::sentence_to_tokens(sentence) ) 
        }
        let tokens_corpus: Vec<&str> = tokens.concat() ; 
        let vocab = Tokenizer::get_freq_map( tokens_corpus ) ; 
    }

}