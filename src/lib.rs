pub mod tokenizer;
pub mod summarizer;

use std::ffi::CString;
use self::summarizer::Summarizer;

#[no_mangle]
pub extern "C" fn summarize( text: *const u8 , length: usize , reduction_factor: f32 ) -> *const u8 {
    unsafe {
        match std::str::from_utf8( std::slice::from_raw_parts( text , length ) ) {
            Ok( text ) => {
                let summary = Summarizer::compute(text, reduction_factor) ;
                let c_summary = CString::new( summary ).unwrap() ;
                let c_summary_ptr = c_summary.as_ptr() ; 
                std::mem::forget( c_summary );
                c_summary_ptr as *const u8
            } , 
            Err( _ ) => {
                let c_summary = CString::new( e.to_string() ).unwrap() ;
                c_summary.as_ptr() as *const u8
            }
        }
    }    
}
