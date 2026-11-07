# Architecture Assessment — JBlock Ecosystem

As-is diagnosis of **JBlock** (`java-blockchain`) and **JBlock API** (`java-blockchain-api`). Combines a scan of 12 common software-architecture foundations with a deeper reading through PoEAA, SOLID/YAGNI, GoF selection rules, persistence, EDA / modular monolith, real-time delivery, and quality/safety (`feature-architecture` skill lenses).

Not a gated feature proposal — use as context when starting Analysis/Architecture for a concrete change.

| Artifact | Repository | Role |
|----------|------------|------|
| **JBlock** | `java-blockchain` | Domain Maven library (chain, mining, wallet, transactions) |
| **JBlock API** | `java-blockchain-api` | Quarkus app: REST, PostgreSQL ledger, Kafka fan-out, React demo UI |

Product / market positioning lives in the private `java-blockchain-docs` workspace (`strategy/`), not in the open-source API docs.

---

## 1. Executive verdict

The ecosystem is strong in **tests + CI**, **REST**, **Docker**, **React demo**, and **Kafka peer sync**. It uses **OO/GoF and SOLID informally**. It does **not** practice structured Hexagonal/Clean/DDD or microservices.

| Lens | Verdict |
|------|---------|
| **System shape** | Correct default: **modular monolith** (API) + **shared domain library** (JBlock). Kafka is peer-sync, not a microservice mesh. **YAGNI:** do **not** split services yet. |
| **Domain logic (PoEAA)** | Library leans **Domain Model–ish** but drifts anemic (public fields, static helpers). API is mostly **Transaction Script** — fine if scripts stay short. |
| **DIP / ports** | Weak. Concretes constructed in policy (`new CryptoChain()`, `new ECDSA()`); public mutable fields on `@Singleton`. CDI covers some collaborators, not the seams that matter for tests. |
| **EDA** | Useful for multi-node sync; incomplete reliability (EventBus fire-and-forget after mine; FIXME on empty valid-tx races). |
| **Real-time UI** | REST fetch / on-demand refresh. For live explorer/pool: **SSE + REST**, not WebSocket. |
| **Quality** | Strong tests for codebase size; money paths need clearer mine → persist → broadcast consistency. |

**Bottom line:** Keep the modular monolith. Invest in **Service Layer + ports at API boundaries**, **idempotent Kafka consumption**, and **explicit consistency around mining** — not Hexagonal ceremony inside the library, and not microservices.

---

## 2. System view & responsibility split

```
┌──────────────────────────────────────────────────────────────────┐
│ java-blockchain-api  (Quarkus modular monolith)                  │
│                                                                  │
│  Web / JAX-RS Resources  ──► Transaction Scripts / coordinators  │
│       │                         │                                │
│       │                         ├── Vert.x EventBus (in-proc)    │
│       │                         ├── PostgreSQL / Panache repos    │
│       │                         └── Kafka Publish/Subscribe      │
│       └── React SPA (same origin, polling-style fetch)           │
│                                                                  │
│  Shared in-memory node state: Blockchain, TransactionMiner        │
│  (@Singleton + public fields)                                    │
└────────────────────────────┬─────────────────────────────────────┘
                             │ Maven dependency
                             ▼
┌──────────────────────────────────────────────────────────────────┐
│ java-blockchain (JBlock) — domain / crypto / chain algorithms    │
│  BlockChain → NetChain → CryptoChain | Wallet | Tx | Miner | …   │
│  + broad unit test suite                                         │
└──────────────────────────────────────────────────────────────────┘
```

| Concern | Where it lives |
|---------|----------------|
| Chain validation, PoW, wallets, pool, signatures | **JBlock** |
| Persistence, HTTP, peer sync, bootstrap, UI | **API** |
| Domain tests | **JBlock** (`src/test`) |
| HTTP/Kafka integration tests | **API** (`src/test`) |

Stage 1/2 scale thinking: single deployable, optional second-node profile, async peer fan-out. Premature microservices amplify dual-write and distributed-transaction tax without payoff.

---

## 3. Quick matrix (12 foundations)

| # | Principle | Status | Notes |
|---|-----------|--------|-------|
| 1 | Test Automation | **Used** | JUnit/Mockito/JaCoCo + QuarkusTest/RestAssured; GitLab CI |
| 2 | Refactoring | **Used** (evolutionary) | `concept` → blockchain → cryptocurrency; visible FIXMEs |
| 3 | Hexagonal Architecture | **Not used** | Candidate = DIP + adapters at **API** edges only |
| 4 | Test Patterns | **Used** (partial) | Fixtures, use-cases, profiles; few doubles; stub `ChainResourceTest` |
| 5 | Clean Architecture | **Not used** | Practical form = Service Layer + dependency rule in API |
| 6 | Domain-Driven Design | **Partial** | Ubiquitous language yes; aggregates/BCs no |
| 7 | Microservices / Saga / CQRS / EDA | **EDA partial**; MS/Saga/CQRS **no** | Kafka peer sync, single monolith |
| 8 | Design Patterns (GoF) | **Used** (selective) | Factory, Template Method, Repository, Observer… |
| 9 | SOLID | **Partial** | Main gap: DIP; SRP weak on `Wallet` / `TransactionMiner` |
| 10 | APIs (REST, GraphQL, gRPC, BFF) | **REST used**; others **not** | No OpenAPI; POST-heavy / leetspeak paths |
| 11 | Frontend | **Used** (API only) | React 16 demo; Maven frontend plugin disabled |
| 12 | Infrastructure (Docker) | **Used** (API/CI only) | Dockerfiles + Kafka compose; lib ships as JAR |

### Evidence notes (compact)

| Area | Evidence |
|------|----------|
| **Tests** | JBlock: Surefire + JaCoCo, CI by responsibility. API: `@QuarkusTest`, Postgres+Kafka in CI. Run: `mvn test` in each repo. |
| **Evolution** | README: `src/doc/concept` → packages; `BlockChain` → `NetChain` → `CryptoChain`. |
| **GoF in code** | `Block.genesis`; mine override; `Tx` + `@JsonSubTypes`; repos; `@Observes` / `@ConsumeEvent`; CDI `@Singleton`. |
| **REST / UI / Docker** | JAX-RS resources + `/q/health*`; React under `webapp/`; Dockerfiles under `src/main/docker/`; compose = Kafka/ZK only. |
| **EDA** | `PublishService` / `SubscribeService`; topics `blockchain`, `transactions`, `cryptochain`. |

### Not applicable today

| Item | Why |
|------|-----|
| Microservices / Saga / CQRS | One deployable + shared lib; Kafka is peer-to-peer, not workflow orchestration. |
| GraphQL / gRPC / BFF | One primary client (same-origin UI + REST). |
| Full Hexagonal/Clean **inside the library** | JBlock is already domain core without HTTP/DB adapters — ceremony without payoff. |
| Frontend / Docker **in `java-blockchain`** | Library ships JAR; UI and containers belong to the API. |

How teaching names map to skill vocabulary: Hexagonal ≈ DIP+Adapter at edges; Clean ≈ Service Layer; EDA yes / MS no; Singleton = antipattern default; Docker = ops DX, not architecture purity.

---

## 4. PoEAA — what you are actually using

### Domain logic

| Pattern | Where | Assessment |
|---------|-------|------------|
| **Domain Model** (partial) | JBlock: `Wallet`, `Tx`, `CryptoChain`, `TransactionPool`, `Miner` | Behavior present (`sign`, `replaceChain`, PoW); weakened by public fields and static helpers. |
| **Transaction Script** | API: `mineTransactions`, `blockAddedPostActions`, bootstrap | Fits YAGNI until dual-write/ordering bugs (already in FIXMEs). |
| **Service Layer** | **Missing** | REST, Kafka in, startup sync, EventBus all touch the same node state — condition for a Service Layer is **already true**. |
| **Table Module** | Not used | N/A. |

### Data source

| Pattern | Where | Assessment |
|---------|-------|------------|
| **Repository** (named) | `BlockRepository`, `WalletRepository` | Good naming; Panache gateways over PostgreSQL ledger tables. |
| **Data Mapper** (spirit) | `BlockCodec`, `WalletCodec` | Owns impedance mismatch well. |
| **Active Record** | Not used | Keep it that way. |
| **Unit of Work** | DB transaction + advisory lease | Mine: pending → Postgres block/wallets/fence → commit → Kafka. Peers hydrate memory. |
| **DTO / Remote Facade** | `WalletInfo`, Kafka deserializers | Coarse remote API is good; path/method conventions hurt evolution. |

**PoEAA recommendations:** enrich Domain Model invariants in JBlock; promote API scripts to Service Layer (`MineTransactions`, `ConductTransaction`, `ReplacePeerChain`); keep Data Mapper + Repository (no generic repo-for-everything); name consistency as best-effort eventual today — or tighten the mine pipeline.

---

## 5. SOLID, GoF & rotting signals

Applied as **design filters**, not a purity score.

| Principle | Observation | Lean |
|-----------|-------------|------|
| **SRP** | Dense `Wallet`, `TransactionMiner`, `ApplicationLifeCycle` | Application services; avoid micro-class explosion. |
| **OCP** | Chain inheritance; `Tx` subtypes | Prefer composition if inheritance deepens; Strategy for pay types only if variants explode. |
| **LSP / ISP** | Subchains OK; small `Chain`/`Tx` | Keep fat interfaces out. |
| **DIP** | `new ECDSA()`, `new CryptoChain()`, concrete repos | Ports + adapters at Postgres/Kafka/(crypto) seams. |
| **YAGNI / KISS** | No speculative MS/GraphQL — good | No Hexagonal folder theater in JBlock; normalize REST; encapsulate state. |

| GoF | Status | Guidance |
|-----|--------|----------|
| Factory Method (`Block.genesis`) | Keep | |
| Template Method (mine hierarchy) | Keep carefully | Prefer Strategy/composition if more chain kinds appear. |
| Observer (EventBus, `@Observes`) | Keep | Document ordering for cross-module effects. |
| Repository | Keep name; tighten | Hide SQL/Panache details. |
| Singleton + public fields | **Antipattern default** | Single instance via DI **behind an interface**. |
| Adapter | Introduce | Postgres + Kafka behind ports. |
| Facade / app service | Formalize lightly | `mineTransactions` coordinator without god-object growth. |
| Strategy / State | Defer | Until variants or workflows demand them. |

| Rotting signal | Present? | Example |
|----------------|----------|---------|
| Rigidity | Emerging | Mine flow spans Resource + Miner + BlockService + Kafka + bootstrap. |
| Fragility | Yes | Empty `validTransactions` race (FIXME); EventBus without persist gate (TODO). |
| Immobility | Moderate | Hard to reuse mining without Quarkus singletons. |
| Viscosity | Moderate | Easier to patch Resource than add Service Layer. |

---

## 6. Persistence, EDA & real-time

| Topic | Current | Guidance |
|-------|---------|----------|
| Store | PostgreSQL + Flyway/Panache | Append-only blocks; NUMERIC money. |
| Integrity | App + SQL constraints | Unique height/hash, pending reservation, settled fence. |
| Transaction boundary | Unclear across mine/persist/broadcast | **Name** UoW scope in any redesign — money/identity-critical. |
| Money | `BigDecimal` — good | Keep; Money value type only if rules accumulate. |
| Consistency | Strong in PostgreSQL; peers eventual via Kafka + HTTP catch-up | Call out in feature constraints. |
| Cache / LB / shard | Not needed | YAGNI; keep health for future LB. |

| EDA topic | Assessment |
|-----------|------------|
| Fit | Kafka for peer fan-out — justified temporal decoupling. |
| Gaps | At-least-once assumed; weak idempotency; dual HTTP bootstrap + Kafka; in-proc EventBus before broadcast ack. |

| If UI needs… | Choice |
|--------------|--------|
| Live pool / new blocks | **SSE + REST commands** (Kafka → hub → SSE) |
| Bidirectional chatty UI | WebSocket — not indicated for read-mostly explorer |
| Rare status | Keep polling |

---

## 7. Quality & safety

| Area | Status | If evolving money/identity |
|------|--------|----------------------------|
| **Tests** | Strong unit (JBlock) + integration (API) | Kafka contract tests; fix stub `ChainResourceTest`; coverage gate; re-enable frontend CI. |
| **Hazard** | Mine-then-async-persist can broadcast before DB success | Gate broadcast on persist or use outbox. |
| **Security** | ECDSA in domain; keys in process/DB | Name key storage, authn/z, secrets in design — not “harden later”. |
| **Reuse** | `src/doc/concept` separate from main | Keep out of artifact; re-validate copied assumptions. |
| **Observability** | Logs + health | Correlate mine id across Postgres settlement + Kafka. |

---

## 8. Unified backlog

### Keep

1. Modular monolith + JBlock library split  
2. Kafka for peer sync (not as a reason to invent microservices)  
3. BSON codecs / repository naming  
4. Broad automated tests + CI  
5. `BigDecimal` balances; ECDSA signing model  
6. YAGNI on GraphQL / gRPC / BFF / CQRS / Saga  

### Change next (high leverage)

| Priority | Action | Benefit |
|----------|--------|---------|
| High | **Service Layer** for mine / conduct tx / peer replace | Thin Resources & `@Incoming`; testable flows |
| High | **Ports** (`ChainStore`, `PeerNotifier`, `TransactionPoolPort`) | DIP seams; mocks without infra |
| High | **Mine pipeline consistency** (persist/outbox before broadcast; fix valid-tx race) | Removes highest risk |
| High | **Encapsulate node state** (no public chain/pool fields) | Safer refactors |
| High | **OpenAPI + idiomatic REST** (`/api/v1/...`, GET for reads) | DX and evolution |
| Medium | Selective DDD in JBlock (domain services for replace / pay types) | Stronger invariants |
| Medium | Testcontainers, builders, frontend CI, Jacoco gates | Local/CI parity |
| Medium | Full `docker-compose` (API + Postgres + Kafka); align JVM Dockerfile to Java 25 | Onboarding / demo |

### Defer

Saga · CQRS / Event Sourcing · microservices split · GraphQL/gRPC/BFF · formal Strategy explosion · WebSocket for demo UI · speculative cache/shard · Hexagonal package rename inside JBlock  

### Checklist

- [ ] Extract API ports (persistence + peer sync + pool)  
- [ ] Introduce Service Layer / use-cases for mine / conduct transaction / bootstrap  
- [ ] OpenAPI 3 + versioned paths; migrate reads to GET  
- [ ] Idempotency / outbox on Kafka; gate broadcast on persist  
- [ ] Encapsulate `@Singleton` node state  
- [ ] Domain services in JBlock for replace and pay-type rules  
- [ ] `docker-compose` with API + Postgres + Kafka  
- [ ] Re-enable frontend build in CI; coverage gates  

### Gated features (need Phase 1/2 approval)

1. Reliable mine pipeline (outbox / persist-before-broadcast)  
2. Service Layer extraction (behavior-preserving)  
3. Live chain/pool updates via SSE  
4. OpenAPI + path normalization  

Do not implement those without clarifying questions and explicit human approval (`feature-architecture` gates).

---

## History

| Date | Note |
|------|------|
| 2026-11-05 | Initial assessments: 12 foundations + `feature-architecture` skill lenses |
| 2026-11-05 | Consolidated into this single document |
