#!/bin/bash
dpkg-deb --build summarizer
mkdir -p packages
mv summarizer.deb packages/summarizer-v0.0.1-amd64.deb