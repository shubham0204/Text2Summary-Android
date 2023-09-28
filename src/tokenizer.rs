use std::collections::HashMap;
use regex::Regex;

pub struct Tokenizer {}

impl Tokenizer {

    pub fn text_to_sentences( text: &str ) -> Vec<&str> {
        let sentence_regex = Regex::new( r"(?:\.|\?|!)(?: \n?)?" ).expect( "Could not parse sentence regex." ) ; 
        sentence_regex.replace_all( &text , "$@$" ) ; 
        let sentences: Vec<&str> = text.split( "$@$" ).collect() ; 
        sentences
    }

    pub fn sentence_to_tokens( sentence: &str ) -> Vec<&str> {
        let tokens: Vec<&str> = sentence.split_ascii_whitespace().collect() ; 
        tokens
    }

    pub fn get_freq_map( words: Vec<&str> ) -> HashMap<&str,usize> {
        let mut freq_map: HashMap<&str,usize> = HashMap::new() ; 
        for word in words.iter() {
            if freq_map.contains_key( word ) {
                freq_map
                    .entry( word )
                    .and_modify( | e | { 
                        *e = *e + 1 
                    } ) ; 
            }
            else {
                freq_map.insert( word.clone() , 1 ) ; 
            }
        }
        freq_map
    }

    pub fn get_weighed_freq_map( freq_map: HashMap<&str,usize> ) -> HashMap<&str,f32> {
        let mut cum_freq: f32 = 0.0 ; 
        freq_map
            .values()
            .into_iter()
            .for_each( | freq | { 
                cum_freq += *freq as f32 ; 
            } ) ; 
        
        let weighed_freq_map: HashMap<&str,f32> = freq_map
            .into_iter()
            .map( | ( k , v ) |  {
                ( k , (v as f32) / cum_freq )
            }
            ).collect() ; 
        weighed_freq_map
    }

}