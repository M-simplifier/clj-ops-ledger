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
