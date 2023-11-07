#!/bin/bash
cbindgen
cargo build --target=x86_64-unknown-linux-gnu --release
cp target/x86_64-unknown-linux-gnu/release/libsummarizer.a debian/summarizer/