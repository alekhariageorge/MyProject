# Evaria Platform Architecture

## Purpose

Evaria is an ecommerce microservices reference platform inspired by ATG repository modeling. It is intended to support ATG-to-microservices migration scenarios while keeping the domain understandable through ATG-like repository definitions and service-owned data.

## Current Stable Services

- `base-repository`: reusable repository framework for repository definitions, feed import, and repository adapters.
- `product-service`: product catalog, category, product, SKU, PLP, PDP, and PDP BFF composition.
- `price-service`: price lists and SKU prices.
- `inventory-service`: SKU inventory and availability.
- `media-service`: product and SKU media metadata.

## Target Platform Modules

- `api-gateway`: single external entry point for routing, TLS termination in deployed environments, coarse auth enforcement, and request correlation.
- `web-bff-service`: page-oriented APIs for login, profile, PLP, PDP, cart, checkout, order confirmation, and order history.
- `user-service`: profile repository, login identity mapping, addresses, and saved payment references.
- `cart-service`: active cart operations, commerce item updates, pricing/inventory checks, and cart summary.
- `order-service`: order repository, checkout state, payment groups, shipping groups, tax, totals, submitted orders, confirmation, and history.
- `config-service`: centralized configuration for local and deployed environments.
- `service-registry`: service discovery for local and cloud deployments when dynamic service addressing is needed.

## Repository Framework Direction

`base-repository` should stay domain-neutral. It owns reusable mechanics only:

- Repository definition loading from `repository-definition.xml`.
- Feed resource resolution from extensionless logical paths such as `repository/add-items`.
- Feed parsing for XML, CSV, XLS, and XLSX.
- Schema validation against item-descriptor property names.
- Repository adapter contracts for storage.
- Value conversion hooks for future typed persistence.

Service-specific logic belongs in each service module, not in `base-repository`.

## Adapter Strategy

Repository access should route through a `RepositoryAdapter` contract. The current adapter is in-memory and feed-backed. Future adapters should be added as optional modules or implementations:

- `RelationalRepositoryAdapter`: SQL databases.
- `DocumentRepositoryAdapter`: document databases.
- `KeyValueRepositoryAdapter`: key-value or wide-column databases.
- `InMemoryRepositoryAdapter`: local development, tests, and bootstrap demos.

Each service should select its adapter through configuration, so service code does not change when persistence changes.

## Feed Strategy

Service code calls:

```java
addItems("repository/add-items");
```

The framework resolves the actual classpath resource:

- `repository/add-items.xml`
- `repository/add-items.csv`
- `repository/add-items.xls`
- `repository/add-items.xlsx`

The extension helps locate the file. The parser is chosen from file content.

## Data Ownership

Each microservice owns its own repository definition, feed, API, and database. Cross-service reads should happen through APIs or BFF orchestration, not shared tables.

## BFF And API Gateway

Use the API Gateway for external routing and shared edge concerns. Use the BFF for page composition:

- PDP combines product, price, inventory, and media.
- PLP combines category, product summaries, price, and media.
- Cart combines cart lines, price, inventory, and product summaries.
- Checkout combines active order state, addresses, shipping methods, payment groups, tax, and totals.
- Order confirmation/history reads submitted order data.

## Checkout And Order Repository

Checkout should not introduce a separate checkout repository unless the workflow later needs separate durable state. ATG-like checkout data should live under OrderRepository:

- order
- commerceItem
- shippingGroup
- hardgoodShippingGroup
- shippingMethod
- paymentGroup
- creditCardPaymentGroup
- orderPriceInfo
- shippingPriceInfo
- taxPriceInfo

## Cross-Cutting Microservices Concerns

- Security: authentication at gateway/BFF, token propagation to downstream services, method-level authorization where needed.
- Resilience: timeouts, retries, circuit breakers, and graceful fallbacks for BFF composition.
- Observability: structured logs, correlation IDs, metrics, traces, and health endpoints.
- Configuration: environment-specific service URLs, adapter type, database connection details, and feature flags.
- Testing: unit tests for framework pieces, repository import tests per service, API contract tests for service calls, and BFF composition tests.

## Implementation Phases

1. Modularize `base-repository` into definition, feed, adapter, and value-conversion layers while keeping existing services working.
2. Add configuration-driven repository adapter selection with in-memory as the default.
3. Add `user-service` with ProfileRepository.
4. Add `order-service` with OrderRepository and cart/order feed data.
5. Add `web-bff-service` for page APIs.
6. Add `api-gateway` and route services through it.
7. Add service discovery/config service if needed for local orchestration.
8. Add persistence adapters service by service.
9. Add frontend pages for login, profile, PLP, PDP, cart, checkout, confirmation, and history.
