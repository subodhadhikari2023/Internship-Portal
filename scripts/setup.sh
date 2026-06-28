#!/usr/bin/env bash
# setup.sh — Bootstrap the Internship Portal on a new machine and start the backend.
#
# What it does:
#   1. Verifies required tools (Java 17+, Maven, MySQL client)
#   2. Loads environment variables from .env at the repo root
#   3. Validates required env vars are set
#   4. Creates the MySQL database if it does not already exist
#   5. Starts the Spring Boot backend
#
# Usage:
#   chmod +x scripts/setup.sh
#   ./scripts/setup.sh

set -euo pipefail

REPO_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
ENV_FILE="$REPO_ROOT/.env"
BACKEND_DIR="$REPO_ROOT/backend"

RED='\033[0;31m'; GREEN='\033[0;32m'; YELLOW='\033[1;33m'; CYAN='\033[0;36m'; NC='\033[0m'
info()    { echo -e "${CYAN}[INFO]${NC}  $*"; }
success() { echo -e "${GREEN}[OK]${NC}    $*"; }
warn()    { echo -e "${YELLOW}[WARN]${NC}  $*"; }
error()   { echo -e "${RED}[ERROR]${NC} $*" >&2; exit 1; }

echo ""
echo "╔══════════════════════════════════════════════════╗"
echo "║     Internship Portal — Setup & Run (Backend)    ║"
echo "╚══════════════════════════════════════════════════╝"
echo ""

# ── 1. Check Java ─────────────────────────────────────────────────────────────
info "Checking Java..."
command -v java &>/dev/null || error "Java not found. Install JDK 17 or later."
JAVA_VER=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}' | cut -d'.' -f1)
[[ "$JAVA_VER" == "1" ]] && JAVA_VER=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}' | cut -d'.' -f2)
[[ "$JAVA_VER" -lt 17 ]] && error "Java 17+ required. Found Java $JAVA_VER."
success "Java $JAVA_VER"

# ── 2. Check Maven ────────────────────────────────────────────────────────────
info "Checking Maven..."
MVN_CMD=""
if [[ -f "$BACKEND_DIR/mvnw" ]]; then
    chmod +x "$BACKEND_DIR/mvnw"
    MVN_CMD="$BACKEND_DIR/mvnw"
    success "Using Maven Wrapper (mvnw)"
elif command -v mvn &>/dev/null; then
    MVN_CMD="mvn"
    success "Using system Maven"
else
    error "Maven not found. Install Maven or ensure the mvnw wrapper is in backend/."
fi

# ── 3. Check MySQL client ─────────────────────────────────────────────────────
info "Checking MySQL client..."
command -v mysql &>/dev/null || error "MySQL client not found. Install MySQL 8.0+."
success "MySQL client found"

# ── 4. Load .env ──────────────────────────────────────────────────────────────
info "Loading environment variables from .env..."
[[ -f "$ENV_FILE" ]] || error ".env not found at $REPO_ROOT. Copy .env.example to .env and fill in the values."
while IFS='=' read -r key rest || [[ -n "$key" ]]; do
    [[ "$key" =~ ^[[:space:]]*# ]] && continue
    [[ -z "${key// }" ]] && continue
    export "$key=$rest"
done < "$ENV_FILE"
success "Environment variables loaded"

# ── 5. Validate required vars ─────────────────────────────────────────────────
info "Validating required variables..."
MISSING=()
for VAR in DATABASE_URL DATABASE_USERNAME DATABASE_PASSWORD MYSQL_ROOT_PASSWORD \
           JWT_SECRET ADMIN_USERNAME ADMIN_PASSWORD ADMIN_EMAIL ADMIN_PHONE_NUMBER \
           ROOT URLPREFIX; do
    [[ -z "${!VAR:-}" ]] && MISSING+=("$VAR")
done
[[ ${#MISSING[@]} -gt 0 ]] && error "Missing required variables in .env: ${MISSING[*]}"

for VAR in ADMIN_USERNAME ADMIN_PASSWORD ADMIN_EMAIL ADMIN_PHONE_NUMBER \
           SPRING_MAIL_USERNAME SPRING_MAIL_PASSWORD; do
    [[ "${!VAR:-}" == "CHANGE_ME"* ]] && warn "$VAR is still set to a placeholder value."
done
success "Variables look good"

# ── 6. Create database if missing ─────────────────────────────────────────────
DB_HOST=$(echo "$DATABASE_URL" | sed -E 's|jdbc:mysql://([^:/]+).*|\1|')
DB_PORT=$(echo "$DATABASE_URL" | sed -E 's|jdbc:mysql://[^:]+:([0-9]+).*|\1|')
DB_PORT="${DB_PORT:-3306}"
DB_NAME=$(echo "$DATABASE_URL" | sed -E 's|.*/([^?]+)(\?.*)?$|\1|')

info "Checking database '$DB_NAME' on $DB_HOST:$DB_PORT..."
DB_EXISTS=$(mysql -h "$DB_HOST" -P "$DB_PORT" -u root -p"$MYSQL_ROOT_PASSWORD" \
    --batch --skip-column-names \
    -e "SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME='$DB_NAME';" 2>/dev/null || true)

if [[ -n "$DB_EXISTS" ]]; then
    success "Database '$DB_NAME' already exists"
else
    warn "Database '$DB_NAME' not found — creating it now..."
    mysql -h "$DB_HOST" -P "$DB_PORT" -u root -p"$MYSQL_ROOT_PASSWORD" \
        -e "CREATE DATABASE \`$DB_NAME\` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;" 2>/dev/null \
        || error "Failed to create database. Check MYSQL_ROOT_PASSWORD in .env."
    success "Database '$DB_NAME' created"
fi

# ── 7. Create storage directory ───────────────────────────────────────────────
info "Ensuring storage directory exists at $ROOT..."
mkdir -p "$ROOT"
success "Storage directory ready"

# ── 8. Start the backend ──────────────────────────────────────────────────────
echo ""
info "Starting Internship Portal backend on http://localhost:8080/internship-portal ..."
info "Press Ctrl+C to stop."
echo ""

cd "$BACKEND_DIR"
exec $MVN_CMD spring-boot:run
