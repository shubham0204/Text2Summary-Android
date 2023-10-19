pub mod tokenizer;
pub mod summarizer;
use self::summarizer::Summarizer;

fn main() {

    let text = "Peruvian territory was home to several cultures during the ancient and medieval periods, and has one of the longest histories of civilization of any country, tracing its heritage back to the 10th millennium BCE. Notable pre-colonial cultures and civilizations include the Caral-Supe civilization (the earliest civilization in the Americas and considered one of the cradles of civilization), the Nazca culture, the Wari and Tiwanaku empires, the Kingdom of Cusco, and the Inca Empire, the largest known state in the pre-Columbian Americas. The Spanish Empire conquered the region in the 16th century and Charles V established a viceroyalty with the official name of the Kingdom of Peru that encompassed most of its South American territories, with its capital in Lima. Higher education started in the Americas with the official establishment of the National University of San Marcos in Lima in 1551." ; 

    let summary = Summarizer::compute(text, 0.6) ; 
    println!( "{}" , summary ) ; 

}
