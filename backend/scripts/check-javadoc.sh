#!/usr/bin/env bash
# check-javadoc.sh
# Scans the production Java sources for missing Javadoc and prints a report.
# Exit code 0 even if violations are found (warnings only).
# Usage: ./scripts/check-javadoc.sh [<java-file-path>]
#
# When called with no argument, checks all production sources.
# When called with a file path, checks only that file.

set -euo pipefail

BACKEND_DIR="$(cd "$(dirname "$0")/.." && pwd)"

if [[ $# -gt 0 && -f "$1" ]]; then
    FILE="$1"
    echo "[doc-check] Checking Javadoc in: $FILE"
    # Quick grep-based heuristic: flag public methods/classes lacking /** */
    python3 - "$FILE" <<'PYEOF'
import sys, re

path = sys.argv[1]
with open(path) as f:
    lines = f.readlines()

issues = []
for i, line in enumerate(lines, 1):
    stripped = line.strip()
    # Detect public method/class/interface declarations without a preceding Javadoc block
    if re.match(r'(public|protected)\s+(static\s+|final\s+|abstract\s+|synchronized\s+)*'
                r'(\w+\s+)?\w+\s*[\(<]', stripped):
        # Look backwards for a Javadoc comment (up to 10 lines back)
        start = max(0, i - 11)
        preceding = ''.join(lines[start:i-1])
        if '/**' not in preceding:
            issues.append(f"  Line {i}: Missing Javadoc for: {stripped[:80]}")

if issues:
    print(f"[doc-check] ⚠  Potential missing Javadoc ({len(issues)} location(s)):")
    for issue in issues[:15]:
        print(issue)
    if len(issues) > 15:
        print(f"  ... and {len(issues) - 15} more")
else:
    print("[doc-check] ✓  No obvious missing Javadoc detected")
PYEOF
else
    echo "[doc-check] Running full Checkstyle Javadoc check on production sources..."
    cd "$BACKEND_DIR"
    ./mvnw checkstyle:check --no-transfer-progress -q 2>&1 \
        | grep -E "^\[WARN\]|^\[ERROR\]|WARNING|Missing a Javadoc" \
        | head -40 \
        || true
    echo "[doc-check] Done. Run './mvnw checkstyle:check' for the full report."
fi
