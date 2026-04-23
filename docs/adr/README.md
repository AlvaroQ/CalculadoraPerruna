# Architectural Decision Records

Este directorio registra las decisiones arquitectónicas relevantes de CalculadoraPerruna siguiendo el formato [MADR](https://adr.github.io/madr/).

Cada ADR documenta una decisión con **tradeoff real**. Las decisiones evidentes o derivables del código no se documentan aquí — viven en el código y en el README.

## Índice

| ID | Título | Estado | Fecha |
|----|--------|--------|-------|
| [001](001-hilt-over-koin.md) | Hilt como motor de inyección de dependencias | Accepted | 2026-04-23 |
| [002](002-material3-expressive-adoption.md) | Adopción de Material 3 Expressive | Accepted | 2026-04-23 |
| [003](003-predictive-back-strategy.md) | Estrategia de adopción de Predictive Back | Accepted | 2026-04-23 |
| [004](004-accessibility-baseline.md) | Baseline de accesibilidad WCAG 2.2 AA + EAA 2025 | Accepted | 2026-04-23 |
| [005](005-glance-vs-remoteviews.md) | Glance sobre RemoteViews para widget de raza favorita | Accepted | 2026-04-23 |
| [006](006-wear-standalone-vs-companion.md) | Wear OS standalone primero, companion como iteración | Accepted | 2026-04-23 |

## Estados posibles

- **Proposed** — propuesto pero aún no decidido
- **Accepted** — decidido y vigente
- **Deprecated** — reemplazado por otro ADR (indica cuál)
- **Superseded** — obsoleto

## Convenciones

- Numeración monotónica creciente, sin huecos
- Un ADR por decisión, nunca agrupar decisiones distintas
- Si una decisión cambia: nuevo ADR que supersede al anterior, no editar histórico
