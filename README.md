# Recommendation Service

> **Combines rule-based analysis with Groq AI to deliver actionable cost optimization recommendations for each API — with Redis caching to keep response times fast.**

---

## Overview

The Recommendation Service runs on port `8084` and is the intelligence layer of the platform. When asked for a recommendation for a given API, it fetches that API's usage context from the Analytics Service via Feign, runs it through a local rule engine, then sends a structured prompt to the Groq AI API (OpenAI-compatible) to generate a human-readable optimization suggestion. Results are cached in Redis to avoid repeated LLM calls for the same API within the cache TTL window.

---

## Port

`8084`

---

## Responsibilities

- Fetch usage context from Analytics Service via Feign client
- Apply rule-based heuristics to identify obvious optimization opportunities
- Send enriched context to Groq AI (LLM) for natural language recommendations
- Cache recommendations in Redis with a configurable TTL
- Serve recommendations via a simple REST endpoint

---

## API Endpoints

| Method | Endpoint | Auth Required | Description |
|--------|----------|---------------|-------------|
| GET | `/recommendations/{apiId}` | Yes (JWT via Gateway) | Get AI + rule-based recommendation for the given API |

### Sample Response

```json
{
  "apiId": "openai-gpt4",
  "recommendation": "Your GPT-4 usage averages 1,800 tokens per call with a peak at 3,200. Consider switching to GPT-4o-mini for calls under 500 tokens — estimated 60% cost reduction on those requests.",
  "generatedAt": "2026-06-24T10:05:00Z",
  "cached": false
}
```

---

## Dependencies

- **Analytics Service** — fetched via Feign for usage context
- **Groq AI API** — LLM inference (OpenAI-compatible API)
- **Redis** — Recommendation caching

---

## Configuration

```yaml
server:
  port: 8084

analytics:
  service:
    url: ${ANALYTICS_SERVICE_URL:http://localhost:8083}

groq:
  api:
    key: ${GROQ_API_KEY}
    url: ${GROQ_API_URL:https://api.groq.com/openai/v1/chat/completions}

cache:
  recommendation:
    ttl: ${CACHE_TTL:5}   # minutes
```

---

## Environment Variables

| Variable | Description |
|----------|-------------|
| `ANALYTICS_SERVICE_URL` | URL of Analytics service (default: `http://localhost:8083`) |
| `GROQ_API_KEY` | Groq API key for LLM inference |
| `GROQ_API_URL` | Groq endpoint (default: `https://api.groq.com/openai/v1/chat/completions`) |
| `REDIS_HOST` | Redis host |
| `REDIS_PORT` | Redis port |
| `REDIS_PASSWORD` | Redis password |
| `CACHE_TTL` | Cache TTL in minutes (default: `5`) |

---

## Circuit Breaker

Resilience4j is enabled for the Feign client to Analytics. If Analytics is unavailable, the recommendation endpoint returns a graceful fallback response.

---

## Swagger UI

http://localhost:8084/swagger-ui.html

---

## Running

```bash
cd services/recommendation
./mvnw spring-boot:run
```
