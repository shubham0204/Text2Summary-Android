# Text Summarization With TF-IDF In Rust

## Building the Debian package

`src/lib.rs` defines a FFI (Foreign Function Interface) which accepts a string (`const* uint8_t` in C and `*const u8` in Rust) and returns the summarized version of the string, given the `reduction_factor` and `length` of the string. The length is expressed with `usize` in Rust and with `const uintptr_t` in C. Our goal is generate two resources for building a C library for our Rust project:

1. A static library which provides the implementation of the `summarize` function in `src/lib.rs`
2. A header file which provides the declaration/prototype of the `summarize` function in `src/lib.rs`

### 1. Generating C headers for FFI

We use `cbindgen`, a crate provided by Mozilla, to generate C headers for functions defined in Rust source code. To install `cbindgen`, use,

```
$> cargo install --force cbindgen
```

Once installed, we use `cbindgen` to generate headers for `src/lib.rs` with target `lang` as `C`. The header file is produced in our Debian package's directory by specifying the `--output` option.

```
$> cbindgen --lang C --output debian/summarizer/summarizer.h
```

The contents of the header `summarizer.h` are,

```c
#include <stdarg.h>
#include <stdbool.h>
#include <stdint.h>
#include <stdlib.h>

const uint8_t *summarize(const uint8_t *text, uintptr_t length, float reduction_factor);
```

Notice, how `const* u8` and `usize` were transformed to `const uint8_t` and `uintptr_t` respectively with ease. Without `cbindgen` performing this task, we would have to determine C equivalent data-types by ourselves which may lead to inconsistency errors.

## 2. Generating a static library for our Rust code

The header file produced in step 1 will only provide the prototype of the `summarize` function to the calling C program. Upon compilation, we need to provide a library to the linker that will contain the implementation of the `summarize` function, else an `Unresolved reference` error is evident.

We generate a static library instead of a dynamic library in order to eliminate most external dependencies. However static libraries are heavier than dynamic libraries, the latter being loaded at run-time by operating system.

To start building a static library, we first need to set `crate_type` to `staticlib` in `Cargo.toml`,

```toml
[lib]
name = "summarizer"
crate_type = [ "staticlib" ]
```

Next, we build the project with `cargo build`, specifying the `target` architecture and type of build required (in our case, we produce an optimized `release` build),

```
$> cargo build --target=x86_64-unknown-linux-gnu --release
```

once the static library `libsummarizer.a` is generated in the `target/x86_64-unknown-linux-gnu/release` directory, we can copy it to our Debian package's directory,

```
$> cp target/x86_64-unknown-linux-gnu/release/libsummarizer.a debian/summarizer/
```

### 3. Packaging the header and library

We've generated the library and header in steps 1 and 2, and we can now build a Debian package which would perform the following tasks after its installation on the user's system,

1. Copy `libsummarizer.a` to `/usr/local/lib/`
2. Copy `summarizer.h` to `/usr/include/`

These two steps are accomplished with the `postinst` script in `debian/summarizer/DEBIAN/`

```
#!/bin/bash
cp ../libsummarizer.so /usr/local/lib/
cp ../summarizer.h /usr/include/
```

the `control` script in the same directory provides information about the package,

```
Package: Summarizer
Version: 0.0.1
Maintainer: Shubham Panchal
Architecture: amd64
Description: A text summarizer based on TF-IDF
```

To build the package with `dpkg-deb` utility and then rename it, we can write a simple Bash script `build_package.sh`,

```
#!/bin/bash
dpkg-deb --build summarizer
mkdir -p packages
mv summarizer.deb packages/summarizer-v0.0.1-amd64.deb
```

To build the package, execute `build_package.sh`,

```
$> cd debian
$ debian> bash build_package.sh 
```

The package `summarizer-v0.0.1-amd64.deb` will be generated in `debian/packages` directory.