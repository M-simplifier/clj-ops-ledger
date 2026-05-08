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
{:git/tag "v0.1.0-pre.6"
 :git/sha "89a06b941e32bb9fe78e5fab22d005a5147234b8"}
```

The CI gate runs backend tests, a CLJS release build, and:

```bash
clojure -M:formsmith check src test
```
