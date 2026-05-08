# clj-ops-ledger

`clj-ops-ledger` is a small public full-stack Clojure/ClojureScript operations
ledger. It tracks intake items, owners, statuses, and risk notes for a support
team.

The repo is intentionally modest: enough backend, frontend, routing, schema,
state, and CI surface to behave like a real app without carrying private data.

## Run Locally

```bash
npm install
bb ci
clojure -M:run
```

Open:

```text
http://localhost:8080
```

## Formsmith Gate

This repo consumes Formsmith as a released source dependency:

```clojure
{:git/tag "v0.1.0-pre.5"
 :git/sha "4bd1d7228aebf24a0cc7b80c83c84396ea7d1fbc"}
```

The CI gate runs backend tests, a CLJS release build, and:

```bash
clojure -M:formsmith check src test
```
