ReconBuddy

A lightweight command-line security testing tool for penetration testers and bug bounty hunters. ReconBuddy helps generate common XSS and SQLi payloads, encode/decode strings, and verify payload reflection in HTTP responses.

⚠️ Legal Disclaimer

This tool is for AUTHORIZED security testing only. Using this tool against systems without explicit permission is illegal and unethical. The author is not responsible for any misuse or damage caused by this tool.

Only use ReconBuddy on:

Systems you own
Systems you have written permission to test
Bug bounty programs with defined scope

Features
1. XSS Helper
Generates common XSS payloads with URL encoding for quick testing:

Benign reflector probes
Event handler PoCs
Attribute break attempts
Classic script injection

2. SQLi Helper
Creates SQL injection test payloads including:

Boolean-based injection
Comment-based bypass
Time-based blind injection
Error-based injection

3. Encoder/Decoder
Multi-format encoding utilities:

URL encoding
HTML entity escaping
JavaScript unicode escaping
Base64 encoding

4. Base64 Decoder
Standalone Base64 decoding with error handling for invalid input.
5. Reflection Checker
Paste HTML responses to verify if your payloads are reflected in the output (helps identify potential XSS vectors).
Requirements

Java 11 or higher
No external dependencies

Installation

Clone the repository:

bashgit clone https://github.com/yourusername/reconbuddy.git
cd reconbuddy

Compile:

bashjavac ReconBuddy.java

Run:

bashjava ReconBuddy
Usage

Start the tool and enter your target base URL
Choose a module from the menu
Follow the prompts for each module

Example Session
=== ReconBuddy (for AUTHORIZED testing only) ===
Enter base URL (e.g., https://example.com): https://testsite.com

Choose a module:
1) XSS helper
2) SQLi helper
3) Encoder/Decoder
4) Reflect check (paste HTML)
5) Base64 decode
0) Exit
> 1

[XSS helper] Authorized testing only.
Parameter name (e.g., q): search

Payload #1: "><x>
URL-encoded : %22%3E%3Cx%3E
Test URL    : https://testsite.com?search=%22%3E%3Cx%3E
...
Features in Detail
URL Validation
The tool validates input URLs and warns if they don't start with http:// or https://, but allows you to continue if needed.

Error Handling
Empty input validation
Base64 decode error handling
General encoding exception handling

Copy-Paste Ready
All generated test URLs include ready-to-use curl commands for quick testing.

Contributing
Contributions are welcome! Please feel free to submit a Pull Request. Some ideas for enhancement:

Additional payload templates
More encoding formats
Request/response logging
Custom payload file loading
Batch testing mode

License
MIT License - See LICENSE file for details

Author
Created for educational and authorized security testing purposes.

Acknowledgments
Built for the security testing community. Use responsibly and ethically.

Remember: With great power comes great responsibility. Always get permission before testing.
